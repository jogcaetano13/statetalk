package com.joel.communication_android.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.joel.communication_android.builders.PagingBuilder
import com.joel.communication_android.envelope.EnvelopeList
import com.joel.communication_android.exceptions.CommunicationsException
import com.joel.communication_android.models.PagingModel
import com.joel.communication_android.states.AsyncState

@ExperimentalPagingApi
@PublishedApi
internal class RemoteAndLocalPagingSource<T : PagingModel>(
    private val builder: PagingBuilder<T>,
    private val doApiCall: suspend (page: Int) -> AsyncState<EnvelopeList<T>>
) : RemoteMediator<Int, T>() {

    override suspend fun initialize(): InitializeAction {
        return if (builder.lastUpdatedTimestamp?.invoke() != null &&
            System.currentTimeMillis() - builder.lastUpdatedTimestamp?.invoke()!! < builder.cacheTimeout.inWholeMilliseconds &&
            builder.refresh.not()
        )
            InitializeAction.SKIP_INITIAL_REFRESH
        else
            InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        val loadPage = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )

                lastItem.page
            }
        }

        val page = loadPage ?: 1

        val resultState = doApiCall(page)
        val nextPage = page + 1

        return try {
            when(resultState) {
                AsyncState.Empty -> MediatorResult.Error(Throwable("Empty data"))
                is AsyncState.Error -> MediatorResult.Error(Throwable(resultState.error.errorBody))
                is AsyncState.Success -> {
                    val envelopeList = resultState.data

                    envelopeList.data.forEach {
                        it.page = nextPage
                    }

                    if (loadType == LoadType.REFRESH) {
                        envelopeList.data.forEach {
                            it.lastUpdatedTimestamp = System.currentTimeMillis()
                        }
                    }

                    if (loadType == LoadType.REFRESH && builder.deleteOnRefresh) {
                        if (builder.deleteAll == null)
                            throw CommunicationsException("You must implement 'deleteAll()' function if 'deleteOnRefresh' is true!")

                        builder.deleteAll?.invoke()
                    }

                    if (builder.insertAll == null)
                        throw CommunicationsException("You must implement 'insertAll()' function!")

                    builder.insertAll?.invoke(envelopeList.data)

                    MediatorResult.Success(envelopeList.data.isEmpty())
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
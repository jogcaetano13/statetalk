package com.joel.communication_android.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.joel.communication_android.builders.PagingBuilder
import com.joel.communication_android.envelope.EnvelopeList
import com.joel.communication_android.models.PagingModel
import com.joel.communication_android.states.AsyncState

@PublishedApi
internal class NetworkPagingSource<T : PagingModel>(
    private val builder: PagingBuilder<T>,
    private val doApiCall: suspend (page: Int) -> AsyncState<EnvelopeList<T>>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.lastItemOrNull()?.page
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1

        return try {
            val resultState = doApiCall(page)
            val nextPage = page + 1

            return when(resultState) {
                AsyncState.Empty -> LoadResult.Error(Throwable("Empty data"))
                is AsyncState.Error -> LoadResult.Error(Throwable(resultState.error.errorBody))
                is AsyncState.Success -> {
                    val envelopeList = resultState.data
                    envelopeList.data.forEach {
                        it.page = nextPage
                    }

                    builder.insertAll?.let {
                        it(envelopeList.data)
                    }

                    LoadResult.Page(
                        data = envelopeList.data,
                        prevKey = null,
                        nextKey = if (envelopeList.data.size < builder.defaultPageSize)
                            null
                        else
                            nextPage
                    )
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
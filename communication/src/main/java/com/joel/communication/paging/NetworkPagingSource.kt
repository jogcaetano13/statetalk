package com.joel.communication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.joel.communication.builders.PagingBuilder
import com.joel.communication.envelope.EnvelopeList
import com.joel.communication.extensions.apiError
import com.joel.communication.models.ErrorResponse
import com.joel.communication.models.PagingModel
import com.joel.communication.states.AsyncState

@PublishedApi
internal class NetworkPagingSource<T : PagingModel>(
    private val builder: PagingBuilder<T>,
    private val onError: suspend (error: ErrorResponse) -> Unit,
    private val onEmpty: suspend () -> Unit,
    private val onLoading: suspend () -> Unit,
    private val doApiCall: suspend (page: Int) -> AsyncState<EnvelopeList<T>>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.lastItemOrNull()?.page
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1

        return try {

            onLoading()

            val resultState = doApiCall(page)
            val nextPage = page + 1

            return when(resultState) {
                AsyncState.Empty -> {
                    onEmpty()
                    LoadResult.Error(Throwable("Empty data"))
                }
                is AsyncState.Error -> {
                    onError(resultState.error)
                    LoadResult.Error(Throwable(resultState.error.errorBody))
                }
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
            onError(e.apiError)
            LoadResult.Error(e)
        }
    }
}
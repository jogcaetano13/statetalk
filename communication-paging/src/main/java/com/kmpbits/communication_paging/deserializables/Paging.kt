package com.kmpbits.communication_paging.deserializables

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kmpbits.communication_core.deserializables.responseAsync
import com.kmpbits.communication_core.exceptions.CommunicationException
import com.kmpbits.communication_core.request.CommunicationRequest
import com.kmpbits.communication_paging.builders.PagingBuilder
import com.kmpbits.communication_paging.models.PagingModel
import com.kmpbits.communication_paging.sources.NetworkPagingSource
import com.kmpbits.communication_paging.sources.RemoteAndLocalPagingSource
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
inline fun <reified T : PagingModel> CommunicationRequest.responsePaginated(
    crossinline builder: PagingBuilder<T>. () -> Unit = {}
): Flow<PagingData<T>> {
    val pagingBuilder = PagingBuilder<T>().also(builder)

    if (pagingBuilder.onlyApiCall.not() && pagingBuilder.itemsDataSource == null)
        throw CommunicationException("Items datasource must not be null!")

    this@responsePaginated.immutableRequestBuilder.preCall?.invoke()

    return if (pagingBuilder.onlyApiCall) {
        Pager(
            config = PagingConfig(pagingBuilder.defaultPageSize),
            pagingSourceFactory = {
                NetworkPagingSource(
                    pagingBuilder
                ) {
                    updateUrlPage(pagingBuilder.pageQueryName, it)
                    this@responsePaginated.responseAsync()
                }
            }
        ).flow
    } else {
        Pager(
            config = PagingConfig(pagingBuilder.defaultPageSize),
            remoteMediator = RemoteAndLocalPagingSource(
                pagingBuilder
            ) {
                updateUrlPage(pagingBuilder.pageQueryName, it)
                this@responsePaginated.responseAsync()
            },
            pagingSourceFactory = pagingBuilder.itemsDataSource!!
        ).flow
    }
}

@PublishedApi
internal fun CommunicationRequest.updateUrlPage(pageQueryName: String, page: Int) {
    immutableRequestBuilder.updateParameter(pageQueryName, page)
    updateUrl()
}
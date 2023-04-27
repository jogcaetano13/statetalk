@file:Suppress("BlockingMethodInNonBlockingContext")

package com.joel.communication.deserializables

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.joel.communication.builders.PagingBuilder
import com.joel.communication.dispatchers.CommunicationDispatcher
import com.joel.communication.dispatchers.CommunicationDispatcherImpl
import com.joel.communication.exceptions.CommunicationsException
import com.joel.communication.extensions.updateUrl
import com.joel.communication.models.PagingModel
import com.joel.communication.paging.NetworkPagingSource
import com.joel.communication.paging.RemoteAndLocalPagingSource
import com.joel.communication.request.CommunicationRequest
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
inline fun <reified T : PagingModel> CommunicationRequest.responsePaginated(
    dispatcher: CommunicationDispatcher = CommunicationDispatcherImpl,
    crossinline builder: PagingBuilder<T>. () -> Unit = {}
): Flow<PagingData<T>> {
    val pagingBuilder = PagingBuilder<T>().also(builder)


    if (pagingBuilder.onlyApiCall.not() && pagingBuilder.itemsDataSource == null)
        throw CommunicationsException("Items datasource must not be null!")

    this@responsePaginated.builder.preCall?.invoke()

    return if (pagingBuilder.onlyApiCall) {
        Pager(
            config = PagingConfig(pagingBuilder.defaultPageSize),
            pagingSourceFactory = {
                NetworkPagingSource(
                    pagingBuilder
                ) {
                    updateUrlPage(pagingBuilder.pageQueryName, it)
                    this@responsePaginated.responseStateAsync(dispatcher)
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
                this@responsePaginated.responseStateAsync(dispatcher)
            },
            pagingSourceFactory = pagingBuilder.itemsDataSource!!
        ).flow
    }
}

@PublishedApi
internal fun CommunicationRequest.updateUrlPage(pageQueryName: String, page: Int) {
    builder.updateParameter(pageQueryName, page)
    requestBuilder.updateUrl(builder)
}
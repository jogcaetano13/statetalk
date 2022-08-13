package com.joel.communication.extensions

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.joel.communication.states.PagingLoadState

fun CombinedLoadStates.toPagingLoadingState() = when {
    refresh is LoadState.Loading -> PagingLoadState.Loading
    append is LoadState.Loading -> PagingLoadState.BottomLoading
    refresh is LoadState.Error -> PagingLoadState.Error((refresh as LoadState.Error).error)
    append is LoadState.Error -> PagingLoadState.Error((append as LoadState.Error).error)
    else -> PagingLoadState.Nothing
}
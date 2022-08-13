package com.joel.communication.states

sealed class PagingLoadState {
    object Loading : PagingLoadState()
    object BottomLoading : PagingLoadState()
    data class Error(val error: Throwable) : PagingLoadState()
    // This should not be used. It is only for unused paging states
    object Nothing : PagingLoadState()
}

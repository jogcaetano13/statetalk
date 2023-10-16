# Communication Android
## Lightweight network library written in Kotlin

[![](https://jitpack.io/v/jogcaetano13/communication.svg)](https://jitpack.io/#jogcaetano13/communication)

## Features

- LiveData responses
- Flow responses
- Object deserialization
- Paging response

## Installation

First you need to add the jitpack to ```settings.gradle```

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }
}
```

Install the dependencies via ```build.gradle``` (app module).

```kotlin
dependencies {
    //...
    // This is mandatory
    implementation("com.github.jogcaetano13.communication:communication-core:<latest_version>")
    
    // This dependency is optional, only if you want to make requests for livedata or flow
    implementation("com.github.jogcaetano13.communication:communication-android:<latest_version>")

    // This dependency is optional, only if you want to make requests for paging
    implementation("com.github.jogcaetano13.communication:communication-paging:<latest_version>")
}
```

## Using

### Initiation

Initiate the client in singleton class or dependency injection

```kotlin
val client = communicationClient {
    baseUrl = BASE_URL
}
```
*You can add another customization, like header.*

### Making a request

To make a request, you only need to invoke the call function in the Client instance.

##### Normal response

````kotlin
val response: ComunicationResponse = client.call {
    path = PATH
    
}.response()
````

You can also call and deserialize to a specific object

````kotlin
val response: Model = client.call {
    path = PATH
    
}.responseToModel<Model>()
````

##### Flow (Only available in communication-android)

```kotlin
val response: Flow<ResultState<Model>> = client.call {
    path = PATH

    parameter(key to value)

}.responseFlow()
```

###### You can add more customization like change the method, replace local call on success response and observing that local data

```kotlin
val response: Flow<ResultState<Model>> = client.call {
    path = PATH
    method = HttpMethod.Post

}.responseFlow {
    onNetworkSuccess { data ->
        /* Do anything with the response, like replace local database data */
    }
    
    local {
        observe { /* Keep track on local database changes */ }
    }
}
```

##### Paging response (Only available in communication-android)

You don't need to provide the page, it will be increased automatically when it needs.

```kotlin
val response: Flow<PagingData<Model>> = call {
    path = PATH

    parameter(key, value)
}.responsePaginated {
    onlyApiCall = true

    // You can also change the parameter "page" name
    pageQueryName = "wherever your page parameter is called"

    // These 3 functions are mandatory if 'onlyApiCall' is false
    pagingSource {  }
    deleteAll {  }
    insertAll {  }

    // If you want to show loading only if it hasn't items, provide the first nullable item from database,
    // otherwise, loading will trigger every time the screen is opened.
    firstItemDatabase { /* The first item of the database in flow */ }
}
```

The response paginated doesn't return a state, so you need to handle the loading and errors in the ui, for example:

```kotlin
lifecycleScope.launch { 
    adapter.loadStateFlow.collectLatest {
        if (it.refresh is LoadState.Error) {
            Toast.makeText(
                this@MainActivity,
                (it.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT).show()
        }

        binding.loadingPb.isVisible = it.refresh is LoadState.Loading
    }
}
```

For the loading and error when load more, you need to create a LoadStateAdapter.

##### Using the response flow

*Don't need to launch a coroutine in another thread, the library does it internally.*

```kotlin
response.observe(this) {
    when(it) {
        is ResultState.Error -> {}
        is ResultState.Loading -> {}
        is ResultState.Empty -> {}
        is ResultState.Success -> {}
    }
}
```

```kotlin
lifecycleScope.launch {
    response.collectLatest {
        when(it) {
            ResultState.Empty -> {}
            is ResultState.Error -> {}
            ResultState.Loading -> {}
            is ResultState.Success -> {}
        }
    }
}
```

## License

MIT

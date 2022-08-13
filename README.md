# Communication Android
## Lightweight network library written in Kotlin

## Features

- LiveData responses
- Flow responses
- Object deserialization
- Paging response

## Installation

Install the dependencies via ```build.gradle```.

```kotlin
dependencies {
    ...
    implementation("com.github.joel.libraries:communication:1.0.0")
}
```

## Using

### Initiation

Initiate the client in singleton class or dependency injection

```kotlin
val client = communicationClient {
    baseUrl = "BASE_URL"
}
```
*You can add another customization, like header and parameters.*

### Making a request

To make a request, you only need to invoke the call function in the Client instance.

##### Flow

```kotlin
val response: Flow<ResultState<Model>> = client.call {
    path = PATH

    parameter(key, value)

}.responseFlow()
```

##### Paging response

```kotlin
val response: Flow<ResultState<PagingData<Model>>> = call {
    path = PATH

    parameter(key, value)
}.responsePaginated {
    onlyApiCall = true

    // These 3 functions are mandatory if 'onlyApiCall' is false
    pagingSource {  }
    deleteAll {  }
    insertAll {  }

    // If you want to show loading only if it hasn't items, provide the first nullable item from database,
    // otherwise, loading will trigger every time the screen is opened.
    firstItemDatabase { dao.getChallenge() }
}
```

##### Using the response flow

*Don't need to launch a coroutine in another thread, the library does that internally.*

```kotlin
viewModel.challenges().observe(this) {
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
    viewModel.getChallenges().collectLatest {
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

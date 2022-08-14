# Communication Android
## Lightweight network library written in Kotlin

[![](https://jitpack.io/v/jogcaetano13/communication_android.svg)](https://jitpack.io/#jogcaetano13/communication_android)

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
    ...
    implementation("com.github.jogcaetano13:communication_android:<latest_version>")
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

You don't need to provide the page, it will be increased automatically when it needs.

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

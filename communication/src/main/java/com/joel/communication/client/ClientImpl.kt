package com.joel.communication.client

import com.joel.communication.builders.ClientBuilder
import com.joel.communication.calls.Call
import com.joel.communication.client.interceptors.CustomHeaderInterceptor
import com.joel.communication.client.interceptors.LoggingInterceptor
import com.joel.communication.request.CommunicationRequest
import com.joel.communication.request.RequestBuilder
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

internal class ClientImpl private constructor() : Client {

    companion object {
        @Volatile private var thisInstance: ClientImpl? = null

        val instance = thisInstance ?: synchronized(this) {
            ClientImpl().also { thisInstance = it }
        }
    }

    internal var baseUrl: String = ""

    internal var builder: ClientBuilder = ClientBuilder.instance

    internal val client = createClient()

    @PublishedApi
    internal fun call(request: CommunicationRequest) = client.newCall(request.request)

    /**
     * This method should be called for every request.
     *
     * @param builder Have the customization of the request like the path and headers.
     * @return A [CommunicationRequest] that can be used to deserialize the response.
     */
    override fun call(builder: RequestBuilder. () -> Unit) = Call().request(builder)

    private fun createClient(): OkHttpClient {
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }
        val trustManager: X509TrustManager = trustManagers[0] as X509TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .addDefaultInterceptor()
            .addInterceptor(CustomHeaderInterceptor(builder.headers))
            .build()
    }

    private fun OkHttpClient.Builder.addDefaultInterceptor(): OkHttpClient.Builder {
        val loggingInterceptor = LoggingInterceptor().also {
            it.level = builder.logLevel
        }

        addInterceptor(loggingInterceptor)

        builder.interceptors.forEach {
            addInterceptor(it)
        }

        connectTimeout(builder.timeoutBuilder.connectionTimeout.millis, TimeUnit.MILLISECONDS)
        readTimeout(builder.timeoutBuilder.readTimeout.millis, TimeUnit.MILLISECONDS)
        writeTimeout(builder.timeoutBuilder.writeTimeout.millis, TimeUnit.MILLISECONDS)
        return this
    }
}
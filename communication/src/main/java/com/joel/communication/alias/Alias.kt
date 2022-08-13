package com.joel.communication.alias

import com.joel.communication.enums.HttpHeader

/**
 * @author joelcaetano
 * Created 28/11/2021 at 13:39
 */
typealias Header = Pair<HttpHeader, String>
typealias Parameter = Pair<String, Any?>

typealias Headers = MutableList<Header>
typealias Parameters = MutableList<Parameter>
typealias Body = Map<String, Any?>
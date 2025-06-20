package com.kmpbits.communication_core.alias

import com.kmpbits.communication_core.enums.HttpHeader

typealias Header = Pair<HttpHeader, String>
typealias Parameter = Pair<String, Any?>

typealias Headers = MutableList<Header>
typealias Parameters = MutableList<Parameter>
typealias Body = Map<String, Any?>
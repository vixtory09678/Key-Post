package com.wannachat.roboticsza.keypost.model.firebase

data class ListKeyValue(
        val content:String = "",
        val title:String = ""
)

data class ListKeyValueData(
        val key:String,
        val listKeyValue:ListKeyValue
)

package com.wannachat.roboticsza.keypost.adapter.recycleview.model

import java.io.Serializable

class KeyValueData(val key: String, val keyValues : List<KeyValue>?): Serializable
class KeyValue(val content:String = "" ,val title:String = "") : Serializable
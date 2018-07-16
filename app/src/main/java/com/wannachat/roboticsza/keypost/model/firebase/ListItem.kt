package com.wannachat.roboticsza.keypost.model.firebase

data class ListItem(
        val score:Int = 0,
        val color:Int =0,
        val title:String = ""
)

data class ListItemData(
        val key:String,
        val listItem: ListItem
)

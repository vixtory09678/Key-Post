package com.wannachat.roboticsza.keypost.adapter.recycleview.holder

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.wannachat.roboticsza.keypost.R

class ListItemKeyValueHolder : BaseViewRecycleHolder{
    private var context: Context
    private var txtTitle: TextView
    private var txtContent: TextView

    constructor(itemView: View, context: Context) : super(itemView){
        txtTitle = itemView.findViewById(R.id.item_key_value_title) as TextView
        txtContent = itemView.findViewById(R.id.item_key_value_content) as TextView
        this.context = context
    }

    fun setTitle(title:String){
        txtTitle.text = title
    }

    fun setContent(content:String) {
        txtContent.text = content
    }
}
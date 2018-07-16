package com.wannachat.roboticsza.keypost.adapter.recycleview.holder

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.wannachat.roboticsza.keypost.R
import kotlinx.android.synthetic.main.item_keypost.view.*

class ListItemKeyPostHolder : BaseViewRecycleHolder {
    private var context: Context
    private var tabColor: View
    private var txtTitle: TextView
    var btnEdit: Button
    private var scoreBar: ProgressBar

    constructor(itemView: View, context: Context) : super(itemView){
        tabColor = itemView.findViewById(R.id.itemTabColor) as View
        txtTitle = itemView.findViewById(R.id.itemTextTitle) as TextView
        btnEdit = itemView.findViewById(R.id.itemBtnEdit) as Button
        scoreBar = itemView.findViewById(R.id.score) as ProgressBar
        this.context = context
    }

    fun setColorTab(color:Int){
        tabColor.setBackgroundColor(color)
    }

    fun setTitle(strTitle:String) {
        txtTitle.text = strTitle
    }

    fun setScore(score:Int){
        if(score in 1..100){
            scoreBar.progress = score
        }
    }
}
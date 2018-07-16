package com.wannachat.roboticsza.keypost.adapter.recycleview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wannachat.roboticsza.keypost.R
import com.wannachat.roboticsza.keypost.adapter.recycleview.holder.BaseViewRecycleHolder
import com.wannachat.roboticsza.keypost.adapter.recycleview.holder.ListItemKeyPostHolder
import com.wannachat.roboticsza.keypost.model.firebase.ListItemData
import org.jetbrains.anko.sdk25.coroutines.onClick

class ListItemKeyPostAdapter(private var context: Context) : RecyclerView.Adapter<BaseViewRecycleHolder>() {

    private var listItem: ArrayList<ListItemData>? = null
    private var mOnEditButtonClick: OnEditButtonClick? = null

    public fun setData(data: ArrayList<ListItemData>): ListItemKeyPostAdapter {
        listItem = data
        return this
    }

    fun setOnEditButtonClick(onEditClick: OnEditButtonClick?){
        mOnEditButtonClick = onEditClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewRecycleHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_keypost,parent,false)
        return ListItemKeyPostHolder(view,context)
    }

    override fun getItemCount(): Int {
        return listItem!!.size
    }

    override fun onBindViewHolder(holder: BaseViewRecycleHolder, position: Int) {
        val data = listItem!![position].listItem

        when(holder){
            is ListItemKeyPostHolder -> {
                holder.setColorTab(data.color)
                holder.setTitle(data.title)
                holder.setScore(data.score)

                holder.btnEdit.onClick {
                    mOnEditButtonClick ?: return@onClick
                    mOnEditButtonClick?.onEditButtonClick(position)
                }
            }
        }
    }

    interface OnEditButtonClick{
        fun onEditButtonClick(position: Int)
    }

}
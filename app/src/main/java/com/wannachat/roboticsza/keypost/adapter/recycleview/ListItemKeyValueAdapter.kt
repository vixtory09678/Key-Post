package com.wannachat.roboticsza.keypost.adapter.recycleview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wannachat.roboticsza.keypost.R
import com.wannachat.roboticsza.keypost.adapter.recycleview.holder.BaseViewRecycleHolder
import com.wannachat.roboticsza.keypost.adapter.recycleview.holder.ListItemKeyValueHolder
import com.wannachat.roboticsza.keypost.adapter.recycleview.model.KeyValue
import org.jetbrains.anko.sdk25.coroutines.onLongClick

class ListItemKeyValueAdapter(private var context: Context) : RecyclerView.Adapter<BaseViewRecycleHolder>() {

    private var data:ArrayList<KeyValue>? = null
    private var mOnLongClickItemKeyValue:OnLongClickItemKeyValue? = null


    fun setData(data: ArrayList<KeyValue>?): ListItemKeyValueAdapter {
        this.data = data
        return this
    }

    fun setOnLongClickItemKeyValue(onLongClick: OnLongClickItemKeyValue){
        this.mOnLongClickItemKeyValue = onLongClick
    }

    fun deleteItem(position: Int){
        data?.remove(data?.get(position))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewRecycleHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_list_key_value,parent,false)
        return ListItemKeyValueHolder(view,context)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: BaseViewRecycleHolder, position: Int) {
        data ?: return

        val dataGet = data!![position]
        when(holder){
            is ListItemKeyValueHolder -> {
                holder.setTitle(dataGet.title)
                holder.setContent(dataGet.content)
            }
        }

        holder.itemView.onLongClick {
            mOnLongClickItemKeyValue?.onLongClickItemKeyValue(position)
        }

    }

    interface OnLongClickItemKeyValue{
        fun onLongClickItemKeyValue(position: Int)
    }
}
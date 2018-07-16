package com.wannachat.roboticsza.keypost

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import com.wannachat.roboticsza.keypost.adapter.recycleview.ListItemKeyValueAdapter
import com.wannachat.roboticsza.keypost.adapter.recycleview.model.KeyValue
import com.wannachat.roboticsza.keypost.common.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_key_post_acitivity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.io.Serializable

class EditKeyPostActivity : BaseActivity() ,ListItemKeyValueAdapter.OnLongClickItemKeyValue {

    private lateinit var listItemKeyValueAdapter:ListItemKeyValueAdapter

    private lateinit var recyclerView: RecyclerView

    private val KEY_VALUE_CODE = 11
    private var colorPick = -1

    private val WARPER_DELETE = 0
    private val WARPER_SAVE = 1
    private val WARPER_ADD = 2

    object DataFromMain{
        const val EDIT_KEY = "EDIT_KEY"
        const val ADD_KEY = "ADD_KEY"

        var status:String? = ""
        var title:String? = ""
        var color:Int? = -1
        var keyValue: ArrayList<KeyValue>? = ArrayList()
        var lastScore:Int? = -1
    }

    private lateinit var rfabHelper: RapidFloatingActionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_key_post_acitivity)
        setupBindView()
        setup()
    }

    private fun setupBindView(){
        recyclerView = findViewById(R.id.edit_key_post_recycle) as RecyclerView
    }

    private fun setup(){
        recyclerView.layoutManager = LinearLayoutManager(this@EditKeyPostActivity,
                LinearLayoutManager.VERTICAL,
                false)

        listItemKeyValueAdapter = ListItemKeyValueAdapter(applicationContext)
        listItemKeyValueAdapter.setOnLongClickItemKeyValue(this)


        val colorArray = resources.getIntArray(R.array.demo_colors)
        palette.setColors(colorArray)
        palette.setOnColorSelectedListener {
            colorPick = it
        }
        setButtonMenu()

        checkTypeActivity()
    }

    private fun checkTypeActivity(){

        DataFromMain.status = intent?.getStringExtra("STATUS")
        DataFromMain.title = intent?.getStringExtra("TITLE")
        DataFromMain.color = intent?.getIntExtra("COLOR", Color.WHITE) ?: 0
        DataFromMain.keyValue = intent?.extras?.get("KEY_VALUE") as ArrayList<KeyValue>
        DataFromMain.lastScore = intent?.getIntExtra("SCORE",0)

        if(DataFromMain.status == DataFromMain.ADD_KEY) return

        edit_key_post_title.setText(DataFromMain.title)
        palette.setSelectedColor(DataFromMain.color!!)
        recyclerView.adapter = listItemKeyValueAdapter.setData(DataFromMain.keyValue!!)

    }

    private fun setButtonMenu(){
        val rfaContent = RapidFloatingActionContentLabelList(this)

        val items = ArrayList<RFACLabelItem<Any>>()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(object : RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any>{
            override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position){
                    WARPER_ADD -> {
                        val intent = Intent(applicationContext,KeyValueEditActivity::class.java)
                        startActivityForResult(intent,KEY_VALUE_CODE)
                    }

                    WARPER_SAVE -> {
                        sendResultBack()
                    }
                }

                rfabHelper.toggleContent()
            }

            override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
                when(position){
                    WARPER_ADD -> {
                        val intent = Intent(applicationContext,KeyValueEditActivity::class.java)
                        startActivityForResult(intent,KEY_VALUE_CODE)
                    }

                    WARPER_SAVE -> {
                        sendResultBack()
                    }
                }

                rfabHelper.toggleContent()
            }
        })

        items.add(RFACLabelItem<Any>()
                .setLabel("Delete")
                .setResId(R.drawable.ic_delete_black_24dp)
                .setIconNormalColor(Color.parseColor("#ffff0000"))
                .setIconPressedColor(Color.parseColor("#ff3e2723"))
                .setLabelColor(Color.BLACK)
                .setLabelSizeSp(14)
                .setWrapper(WARPER_DELETE)
        )

        items.add(RFACLabelItem<Any>()
                .setLabel("Save")
                .setResId(R.drawable.ic_save_black_24dp)
                .setIconNormalColor(Color.parseColor("#ff0000ff"))
                .setIconPressedColor(Color.parseColor("#ff3e2723"))
                .setLabelColor(Color.BLACK)
                .setLabelSizeSp(14)
                .setWrapper(WARPER_SAVE)
        )

        items.add(RFACLabelItem<Any>()
                .setLabel("Add Key")
                .setResId(R.drawable.ic_playlist_add_black_24dp)
                .setIconNormalColor(Color.parseColor("#ff00ff00"))
                .setIconPressedColor(Color.parseColor("#ff3e2723"))
                .setLabelColor(Color.BLACK)
                .setLabelSizeSp(14)
                .setWrapper(WARPER_ADD)
        )



        rfaContent.items = items
        rfabHelper = RapidFloatingActionHelper(this,activity_main_rfal,activity_main_rfab,rfaContent).build()


    }

    override fun onLongClickItemKeyValue(position: Int) {
        alert ("Do you want to delete this item?"){
            yesButton { listItemKeyValueAdapter.deleteItem(position) }
            noButton {  }
        }.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            KEY_VALUE_CODE ->{
                if(resultCode == Activity.RESULT_OK){
                    data ?: return
                    val key = data.getStringExtra(KeyValueEditActivity().KEY)
                    val value = data.getStringExtra(KeyValueEditActivity().VALUE)
                    DataFromMain.keyValue!!.add(KeyValue(key,value))
                    recyclerView.adapter = listItemKeyValueAdapter.setData(DataFromMain.keyValue!!)
                }
            }
        }
    }

    private fun sendResultBack(){
        val title = edit_key_post_title.text.toString()

        if(title.isEmpty()){
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        val intentResult = intent

        intentResult.putExtra("TITLE",title)
        intentResult.putExtra("COLOR",colorPick)
        intentResult.putExtra("SCORE",DataFromMain.lastScore)
        intentResult.putExtra("KEY_VALUE",DataFromMain.keyValue!! as Serializable)

        setResult(Activity.RESULT_OK,intentResult)
        finish()
    }
}

package com.wannachat.roboticsza.keypost

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.wannachat.roboticsza.keypost.adapter.recycleview.ListItemKeyPostAdapter
import com.wannachat.roboticsza.keypost.adapter.recycleview.model.KeyValue
import com.wannachat.roboticsza.keypost.adapter.recycleview.model.KeyValueData
import com.wannachat.roboticsza.keypost.common.BaseActivity
import com.wannachat.roboticsza.keypost.constant.KeyPostConstantPath
import com.wannachat.roboticsza.keypost.constant.KeyValuesConstantPath
import com.wannachat.roboticsza.keypost.model.firebase.ListItem
import com.wannachat.roboticsza.keypost.model.firebase.ListItemData

import kotlinx.android.synthetic.main.activity_main_post_key.*
import org.jetbrains.anko.indeterminateProgressDialog
import java.io.Serializable

class MainKeyPostActivity : BaseActivity(), ListItemKeyPostAdapter.OnEditButtonClick {

    private val ADD_KEYPOST = 1
    private val EDIT_KEYPOST = 2


    private lateinit var recycler: RecyclerView
    lateinit var listItem:ArrayList<ListItemData>

    var keyFirebaseEdit:String = ""

    lateinit var listKeyValue:ArrayList<KeyValue>
    lateinit var listKeyValueData:ArrayList<KeyValueData>

    private lateinit var listItemKeyPostAdapter: ListItemKeyPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_post_key)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { it ->
            val intent = Intent(this@MainKeyPostActivity,EditKeyPostActivity::class.java)

            intent.putExtra("STATUS",EditKeyPostActivity.DataFromMain.EDIT_KEY)

            intent.putExtra("TITLE","")
            intent.putExtra("COLOR",resources.getIntArray(R.array.demo_colors)[0])
            intent.putExtra("SCORE",0)
            intent.putExtra("KEY_VALUE", listOf<KeyValue>() as Serializable)

            startActivityForResult(intent,ADD_KEYPOST)
        }
        setup()
        setupFirebase()
    }

    private fun setup(){
        listItem = ArrayList()
        listKeyValue = ArrayList()
        listKeyValueData = ArrayList()
        listItemKeyPostAdapter = ListItemKeyPostAdapter(applicationContext)
        recycler = findViewById(R.id.recycleViewKeyPost) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this@MainKeyPostActivity,
                LinearLayoutManager.VERTICAL,
                false)

        listItemKeyPostAdapter.setOnEditButtonClick(this)
    }

    override fun onEditButtonClick(position: Int) {
        openEditKeyPostActivity(position)

    }

    private fun openEditKeyPostActivity(position: Int) {
        val intentResult = Intent(this,EditKeyPostActivity::class.java)

        intentResult.putExtra("STATUS",EditKeyPostActivity.DataFromMain.EDIT_KEY)

        intentResult.putExtra("TITLE",listItem[position].listItem.title)
        intentResult.putExtra("COLOR",listItem[position].listItem.color)
        intentResult.putExtra("SCORE",listItem[position].listItem.score)
        intentResult.putExtra("KEY_VALUE",listKeyValue as Serializable)

        startActivityForResult(intentResult,EDIT_KEYPOST)
    }

    private fun setupFirebase(){
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser ?: return
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val mRefAddChild = database.reference.child(KeyPostConstantPath.NAME).child(user?.uid)
        val mRefKeyValue = database.reference.child(KeyValuesConstantPath.NAME).child(user?.uid)

        val childEventKeyValueAdd = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(data: DataSnapshot?, p1: String?) {
                val dataStr = data.toString()
                println(dataStr)
            }
            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                val dataStr = data.toString()
                println(dataStr)
            }
            override fun onChildAdded(data: DataSnapshot?, p1: String?) {

                val dataStr = data.toString()
                println(dataStr)
                data ?: return
                if(data.exists()){
                    for(item in data.children){
                        val key = item.key
                        val itemStr = item.toString()
                        println(itemStr)
                        val dataGet = item.getValue<KeyValue>(KeyValue::class.java) ?: return
                        listKeyValue.add(dataGet)
                    }
                    listKeyValueData.add(KeyValueData(data.key,listKeyValue))
                }

            }
            override fun onChildRemoved(data: DataSnapshot?) {
                val dataStr = data.toString()
                println(dataStr)
            }
        }


        val childEventAddChild = object : ChildEventListener{
            override fun onCancelled(data: DatabaseError?) {}
            override fun onChildMoved(data: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(data: DataSnapshot?, p1: String?) {
                val dataStr = data.toString()
                println(dataStr)
            }
            override fun onChildAdded(data: DataSnapshot?, p1: String?) {

                val dataStr = data.toString()
                println("childEvent Add child $dataStr")
                data ?: return
                if(data.exists()){
                    val key = data.key
                    val dataGet = data.getValue<ListItem>(ListItem::class.java) ?: return
                    listItem.add(ListItemData(key,dataGet))
                    updateUi()

                    keyFirebaseEdit = key
                }

            }
            override fun onChildRemoved(data: DataSnapshot?) {

                val key = data!!.key
                for(list in listItem){
                    val keyList = list.key
                    if(key == keyList){
                        listItem.remove(list)
                        updateUi()
                        break
                    }
                }
            }
        }

        mRefAddChild.addChildEventListener(childEventAddChild)
        mRefKeyValue.addChildEventListener(childEventKeyValueAdd)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) return

        val dialogLoad = indeterminateProgressDialog("Loading..")
        dialogLoad.show()

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser ?: return
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        val title:String = data?.getStringExtra("TITLE") ?: ""
        val color = data?.getIntExtra("COLOR", Color.WHITE) ?: 0
        val keyValue = data?.extras?.get("KEY_VALUE") as ArrayList<KeyValue>
        val lastScore = data?.getIntExtra("SCORE",0)

        when(requestCode){
            ADD_KEYPOST ->{
                var mRef = database.getReference(KeyPostConstantPath.NAME).child(user?.uid)
                val item = ListItem(lastScore, color, title)

                val key = mRef.push().key
                mRef.child(key).setValue(item)

                mRef = database.getReference(KeyValuesConstantPath.NAME).child(user?.uid)
                mRef.child(key).setValue(keyValue)
            }

            EDIT_KEYPOST ->{
                var mRef = database.getReference(KeyPostConstantPath.NAME).child(user?.uid)
                val item = ListItem(lastScore, color, title)


                mRef.child(keyFirebaseEdit).setValue(item)

                mRef = database.getReference(KeyValuesConstantPath.NAME).child(user?.uid)
                mRef.child(keyFirebaseEdit).setValue(keyValue)

            }
        }
        dialogLoad.dismiss()
    }

    fun updateUi(){
        recycler.adapter = listItemKeyPostAdapter.setData(listItem)
    }
}

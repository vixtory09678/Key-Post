package com.wannachat.roboticsza.keypost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.wannachat.roboticsza.keypost.common.BaseActivity
import kotlinx.android.synthetic.main.activity_key_value_edit.*

class KeyValueEditActivity : BaseActivity() ,View.OnClickListener {

    val VALUE = "value"
    val KEY = "key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_value_edit)
        setup()
    }

    private fun setup(){
        edit_key_value_btn_add.setOnClickListener(this@KeyValueEditActivity)
        edit_key_value_key.imeOptions = EditorInfo.IME_ACTION_NEXT
        edit_key_value_value.imeOptions = EditorInfo.IME_ACTION_DONE

        edit_key_value_key.setOnEditorActionListener{v,actionId,event ->
            var handled = false
            if(actionId == EditorInfo.IME_ACTION_NEXT){
                edit_key_value_key.nextFocusDownId = R.id.edit_key_value_value
            }
            return@setOnEditorActionListener handled
        }

        edit_key_value_value.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if(actionId == EditorInfo.IME_ACTION_DONE){
                saveDataAndExit()
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }

    override fun onClick(v: View?) {
        saveDataAndExit()
    }

    private fun saveDataAndExit(){
        val key = edit_key_value_key.text.toString()
        val value = edit_key_value_value.text.toString()

        if(key.isEmpty() || value.isEmpty()){
            showBottomMessage("some data not fill")
            return
        }

        val getIntent:Intent = intent
        getIntent.putExtra(KEY,key)
        getIntent.putExtra(VALUE,value)
        setResult(Activity.RESULT_OK,getIntent)
        hideKeyboard()
        finish()
    }
}

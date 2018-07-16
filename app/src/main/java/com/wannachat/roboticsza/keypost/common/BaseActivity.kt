package com.wannachat.roboticsza.keypost.common;

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.wannachat.roboticsza.keypost.LoginActivity
import com.wannachat.roboticsza.keypost.R
import org.jetbrains.anko.startActivity

open class BaseActivity : AppCompatActivity() {

    protected fun getRootView(): View {
        return findViewById<View>(android.R.id.content)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item == null) return false
        when (item.itemId){
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                startActivity<LoginActivity>()
                finish()
                return true
            }
        }
        return return super.onOptionsItemSelected(item)
    }


    protected fun showBottomMessage(message: String) {
        Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT).show()
    }

    protected fun showBottomMessage(message: String, durationType: Int) {
        Snackbar.make(getRootView(), message, durationType).show()
    }

    protected fun showBottomMessage(strResId: Int) {
        Snackbar.make(getRootView(), strResId, Snackbar.LENGTH_SHORT).show()
    }

    protected fun showBottomMessage(strResId: Int, durationType: Int) {
        Snackbar.make(getRootView(), strResId, durationType).show()
    }

    protected fun showPopupMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showPopupMessage(strResId: Int) {
        Toast.makeText(this, strResId, Toast.LENGTH_SHORT).show()
    }

    protected fun openActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
        finish()
    }


    protected fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0)
    }
}

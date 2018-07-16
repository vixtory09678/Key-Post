package com.wannachat.roboticsza.keypost;

import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.wannachat.roboticsza.keypost.common.BaseActivity
import java.util.*
import com.facebook.AccessToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.wannachat.roboticsza.keypost.model.firebase.Users
import kotlinx.android.synthetic.main.login_activity.*


public class LoginActivity : BaseActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var mAuth: FirebaseAuth
    private val EMAIL:String = "email"
    private val PUBLIC_PROFILE:String = "public_profile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        registerCallbackFacebook()
    }

    private fun registerCallbackFacebook() {
        callbackManager = CallbackManager.Factory.create()
        mAuth = FirebaseAuth.getInstance()
        login_button.setReadPermissions(Arrays.asList(EMAIL,PUBLIC_PROFILE))
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result?.accessToken)
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException?) {}
        })
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null){
            goToMainActivity()
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken?){

        val credential:AuthCredential = FacebookAuthProvider.getCredential(token!!.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OnCompleteListener {
                    if(it.isSuccessful){
                        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        if (user != null){
                            val database:FirebaseDatabase = FirebaseDatabase.getInstance()
                            var mRef = database.getReference("users").child(user.uid)
                            mRef.setValue(Users(user.uid, user.displayName ?: "", user.email ?: "", user.photoUrl.toString()))
                            goToMainActivity()
                        }
                    }else{

                    }

                })
    }

    private fun goToMainActivity(){
        openActivity(MainKeyPostActivity::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}

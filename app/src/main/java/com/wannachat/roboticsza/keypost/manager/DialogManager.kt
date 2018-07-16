package com.wannachat.roboticsza.keypost.manager

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.wannachat.roboticsza.keypost.R

class DialogManager {

    private lateinit var materialDialog:MaterialDialog

    private object Holder { val INSTANCE = DialogManager() }
    companion object {
        val instance: DialogManager by lazy { Holder.INSTANCE }
    }

    fun showDialog(context: Context, title: String, content: String, positive: String, negative: String, callback: MaterialDialog.SingleButtonCallback) {
        dismissDialog()
        materialDialog = MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .onNegative(callback)
                .onPositive(callback)
                .show()
    }

    fun showLoading(context: Context) {
        dismissDialog()
        materialDialog = MaterialDialog.Builder(context)
                .content(R.string.loading)
                .progress(true, 0)
                .show()
    }

    fun isDialogShowing(): Boolean {
        return materialDialog != null && materialDialog.isShowing
    }

    fun dismissDialog() {
        if (materialDialog != null) {
            materialDialog.dismiss()
        }
    }

}
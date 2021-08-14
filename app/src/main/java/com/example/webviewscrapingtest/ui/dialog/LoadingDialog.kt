package com.example.webviewscrapingtest.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.webviewscrapingtest.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadingDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return context?.let { context ->
            MaterialAlertDialogBuilder(context).apply {
                setView(R.layout.layout_loading)
                isCancelable = false
                background = ColorDrawable(Color.TRANSPARENT)
            }.create()
        } ?: throw IllegalStateException()
    }
}
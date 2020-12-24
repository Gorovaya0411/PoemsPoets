package com.example.poemspoets.ui.dialogFragments

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.poemspoets.ui.activities.R

class ForEmptyJobsDialog(private val callback: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme
        )
        builder.setMessage("К сожалению, у вас нет работ.\nХотите создать их прямо сейчас?)")
            .setPositiveButton("Конечно!") { _, _ ->
                callback.invoke()
            }.setNegativeButton("Не-a") { _, _ ->
                dialog!!.dismiss()
            }
        return builder.create()
    }
}
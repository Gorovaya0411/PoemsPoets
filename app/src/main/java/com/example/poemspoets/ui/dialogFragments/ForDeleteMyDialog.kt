package com.example.poemspoets.ui.dialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poemspoets.ui.activities.R

class ForDeleteMyDialog(private val callback: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(
            requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme
        )

        builder.setMessage("Удалить этот стих из добавленных?")
            .setPositiveButton("Да") { _, _ ->
                callback.invoke()
            }.setNegativeButton("Нет") { _, _ ->
                dialog!!.dismiss()
            }

        return builder.create()
    }
}
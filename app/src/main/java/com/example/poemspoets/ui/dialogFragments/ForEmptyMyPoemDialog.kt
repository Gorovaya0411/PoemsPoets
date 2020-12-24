package com.example.poemspoets.ui.dialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poemspoets.ui.activities.R

class ForEmptyMyPoemDialog(private val callback: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(
            requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert_TestDialogTheme
        )

        builder.setMessage("У вас пока нет добавленных стихов!:(\nХотите приступить к изучению и добавлению прямо сейчас?)")
            .setPositiveButton("Хочу!") { _, _ ->
                callback.invoke()
            }.setNegativeButton("Потом)") { _, _ ->
                dialog!!.dismiss()
            }

        return builder.create()
    }
}
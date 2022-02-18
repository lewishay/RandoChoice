package com.example.randochoice

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.save_input.view.*

class SaveDialog(private val list: ArrayList<String>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save)
            if(list.isNotEmpty()) {
                val view = LayoutInflater.from(it).inflate(R.layout.save_input, null)
                builder.setView(view)
                builder
                    .setPositiveButton(R.string.save, DialogInterface.OnClickListener { _, _ ->
                        val data = list.joinToString("\n")
                        val name = view.saveInput.text.toString()
                        if (name != "") {
                            it.openFileOutput(name, Context.MODE_PRIVATE).use { outputStream ->
                                outputStream.write(data.toByteArray())
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { _, _ ->

                    })
            } else {
                builder.setMessage(R.string.emptyList)
                    .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { _, _ -> })
                builder.create()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package com.example.randochoice

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class SaveDialog(private val list: ArrayList<String>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.save)
            if(list.isNotEmpty()) {
                val view = it.layoutInflater.inflate(R.layout.save_input, null)
                builder.setView(view)
                builder.setPositiveButton(R.string.save) { _, _ ->
                    val data = list.joinToString("\n")
                    val saveInput = view.findViewById<EditText>(R.id.saveInput)
                    val name = saveInput.text.toString()
                    if (name != "") {
                        it.openFileOutput(name, Context.MODE_PRIVATE).use { outputStream ->
                            outputStream.write(data.toByteArray())
                        }
                    }
                }
                builder.setNegativeButton(R.string.cancel) { _, _ -> }
            } else {
                builder.setMessage(R.string.emptyList)
                builder.setPositiveButton(R.string.ok) { _, _ -> }
                builder.create()
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
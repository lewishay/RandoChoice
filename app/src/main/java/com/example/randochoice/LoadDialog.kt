package com.example.randochoice

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import java.io.File

class LoadDialog(
    private val list: ArrayList<String>,
    private val listView: ListView,
    private val listAdapter: ArrayAdapter<String>,
    private val dir: File) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.load)
                .setPositiveButton(R.string.load,
                    DialogInterface.OnClickListener { _, _ ->
                        list.clear()
                        val file = File(dir, "example_list.txt")
                        file.readText().lines().forEach { item ->
                            list.add(item)
                        }
                        listView.adapter = listAdapter
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        // User cancelled the dialog
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

package com.example.randochoice

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.io.File

class LoadDialog(
    private val list: ArrayList<String>,
    private val listView: ListView,
    private val listAdapter: ArrayAdapter<String>,
    private val dir: File,
    private val resultTextView: TextView) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val fileList = dir.listFiles()
            builder.setTitle(R.string.load)
            if(fileList != null && fileList.isNotEmpty()) {
                val fileNameList = fileList.map { file -> file.name }.sorted().toTypedArray()
                builder.setItems(fileNameList) { _, item ->
                    list.clear()
                    val file = File(dir, fileNameList[item])
                    file.readText().lines().forEach { entry ->
                        list.add(entry)
                    }
                    listView.adapter = listAdapter
                    resultTextView.text = ""
                }
                builder.setNegativeButton(R.string.cancel) { _, _ -> }
                builder.create()
            } else {
                builder.setMessage(R.string.noFiles)
                builder.setPositiveButton(R.string.ok) { _, _ -> }
                builder.create()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

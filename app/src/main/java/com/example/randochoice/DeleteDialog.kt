package com.example.randochoice

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.io.File

class DeleteDialog(private val dir: File) : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val fileList = dir.listFiles()
                builder.setTitle(R.string.delete)
                if(fileList != null && fileList.isNotEmpty()) {
                    val fileNameList = fileList.map { file -> file.name }.sorted().toTypedArray()
                    builder.setItems(fileNameList) { _, item ->
                        val file = File(dir, fileNameList[item])
                        file.delete()
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
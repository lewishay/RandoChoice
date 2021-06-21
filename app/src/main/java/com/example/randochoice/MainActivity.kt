package com.example.randochoice

import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import java.lang.StrictMath.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val choiceInput = findViewById<EditText>(R.id.choiceInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val chooseButton = findViewById<Button>(R.id.choiceButton)
        val choiceListView = findViewById<ListView>(R.id.choiceList)
        val choiceList = ArrayList<String>()
        val choiceListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, choiceList)
        choiceListView.adapter = choiceListAdapter
        val resultTextView = findViewById<TextView>(R.id.result)

        addButton.setOnClickListener {
            val input = choiceInput!!.text.toString()
            if (input != "") {
                val ellipsis = if (input.length >= 30) "..." else ""
                choiceList.add(input.take(30) + ellipsis)
                choiceListView.adapter = choiceListAdapter
                choiceInput.setText("")
            }
        }

        chooseButton.setOnClickListener {
            if (choiceList.isNotEmpty()) {
                val random = abs(Random.nextInt() % choiceList.size)
                resultTextView.text = choiceList[random]
            }
        }

        choiceListView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            choiceList.removeAt(position)
            choiceListView.adapter = choiceListAdapter
        }
    }
}
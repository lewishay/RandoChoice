package com.example.randochoice

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.animation.AnimationUtils
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
        val loadButton = findViewById<ImageButton>(R.id.loadButton)
        val saveButton = findViewById<ImageButton>(R.id.saveButton)
        val deleteButton = findViewById<ImageButton>(R.id.deleteButton)
        val clearListButton = findViewById<Button>(R.id.clearButton)
        val choiceListView = findViewById<ListView>(R.id.choiceList)
        val choiceList = ArrayList<String>()
        val choiceListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, choiceList)
        choiceListView.adapter = choiceListAdapter
        val resultTextView = findViewById<TextView>(R.id.result)
        val resultTextAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.result_text_animation)

        val audioAttrib = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        val soundPlayer = SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(2).build()
        val resultSound = soundPlayer.load(this, R.raw.result_sound, 1)

        val loadDialog = LoadDialog(choiceList, choiceListView, choiceListAdapter, filesDir)
        val saveDialog = SaveDialog(choiceList)
        val deleteDialog = DeleteDialog(filesDir)

        fun resultAnimation() {
            resultTextView.startAnimation(resultTextAnimation)
            soundPlayer.play(resultSound, 1f, 1f, 0, 0, 1f)
        }

        addButton.setOnClickListener {
            val input = choiceInput!!.text.toString()
            if (input != "") {
                val ellipsis = if (input.length >= 18) "..." else ""
                choiceList.add(input.take(18) + ellipsis)
                choiceListView.adapter = choiceListAdapter
                choiceInput.setText("")
                resultTextView.text = ""
            }
        }

        chooseButton.setOnClickListener {
            if (choiceList.isEmpty()) {
                resultTextView.text = getString(R.string.emptyList)
                resultAnimation()
            } else {
                val result = choiceList[abs(Random.nextInt() % choiceList.size)]
                resultTextView.text = result
                resultAnimation()
            }
        }

        choiceListView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            choiceList.removeAt(position)
            choiceListView.adapter = choiceListAdapter
        }

        clearListButton.setOnClickListener {
            if (choiceList.isEmpty()) {
                resultTextView.text = getString(R.string.emptyList)
                resultAnimation()
            } else {
                choiceList.clear()
                choiceListView.adapter = choiceListAdapter
                resultTextView.text = ""
            }
        }

        loadButton.setOnClickListener {
            loadDialog.show(supportFragmentManager, getString(R.string.load))
        }

        saveButton.setOnClickListener {
            saveDialog.show(supportFragmentManager, getString(R.string.save))
        }

        deleteButton.setOnClickListener {
            deleteDialog.show(supportFragmentManager, getString(R.string.delete))
        }
    }
}
package com.example.randochoice

import android.animation.AnimatorInflater
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
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

        val audioAttrib = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        val soundPlayer = SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(2).build()
        val resultSound = soundPlayer.load(this, R.raw.result_sound, 1)

        val loadDialog = LoadDialog(choiceList, choiceListView, choiceListAdapter, filesDir, resultTextView)
        val saveDialog = SaveDialog(choiceList)
        val deleteDialog = DeleteDialog(filesDir)

        val listItemAnimation = AnimatorInflater.loadAnimator(applicationContext, R.animator.list_item_animation)
        val resultTextAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.result_text_animation)
        fun resultAnimation(resultText: String) {
            resultTextView.text = resultText
            resultTextView.startAnimation(resultTextAnimation)
            soundPlayer.play(resultSound, 1f, 1f, 0, 0, 1f)
        }

        fun ensureNoAnimation(action: () -> Unit) {
            if (!listItemAnimation.isRunning) action()
        }

        addButton.setOnClickListener { ensureNoAnimation {
            val input = choiceInput!!.text.toString()
            if (input != "") {
                val ellipsis = if (input.length >= 18) "..." else ""
                choiceList.add(input.take(18) + ellipsis)
                choiceListAdapter.notifyDataSetChanged()
                choiceInput.setText("")
                resultTextView.text = ""
            }
        }}

        fun listAnimation(row: Int) {
            if (row == choiceList.size || row > 7) {
                val result = choiceList[abs(Random.nextInt() % choiceList.size)]
                resultAnimation(result)
            } else {
                listItemAnimation.setTarget(choiceListView.getChildAt(row))
                listItemAnimation.start()
                listItemAnimation.doOnEnd {
                    listItemAnimation.removeAllListeners()
                    listAnimation(row + 1)
                }
            }
        }

        chooseButton.setOnClickListener { ensureNoAnimation {
            if (choiceList.isEmpty()) {
                resultAnimation(getString(R.string.emptyList))
            } else {
                resultTextView.text = ""
                listAnimation(0)
            }
        }}

        choiceListView.onItemClickListener = OnItemClickListener { _, _, position, _ -> ensureNoAnimation {
            choiceList.removeAt(position)
            choiceListAdapter.notifyDataSetChanged()
        }}

        clearListButton.setOnClickListener { ensureNoAnimation {
            if (choiceList.isEmpty()) {
                resultAnimation(getString(R.string.emptyList))
            } else {
                choiceList.clear()
                choiceListAdapter.notifyDataSetChanged()
                resultTextView.text = ""
            }
        }}

        loadButton.setOnClickListener { ensureNoAnimation {
            loadDialog.show(supportFragmentManager, getString(R.string.load))
        }}

        saveButton.setOnClickListener {
            saveDialog.show(supportFragmentManager, getString(R.string.save))
        }

        deleteButton.setOnClickListener {
            deleteDialog.show(supportFragmentManager, getString(R.string.delete))
        }
    }
}
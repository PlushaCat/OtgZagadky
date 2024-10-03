package com.example.otgzagadky

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var riddleTextView: TextView
    private lateinit var answerButton: Button
    private lateinit var nextRiddleButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var riddleNumberTextView: TextView

    private val riddles = listOf(
        "Что можно найти только один раз в минуте?",
        "Что имеет шею, но не имеет головы?",
        "Что можно съесть, но нельзя купить?",
        "Что есть у всех людей, но никто не может увидеть?",
        "Что имеет язык, но не может говорить?",
        "Что имеет два лица, но не может улыбаться?",
        "Что есть у дерева, но не у человека?",
        "Что может упасть, но не может разбиться?",
        "Что имеет ноги, но не может ходить?",
        "Что имеет глаза, но не может видеть?",
        "Что может летать, но не имеет крыльев?",
        "Что имеет сердце, но не бьется?",
        "Что можно взять, но нельзя держать?",
        "Что есть у моря, но не у озера?",
        "Что может быть очень большим, но не имеет веса?"
    )

    private val answers = listOf(
        "Буква М",
        "Бутылка",
        "Домашнее задание",
        "Тень",
        "Ботинки",
        "Монета",
        "Корни",
        "Дождь",
        "Стул",
        "Игла",
        "Время",
        "Арбуз",
        "Слово",
        "Песок",
        "Пузырь"
    )

    private var currentRiddleIndex = 0
    private val shownRiddles = mutableSetOf<Int>()
    private var correctAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        riddleTextView = findViewById(R.id.riddleTextView)
        answerButton = findViewById(R.id.answerButton)
        nextRiddleButton = findViewById(R.id.nextRiddleButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        riddleNumberTextView = findViewById(R.id.riddleNumberTextView)

        answerButton.setOnClickListener {
            val intent = Intent(this, AnswerActivity::class.java)
            intent.putExtra("riddle", riddles[currentRiddleIndex])
            intent.putExtra("answers", answers[currentRiddleIndex])
            startActivity(intent)
        }

        nextRiddleButton.setOnClickListener {
            showNextRiddle()
        }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra("correctAnswers", correctAnswersCount)
            intent.putExtra("totalRiddles", riddles.size)
            startActivity(intent)
        }

        statisticsButton.isEnabled = false
    }

    private fun showNextRiddle() {
        if (currentRiddleIndex < riddles.size) {
            while (shownRiddles.contains(currentRiddleIndex)) {
                currentRiddleIndex = Random.nextInt(riddles.size)
            }
            shownRiddles.add(currentRiddleIndex)
            riddleTextView.text = riddles[currentRiddleIndex]
            riddleNumberTextView.text = "Загадка № ${currentRiddleIndex + 1}"
            answerButton.visibility = View.VISIBLE
            nextRiddleButton.visibility = View.GONE
            if (currentRiddleIndex == riddles.size - 1) {
                statisticsButton.isEnabled = true
            }
        } else {

            statisticsButton.isEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val isCorrect = data?.getBooleanExtra("isCorrect", false) ?: false
            if (isCorrect) {
                correctAnswersCount++
            }
            nextRiddleButton.visibility = View.VISIBLE
            answerButton.visibility = View.GONE
        }
    }

}

class StatisticsActivity : AppCompatActivity() {

    private lateinit var correctAnswersTextView: TextView
    private lateinit var totalRiddlesTextView: TextView
    private lateinit var homeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistika)

        correctAnswersTextView = findViewById(R.id.correctAnswersTextView)
        totalRiddlesTextView = findViewById(R.id.totalRiddlesTextView)
        homeButton = findViewById(R.id.homeButton)

        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalRiddles = intent.getIntExtra("totalRiddles", 0)

        correctAnswersTextView.text = "Правильных ответов: $correctAnswers"
        totalRiddlesTextView.text = "Всего загадок: $totalRiddles"

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

class AnswerActivity : AppCompatActivity() {

    private lateinit var riddleTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var checkButton: Button
    private lateinit var answerList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        riddleTextView = findViewById(R.id.riddleTextView)
        answerEditText = findViewById(R.id.answerEditText)
        checkButton = findViewById(R.id.checkButton)

        val riddle = intent.getStringExtra("riddle")
        riddleTextView.text = riddle
        answerList = intent.getStringArrayListExtra("answers") as MutableList<String>

        answerList.shuffle()
        answerEditText.setText("")

        checkButton.setOnClickListener {
            checkAnswer()
        }
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString()
        val correctAnswer = answerList[0]
        val result = userAnswer.toLowerCase() == correctAnswer.toLowerCase()

        val intent = Intent()
        intent.putExtra("isCorrect", result)
        setResult(RESULT_OK, intent)
        finish()
    }
}
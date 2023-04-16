package com.example.czechapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.czechapp.databinding.FragmentFlashCardBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream


class FlashCardFragment : Fragment() {

    lateinit var _binding: FragmentFlashCardBinding
    var cardList: ArrayList<Card> = ArrayList()
    var index = 0
    var translation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFlashCardBinding.inflate(inflater, container, false)
        readFile()

        getRandomCard()
        setupClickListeners()
        return _binding.root

    }

    fun getRandomCard(){
        if (!cardList.isEmpty()) {
            if(cardList.size > 1){
                index = (0..cardList.size-1).random()
                _binding.flashcardTextView.text = cardList.get(index).sentence
            }else{
                _binding.flashcardTextView.text = cardList.get(index).sentence
            }
        }
    }

    private fun setupClickListeners() {

        _binding.nextButton.setOnClickListener{
                getRandomCard()
        }

        _binding.fragmentFlashcard.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(!cardList.isEmpty()){
                    if(!translation && _binding.fragmentFlashcard.startState != _binding.fragmentFlashcard.currentState){
                        Log.d("currentState", "true start state" + _binding.fragmentFlashcard.startState + "current state" + _binding.fragmentFlashcard.currentState)
                        _binding.flashcardTextView.text = cardList.get(index).translation
                        translation = true
                    }else{
                        Log.d("currentState", "false" + _binding.fragmentFlashcard.currentState)
                        _binding.flashcardTextView.text = cardList.get(index).sentence
                        translation = false
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })

    }

    private fun readFile() {
        val path = context?.getFilesDir()
        val letDirectory = File(path, "LET")
        letDirectory.mkdirs()
        val _file = File(letDirectory, "Hey.txt")

        if (_file.exists()) {
            val inputAsString = FileInputStream(_file).bufferedReader().use { it.readText() }
            if (inputAsString == "") {
                Log.d("List empty first fragment", "empty")
            } else {
                Log.d("List File", "List File" + inputAsString)
                var s = Json.decodeFromString<ArrayList<Card>>(inputAsString)
                Log.d("List Decoded", "List Decoded" + s.toString())
                cardList = s
            }
        }
    }

}

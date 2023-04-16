package com.example.czechapp

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.czechapp.databinding.FragmentTranslationBinding
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString


class TranslationFragment : Fragment() {

    private lateinit var _binding: FragmentTranslationBinding
    var cardList: ArrayList<Card> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTranslationBinding.inflate(inflater, container, false)

        readFile()

        addCardToView()
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }


    private fun addCardToView() {
        val listview = _binding.cardView
        var project_adapter: CardAdapter? = null
        project_adapter = CardAdapter(cardList, {index ->
            cardList.removeAt(index)
            project_adapter?.updateCards(cardList)
            uploadFile()
        })
        listview.adapter = project_adapter
    }

    private fun uploadFile(){
        val path = context?.getFilesDir()
        Log.d("contexttwo" ,""+path)
        val letDirectory = File(path, "LET")
        letDirectory.mkdirs()
        val _file = File(letDirectory, "Hey.txt")


        if(_file.exists()) {
            val inputAsString = FileInputStream(_file).bufferedReader().use { it.readText() }
            if(inputAsString != ""){
                _file.delete()
                val jsonData = Json.encodeToString(cardList)
                println(jsonData)
                _file.appendText(jsonData)
                Log.d("List update","File updated")
            }
        }else{
            Log.d("List empty","File is empty")
        }

    }

    private fun readFile(){
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


    private fun setupClickListeners(view: View) {
        _binding.buttonTranslation.setOnClickListener {
            var nextFrag = WriteTranslationFragment()
            requireActivity()!!.supportFragmentManager.beginTransaction()
                .replace((view.parent as ViewGroup).id, nextFrag)
                .addToBackStack(null)
                .commit()
        }


    }



}
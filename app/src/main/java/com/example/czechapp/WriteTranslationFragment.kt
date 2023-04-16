package com.example.czechapp


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.czechapp.databinding.FragmentWriteTranslationBinding
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream

class WriteTranslationFragment : Fragment() {

    private var _binding: FragmentWriteTranslationBinding? = null
    private val binding get() = _binding!!
    var cardList: ArrayList<Card> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteTranslationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        binding.backButton.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStack()
        }
        binding.saveButton.setOnClickListener {

            val path = context?.getFilesDir()
            Log.d("contexttwo" ,""+path)
            val letDirectory = File(path, "LET")
            letDirectory.mkdirs()
            val _file = File(letDirectory, "Hey.txt")


            if(_file.exists()) {
                val inputAsString = FileInputStream(_file).bufferedReader().use { it.readText() }
                if(inputAsString != ""){
                    var s = Json.decodeFromString<ArrayList<Card>>(inputAsString)
                    cardList = s
                    _file.delete()
                }
            }else{
                Log.d("List empty","File is empty")
            }

            var card = Card(1,binding.translationEditText.text.toString(),binding.sentenceEditText.text.toString())
            cardList.add(card)
            Log.d("List size","" +cardList.size)


            val jsonData = Json.encodeToString(cardList)

            println(jsonData)
           _file.appendText(jsonData)
            val fragmentManager: FragmentManager = parentFragmentManager
            fragmentManager.popBackStack()


/*
               var nextFrag = TranslationFragment()
               requireActivity()!!.supportFragmentManager.beginTransaction()
               .replace((view.parent as ViewGroup).id, nextFrag).addToBackStack(null).commit()
*/
  }

}
}
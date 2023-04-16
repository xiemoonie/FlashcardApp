package com.example.czechapp

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.czechapp.CardAdapter.ProjectViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import java.util.ArrayList

class CardAdapter(private var cards: ArrayList<Card>,
                  private val deleteFunction: (Int) -> Unit) : RecyclerView.Adapter<ProjectViewHolder>(){
    override fun getItemCount(): Int {
        return cards.size
    }

    fun updateCards(cards: ArrayList<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(cards[position], position, deleteFunction)
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardText: TextView
        private val cardLayout : MotionLayout
        private var motionListener : MotionLayout.TransitionListener? = null

        init {
            cardText = itemView.findViewById(R.id.cardText)
            cardLayout = itemView.findViewById(R.id.card_layout)

        }

        fun bind(cards: Card, position: Int, deleteFunction: (Int) -> Unit) {
            cardText.text = cards.sentence
            motionListener?.let { cardLayout.removeTransitionListener(it) }
            motionListener =  object : MotionLayout.TransitionListener { override fun onTransitionStarted(
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
                Log.d("hey2","position" +position)
                deleteFunction(position)

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        }
        cardLayout.addTransitionListener(motionListener)
    }

    }
}

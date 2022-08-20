package com.betulnecanli.purplepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.data.model.Goals
import com.betulnecanli.purplepage.databinding.GoalItemBinding

class GoalAdapter(
    private val listener : OnItemClickListener
): RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    inner class GoalViewHolder(private val binding : GoalItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val goal = differ.currentList[position]
                        listener.OnItemClick(goal)
                    }
                }

                gCheckButton.setOnClickListener {
                    val position = adapterPosition
                    if(position!= RecyclerView.NO_POSITION){
                        val goal = differ.currentList[position]
                        listener.onCheckedBoxClick(goal,gCheckButton.isChecked)
                    }
                }
            }

        }


        fun bind(g :Goals){
            binding.apply {
                gTitle.text = g.goalTitle
                gCheckButton.isChecked = g.isCheckedG
                gTitle.paint.isStrikeThruText = g.isCheckedG
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Goals>(){
        override fun areItemsTheSame(oldItem: Goals, newItem: Goals): Boolean {
           return oldItem.uidG == newItem.uidG
        }

        override fun areContentsTheSame(oldItem: Goals, newItem: Goals): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)
    var mGoals : List<Goals>
        get() = differ.currentList
        set(value){
            differ.submitList(value)
        }


    interface OnItemClickListener{
        fun OnItemClick(g : Goals)
        fun onCheckedBoxClick(g:Goals, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder(
            GoalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = mGoals[position]
        holder.bind(goal)
    }

    override fun getItemCount(): Int = mGoals.size

}
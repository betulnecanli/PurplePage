package com.betulnecanli.purplepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.databinding.SubjectItemBinding
import com.betulnecanli.purplepage.data.model.Subjects

class SubjectAdapter(
    private val listener :OnItemClickListener
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(
        private val binding : SubjectItemBinding
        ) : RecyclerView.ViewHolder(binding.root)
    {
        init{
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val sub = differ.currentList[position]
                        listener.onItemClick(sub)
                    }
                }

                subjectCheck.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val sub = differ.currentList[position]
                        listener.onCheckBoxClick(sub, subjectCheck.isChecked)

                    }
                }

            }
        }
        fun bind(subject : Subjects ){
            binding.apply {
                subjectText.text = subject.subjectTitle
                subjectCheck.isChecked = subject.isChecked
                subjectText.paint.isStrikeThruText = subject.isChecked
            }
        }

    }
    private val differCallback = object : DiffUtil.ItemCallback<Subjects>() {

        override fun areItemsTheSame(oldItem: Subjects, newItem: Subjects): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Subjects, newItem: Subjects): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)
    var mSubject: List<Subjects>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
       return SubjectViewHolder(
           SubjectItemBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
       val currentSubject = mSubject[position]
        holder.bind(currentSubject)

    }

    override fun getItemCount() = mSubject.size



    interface OnItemClickListener{
        fun onItemClick(subject : Subjects)
        fun onCheckBoxClick(subject: Subjects, isChecked : Boolean)
    }
}
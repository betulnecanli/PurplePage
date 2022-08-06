package com.betulnecanli.purplepage.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.databinding.SubjectItemBinding
import com.betulnecanli.purplepage.model.Subjects

class SubjectAdapter : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(val binding : SubjectItemBinding) : RecyclerView.ViewHolder(binding.root)
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

        holder.binding.apply {
            subjectText.text = currentSubject.subjectTitle
            subjectCheck.isChecked = currentSubject.isChecked
        }

        holder.binding.subjectCheck.apply {
            setOnClickListener {

                holder.binding.apply {
                    if(isChecked){

                        subjectText.paintFlags = subjectText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    }else{
                        subjectText.paintFlags = subjectText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }
        }
    }

    override fun getItemCount() = mSubject.size
}
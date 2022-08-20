package com.betulnecanli.purplepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.data.model.Projects
import com.betulnecanli.purplepage.databinding.ProjectItemBinding

class ProjectAdapter(
    private val listener : OnItemClickListener
): RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Projects>(){
        override fun areItemsTheSame(oldItem: Projects, newItem: Projects): Boolean {
            return oldItem.uidP == newItem.uidP
        }

        override fun areContentsTheSame(oldItem: Projects, newItem: Projects): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)
    var mProject : List<Projects>
        get() = differ.currentList
        set(value){
            differ.submitList(value)
        }


    inner class ProjectViewHolder
        (private val binding : ProjectItemBinding)
        :RecyclerView.ViewHolder(binding.root){

            init{

                binding.apply {
                    root.setOnClickListener {
                        val position = adapterPosition
                        if(position != RecyclerView.NO_POSITION){
                            val project = differ.currentList[position]
                            listener.onItemClick(project)
                        }

                    }


                    pCheckButton.setOnClickListener {
                        val position = adapterPosition
                        if(position != RecyclerView.NO_POSITION){
                            val project = differ.currentList[position]
                            listener.onCheckboxClick(project, pCheckButton.isChecked)
                        }
                    }
                }


            }


        fun bind(p : Projects){
            binding.apply {
                pTitle.text = p.projectTitle
                pCheckButton.isChecked = p.isCheckedP
                pTitle.paint.isStrikeThruText = p.isCheckedP
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
       return ProjectViewHolder(
           ProjectItemBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentProject = mProject[position]
        holder.bind(currentProject)
    }

    override fun getItemCount(): Int = mProject.size


    interface OnItemClickListener{
        fun onItemClick(p : Projects)
        fun onCheckboxClick(p : Projects, isChecked : Boolean)
    }

}
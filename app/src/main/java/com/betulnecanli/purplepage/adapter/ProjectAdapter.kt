package com.betulnecanli.purplepage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.databinding.ProjectItemBinding

class ProjectAdapter(

): RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {


    inner class ProjectViewHolder
        (private val binding : ProjectItemBinding)
        :RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}
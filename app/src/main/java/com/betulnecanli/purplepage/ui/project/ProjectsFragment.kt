package com.betulnecanli.purplepage.ui.project

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.adapter.ProjectAdapter
import com.betulnecanli.purplepage.data.model.Projects
import com.betulnecanli.purplepage.databinding.FragmentProjectsBinding
import com.betulnecanli.purplepage.utils.exhaustive
import com.betulnecanli.purplepage.utils.onQueryTextChanged
import com.betulnecanli.purplepage.viewmodel.ProjectViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects), ProjectAdapter.OnItemClickListener {

        private var _binding : FragmentProjectsBinding? = null
        private val binding get() = _binding!!

        private val viewModel : ProjectViewModel by viewModels()
        private lateinit var adapterP : ProjectAdapter

        private lateinit var preferences : SharedPreferences
        private lateinit var editor : SharedPreferences.Editor
        private var projectDone : Int = 0
        private var pListLength : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search
        binding.projectSearch.onQueryTextChanged {
            viewModel.searchQueryP.value=it
        }
        val pendingQuery = viewModel.searchQueryP.value
        if(pendingQuery != null && pendingQuery.isNotEmpty()){
            binding.projectSearch.setQuery(pendingQuery,false)
        }


        preferences = requireActivity().getSharedPreferences("doneProjects", Context.MODE_PRIVATE)
        editor = preferences.edit()

        pListLength = preferences.getInt("pNum",0)
        binding.projectProgressBar.max = 1000


        if(pListLength != 0){
            projectDone = ((preferences.getInt("donePro",0))*100)/pListLength
            binding.projectPercent.setText("$projectDone%")
            ObjectAnimator.ofInt(binding.projectProgressBar,"progress",(projectDone*10))
                .setDuration(1000)
                .start()
        }
        else{
            binding.projectPercent.setText("0%")
            ObjectAnimator.ofInt(binding.projectProgressBar,"progress",0)
                .setDuration(1000)
                .start()
        }

        viewModel.dinlenenveriP.observe(viewLifecycleOwner, Observer {
            editor.putInt("donePro", (it+(preferences.getInt(("donePro"),0))))
            editor.apply()
            projectDone = ((preferences.getInt("donePro",0))*100)/pListLength
            ObjectAnimator.ofInt(binding.projectProgressBar,"progress",projectDone*10)
                .setDuration(1000)
                .start()
            binding.projectPercent.setText("$projectDone%")
        })


        setupRcy()
        binding.apply {
            projectFloatButton.setOnClickListener {
                viewModel.navigateToAddScreen()
            }


            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                   val project = adapterP.mProject[viewHolder.adapterPosition]
                    viewModel.deleteProject(project)
                }

            }).attachToRecyclerView(projectsRcy)

        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.projectEvent.collect { event ->
                when (event) {
                    is ProjectViewModel.ProjectEvents.NavigatetoAddScreen -> {
                        val action = ProjectsFragmentDirections.actionGlobalAddEditProject(null)
                        findNavController().navigate(action)
                    }
                    is ProjectViewModel.ProjectEvents.NavigatetoEditScreen -> {
                        val action = ProjectsFragmentDirections.actionGlobalAddEditProject(project = event.p)
                        findNavController().navigate(action)

                    }
                    is ProjectViewModel.ProjectEvents.ShowUndoDeleteMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Project Deleted",
                            Snackbar.LENGTH_LONG
                        ).setAction(
                            "UNDO"
                        ){
                            viewModel.clickUndo(event.p)
                        }.show()

                    }
                }.exhaustive

            }
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.projectSearch.setOnQueryTextListener(null)
    }

    fun setupRcy(){

        adapterP = ProjectAdapter(this)
        binding.projectsRcy.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterP
        }

        viewModel.getAllProjects.observe(requireActivity()){listProjects ->
            updateUI(listProjects)
            adapterP.mProject = listProjects
            pListLength = listProjects.size
            editor.putInt("pNum",listProjects.size)
            editor.apply()

            if(pListLength!=0){
                projectDone = ((preferences.getInt("donePro",0))*100)/pListLength
                binding.projectPercent.setText("$projectDone%")
                ObjectAnimator.ofInt(binding.projectProgressBar,"progress",projectDone*10)
                    .setDuration(1000)
                    .start()
            }
            else if(pListLength == 0){
                editor.putInt("donePro", 0)
                editor.apply()
                projectDone = preferences.getInt("donePro",0)
                binding.projectPercent.setText("0%")
                ObjectAnimator.ofInt(binding.projectProgressBar,"progress",0)
                    .setDuration(1000)
                    .start()
            }
        }


        viewModel.searhProject.observe(requireActivity()){listProjects ->
            updateUI(listProjects)
            adapterP.mProject = listProjects

        }

    }


    fun updateUI(list : List<Projects>){
        if(list.isNotEmpty()){
            binding.projectsRcy.visibility  = View.VISIBLE
        }
        else{
            binding.projectsRcy.visibility  = View.GONE
        }


    }

    override fun onItemClick(p: Projects) {
        viewModel.navigateToEditScreen(p)
    }

    override fun onCheckboxClick(p: Projects, isChecked: Boolean) {
        viewModel.onProjectCheckedChanged(p,isChecked)
    }


}
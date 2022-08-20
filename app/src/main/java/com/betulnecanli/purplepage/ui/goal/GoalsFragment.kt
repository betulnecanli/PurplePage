package com.betulnecanli.purplepage.ui.goal

import android.animation.ObjectAnimator
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
import com.betulnecanli.purplepage.adapter.GoalAdapter
import com.betulnecanli.purplepage.data.model.Goals
import com.betulnecanli.purplepage.databinding.FragmentGoalsBinding
import com.betulnecanli.purplepage.utils.exhaustive
import com.betulnecanli.purplepage.utils.onQueryTextChanged
import com.betulnecanli.purplepage.viewmodel.GoalViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment : Fragment(R.layout.fragment_goals), GoalAdapter.OnItemClickListener {

    private var _binding : FragmentGoalsBinding? = null
    private val binding get() = _binding!!


    private val viewModel : GoalViewModel by viewModels()
    private lateinit var adapterG : GoalAdapter

    private lateinit var preferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    private var goalDone : Int = 0
    private var gListLength : Int = 0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search
        binding.searchViewGoals.onQueryTextChanged {
            viewModel.searchQueryG.value = it
        }
        val pendingQuery = viewModel.searchQueryG.value
        if(pendingQuery != null && pendingQuery.isNotEmpty()){
            binding.searchViewGoals.setQuery(pendingQuery,false)
        }

        preferences = requireActivity().getSharedPreferences("doneGoals", Context.MODE_PRIVATE)
        editor = preferences.edit()

        gListLength = preferences.getInt("gNum",0)
        binding.progressBarGoals.max = 1000

        if(gListLength != 0){
            goalDone = ((preferences.getInt("doneGoa",0))*100)/gListLength
            binding.percentGoals.setText("$goalDone%")
            ObjectAnimator.ofInt(binding.progressBarGoals,"progress",(goalDone*10))
                .setDuration(1000)
                .start()
        }
        else{
            binding.percentGoals.setText("0%")
            ObjectAnimator.ofInt(binding.progressBarGoals,"progress",0)
                .setDuration(1000)
                .start()
        }

        viewModel.dinlenenVeriG.observe(viewLifecycleOwner, Observer {
            editor.putInt("doneGoa", (it+(preferences.getInt(("doneGoa"),0))))
            editor.apply()
            goalDone = ((preferences.getInt("doneGoa",0))*100)/gListLength
            ObjectAnimator.ofInt(binding.progressBarGoals,"progress",goalDone*10)
                .setDuration(1000)
                .start()
            binding.percentGoals.setText("$goalDone%")
        })

        setupRcy()

        binding.apply {
            floatButtonGoals.setOnClickListener {
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
                    val goal = adapterG.mGoals[viewHolder.adapterPosition]
                    viewModel.deleteGoal(goal)
                }

            }).attachToRecyclerView(recycGoals)

        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.goalEvent.collect{event->
                when(event){
                    is GoalViewModel.GoalEvents.NavigatetoAddScreen -> {
                        val action = GoalsFragmentDirections.actionGlobalAddEditGoal(null)
                        findNavController().navigate(action)
                    }
                    is GoalViewModel.GoalEvents.NavigatetoEditScreen -> {
                        val action = GoalsFragmentDirections.actionGlobalAddEditGoal(event.g)
                        findNavController().navigate(action)
                    }
                    is GoalViewModel.GoalEvents.ShowUndoDeleteMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Goal Deleted",
                            Snackbar.LENGTH_LONG
                        ).setAction("UNDO"){
                            viewModel.clickUndo(event.g)
                        }.show()
                    }
                }.exhaustive
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.searchViewGoals.setOnQueryTextListener(null)
    }

    fun setupRcy(){
        adapterG = GoalAdapter(this)
        binding.recycGoals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterG
        }

        viewModel.getAllGoals.observe(requireActivity()){listGoals->
            updateUI(listGoals)
            adapterG.mGoals = listGoals
            gListLength = listGoals.size
            editor.putInt("gNum",listGoals.size)
            editor.apply()

            if(gListLength!=0){
                goalDone = ((preferences.getInt("doneGoa",0))*100)/gListLength
                binding.percentGoals.setText("$goalDone%")
                ObjectAnimator.ofInt(binding.progressBarGoals,"progress",goalDone*10)
                    .setDuration(1000)
                    .start()
            }
            else if(gListLength == 0){
                editor.putInt("doneGoa", 0)
                editor.apply()
                goalDone = preferences.getInt("doneGoa",0)
                binding.percentGoals.setText("0%")
                ObjectAnimator.ofInt(binding.progressBarGoals,"progress",0)
                    .setDuration(1000)
                    .start()
            }




        }


        viewModel.searchedGoals.observe(requireActivity()){listGoals->
            updateUI(listGoals)
            adapterG.mGoals = listGoals
        }

    }

    fun updateUI(list : List<Goals>){
        if(list.isNotEmpty()){
            binding.recycGoals.visibility  = View.VISIBLE
        }
        else{
            binding.recycGoals.visibility  = View.GONE
        }
    }

    override fun OnItemClick(g: Goals) {
        viewModel.navigateToEditScreen(g)
    }

    override fun onCheckedBoxClick(g: Goals, isChecked: Boolean) {
        viewModel.onGoalCheckedBoxChanged(g,isChecked)
    }


}
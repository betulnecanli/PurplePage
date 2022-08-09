package com.betulnecanli.purplepage.ui.subject

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.adapter.SubjectAdapter
import com.betulnecanli.purplepage.databinding.FragmentSubjectsBinding
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.utils.exhaustive
import com.betulnecanli.purplepage.viewmodel.SubjectViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectsFragment : Fragment(R.layout.fragment_subjects), SubjectAdapter.OnItemClickListener {

    private var _binding : FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var subjectAdapter : SubjectAdapter

    private val viewModel : SubjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSubjectsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("add_edit_request"){
            _, bundle ->
            val result  = bundle.getInt("add_edit_request")
            viewModel.onAddEditResult(result)
        }


        setupRcy()

        binding.apply {

            addSFloatB.setOnClickListener {
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
                    val subject = subjectAdapter.mSubject[viewHolder.adapterPosition]
                    viewModel.deleteSubject(subject)
                }
            }).attachToRecyclerView(subjectsRcy)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.subjectEvent.collect{event ->
                when(event){
                    is SubjectViewModel.SubjectEvents.NavigateToAddScreen -> {
                        val action = SubjectsFragmentDirections.actionGlobalAddEditSubject(null)
                        findNavController().navigate(action)
                    }
                    is SubjectViewModel.SubjectEvents.NavigateToEditScreen -> {
                        val action = SubjectsFragmentDirections.actionGlobalAddEditSubject(event.subject)
                        findNavController().navigate(action)
                    }
                    is SubjectViewModel.SubjectEvents.ShowUndoDeleteMessage ->{
                        Snackbar.make(requireView(),
                        "Subject Deleted",
                        Snackbar.LENGTH_LONG).setAction("UNDO"){
                            viewModel.clickedUndo(event.subject)
                        }.show()
                    }
                    is SubjectViewModel.SubjectEvents.ShowSubjectSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),
                        event.msg,
                        Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

    }


    private fun setupRcy(){
        subjectAdapter = SubjectAdapter(this)
        binding.subjectsRcy.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter=subjectAdapter
        }

        viewModel.getAllSubjects.observe(requireActivity()){ listSubjects ->
                    updateUI(listSubjects)
                    subjectAdapter.mSubject = listSubjects
        }
    }


    private fun updateUI(list : List<Subjects>){
        if(list.isNotEmpty()){
            binding.subjectsRcy.visibility  = View.VISIBLE
        }
        else{
            binding.subjectsRcy.visibility  = View.GONE
        }

    }

    override fun onItemClick(subject: Subjects) {
        viewModel.navigateToAddScreen()
    }

    override fun onCheckBoxClick(subject: Subjects, isChecked: Boolean) {
        viewModel.onSubjectCheckChanged(subject,isChecked)
    }

}
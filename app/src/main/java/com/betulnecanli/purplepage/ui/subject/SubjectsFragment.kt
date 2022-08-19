package com.betulnecanli.purplepage.ui.subject

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.adapter.SubjectAdapter
import com.betulnecanli.purplepage.databinding.FragmentSubjectsBinding
import com.betulnecanli.purplepage.data.model.Subjects
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

    lateinit var preferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    private var subjectDone : Int = 0
    private var listLength : Int = 0


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

        preferences = requireActivity().getSharedPreferences("doneSubjects", Context.MODE_PRIVATE)
        editor = preferences.edit()

        listLength = preferences.getInt("sNum",0)
        binding.subjectsProgressBar.max =1000


        if(listLength!= 0){

            subjectDone = ((preferences.getInt("doneSub",0))*100)/listLength
           // subjectDone = preferences.getInt(("doneSub"),0)
            binding.progressPercent.setText("$subjectDone%")
            ObjectAnimator.ofInt(binding.subjectsProgressBar,"progress",(subjectDone*10))
                .setDuration(1000)
                .start()
        }
        else{
            binding.progressPercent.setText("0%")
            ObjectAnimator.ofInt(binding.subjectsProgressBar,"progress",0)
                .setDuration(1000)
                .start()

        }
        //listening checked subjects
        viewModel.dinlenenVeri.observe(viewLifecycleOwner, Observer {
            editor.putInt("doneSub", (it+(preferences.getInt(("doneSub"),0))))
            editor.apply()
            var a = preferences.getInt("doneSub",0)

            subjectDone = ((preferences.getInt("doneSub",0))*100)/listLength
            //subjectDone = preferences.getInt(("doneSub"),0)

            ObjectAnimator.ofInt(binding.subjectsProgressBar,"progress",subjectDone*10)
                .setDuration(2000)
                .start()

            binding.progressPercent.setText("$subjectDone%")



        })

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
                    listLength = listSubjects.size
                    editor.putInt("sNum",listSubjects.size)
                    editor.apply()
                    Log.d("listenumm", "$listLength")
                if(listLength!=0){
                        subjectDone = ((preferences.getInt("doneSub",0))*100)/listLength
                        //subjectDone = preferences.getInt("doneSub",0)
                        binding.progressPercent.setText("$subjectDone%")

                        ObjectAnimator.ofInt(binding.subjectsProgressBar,"progress",subjectDone*10)
                            .setDuration(2000)
                            .start()
                    }
                else if(listLength == 0){
                        editor.putInt("doneSub", 0)
                        editor.apply()
                        subjectDone = preferences.getInt("doneSub",0)
                        binding.progressPercent.setText("0%")
                        ObjectAnimator.ofInt(binding.subjectsProgressBar,"progress",0)
                            .setDuration(2000)
                            .start()
                    }


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
        viewModel.navigateToEditScreen(subject)
    }

    override fun onCheckBoxClick(subject: Subjects, isChecked: Boolean) {
        viewModel.onSubjectCheckChanged(subject,isChecked)
    }

}
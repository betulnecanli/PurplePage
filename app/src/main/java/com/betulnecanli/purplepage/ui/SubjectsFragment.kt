package com.betulnecanli.purplepage.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.adapter.SubjectAdapter
import com.betulnecanli.purplepage.databinding.FragmentSubjectsBinding
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.viewmodel.SubjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubjectsFragment : Fragment(R.layout.fragment_subjects) {

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

        showAddSubjectDialog()

        return binding.root
    }


    private fun showAddSubjectDialog(){
        binding.addSFloatB.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.add_subject_layout,null)
            val editText: EditText = dialogLayout.findViewById(R.id.editTextSubject)

            with(builder){
                setTitle("Enter some Subject!")
                setPositiveButton("OK"){
                    dialog, which ->
                    val subjectName = editText.text.toString()
                    val sub = Subjects(0,subjectName,false)
                    viewModel.insertSubjects(sub)

                   Log.d("Positive",subjectName)

                }

                setNegativeButton("Cancel"){
                    dialog, which ->
                    Log.d("Negative", "Fail")
                }

                setView(dialogLayout)
                show()
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRcy()


    }


    private fun setupRcy(){
        subjectAdapter = SubjectAdapter()
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

}
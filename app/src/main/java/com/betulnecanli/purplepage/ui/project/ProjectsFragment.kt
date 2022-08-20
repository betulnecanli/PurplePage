package com.betulnecanli.purplepage.ui.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.betulnecanli.purplepage.R
import com.betulnecanli.purplepage.adapter.ProjectAdapter
import com.betulnecanli.purplepage.databinding.FragmentProjectsBinding
import com.betulnecanli.purplepage.viewmodel.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectsFragment : Fragment(R.layout.fragment_projects) {

        private var _binding : FragmentProjectsBinding? = null
        private val binding get() = _binding!!

        private val viewModel : ProjectViewModel by viewModels()
        private lateinit var adapterP : ProjectAdapter



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




}
package com.betulnecanli.purplepage.ui.project

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.betulnecanli.purplepage.viewmodel.AddEditProjectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditProject: DialogFragment() {

    private val viewModel : AddEditProjectViewModel by viewModels()
}
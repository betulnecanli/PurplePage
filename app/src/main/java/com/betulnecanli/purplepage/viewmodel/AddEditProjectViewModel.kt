package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.data.dao.ProjectsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    val daoP : ProjectsDao
): ViewModel() {
}
package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.data.dao.ProjectsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val dao: ProjectsDao,
    private val state : SavedStateHandle
): ViewModel() {



}
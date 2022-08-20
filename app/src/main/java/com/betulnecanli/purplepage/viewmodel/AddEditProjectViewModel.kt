package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.data.dao.ProjectsDao
import com.betulnecanli.purplepage.data.model.Projects
import com.betulnecanli.purplepage.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditProjectViewModel @Inject constructor(
    private val daoP : ProjectsDao,
    private val state : SavedStateHandle,
    @ApplicationScope private val applicationScope : CoroutineScope
): ViewModel() {

    val project = state.get<Projects>("project")
    var projectTitle = state.get<String>("projectTitle") ?: project?.projectTitle ?: ""
        set(value){
            field = value
            state.set("projectTitle", value)
        }


    fun onSaveClick(p : Projects){
        if(p.projectTitle == ""){
            return
        }
        if(project != null){
            val updatedProject = project.copy(projectTitle = p.projectTitle)
            updateProject(updatedProject)
        }
        else{
            val newProject = Projects(projectTitle = p.projectTitle)
            inserProject(newProject)
        }
    }


    fun inserProject(p: Projects) = applicationScope.launch {
        daoP.insertProject(p)
    }



    fun updateProject(p: Projects) = applicationScope.launch {
        daoP.updateProject(p)
    }
}
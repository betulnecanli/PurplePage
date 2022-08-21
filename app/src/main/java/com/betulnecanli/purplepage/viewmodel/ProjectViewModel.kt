package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.*
import com.betulnecanli.purplepage.data.dao.ProjectsDao
import com.betulnecanli.purplepage.data.model.Projects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val dao: ProjectsDao,
    private val state : SavedStateHandle
): ViewModel() {

    private val projectEventChannel = Channel<ProjectEvents>()
    val projectEvent = projectEventChannel.receiveAsFlow()

    val searchQueryP = state.getLiveData("projectSearchQuery","")
    private val projectFlow = searchQueryP.asFlow().flatMapLatest { query ->
        dao.searchProject(query)
    }

    val dinlenenveriP : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    var projectNum = 0


    fun onProjectCheckedChanged(p : Projects, isChecked : Boolean) = viewModelScope.launch {
        dao.updateProject(p.copy(isCheckedP = isChecked))
        if(isChecked){
            projectNum = 1
            dinlenenveriP.value = projectNum
            projectNum = 0
            dinlenenveriP.value = projectNum
        }
        else{
            projectNum = -1
            dinlenenveriP.value = projectNum
            projectNum = 0
            dinlenenveriP.value = projectNum
        }
    }

    fun deleteProject(p : Projects) = viewModelScope.launch {
        dao.deleteProject(p)
        projectEventChannel.send(ProjectEvents.ShowUndoDeleteMessage(p))
        if(p.isCheckedP){
            projectNum = -1
            dinlenenveriP.value = projectNum
            projectNum = 0
            dinlenenveriP.value = projectNum
        }
    }

    val getAllProjects = dao.getAllProjects().asLiveData()
    val searhProject = projectFlow.asLiveData()

    fun navigateToAddScreen() = viewModelScope.launch {
        projectEventChannel.send(ProjectEvents.NavigatetoAddScreen)
    }

    fun navigateToEditScreen(p : Projects) = viewModelScope.launch {
        projectEventChannel.send(ProjectEvents.NavigatetoEditScreen(p))
    }

    fun clickUndo(p : Projects) = viewModelScope.launch {
        dao.insertProject(p)
        if(p.isCheckedP){
            projectNum = 1
            dinlenenveriP.value = projectNum
            projectNum = 0
            dinlenenveriP.value = projectNum
        }
    }





    sealed class ProjectEvents{
        object NavigatetoAddScreen : ProjectEvents()
        data class NavigatetoEditScreen(val p:Projects) : ProjectEvents()
        data class ShowUndoDeleteMessage(val p : Projects) : ProjectEvents()


    }
}
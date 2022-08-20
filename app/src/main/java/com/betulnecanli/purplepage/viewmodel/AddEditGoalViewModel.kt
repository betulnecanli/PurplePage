package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.data.dao.GoalsDao
import com.betulnecanli.purplepage.data.model.Goals
import com.betulnecanli.purplepage.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditGoalViewModel @Inject constructor(
    private val daoG : GoalsDao,
    private val state : SavedStateHandle,
    @ApplicationScope private val applicationScope : CoroutineScope
): ViewModel() {

    val goal = state.get<Goals>("goal")
    var goalTitle = state.get<String>("goalTitle") ?: goal?.goalTitle ?: ""
        set(value){
            field = value
            state.set("goalTitle", value)
        }


    fun onSaveClick(g : Goals){
        if(g.goalTitle == ""){
            return
        }
        if(goal != null){
            val updatedProject = goal.copy(goalTitle = g.goalTitle)
            updateProject(updatedProject)
        }
        else{
            val newProject = Goals(goalTitle = g.goalTitle)
            inserProject(newProject)
        }
    }


    fun inserProject(g: Goals) = applicationScope.launch {
        daoG.insertGoal(g)
    }



    fun updateProject(g : Goals) = applicationScope.launch {
        daoG.updateGoal(g)
    }
}
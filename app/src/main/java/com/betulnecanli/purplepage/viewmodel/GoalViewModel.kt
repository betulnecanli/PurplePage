package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.*
import com.betulnecanli.purplepage.data.dao.GoalsDao
import com.betulnecanli.purplepage.data.model.Goals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val daoG : GoalsDao,
    private val state : SavedStateHandle
): ViewModel() {

    private val goalEventChannel = Channel<GoalEvents>()
    var goalEvent = goalEventChannel.receiveAsFlow()

    val searchQueryG = state.getLiveData("goalSearchQuery","")
    private val goalFlow = searchQueryG.asFlow().flatMapLatest { query ->
        daoG.searchGoals(query)

    }

    val dinlenenVeriG : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var goalsNum = 0

    val searchedGoals = goalFlow.asLiveData()
    val getAllGoals = daoG.getAllGoals().asLiveData()


    fun deleteGoal(g : Goals) = viewModelScope.launch {
        daoG.deleteGoal(g)
        if(g.isCheckedG){
            goalsNum = 1
            dinlenenVeriG.value = goalsNum
            goalsNum = 0
            dinlenenVeriG.value = goalsNum
        }
    }

    fun onGoalCheckedBoxChanged(g : Goals, isChecked : Boolean) = viewModelScope.launch {
        daoG.updateGoal(g.copy(isCheckedG = isChecked))
        if(isChecked){
            goalsNum = 1
            dinlenenVeriG.value = goalsNum
            goalsNum = 0
            dinlenenVeriG.value = goalsNum
        }
        else{
            goalsNum = -1
            dinlenenVeriG.value = goalsNum
            goalsNum = 0
            dinlenenVeriG.value = goalsNum
        }
    }

    fun navigateToAddScreen() = viewModelScope.launch {
        goalEventChannel.send(GoalEvents.NavigatetoAddScreen)
    }

    fun navigateToEditScreen(g : Goals) = viewModelScope.launch {
        goalEventChannel.send(GoalEvents.NavigatetoEditScreen(g))
    }

    fun clickUndo(g: Goals) = viewModelScope.launch {
        daoG.insertGoal(g)
        if(g.isCheckedG){
            goalsNum = 1
            dinlenenVeriG.value = goalsNum
            goalsNum = 0
            dinlenenVeriG.value = goalsNum
        }

    }






    sealed class GoalEvents{
        object NavigatetoAddScreen : GoalEvents()
        data class NavigatetoEditScreen(val g : Goals) : GoalEvents()
        data class ShowUndoDeleteMessage(val g : Goals) : GoalEvents()

    }


}
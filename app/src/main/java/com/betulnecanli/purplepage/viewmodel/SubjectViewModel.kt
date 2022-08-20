package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.*
import com.betulnecanli.purplepage.data.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import com.betulnecanli.purplepage.ui.ADD_RESULT_OK
import com.betulnecanli.purplepage.ui.EDIT_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel
@Inject
constructor(private val subjectRepo: SubjectsRepo,
            private val state : SavedStateHandle
            ) : ViewModel() {


    private val subjectEventChannel = Channel<SubjectEvents>()
    val subjectEvent = subjectEventChannel.receiveAsFlow()


    val searchQuery = state.getLiveData("subjectSearchQuery","")

    private val subjectFlow = searchQuery.asFlow().flatMapLatest {query ->
        subjectRepo.searchSubject(query)
    }



    val dinlenenVeri : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }

    var subjectNum = 0

    fun insertSubjects(subject: Subjects) = viewModelScope.launch {
        subjectRepo.insertSubject(subject)
    }

    fun onSubjectCheckChanged(subject: Subjects, completed : Boolean) = viewModelScope.launch {
        subjectRepo.updateSubject(subject.copy(isChecked = completed))
        if(completed){
            subjectNum = 1
            dinlenenVeri.value = subjectNum
            subjectNum=0
            dinlenenVeri.value = subjectNum
        }
        else{
            subjectNum = -1
            dinlenenVeri.value = subjectNum
            subjectNum = 0
            dinlenenVeri.value = subjectNum
        }
    }

    fun deleteSubject(subject : Subjects) = viewModelScope.launch {
        subjectRepo.deleteSubject(subject)
        subjectEventChannel.send(SubjectEvents.ShowUndoDeleteMessage(subject))
        if(subject.isChecked){
            subjectNum = -1
            dinlenenVeri.value = subjectNum
            subjectNum = 0
            dinlenenVeri.value = subjectNum
        }

    }


    val getAllSubjects = subjectRepo.getAllSubjects().asLiveData()
    val searchSubject = subjectFlow.asLiveData()

    fun navigateToAddScreen() = viewModelScope.launch {
        subjectEventChannel.send(SubjectEvents.NavigateToAddScreen)
    }
    fun navigateToEditScreen(subject: Subjects) = viewModelScope.launch {
        subjectEventChannel.send(SubjectEvents.NavigateToEditScreen(subject))
    }

    fun clickedUndo(subject: Subjects) = viewModelScope.launch {
        subjectRepo.insertSubject(subject)
        if(subject.isChecked){
            subjectNum = 1
            dinlenenVeri.value = subjectNum
            subjectNum = 0
            dinlenenVeri.value = subjectNum
        }

    }

    fun onAddEditResult(result : Int){
        when(result)
        {
            ADD_RESULT_OK -> showSubjectSavedConfirmationMessage("Subject Added")
            EDIT_RESULT_OK -> showSubjectSavedConfirmationMessage("Subject Updated")
        }
    }

    private fun showSubjectSavedConfirmationMessage(msg : String) = viewModelScope.launch {
        subjectEventChannel.send(SubjectEvents.ShowSubjectSavedConfirmationMessage(msg))
    }


    sealed class SubjectEvents{

    object NavigateToAddScreen: SubjectEvents()
    data class NavigateToEditScreen(val subject : Subjects) : SubjectEvents()
    data class ShowUndoDeleteMessage(val subject : Subjects) : SubjectEvents()
    data class ShowSubjectSavedConfirmationMessage(val msg : String) : SubjectEvents()




    }
}
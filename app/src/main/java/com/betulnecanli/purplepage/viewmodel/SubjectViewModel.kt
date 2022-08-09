package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import com.betulnecanli.purplepage.ui.ADD_RESULT_OK
import com.betulnecanli.purplepage.ui.EDIT_RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel
@Inject
constructor(val subjectRepo: SubjectsRepo) : ViewModel() {


    private val subjectEventChannel = Channel<SubjectEvents>()
    val subjectEvent = subjectEventChannel.receiveAsFlow()


    fun insertSubjects(subject: Subjects) = viewModelScope.launch {
        subjectRepo.insertSubject(subject)
    }

    fun onSubjectCheckChanged(subject: Subjects, completed : Boolean) = viewModelScope.launch {
        subjectRepo.updateSubject(subject.copy(isChecked = completed))
    }

    fun deleteSubject(subject : Subjects) = viewModelScope.launch {
        subjectRepo.deleteSubject(subject)
        subjectEventChannel.send(SubjectEvents.ShowUndoDeleteMessage(subject))
    }

    val getAllSubjects = subjectRepo.getAllSubjects().asLiveData()


    fun navigateToAddScreen() = viewModelScope.launch {
        subjectEventChannel.send(SubjectEvents.NavigateToAddScreen)
    }

    fun clickedUndo(subject: Subjects) = viewModelScope.launch {
        subjectRepo.insertSubject(subject)
    }

    fun onAddEditResult(result : Int){
        when(result)
        {
            ADD_RESULT_OK -> showSubjectSavedConfirmationMessage("Subject Added")
            EDIT_RESULT_OK -> showSubjectSavedConfirmationMessage("Subject Updated")
        }
    }

    fun showSubjectSavedConfirmationMessage(msg : String) = viewModelScope.launch {
        subjectEventChannel.send(SubjectEvents.ShowSubjectSavedConfirmationMessage(msg))
    }


    sealed class SubjectEvents{

    object NavigateToAddScreen: SubjectEvents()
    data class NavigateToEditScreen(val subject : Subjects) : SubjectEvents()
    data class ShowUndoDeleteMessage(val subject : Subjects) : SubjectEvents()
    data class ShowSubjectSavedConfirmationMessage(val msg : String) : SubjectEvents()




    }
}
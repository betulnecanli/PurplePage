package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betulnecanli.purplepage.di.ApplicationScope
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import com.betulnecanli.purplepage.ui.ADD_RESULT_OK
import com.betulnecanli.purplepage.ui.EDIT_RESULT_OK
import com.betulnecanli.purplepage.ui.subject.AddEditSubject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditSubjectViewModel @Inject constructor(
    val subjectrepo : SubjectsRepo,
    private val state : SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    private val addEditSubjectChannel = Channel<AddEditSubjectEvent>()
    val addEditSubjectEvent = addEditSubjectChannel.receiveAsFlow()

    val subject = state.get<Subjects>("subject")
    var subjectTitle = state.get<String>("subjectTitle") ?: subject?.subjectTitle ?: ""
        set(value) {
            field = value
            state.set("subjectTitle", value)
        }

    fun onSaveClick(s : Subjects){
        if(s.subjectTitle == ""){
            showInvalidInputMessage("Subject cannot be empty.")
            return
        }

        else{
            val newSubject = Subjects(subjectTitle = s.subjectTitle)
            insertSubject(newSubject)
        }
    }


    fun showInvalidInputMessage(msg : String) = applicationScope.launch {
        addEditSubjectChannel.send(AddEditSubjectEvent.ShowInvalidInputMessage(msg))
    }

    fun insertSubject(subject : Subjects) = applicationScope.launch {
        subjectrepo.insertSubject(subject)
        addEditSubjectChannel.send(AddEditSubjectEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }
    fun updateSubject(subject: Subjects) = applicationScope.launch {
        subjectrepo.updateSubject(subject)
        addEditSubjectChannel.send(AddEditSubjectEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }


    sealed class AddEditSubjectEvent{
        data class ShowInvalidInputMessage(val msg : String) : AddEditSubjectEvent()
        data class NavigateBackWithResult(val result : Int) : AddEditSubjectEvent()

    }

}
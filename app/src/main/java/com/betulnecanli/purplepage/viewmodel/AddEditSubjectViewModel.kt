package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.di.ApplicationScope
import com.betulnecanli.purplepage.data.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import com.betulnecanli.purplepage.ui.ADD_RESULT_OK
import com.betulnecanli.purplepage.ui.EDIT_RESULT_OK
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


    val subject = state.get<Subjects>("subject")
    var subjectTitle = state.get<String>("subjectTitle") ?: subject?.subjectTitle ?: ""
        set(value) {
            field = value
            state.set("subjectTitle", value)
        }

    fun onSaveClick(s : Subjects){
        if(s.subjectTitle == ""){
            return
        }

        if(subject != null){
            val updatedSubject = subject.copy(subjectTitle=s.subjectTitle)
            updateSubject(updatedSubject)
        }
        else{
            val newSubject = Subjects(subjectTitle = s.subjectTitle)
            insertSubject(newSubject)
        }


    }
    fun insertSubject(subject : Subjects) = applicationScope.launch {
        subjectrepo.insertSubject(subject)
        }
    fun updateSubject(subject: Subjects) = applicationScope.launch {
        subjectrepo.updateSubject(subject)
         }




}
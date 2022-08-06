package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SubjectViewModel
@Inject
constructor(val subjectRepo: SubjectsRepo) : ViewModel()
{
    fun insertSubjects(subject: Subjects) = viewModelScope.launch {
        subjectRepo.insertSubject(subject)
    }

    fun updateSubjects(subject: Subjects) = viewModelScope.launch {
        subjectRepo.updateSubject(subject)
    }

    val getAllSubjects = subjectRepo.getAllSubjects().asLiveData()


}
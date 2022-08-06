package com.betulnecanli.purplepage.viewmodel

import androidx.lifecycle.ViewModel
import com.betulnecanli.purplepage.di.ApplicationScope
import com.betulnecanli.purplepage.model.Subjects
import com.betulnecanli.purplepage.repository.SubjectsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditSubjectViewModel @Inject constructor(
    val subjectrepo : SubjectsRepo,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun inserSubject(subject : Subjects) = applicationScope.launch {
        subjectrepo.insertSubject(subject)
    }

}
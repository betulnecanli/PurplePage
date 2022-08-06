package com.betulnecanli.purplepage.repository

import com.betulnecanli.purplepage.dao.SubjectsDao
import com.betulnecanli.purplepage.model.Subjects
import javax.inject.Inject

class SubjectsRepo
@Inject
constructor(private val dao: SubjectsDao){

    suspend fun insertSubject(subject : Subjects) = dao.insertSubject(subject)

    suspend fun updateSubject(subject: Subjects) = dao.updateSubject(subject)

    fun getAllSubjects() = dao.getAllSubjects()
}
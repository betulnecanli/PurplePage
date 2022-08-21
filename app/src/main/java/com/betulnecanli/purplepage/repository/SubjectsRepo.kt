package com.betulnecanli.purplepage.repository

import com.betulnecanli.purplepage.data.dao.SubjectsDao
import com.betulnecanli.purplepage.data.model.Subjects
import javax.inject.Inject

class SubjectsRepo
@Inject
constructor(private val dao: SubjectsDao){


    suspend fun insertSubject(subject : Subjects) = dao.insertSubject(subject)

    suspend fun updateSubject(subject: Subjects) = dao.updateSubject(subject)

    suspend fun deleteSubject(subject : Subjects) = dao.deleteSubject(subject)

    fun searchSubject(query : String) = dao.searchSubject(query)

    fun getAllSubjects() = dao.getAllSubjects()
}
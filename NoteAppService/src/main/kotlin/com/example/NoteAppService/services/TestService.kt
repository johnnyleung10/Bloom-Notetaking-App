package com.example.NoteAppService.services

import com.example.NoteAppService.models.TestModel
import com.example.NoteAppService.repositories.NoteRepository
import org.springframework.stereotype.Service

@Service
class TestService(){

    fun getTest(): List<TestModel> {
        return listOf(TestModel("hello world!"))
    }

//    fun post(note: Note) {
//        db.save(note)
//    }
}
package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.TestModel
import com.example.NoteAppService.services.TestService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test")
class TestResource(val service: TestService) {

    @GetMapping
    fun index(): List<TestModel> {
        return service.getTest()
    }

//    @PostMapping
//    fun post(@RequestBody note: Note) {
//        service.post(note)
//    }
}
package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.EmptyResponse
import com.example.NoteAppService.models.Note
import com.example.NoteAppService.services.NoteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/note")
class NoteResource(val service: NoteService) {

    @GetMapping
    fun index(): List<Note> {
        return service.findNotes()
    }

    @PostMapping
    fun post(@RequestBody note: Note): ResponseEntity<EmptyResponse> {
        return try{
            service.insertOrUpdateNote(note)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping
    fun put(@RequestBody note: Note): ResponseEntity<EmptyResponse> {
        return try {
            service.insertOrUpdateNote(note)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping
    fun delete(@RequestParam noteId: Long) : ResponseEntity<EmptyResponse> {
        return try {
            service.delete(noteId)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}
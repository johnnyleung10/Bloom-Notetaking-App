package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.EmptyResponse
import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.services.FolderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/folder")
class FolderResource(val service: FolderService) {

    @GetMapping
    fun index(): List<Folder> {
        return service.findAllFolder()
    }

    @PostMapping
    fun post(@RequestBody folder: Folder): ResponseEntity<EmptyResponse> {
        return try{
            service.insertOrUpdate(folder)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //this is just a testing of put. It needs to be rewritten
    @PutMapping
    fun put(@RequestBody folder: Folder) : ResponseEntity<EmptyResponse> {
        return try{
            service.insertOrUpdate(folder)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //      this is just a testing of delete by id. It needs to be rewritten
    @DeleteMapping
    fun delete(@RequestParam folderId: Long) : ResponseEntity<EmptyResponse> {
        return try{
            service.delete(folderId)
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.OK)
        } catch(ex: Exception){
            ResponseEntity<EmptyResponse>(EmptyResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
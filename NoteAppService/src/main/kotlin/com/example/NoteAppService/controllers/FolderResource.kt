package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.services.FolderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/folder")
class FolderResource(val service: FolderService) {

    @GetMapping
    fun index(): List<Folder> {
        return service.findFolder()
    }

    @PostMapping
    fun post(@RequestBody folder: Folder) {
        service.post(folder)
    }
}
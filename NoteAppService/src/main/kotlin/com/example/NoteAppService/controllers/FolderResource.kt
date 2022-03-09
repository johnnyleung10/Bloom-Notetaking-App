package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.services.FolderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/folder")
class FolderResource(val service: FolderService) {

    @GetMapping
    fun index(): List<Folder> {
        return service.findAllFolder()
    }

//      this is just a testing of get by id. It needs to be rewritten
//    @GetMapping("/{folderId}")
//    fun index(@PathVariable folderId: Long): Folder? {
//        return service.findFolder(folderId)
//    }

    @PostMapping
    fun post(@RequestBody folder: Folder): Long {
        return service.post(folder)
    }

    //this is just a testing of put. It needs to be rewritten
    @PutMapping
    fun put(@RequestBody folder: Folder) {
        return service.put(1, folder.title)
    }

    //      this is just a testing of delete by id. It needs to be rewritten
//    @DeleteMapping("/{folderId}")
//    fun delete(@PathVariable folderId: Long) {
//        service.delete(folderId)
//    }
}
package com.example.NoteAppService.services

import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.repositories.FolderRepository
import org.springframework.stereotype.Service

@Service
class FolderService(private val db: FolderRepository){

    fun findFolder(): List<Folder> {
        val folderList: MutableList<Folder> = arrayListOf()
        db.findAll().forEach{folderList.add(it)}
        return folderList
    }

    fun post(folder: Folder) {
        db.save(folder)
    }
}
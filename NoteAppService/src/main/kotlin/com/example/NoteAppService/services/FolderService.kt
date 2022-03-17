package com.example.NoteAppService.services

import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.repositories.FolderRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class FolderService(private val db: FolderRepository){

    fun findAllFolder(): List<Folder> {
        try {
            val folderList: MutableList<Folder> = arrayListOf()
            db.findAll().forEach{folderList.add(it)}
            return folderList
        } catch(ex: DataAccessException){
            throw(ex)
        }

    }

    fun insertOrUpdate(folder: Folder) {
        try {
            db.save(folder)
        } catch(ex: DataAccessException){
            throw(ex)
        }
    }

    fun delete(folderId: Long) : Boolean {
        try {
            val folderOptional = db.findById(folderId)
            if (folderOptional.isPresent) {
                db.deleteById(folderId)
                return true
            }
            return false
        } catch(ex: DataAccessException){
            throw(ex)
        }
    }
}
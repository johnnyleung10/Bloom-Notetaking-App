package com.example.NoteAppService.services

import com.example.NoteAppService.models.Folder
import com.example.NoteAppService.repositories.FolderRepository
import org.springframework.stereotype.Service

@Service
class FolderService(private val db: FolderRepository){

    fun findAllFolder(): List<Folder> {
        val folderList: MutableList<Folder> = arrayListOf()
        db.findAll().forEach{folderList.add(it)}
        return folderList
    }

    fun findFolder(folderId: Long): Folder? {
        var folderOptional = db.findById(folderId)
        if (folderOptional.isPresent) {
            return folderOptional.get()
        }
        return null
    }

    fun insertOrUpdate(folder: Folder) {
        db.save(folder)
    }

    fun delete(folderId: Long) : Boolean {
        val folderOptional = db.findById(folderId)
        if (folderOptional.isPresent) {
            db.deleteById(folderId)
            return true
        }
        return false
    }

    fun put(folderId: Long, title: String? = null, dateModified: String? = null, dateDeleted: String? = null) : Long {
        val folderOptional = db.findById(folderId)

        if (folderOptional.isPresent) {
            val folder = folderOptional.get()
            title?.let { folder.title = title }
            dateModified?.let { folder.dateModified = dateModified }
            dateDeleted?.let { folder.dateDeleted = dateDeleted }
            return db.save(folder).id
        }
        return -1
    }
}
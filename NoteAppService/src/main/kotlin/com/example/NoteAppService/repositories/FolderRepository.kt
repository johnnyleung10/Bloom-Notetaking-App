package com.example.NoteAppService.repositories

import com.example.NoteAppService.models.Folder
import org.springframework.data.repository.CrudRepository

interface FolderRepository : CrudRepository<Folder, Long>{

}
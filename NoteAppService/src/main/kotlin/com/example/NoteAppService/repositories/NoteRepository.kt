package com.example.NoteAppService.repositories

import com.example.NoteAppService.models.Note
import org.springframework.data.repository.CrudRepository

interface NoteRepository : CrudRepository<Note, Long>{

}
package com.example.NoteAppService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NoteAppServiceApplication

fun main(args: Array<String>) {
	runApplication<NoteAppServiceApplication>(*args)
}

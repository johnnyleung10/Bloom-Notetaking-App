package com.example.notetakingapp.models

import android.content.Context

class FolderModel(
    title: String,
    context: Context
) : FileModel(title, context) {

    var contains = ArrayList<NoteModel>()
}
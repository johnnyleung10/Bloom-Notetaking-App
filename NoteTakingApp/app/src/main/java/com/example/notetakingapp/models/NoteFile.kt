package com.example.notetakingapp.models

import android.content.Context
import android.text.SpannableStringBuilder

class NoteFile(
    title : String,
    contents : SpannableStringBuilder,
    currFolder : String,
    context: Context,
) : NoteModel(title, contents, currFolder, context) {

}
package com.example.notetakingapp.models

import android.text.SpannableStringBuilder

class NoteFile(
    title : String,
    contents : SpannableStringBuilder,
    currFolder : String
) : NoteModel(title, contents, currFolder) {

}
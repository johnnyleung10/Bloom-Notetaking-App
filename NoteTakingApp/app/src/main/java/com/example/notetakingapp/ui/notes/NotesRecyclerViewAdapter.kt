package com.example.notetakingapp.ui.notes

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.models.NoteCellViewModel

class NotesRecyclerViewAdapter(var noteList: ArrayList<NoteCellViewModel>, private val onNoteClicked: (position: Int) -> Unit, var folderId: Long) : RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    private var editMode: Boolean = false
    var checked : MutableLiveData<MutableList<Long>> = MutableLiveData<MutableList<Long>>()

    init {
        checked.value = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_cell, parent, false)

        return ViewHolder(view, onNoteClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noteCellViewModel = noteList[position]
        holder.noteTitle.text = noteCellViewModel.title
        holder.checkbox.isChecked = checked.value?.contains(noteCellViewModel.noteId) == true
        holder.checkbox.isVisible = editMode
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun editMode(){
        editMode = !editMode
        this.notifyDataSetChanged()
    }

    fun selectAll(select: Boolean){
        if (select) {
            for (i in noteList)
                if (checked.value?.contains(i.noteId) == false) checked.value?.add(i.noteId)
        } else for(i in noteList) checked.value?.remove(i.noteId)

        checked.value = checked.value
        this.notifyDataSetChanged()
    }

    fun setNotes(notes: List<NoteCellViewModel>){
        noteList.clear()
        noteList.addAll(notes)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        init {
            itemView.setOnClickListener(this)

            if (folderId == 2.toLong())
                noteTitle.setTextColor(ColorStateList.valueOf(Color.LTGRAY))

            checkbox.setOnClickListener {
                val id = noteList[adapterPosition].noteId
                if (checked.value?.contains(id) == true) checked.value?.remove(id)
                else checked.value?.add(id)

                checked.value = checked.value
            }
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (folderId != 2.toLong()) onItemClicked(position)
        }
    }
}

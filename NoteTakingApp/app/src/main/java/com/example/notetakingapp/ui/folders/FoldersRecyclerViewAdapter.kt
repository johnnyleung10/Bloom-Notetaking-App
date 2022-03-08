package com.example.notetakingapp.ui.folders

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.models.FolderCellViewModel
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.example.notetakingapp.R

class FoldersRecyclerViewAdapter(var folderCellList: ArrayList<FolderCellViewModel>, private val onFolderClicked: (position: Int) -> Unit, private val onFolderRenamed: (position: Int, newTitle: String) -> Unit) : RecyclerView.Adapter<FoldersRecyclerViewAdapter.ViewHolder>() {

    private var editMode: Boolean = false
    private var selectAll: Boolean = false
    private var customCheck: Boolean = true
    var checked : MutableLiveData<MutableList<Int>> = MutableLiveData<MutableList<Int>>()

    init {
        checked.value = ArrayList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folder_cell, parent, false)

        return ViewHolder(view, onFolderClicked, onFolderRenamed)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val folderCellViewModel = folderCellList[position]
        holder.folderTitle.setText(folderCellViewModel.title)

        holder.notesInFolderCount.text = "(${folderCellViewModel.noteCount})"
        holder.notesInFolderCount.isVisible = !editMode

        if (position == 0 || position == 1) {
            holder.folderTitle.inputType = InputType.TYPE_NULL
            return
        }
        if (!editMode) {
            holder.folderTitle.inputType = InputType.TYPE_NULL
            holder.folderTitle.width = holder.folderTitle.text.length * 50
            holder.folderTitle.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            holder.checkbox.isChecked = false
            customCheck = true
        } else{
            holder.folderTitle.width = 1200
            holder.folderTitle.inputType = InputType.TYPE_CLASS_TEXT
            holder.folderTitle.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)

            if (!customCheck)
                holder.checkbox.isChecked = selectAll
        }

        holder.checkbox.isVisible = editMode
    }

    override fun getItemCount(): Int {
        return folderCellList.size
    }

    fun editMode(){
        editMode = !editMode
        this.notifyDataSetChanged()
    }

    fun selectAll(select: Boolean){
        selectAll = select

        val newChecked = ArrayList<Int>()
        if (selectAll){
            for(i in 2 until folderCellList.size)
                newChecked.add(i)
        }
        checked.value = newChecked
        customCheck = false
        this.notifyDataSetChanged()
    }

    fun setFolders(folderCells: ArrayList<FolderCellViewModel>){
        folderCellList.clear()
        folderCellList.addAll(folderCells)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(
        ItemView: View,
        private val onItemClicked: (position: Int) -> Unit,
        private val onFolderRenamed: (position: Int, newTitle: String, ) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val folderTitle: EditText = itemView.findViewById(R.id.folderTitle)
        val notesInFolderCount: TextView = itemView.findViewById(R.id.notesInFolderCount)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        init {
            itemView.setOnClickListener(this)

            folderTitle.setOnFocusChangeListener{ _, _ ->
                onFolderRenamed(adapterPosition, folderTitle.text.toString())
            }

            checkbox.setOnClickListener {
                if (checked.value?.contains(adapterPosition) == true)
                    checked.value?.remove(adapterPosition)
                else
                    checked.value?.add(adapterPosition)

                checked.value = checked.value
                customCheck = true
            }
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}

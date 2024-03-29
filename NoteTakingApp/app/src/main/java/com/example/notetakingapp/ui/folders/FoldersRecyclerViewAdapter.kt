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
import com.example.notetakingapp.utilities.FileManager
import kotlin.math.roundToInt

private const val UNCATEGORIZED_FOLDER : Long = 1
private const val RECENTLY_DELETED_FOLDER : Long = 2
private const val DAILY_ENTRY_FOLDER : Long = 3

class FoldersRecyclerViewAdapter(var folderCellList: ArrayList<FolderCellViewModel>, private val onFolderClicked: (position: Int) -> Unit, private val onFolderRenamed: (position: Int, newTitle: String) -> Unit) : RecyclerView.Adapter<FoldersRecyclerViewAdapter.ViewHolder>() {

    private var editMode: Boolean = false
    private var index = -1
    var checked : MutableLiveData<MutableList<Long>> = MutableLiveData<MutableList<Long>>()

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

        if (!editMode || folderCellViewModel.folderId == UNCATEGORIZED_FOLDER || folderCellViewModel.folderId == RECENTLY_DELETED_FOLDER
            || folderCellViewModel.folderId == DAILY_ENTRY_FOLDER) {
            holder.folderTitle.inputType = InputType.TYPE_NULL
            holder.folderTitle.width = (holder.folderTitle.paint.measureText(holder.folderTitle.text.toString())).roundToInt() + 40
            holder.folderTitle.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            holder.checkbox.isVisible = false
        } else{
            holder.folderTitle.width = 1200
            holder.folderTitle.inputType = InputType.TYPE_CLASS_TEXT
            holder.folderTitle.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY)
            holder.checkbox.isChecked = checked.value?.contains(folderCellViewModel.folderId) == true
            holder.checkbox.isVisible = true
        }
    }

    override fun getItemCount(): Int {
        return folderCellList.size
    }

    fun editMode(){
        editMode = !editMode
        this.notifyDataSetChanged()
    }

    fun selectAll(select: Boolean){
        if (select) {
            for (i in folderCellList)
                if (checked.value?.contains(i.folderId) == false) checked.value?.add(i.folderId)
        } else for (i in folderCellList) checked.value?.remove(i.folderId)
        checked.value?.remove(UNCATEGORIZED_FOLDER)
        checked.value?.remove(RECENTLY_DELETED_FOLDER)
        checked.value?.remove(DAILY_ENTRY_FOLDER)

        checked.value = checked.value
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
        private val onFolderRenamed: (position: Int, newTitle: String) -> Unit
    ) : RecyclerView.ViewHolder(ItemView), View.OnClickListener {

        val folderTitle: EditText = itemView.findViewById(R.id.folderTitle)
        val notesInFolderCount: TextView = itemView.findViewById(R.id.notesInFolderCount)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        init {
            itemView.setOnClickListener(this)

            folderTitle.setOnFocusChangeListener{ _, hasFocus ->
                if (adapterPosition != -1) index = adapterPosition
                if (!hasFocus) {
                    onFolderRenamed(index, folderTitle.text.toString())
                    folderCellList[index].title = folderTitle.text.toString()
                }
            }

            checkbox.setOnClickListener {
                val id = folderCellList[adapterPosition].folderId
                if (checked.value?.contains(id) == true) checked.value?.remove(id)
                else checked.value?.add(id)

                checked.value = checked.value
            }
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }
}

package com.example.notetakingapp.ui.home

import NotesRecyclerViewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentNotesBinding
import com.example.notetakingapp.models.FolderCellViewModel
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteCellViewModel
import com.example.notetakingapp.utilities.FileManager

class NotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private var _binding: FragmentNotesBinding? = null
    private var fm = FileManager.instance
    private var folderId: Long = 0
    private lateinit var folder: FolderModel
    private lateinit var folders: HashMap<Long, FolderModel>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            folderId = it.getLong("folder_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        folders = fm!!.folderList
        folder = folders[folderId]!!

        notesViewModel =
            ViewModelProvider(this).get(NotesViewModel::class.java)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val folderTitle: TextView = binding.folderTitle
        // Observer pattern
        notesViewModel.folderTitle.observe(viewLifecycleOwner, {
            folderTitle.text = it
        })

        val noteCount = binding.noteCount

        // Add notes to ViewModel for note data
        notesViewModel.setNotes(folder.noteList)

        notesViewModel.setFolderTitle(folders[folderId]!!.title)
        notesViewModel.folderID = folderId // Store folderID as well


        val notesRecyclerView = binding.noteContainer
        notesRecyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = NotesRecyclerViewAdapter(notesViewModel.noteCells.value!!, ::onNoteClick)
        notesRecyclerView.adapter = adapter

        // Observer pattern
        notesViewModel.noteCells.observe(viewLifecycleOwner, {
            noteCount.text = "(${it.size})"
            adapter.setNotes(it)
        })

        val editButton: ImageButton = binding.editNotes
        editButton.setOnClickListener{
            adapter.editMode()
            val visible = binding.actionButtons.visibility
            if (visible == View.VISIBLE)
                binding.actionButtons.visibility = View.GONE
            else
                binding.actionButtons.visibility = View.VISIBLE
            adapter.checked.value = ArrayList()
        }

        val newNoteButton: ImageButton = binding.newNote
        newNoteButton.setOnClickListener{
            Log.d("NOTE", "New note clicked")
            newNote()
        }

        val selectAll: Button = binding.selectAllNotes
        val deselectAll: Button = binding.deselectAllNotes
        // TODO: add onclicklistensers
        val delete: Button = binding.deleteNote
        val moveNote: Button = binding.moveNote
        val rename: Button = binding.renameNote

        adapter.checked.observe(viewLifecycleOwner, {
            val size = adapter.checked.value?.size ?: 0
            // TODO: check if size = Note count
            // TODO: discuss whether to keep both select and deselect

            deselectAll.isEnabled = false
            delete.isEnabled = false
            rename.isEnabled = false
            selectAll.isEnabled = false
            moveNote.isEnabled = false

            if (size >= 1){
                deselectAll.isEnabled = true
                delete.isEnabled = true
                moveNote.isEnabled = true
            }
            if (size == 1)
                rename.isEnabled = true
            if (size != 20)
                selectAll.isEnabled = true

        })

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNoteClick(position: Int) {
        // TODO: navigate to note explorer page for note at position
        System.out.println("click on Note $position")
    }

    private fun newNote() {
        //val folderCellViewModel = folderCellViewModels[position]
        val manager = FileManager.instance
        val newNote = manager?.createNewNote("New note", folderId)
        val action = NotesFragmentDirections.actionNavigationNotesToFragmentEditNote(newNote?.id!!)
        NavHostFragment.findNavController(this).navigate(action)
    }
}
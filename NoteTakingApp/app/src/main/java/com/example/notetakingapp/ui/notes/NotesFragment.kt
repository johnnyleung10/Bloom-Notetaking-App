package com.example.notetakingapp.ui.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentNotesBinding
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.ui.folders.FoldersRecyclerViewAdapter
import com.example.notetakingapp.viewmodels.NotesViewModel
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.viewmodels.FoldersViewModel

class NotesFragment : Fragment(), MoveNoteDialogFragment.MoveNoteDialogListener {

    private lateinit var notesViewModel: NotesViewModel
    private var _binding: FragmentNotesBinding? = null
    private lateinit var fm: FileManager
    private var folderId: Long = 0
    private lateinit var folder: FolderModel
    private lateinit var folders: HashMap<Long, FolderModel>
    private lateinit var adapter: NotesRecyclerViewAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            folderId = it.getLong("folder_id")
        }

        fm = FileManager.instance!!
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        folders = fm.folderList
        folder = folders[folderId]!!

        notesViewModel.setFolderTitle(folders[folderId]!!.title)
        notesViewModel.folderID = folderId // Store folderID as well
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupFolderTitle()

        // Setup recycler view
        setupRecyclerView()

        // Only set notes in ViewModel once observer has been created!
        notesViewModel.setNotes(folder.noteList)

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    private fun setupFolderTitle(){
        val folderTitle: TextView = binding.folderTitle
        // Observer pattern
        notesViewModel.folderTitle.observe(viewLifecycleOwner) {
            folderTitle.text = it
        }
    }

    private fun setupRecyclerView() {
        val noteCount = binding.noteCount
        val notesRecyclerView = binding.noteContainer
        notesRecyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = NotesRecyclerViewAdapter(ArrayList(), ::onNoteClick)
        notesRecyclerView.adapter = adapter

        // Observer pattern
        notesViewModel.noteCells.observe(viewLifecycleOwner) {
            noteCount.text = "(${it.size})"
            adapter.setNotes(it)
        }
    }

    private fun addListeners(){
        val newNoteButton: ImageButton = binding.newNote
        val editButton: ImageButton = binding.editNotes
        val delete: Button = binding.deleteNote
        val moveNote: Button = binding.moveNote
        val selectAll: Button = binding.selectAllNotes
        val deselectAll: Button = binding.deselectAllNotes

        newNoteButton.setOnClickListener{
            newNote()
        }

        editButton.setOnClickListener{
            editNotes()
        }

        delete.setOnClickListener{
            deleteNotes()
        }

        moveNote.setOnClickListener { _ ->
            moveNote()
        }

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

        adapter.checked.observe(viewLifecycleOwner) {
            val size = adapter.checked.value?.size ?: 0

            deselectAll.isEnabled = false
            delete.isEnabled = false
            selectAll.isEnabled = false
            moveNote.isEnabled = false

            if (size >= 1) {
                deselectAll.isEnabled = true
                delete.isEnabled = true
                moveNote.isEnabled = true
            }
            if (size != adapter.itemCount)
                selectAll.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNoteClick(position: Int) {
        val noteId = notesViewModel.noteCells.value!![position].noteId
        val action =
            NotesFragmentDirections.actionNavigationNotesToFragmentEditNote(
                noteId
            )
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun newNote() {
        val manager = FileManager.instance
        val newNote = manager?.createNewNote("New Note", folderId)
        val action =
            NotesFragmentDirections.actionNavigationNotesToFragmentEditNote(
                newNote?.id!!
            )
        NavHostFragment.findNavController(this).navigate(action)

        val folder = manager.folderList[notesViewModel.folderID]
        notesViewModel.setNotes(folder!!.noteList)
    }

    private fun editNotes(){
        adapter.editMode()
        val visible = binding.actionButtons.visibility
        if (visible == View.VISIBLE)
            binding.actionButtons.visibility = View.GONE
        else
            binding.actionButtons.visibility = View.VISIBLE
        adapter.checked.value = ArrayList()
    }

    private fun deleteNotes(){
        for (i in adapter.checked.value!!)
            fm.deleteNote(adapter.noteList[i].noteId)
        // Update the view model!
        adapter.selectAll(false)
        notesViewModel.setNotes(folder.noteList)
    }

    private fun moveNote() {
        val dialogFragment = MoveNoteDialogFragment(fm.folderList.values.toTypedArray())
        dialogFragment.show(requireFragmentManager().beginTransaction(), "move_note")
        dialogFragment.setTargetFragment(this, 1);
    }

    /* MoveNoteDialogListener */
    override fun onMoveNote(dialog: DialogFragment, newFolderId: Long) {
        if(adapter.checked.value?.size != 1){
            return
        }
        val notePosition = adapter.checked.value!![0]
        val noteId = adapter.noteList[notePosition].noteId

        fm.moveNote(noteId, newFolderId)

        // Update the view model!
        adapter.selectAll(false)
        val folder = fm.folderList[notesViewModel.folderID]
        notesViewModel.setNotes(folder!!.noteList)
    }
}
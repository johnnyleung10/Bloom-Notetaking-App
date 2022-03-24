package com.example.notetakingapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentNotesBinding
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.viewmodels.NotesViewModel
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.utilities.Profiler
import kotlin.system.measureTimeMillis

class NotesFragment : Fragment(), MoveNoteDialogFragment.MoveNoteDialogListener {

    private lateinit var notesViewModel: NotesViewModel
    private var _binding: FragmentNotesBinding? = null
    private lateinit var fm: FileManager
    private var folderId: Long = 0
    private lateinit var folder: FolderModel
    private lateinit var folders: HashMap<Long, FolderModel>
    private lateinit var adapter: NotesRecyclerViewAdapter
    private lateinit var profiler: Profiler

    // This property is only valid between onCreateView and onDestroyView.
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

        profiler = Profiler.instance!!

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

        adapter = NotesRecyclerViewAdapter(ArrayList(), ::onNoteClick, folderId)
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
        val search: EditText = binding.searchTerm
        val spinner: Spinner = binding.sortBy
        val sortOrder: ImageButton = binding.sortOrder
        val blank: TextView = binding.blank

        if (folder.noteList.size == 0) blank.isVisible = true

        if (folderId == 2.toLong()){
            newNoteButton.isVisible = false
            moveNote.text = "Restore"
            moveNote.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.restore, 0, 0)
        }
        newNoteButton.setOnClickListener{
            val elapsedNewNote= measureTimeMillis { newNote() }

            profiler.open()
            profiler.profile("create a new note", elapsedNewNote)
            profiler.close()
        }

        editButton.setOnClickListener{
            editNotes()
        }

        delete.setOnClickListener{
            if (folderId == 2.toLong()) permanentlyDeleteNotes()
            else deleteNotes()
        }

        moveNote.setOnClickListener {
            if (folderId == 2.toLong()) restoreNote()
            else moveNote()
        }

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

        sortOrder.setOnClickListener{
            search.text.clear()

            var column = "date_modified"
            var order = true

            if (spinner.selectedItemPosition == 0) column = "title"
            else if (spinner.selectedItemPosition == 1) column = "date_created"
            if (sortOrder.contentDescription == "true"){
                order = false
                sortOrder.contentDescription = "false"
            } else sortOrder.contentDescription = "true"

            fm.sortNotes(column, folderId, order)
            notesViewModel.setNotes(folder.noteList)
        }

        search.text.clear()
        search.addTextChangedListener {
            val noteIds = fm.searchNotes(search.text.toString(), folderId)
            val results : ArrayList<NoteModel> = ArrayList()

            for (i in folder.noteList)
                if (i.id in noteIds) results.add(i)

            notesViewModel.setNotes(results)
        }

        ArrayAdapter.createFromResource(
            requireContext(), R.array.sort_by, R.layout.sort_dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(2)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                search.text.clear()
                var column = "date_modified"
                var order = true

                if (position == 0) column = "title"
                else if (position == 1) column = "date_created"
                if (sortOrder.contentDescription == "false") order = false

                fm.sortNotes(column, folderId, order)
                notesViewModel.setNotes(folder.noteList)
            }
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

    private fun updateView(){
        adapter.selectAll(false)
        notesViewModel.setNotes(folder.noteList)
    }

    private fun onNoteClick(position: Int) {
        val noteId = notesViewModel.noteCells.value!![position].noteId
        val action = NotesFragmentDirections.actionNavigationNotesToFragmentEditNote(noteId)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun newNote() {
        val manager = FileManager.instance
        val newNote = manager?.createNewNote("New Note", folderId)
        val action = NotesFragmentDirections.actionNavigationNotesToFragmentEditNote(newNote?.id!!)
        NavHostFragment.findNavController(this).navigate(action)

        notesViewModel.setNotes(folder.noteList)
    }

    private fun editNotes(){
        adapter.editMode()
        val visible = binding.actionButtons.visibility
        if (visible == View.VISIBLE)
            binding.actionButtons.visibility = View.GONE
        else binding.actionButtons.visibility = View.VISIBLE
        adapter.checked.value = ArrayList()
    }

    private fun deleteNotes(){
        for (i in adapter.checked.value!!) fm.deleteNote(i)
        updateView()
    }

    private fun permanentlyDeleteNotes(){
        for (i in adapter.checked.value!!) fm.permanentlyDeleteNote(i)
        updateView()
    }

    private fun restoreNote(){
        for (i in adapter.checked.value!!) fm.restoreNote(i)
        updateView()
    }

    /* MoveNoteDialogListener */
    override fun onMoveNote(dialog: DialogFragment, newFolderId: Long) {
        for (i in adapter.checked.value!!) fm.moveNote(i, newFolderId)
        updateView()
    }

    private fun moveNote() {
        val dialogFragment = MoveNoteDialogFragment(fm.folderList.values.toTypedArray())
        dialogFragment.show(requireFragmentManager().beginTransaction(), "move_note")
        dialogFragment.setTargetFragment(this, 1)
    }
}
package com.example.notetakingapp.ui.home

import NotesRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        // Add notes to ViewModel for note data
        notesViewModel.setNotes(folder.noteList)

        notesViewModel.setFolderTitle(folders[folderId]!!.title)

        val notesRecyclerView = binding.noteContainer
        notesRecyclerView.layoutManager = LinearLayoutManager(activity)

        val adapter = NotesRecyclerViewAdapter(notesViewModel.noteCells.value!!.toList(), ::onNoteClick)
        notesRecyclerView.adapter = adapter

        // Observer pattern
        notesViewModel.noteCells.observe(viewLifecycleOwner, {
            adapter.setNotes(it.toList())
        })

        val editButton: ImageButton = binding.editNotes
        editButton.setOnClickListener{
            adapter.editMode()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNoteClick(position: Int) {
        // TODO: navigate to note explorer page for note at position
        System.out.println("click on folder $position")
    }
}
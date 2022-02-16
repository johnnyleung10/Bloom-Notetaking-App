package com.example.notetakingapp.ui.home

import NotesRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentNotesBinding
import com.example.notetakingapp.models.NoteCellViewModel

class NotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private var _binding: FragmentNotesBinding? = null
    private lateinit var folderId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            folderId = it.getString("folder_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notesViewModel =
            ViewModelProvider(this).get(NotesViewModel::class.java)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val folderTitle: TextView = binding.folderTitle
        // Observer pattern
        notesViewModel.folderTitle.observe(viewLifecycleOwner, {
            folderTitle.text = it
        })

        // TODO: get the folder name here
        notesViewModel.setFolderTitle(folderId)

        val notesRecyclerView = binding.noteContainer
        notesRecyclerView.layoutManager = LinearLayoutManager(activity)

        val data = ArrayList<NoteCellViewModel>()
        // TODO: get data from DB here
        for (i in 1..20) {
            data.add(NoteCellViewModel( "Note " + i))
        }

        val adapter = NotesRecyclerViewAdapter(data, ::onNoteClick)
        notesRecyclerView.adapter = adapter

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
}
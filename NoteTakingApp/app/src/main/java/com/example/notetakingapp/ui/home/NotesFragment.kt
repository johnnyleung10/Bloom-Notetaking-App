package com.example.notetakingapp.ui.home

import NotesRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentNotesBinding
import com.example.notetakingapp.models.FolderCellViewModel
import com.example.notetakingapp.models.NoteCellViewModel

class NotesFragment : Fragment() {

    private lateinit var notesViewModel: com.example.notetakingapp.ui.home.NotesViewModel
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
    ): View? {
        notesViewModel =
            ViewModelProvider(this).get(NotesViewModel::class.java)

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val notesRecyclerView = root.findViewById<RecyclerView>(R.id.noteContainer)

        notesRecyclerView.layoutManager = LinearLayoutManager(activity)

        val data = ArrayList<NoteCellViewModel>()

        // TODO: get data from DB here
        for (i in 1..20) {
            data.add(NoteCellViewModel( "Note " + i))
        }

        val adapter = NotesRecyclerViewAdapter(data, ::onNoteClick)

        notesRecyclerView.adapter = adapter

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
package com.example.notetakingapp.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentEditNoteBinding
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.google.android.material.textfield.TextInputEditText

class EditNoteFragment : Fragment() {

    companion object {
        fun newInstance() = EditNoteFragment()
    }

    private lateinit var viewModel: EditNoteViewModel
    private var _binding: FragmentEditNoteBinding? = null
    private var noteID: Long = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            noteID = it.getLong("note_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel =
            ViewModelProvider(this).get(EditNoteViewModel::class.java)

        val editNoteTitle: TextView = binding.editNoteTitle
        val editNoteContents = binding.editNoteContents

        // Observer pattern
        viewModel.noteTitle.observe(viewLifecycleOwner, {
            editNoteTitle.text = it
        })

        viewModel.setNoteTitle("This is a test title")

        Log.d("EditText", editNoteTitle.text.toString())

//        editNoteTitle.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.d("EditText", "Text title changed")
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })

        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.notetakingapp.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.example.notetakingapp.databinding.FragmentEditNoteBinding
import com.example.notetakingapp.utilities.FileManager

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
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val manager = FileManager.instance
        val note = manager?.getNote(noteID)

        val editNoteTitle = binding.editNoteTitle
        val editNoteContents = binding.editNoteContents

//        val title : MutableLiveData<String> = MutableLiveData<String>()
//        val contents : MutableLiveData<String> = MutableLiveData<String>()

        if (note != null && note.title != "New Note") {
            editNoteTitle.setText(note.title)
        }
        if (note != null && note.contents.toString() != "") {
            editNoteContents.setText(Html.fromHtml(note.contents.toString()))
        }

//        title.value = note!!.title
//        contents.value = note.contents.toString()

        editNoteTitle.setOnFocusChangeListener { v, hasFocus ->
            note!!.title = editNoteTitle.text.toString()
            if (note.title == "")
                note.title = "New Note"
        }

        editNoteContents.setOnFocusChangeListener { v, hasFocus ->
            note!!.contents = SpannableStringBuilder(editNoteContents.text)
        }

//        editNoteContents.addTextChangedListener {
//            System.out.println("text ")
//        }

//        contents.observe(viewLifecycleOwner, {
//            note.contents = SpannableStringBuilder(contents.value)
//        })
//
//        title.observe(viewLifecycleOwner, {
//            note.title = title.value.toString()
//        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
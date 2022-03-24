package com.example.notetakingapp.ui.editnote

import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notetakingapp.databinding.FragmentEditNoteBinding
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.MainActivity

private const val DAILY_ENTRY_FOLDER : Long = 3

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

        val boldButton = binding.boldButton
        var boldFlag = false
        val italicsButton = binding.italicsButton
        var italicsFlag = false
        val underlineButton = binding.underlineButton
        var underlineFlag = false

        var lastCursorPosition = editNoteContents.selectionStart

        if (note != null && note.title != "New Note") {
            editNoteTitle.setText(note.title)
        }
        if (note != null && note.contents.toString() != "") {
            editNoteContents.setText(note.contents)
        }
        if (note != null && note.folderID == DAILY_ENTRY_FOLDER) { // DAILY ENTRY
            editNoteTitle.isEnabled = false
        }

        // BUTTONS
        boldButton.setOnClickListener {
            if (!addSpanToText(editNoteContents, Typeface.BOLD)) {
                boldFlag = !boldFlag
            }
        }
        italicsButton.setOnClickListener {
            if (!addSpanToText(editNoteContents, Typeface.ITALIC)) {
                italicsFlag = !italicsFlag
            }
        }
        underlineButton.setOnClickListener {
            addSpanToText(editNoteContents, 0, underline = true)
            underlineFlag = !underlineFlag
        }

        (requireActivity() as MainActivity).navView.visibility = View.GONE

        editNoteContents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                lastCursorPosition = editNoteContents.selectionStart
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastCursorPosition = editNoteContents.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str: Spannable = editNoteContents.text
                val endLength: Int = start + count


                if (boldFlag) {
                    if (lastCursorPosition <= endLength) {
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            lastCursorPosition,
                            endLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                if (italicsFlag) {
                    if (lastCursorPosition <= endLength) {
                        str.setSpan(
                            StyleSpan(Typeface.ITALIC),
                            lastCursorPosition,
                            endLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
                if (underlineFlag) {
                    if (lastCursorPosition <= endLength) {
                        str.setSpan(
                            UnderlineSpan(),
                            lastCursorPosition,
                            endLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).navView.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        val manager = FileManager.instance
        val note = manager?.getNote(noteID)
        note!!.title = binding.editNoteTitle.text.toString()
        if (note.title == "") note.title = "New Note"
        note!!.contents = SpannableStringBuilder(binding.editNoteContents.text)
        manager.editNote(noteID, title = note.title, contents = note.contents)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun addSpanToText(editNoteContents : EditText, typeface : Int, underline : Boolean? = null) : Boolean {
        // Add span to selected
        val str: Spannable = editNoteContents.text

        val selStart = editNoteContents.selectionStart
        val selEnd = editNoteContents.selectionEnd
        val range = selEnd - selStart
        if (selStart < selEnd) {
            var spanAll = false
            val spans = str.getSpans(selStart, selEnd, StyleSpan::class.java)
            val underlineSpans = str.getSpans(selStart, selEnd, UnderlineSpan::class.java)
            Log.d("SPAN_CHAR", underlineSpans.size.toString())

            // Check to see if add span or remove span
            if (spans.size < range || (underline == true && underlineSpans.size < range)) {
                spanAll = true
            }
            else {
                var i = selStart
                for (j in selStart + 1 until selEnd + 1) {
                    if (underline == true) {
                        val thisSpan = str.getSpans(i, j, UnderlineSpan::class.java)
                        for (k in thisSpan.indices) {
                            Log.d("SPANTYPE", "thisSpan: " + thisSpan[k].spanTypeId + " underSpan: " +UnderlineSpan().spanTypeId)
                            if (thisSpan[k].spanTypeId == UnderlineSpan().spanTypeId) break
                            if (k == thisSpan.lastIndex) spanAll = true
                        }
                        if (spanAll) break
                    } else {
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        for (k in thisSpan.indices) {
                            if (thisSpan[k].style == typeface) break
                            if (k == thisSpan.lastIndex) spanAll = true
                        }
                        if (spanAll) break
                    }
                    i = j
                }
            }
            Log.d("Underline SpanALL", spanAll.toString())
            if (!spanAll && spans.size >= selEnd - selStart || !spanAll && underlineSpans.size >= selEnd - selStart) {
                if (underline == true) {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        val thisSpan = str.getSpans(i, j, UnderlineSpan::class.java)
                        for (span in thisSpan) {
                            str.removeSpan(span)
                        }
                        i = j
                    }
                } else {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        for (span in thisSpan) {
                            if (span.style == typeface) {
                                str.removeSpan(span)
                            }
                        }
                        i = j
                    }
                }
            } else {
                if (underline == true) {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        str.setSpan(
                            UnderlineSpan(),
                            i,
                            j,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        i = j
                    }
                } else {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        str.setSpan(
                            StyleSpan(typeface),
                            i,
                            j,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        i = j
                    }
                }
            }
            return true
        }
        return false
    }
}
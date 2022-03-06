package com.example.notetakingapp.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val boldButton = binding.boldButton
        var boldFlag : Boolean = false
        val italicsButton = binding.italicsButton
        var italicsFlag : Boolean = false

        var lastCursorPosition = editNoteContents.selectionStart

        if (note != null && note.title != "New Note") {
            editNoteTitle.setText(note.title)
        }
        if (note != null && note.contents.toString() != "") {
            editNoteContents.setText(note.contents)
        }

        editNoteTitle.setOnFocusChangeListener { v, hasFocus ->
            note!!.title = editNoteTitle.text.toString()

            if (note.title == "")
                note.title = "New Note"

            manager.editNote(noteID, title = note.title)
        }

        // BUTTONS
        boldButton.setOnClickListener {
            // Bold selected
            val str: Spannable = editNoteContents.text

            val selStart = editNoteContents.selectionStart
            val selEnd = editNoteContents.selectionEnd
            if (selStart < selEnd) {
                var boldAll = false
                val spans = str.getSpans(selStart, selEnd, StyleSpan::class.java)

                // Check to see bold or unbold
                for (i in spans.indices) {
                    Log.d("STYLE_SPAN", spans[i].style.toString())
                    if (spans[i].style != Typeface.BOLD && spans[i].style != Typeface.BOLD_ITALIC) {
                        boldAll = true
                        break
                    }
                }
                Log.d("STYLE", selStart.toString() + " " +selEnd + " " +spans.size)
                if (!boldAll && spans.size >= selEnd - selStart) {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        if (thisSpan.isNotEmpty()) {
                            if (thisSpan[0].style == Typeface.BOLD) {
                                str.removeSpan(thisSpan[0])
                            } else if (thisSpan[0].style == Typeface.BOLD_ITALIC) {
                                str.removeSpan(thisSpan[0])
                                str.setSpan(
                                    StyleSpan(Typeface.ITALIC),
                                    i,
                                    j,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }
                        }
                        i = j
                    }
                } else {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        Log.d("Span", "$i $j")
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        if (thisSpan.isEmpty()) {
                            str.setSpan(
                                StyleSpan(Typeface.BOLD),
                                i,
                                j,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        } else if (thisSpan[0].style == Typeface.ITALIC) {
                            str.removeSpan(thisSpan[0])
                            str.setSpan(
                                StyleSpan(Typeface.BOLD_ITALIC),
                                i,
                                j,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        i = j
                    }
                }
            } else {
                boldFlag = !boldFlag
            }
        }
        italicsButton.setOnClickListener {
            // Italicize selected
            val str: Spannable = editNoteContents.text

            val selStart = editNoteContents.selectionStart
            val selEnd = editNoteContents.selectionEnd
            if (selStart < selEnd) {
                var boldAll = false
                val spans = str.getSpans(selStart, selEnd, StyleSpan::class.java)

                // Check to see italicize or unitalicize
                for (i in spans.indices) {
                    Log.d("STYLE", spans[i].style.toString())
                    if (spans[i].style != Typeface.ITALIC && spans[i].style != Typeface.BOLD_ITALIC) {
                        boldAll = true
                        break
                    }
                }
                Log.d("STYLE", selStart.toString() + " " +selEnd + " " +spans.size)
                if (!boldAll && spans.size >= selEnd - selStart) {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        if (thisSpan.isNotEmpty()) {
                            if (thisSpan[0].style == Typeface.ITALIC) {
                                str.removeSpan(thisSpan[0])
                            } else if (thisSpan[0].style == Typeface.BOLD_ITALIC) {
                                str.removeSpan(thisSpan[0])
                                str.setSpan(
                                    StyleSpan(Typeface.BOLD),
                                    i,
                                    j,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }
                        }
                        i = j
                    }

//                    for (i in spans.indices) {
//                        if (spans[i].style == Typeface.BOLD) {
//                            str.removeSpan(spans[i])
//                        }
//                    }
                } else {
                    var i = selStart
                    for (j in selStart + 1 until selEnd + 1) {
                        Log.d("Span", "$i $j")
                        val thisSpan = str.getSpans(i, j, StyleSpan::class.java)
                        if (thisSpan.isEmpty()) {
                            str.setSpan(
                                StyleSpan(Typeface.ITALIC),
                                i,
                                j,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        } else if (thisSpan[0].style == Typeface.BOLD) {
                            str.removeSpan(thisSpan[0])
                            str.setSpan(
                                StyleSpan(Typeface.BOLD_ITALIC),
                                i,
                                j,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        i = j
                    }
                }
            } else {
                italicsFlag = !italicsFlag
            }
        }

        editNoteContents.setOnFocusChangeListener { v, hasFocus ->
            //note!!.contents = SpannableStringBuilder(editNoteContents.text)
            //note!!.contents = SpannableStringBuilder(editNoteContents.text)
            note!!.contents = SpannableStringBuilder(editNoteContents.text)
            Log.d("HTML", Html.toHtml(note.contents))
            manager.editNote(noteID, contents = note.contents)
        }

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

                if (boldFlag && italicsFlag) {
                    if (lastCursorPosition <= endLength) {
                        Log.d("Span", "$lastCursorPosition $endLength")
                        str.setSpan(
                            StyleSpan(Typeface.BOLD_ITALIC),
                            lastCursorPosition,
                            endLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } else if (boldFlag) {
                    if (lastCursorPosition <= endLength) {
                        Log.d("Span", "$lastCursorPosition $endLength")
                        str.setSpan(
                            StyleSpan(Typeface.BOLD),
                            lastCursorPosition,
                            endLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } else if (italicsFlag) {
                    if (lastCursorPosition <= endLength) {
                        Log.d("Span", "$lastCursorPosition $endLength")
                        str.setSpan(
                            StyleSpan(Typeface.ITALIC),
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
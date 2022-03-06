package com.example.notetakingapp.ui.notes

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.models.FolderModel


class MoveNoteDialogFragment(private val folderList: Array<FolderModel>) : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: MoveNoteDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface MoveNoteDialogListener {
        fun onMoveNote(dialog: DialogFragment, folderid: Long)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//
//            val mView: View = inflater.inflate(R.layout.fragment_rename_folder_dialog, null)
//            val folderNameTextInput = mView.findViewById<EditText>(R.id.newFolderTitle)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout

            builder.setTitle(R.string.choose_folder)
                .setItems(folderList.map{it.title}.toTypedArray(),
                    DialogInterface.OnClickListener { dialog, which ->
                        listener = getTargetFragment() as MoveNoteDialogListener
                        listener.onMoveNote(this, folderList[which].id)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package com.example.notetakingapp.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.notetakingapp.R


class NewFolderDialogFragment : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: NewFolderDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NewFolderDialogListener {
        fun onCreateNewFolder(dialog: DialogFragment, newFolderName: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            val mView: View = inflater.inflate(R.layout.fragment_create_folder_dialog, null)
            val folderNameTextInput = mView.findViewById<EditText>(R.id.newFolderTitle)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(mView)
                // Add action buttons
                .setPositiveButton(R.string.create,
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                        listener = getTargetFragment() as NewFolderDialogListener
                        val newFolderName = folderNameTextInput.text.toString()
                        listener.onCreateNewFolder(this, newFolderName)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
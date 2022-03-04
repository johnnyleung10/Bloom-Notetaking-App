package com.example.notetakingapp.ui.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.example.notetakingapp.viewmodels.FoldersViewModel
import com.example.notetakingapp.utilities.FileManager

class FoldersFragment : Fragment(),
    NewFolderDialogFragment.NewFolderDialogListener,
    RenameFolderDialogFragment.RenameFolderDialogListener {

    private lateinit var foldersViewModel: FoldersViewModel
    private var _binding: FragmentFoldersBinding? = null
    private lateinit var fm: FileManager
    private lateinit var adapter: FoldersRecyclerViewAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fm = FileManager.instance!!
        foldersViewModel = ViewModelProvider(this)[FoldersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup recycler view
        setupRecyclerView()

        // Only set folders in ViewModel once observer has been created!
        foldersViewModel.setFolders(fm.folderList)

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    private fun setupRecyclerView() {
        val folderCount = binding.foldersCount
        val folderRecyclerView = binding.folderContainer
        folderRecyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = FoldersRecyclerViewAdapter(ArrayList(), ::onFolderClick)
        folderRecyclerView.adapter = adapter

        // Observer pattern
        foldersViewModel.folderCells.observe(viewLifecycleOwner) {
            folderCount.text = "(${it.size})"
            adapter.setFolders(it)
        }
    }

    private fun addListeners(){
        val newNoteButton: ImageButton = binding.newNote
        val createFolderButton: Button = binding.createFolder
        val renameFolderButton: Button = binding.renameFolder
        val editButton: ImageButton = binding.editFolder
        val deleteFolderButton: Button = binding.deleteFolder
        val selectAll: Button = binding.selectAllFolders
        val deselectAll: Button = binding.deselectAllFolders

        newNoteButton.setOnClickListener {
            newNote()
        }

        createFolderButton.setOnClickListener {
            createNewFolder()
        }

        renameFolderButton.setOnClickListener {
            renameFolder()
        }

        editButton.setOnClickListener{
            editFolders()
        }

        deleteFolderButton.setOnClickListener{
            deleteFolders()
        }

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

        adapter.checked.observe(viewLifecycleOwner) {
            val size = adapter.checked.value?.size ?: 0

            deselectAll.isEnabled = false
            deleteFolderButton.isEnabled = false
            renameFolderButton.isEnabled = false
            selectAll.isEnabled = false

            if (size >= 1) {
                deselectAll.isEnabled = true
                deleteFolderButton.isEnabled = true
            }
            if (size == 1)
                renameFolderButton.isEnabled = true
            if (size != adapter.itemCount)
                selectAll.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFolderClick(position: Int) {
        val folderCellViewModel = foldersViewModel.folderCells.value!![position]
        val action =
            FoldersFragmentDirections.actionNavigationFoldersToNavigationNotes(
                folderCellViewModel.folderId
            )
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun newNote() {
        val manager = FileManager.instance
        val newNote = manager?.createNewNote("New Note", 1)
        val action =
            FoldersFragmentDirections.actionNavigationFoldersToFragmentEditNote(
                newNote?.id!!
            )
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun createNewFolder() {
        val dialogFragment = NewFolderDialogFragment()
        dialogFragment.show(requireFragmentManager().beginTransaction(), "create_folder")
        dialogFragment.setTargetFragment(this, 1)
    }

    // TODO: convert to in-place rename
    private fun renameFolder() {
        if(adapter.checked.value?.size != 1){
            return
        }
        val folderPosition = adapter.checked.value!![0]
        val folderName = adapter.folderCellList[folderPosition].title

        val dialogFragment = RenameFolderDialogFragment(folderName)
        dialogFragment.show(requireFragmentManager().beginTransaction(), "rename_folder")
        dialogFragment.setTargetFragment(this, 1)
    }

    private fun editFolders(){
        adapter.editMode()
        val visible = binding.actionButtons.visibility
        if (visible == View.VISIBLE)
            binding.actionButtons.visibility = View.GONE
        else
            binding.actionButtons.visibility = View.VISIBLE
        adapter.checked.value = ArrayList()
    }

    private fun deleteFolders(){
        for (i in adapter.checked.value!!)
            fm.deleteFolder(adapter.folderCellList[i].folderId)
        // Update the view model!
        adapter.selectAll(false)
        foldersViewModel.setFolders(fm.folderList)
    }

    /* NewFolderDialogListener */
    override fun onCreateNewFolder(dialog: DialogFragment, newFolderName: String) {
        fm.createNewFolder(newFolderName)
        // Update the view model!
        foldersViewModel.setFolders(fm.folderList)
    }

    /* RenameFolderDialogListener */
    override fun onRenameFolder(dialog: DialogFragment, newFolderName: String) {
        if(adapter.checked.value?.size != 1){
            return
        }
        val folderPosition = adapter.checked.value!![0]
        val folderId = adapter.folderCellList[folderPosition].folderId

        fm.editFolder(folderId, newFolderName)

        // Update the view model!
        adapter.selectAll(false)
        foldersViewModel.setFolders(fm.folderList)
    }

}
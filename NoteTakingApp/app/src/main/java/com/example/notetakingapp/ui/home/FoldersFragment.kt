package com.example.notetakingapp.ui.home

import FoldersRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.example.notetakingapp.models.FolderCellViewModel
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.utilities.FileManager

class FoldersFragment : Fragment(),
    NewFolderDialogFragment.NewFolderDialogListener{

    private lateinit var foldersViewModel: FoldersViewModel
    private var _binding: FragmentFoldersBinding? = null
    private var fm = FileManager.instance
    private lateinit var folders: HashMap<Long, FolderModel>
    private var folderCellViewModels = ArrayList<FolderCellViewModel>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        foldersViewModel =
            ViewModelProvider(this).get(FoldersViewModel::class.java)

        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val createFolderButton = binding.createFolder

        createFolderButton.setOnClickListener { _ ->
            createNewFolder()
        }

        val folderRecyclerView = binding.folderContainer
        folderRecyclerView.layoutManager = LinearLayoutManager(activity)


        // Create ViewModels for folder data
        folders = fm!!.folderList
        for((folderId, folder) in folders){
            folderCellViewModels.add(FolderCellViewModel(folderId, folder.title))
        }

        val adapter = FoldersRecyclerViewAdapter(folderCellViewModels, ::onFolderClick)
        folderRecyclerView.adapter = adapter

        val editButton: ImageButton = binding.editFolder
        editButton.setOnClickListener{
            adapter.editMode()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFolderClick(position: Int) {
        val folderCellViewModel = folderCellViewModels[position]
        val action = FoldersFragmentDirections.actionNavigationFoldersToNavigationNotes(folderCellViewModel.folderId)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun createNewFolder() {
        val dialogFragment = NewFolderDialogFragment()
        dialogFragment.show(requireFragmentManager().beginTransaction(), "create_folder")
        dialogFragment.setTargetFragment(this, 1);
    }

    // NewFolderDialogListener

    override fun onCreateNewFolder(dialog: DialogFragment, newFolderName: String) {
        fm?.createNewFolder(newFolderName)
    }
}
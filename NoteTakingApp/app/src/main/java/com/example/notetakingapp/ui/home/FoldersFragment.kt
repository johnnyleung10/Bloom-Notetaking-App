package com.example.notetakingapp.ui.home

import FoldersRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.example.notetakingapp.models.FolderCellViewModel


class FoldersFragment : Fragment() {

    private lateinit var foldersViewModel: com.example.notetakingapp.ui.home.FoldersViewModel
    private var _binding: FragmentFoldersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val data = ArrayList<FolderCellViewModel>()

        // TODO: get data from DB here
        for (i in 1..20) {
            data.add(FolderCellViewModel( "Folder " + i))
        }

        val adapter = FoldersRecyclerViewAdapter(data, ::onFolderClick)

        folderRecyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFolderClick(position: Int) {
        // TODO: navigate to note explorer page for note at position
        val action = FoldersFragmentDirections.actionNavigationFoldersToNavigationNotes("Folder #${position+1}")
        NavHostFragment.findNavController(this).navigate(action)
        System.out.println("clicked on folder $position")
    }

    private fun createNewFolder() {
        val dialogFragment = NewFolderDialogFragment()
        dialogFragment.show(childFragmentManager, "create_folder")
    }
}
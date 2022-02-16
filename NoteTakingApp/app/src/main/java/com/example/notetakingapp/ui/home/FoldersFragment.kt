package com.example.notetakingapp.ui.home

import FoldersRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.example.notetakingapp.models.FolderCellViewModel

class FoldersFragment : Fragment() {

    private lateinit var foldersViewModel: FoldersViewModel
    private var _binding: FragmentFoldersBinding? = null

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

        val folderRecyclerView = binding.folderContainer
        folderRecyclerView.layoutManager = LinearLayoutManager(activity)

        val data = ArrayList<FolderCellViewModel>()
        // TODO: get data from DB here
        for (i in 1..20) {
            data.add(FolderCellViewModel("Folder " + i))
        }

        val adapter = FoldersRecyclerViewAdapter(data, ::onFolderClick)
        folderRecyclerView.adapter = adapter

        val editButton: ImageButton = binding.editFolder
        editButton.setOnClickListener{
            adapter.editMode()
            val visible = binding.actionButtons.visibility
            if (visible == View.VISIBLE)
                binding.actionButtons.visibility = View.GONE
            else
                binding.actionButtons.visibility = View.VISIBLE
            adapter.checked.value = ArrayList()
        }

        val selectAll: Button = binding.selectAllFolders
        val deselectAll: Button = binding.deselectAllFolders
        // TODO: add onclicklistensers
        val delete: Button = binding.deleteFolder
        val newFolder: Button = binding.newFolder
        val rename: Button = binding.renameFolder

        adapter.checked.observe(viewLifecycleOwner, {
            val size = adapter.checked.value?.size ?: 0
            // TODO: check if size = folder count
            // TODO: discuss whether to implement move folders
            // TODO: discuss whether to keep both select and deselect

            deselectAll.isEnabled = false
            delete.isEnabled = false
            rename.isEnabled = false
            selectAll.isEnabled = false

            if (size >= 1){
                deselectAll.isEnabled = true
                delete.isEnabled = true
            }
            if (size == 1)
                rename.isEnabled = true
            if (size != 20)
                selectAll.isEnabled = true

        })

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

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
}
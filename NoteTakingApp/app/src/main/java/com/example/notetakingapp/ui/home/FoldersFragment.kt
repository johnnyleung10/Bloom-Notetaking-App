package com.example.notetakingapp.ui.home

import FoldersRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentHomeBinding
import com.example.notetakingapp.models.FolderCellViewModel

class FoldersFragment : Fragment() {

    private lateinit var foldersViewModel: com.example.notetakingapp.ui.home.FoldersViewModel
    private var _binding: FragmentHomeBinding? = null

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val folderRecyclerView = root.findViewById<RecyclerView>(R.id.folderContainer)

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
        System.out.println("click on folder $position")
    }
}
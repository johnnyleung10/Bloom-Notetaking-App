package com.example.notetakingapp.ui.home

import FolderRecyclerViewAdapter
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
import com.example.notetakingapp.models.FolderViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val folderRecyclerView = root.findViewById<RecyclerView>(R.id.folderContainer)

        folderRecyclerView.layoutManager = LinearLayoutManager(activity)

        val data = ArrayList<FolderViewModel>()

        // TODO: get data from DB here
        for (i in 1..20) {
            data.add(FolderViewModel( "Folder " + i))
        }

        val adapter = FolderRecyclerViewAdapter(data, ::onFolderClick)

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
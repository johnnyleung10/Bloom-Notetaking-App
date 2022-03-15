package com.example.notetakingapp.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentJournalBinding

class JournalFragment : Fragment() {

    private lateinit var journalViewModel: JournalViewModel
    private var _binding: FragmentJournalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        journalViewModel =
            ViewModelProvider(this).get(JournalViewModel::class.java)

        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.date
        journalViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

//        val spinner: Spinner = binding.moods
//        ArrayAdapter.createFromResource(
//            requireContext(), R.array.moods, R.layout.dropdown
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
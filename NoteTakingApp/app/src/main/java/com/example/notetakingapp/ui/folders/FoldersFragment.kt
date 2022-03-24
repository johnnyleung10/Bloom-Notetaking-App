package com.example.notetakingapp.ui.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentFoldersBinding
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.viewmodels.FoldersViewModel
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.utilities.Profiler
import kotlin.system.measureTimeMillis

class FoldersFragment : Fragment(), NewFolderDialogFragment.NewFolderDialogListener {

    private lateinit var foldersViewModel: FoldersViewModel
    private var _binding: FragmentFoldersBinding? = null
    private lateinit var fm: FileManager
    private lateinit var adapter: FoldersRecyclerViewAdapter
    private lateinit var profiler: Profiler

    // This property is only valid between onCreateView and onDestroyView.
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

        profiler = Profiler.instance!!

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

        adapter = FoldersRecyclerViewAdapter(ArrayList(), ::onFolderClick, ::renameFolder)
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
        val editButton: ImageButton = binding.editFolder
        val deleteFolderButton: Button = binding.deleteFolder
        val selectAll: Button = binding.selectAllFolders
        val deselectAll: Button = binding.deselectAllFolders
        val search: EditText = binding.searchTerm
        val spinner: Spinner = binding.sortBy
        val sortOrder: ImageButton = binding.sortOrder

        newNoteButton.setOnClickListener {
            val elapsedNewNote= measureTimeMillis { newNote() }

            profiler.profile("create a new uncategorized note", elapsedNewNote)
        }

        createFolderButton.setOnClickListener {
            createNewFolder()
        }

        editButton.setOnClickListener{
            editFolders()
        }

        deleteFolderButton.setOnClickListener{
            val elapsedDeleteFolder = measureTimeMillis{ deleteFolders() }
            profiler.profile("delete folder", elapsedDeleteFolder)
        }

        selectAll.setOnClickListener{
            adapter.selectAll(true)
        }

        deselectAll.setOnClickListener{
            adapter.selectAll(false)
        }

        sortOrder.setOnClickListener{
            search.text.clear()

            var column = "date_modified"
            var order = true

            if (spinner.selectedItemPosition == 0) column = "title"
            else if (spinner.selectedItemPosition == 1) column = "date_created"
            if (sortOrder.contentDescription == "true"){
                order = false
                sortOrder.contentDescription = "false"
            } else sortOrder.contentDescription = "true"

            val newList = LinkedHashMap<Long, FolderModel>()
            for (i in fm.sortFolders(column, order))
                newList[i] = fm.folderList[i]!!

            foldersViewModel.setFolders(newList)
        }

        search.text.clear()
        search.addTextChangedListener {
            val elapsedSearchFolder = measureTimeMillis {
                val folderIds = fm.searchFolders(search.text.toString())
                val newList = HashMap<Long, FolderModel>()
                for (i in fm.folderList.keys)
                    if (i in folderIds) newList[i] = fm.folderList[i]!!

                foldersViewModel.setFolders(newList)
            }

            profiler.profile("search folders", elapsedSearchFolder)
        }

        ArrayAdapter.createFromResource(
            requireContext(), R.array.sort_by, R.layout.sort_dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(2)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                search.text.clear()

                var column = "date_modified"
                var order = true

                if (position == 0) column = "title"
                else if (position == 1) column = "date_created"
                if (sortOrder.contentDescription == "false") order = false

                val newList = LinkedHashMap<Long, FolderModel>()
                for (i in fm.sortFolders(column, order))
                    newList[i] = fm.folderList[i]!!

                foldersViewModel.setFolders(newList)
            }
        }

        adapter.checked.observe(viewLifecycleOwner) {
            val size = adapter.checked.value?.size ?: 0

            deselectAll.isEnabled = false
            deleteFolderButton.isEnabled = false
            selectAll.isEnabled = false

            if (size >= 1) {
                deselectAll.isEnabled = true
                deleteFolderButton.isEnabled = true
            }
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
        val action = FoldersFragmentDirections.actionNavigationFoldersToNavigationNotes(folderCellViewModel.folderId)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun newNote() {
        val manager = FileManager.instance
        val newNote = manager?.createNewNote("New Note", 1)
        val action = FoldersFragmentDirections.actionNavigationFoldersToFragmentEditNote(newNote?.id!!)
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun createNewFolder() {
        val dialogFragment = NewFolderDialogFragment()
        dialogFragment.show(requireFragmentManager().beginTransaction(), "create_folder")
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
        for (i in adapter.checked.value!!) fm.deleteFolder(i)
        adapter.selectAll(false)
        foldersViewModel.setFolders(fm.folderList)
    }

    /* NewFolderDialogListener */
    override fun onCreateNewFolder(dialog: DialogFragment, newFolderName: String) {
        val elapsedCreateFolder = measureTimeMillis {
            fm.createNewFolder(newFolderName)
            foldersViewModel.setFolders(fm.folderList)
        }
        profiler.profile("create new folder", elapsedCreateFolder)
    }

    private fun renameFolder(position: Int, title: String){
        if (position == -1) return
        val elapsedEditFolder = measureTimeMillis {
            fm.editFolder(adapter.folderCellList[position].folderId, title)
        }
        profiler.profile("edit folder", elapsedEditFolder)
    }
}
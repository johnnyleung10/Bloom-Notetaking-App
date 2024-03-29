package com.example.notetakingapp.ui.prompt

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentPromptBinding
import com.example.notetakingapp.utilities.DailyEntryManager
import com.example.notetakingapp.utilities.Mood
import com.example.notetakingapp.utilities.Profiler
import java.time.format.DateTimeFormatter
import kotlin.system.measureTimeMillis

private const val REQUEST_CODE = 1000

class PromptFragment : Fragment() {

    private lateinit var promptViewModel: PromptViewModel
    private var _binding: FragmentPromptBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dailyEntryManager: DailyEntryManager
    private lateinit var profiler: Profiler
    private var lastCursorPosition: Int = 0
    private var submitted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyEntryManager = DailyEntryManager.instance!!
        promptViewModel = ViewModelProvider(this).get(PromptViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPromptBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val promptResponse = binding.promptAnswer
        lastCursorPosition = promptResponse.selectionStart

        profiler = Profiler.instance!!

        setupDailyEntry()
        setupObservers()
        setupLinkedNoteButton()

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Sets the image to the uploaded image
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            binding.image.setImageURI(data?.data)
            promptViewModel.dailyEntry.value?.dailyImage = MediaStore.Images.Media.getBitmap(context?.contentResolver, data?.data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDailyEntry(){
        val dailyEntry = dailyEntryManager.getDailyEntryToday()
        promptViewModel.setDailyEntry(dailyEntry)
        setupPrompt()
    }

    private fun setupPrompt(){
        val promptQuestion: TextView = binding.promptQuestion
        promptQuestion.text = promptViewModel.dailyEntry.value?.dailyPrompt?.prompt
    }

    private fun setupLinkedNoteButton() {
        if (dailyEntryManager.getDailyEntryToday().linkedNoteId != null) {
            binding.attachNote.text = "Today's Daily Journal"
            binding.attachNote.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private fun setupObservers(){
        val date: TextView = binding.date
        val promptQuestion: TextView = binding.promptQuestion
        val promptAnswer: EditText = binding.promptAnswer
        val image: ImageView = binding.image

        promptViewModel.dailyEntry.observe(viewLifecycleOwner) {
            date.text = it.dateCreated.format(DateTimeFormatter.ofPattern("MMMM d, u"))
            promptQuestion.text = it.dailyPrompt.prompt
            promptAnswer.setText(it.promptResponse)
            updateDailyEntryColor(it.mood.id.toInt())
            if (it.getDateCreated() != it.getLastModifiedDate()) submitted()
            if (it.dailyImage != null) image.setImageBitmap(it.dailyImage)
        }
    }

    private fun setupPromptResponseListener(){
        val promptResponse = binding.promptAnswer

        promptResponse.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                lastCursorPosition = promptResponse.selectionStart
                if (promptResponse.text.toString() != "" && binding.spinner.selectedItemPosition != 0) readyToSubmit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastCursorPosition = promptResponse.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val response: String = promptResponse.text.toString()
                promptViewModel.dailyEntry.value?.promptResponse = response
            }
        })
    }

    private fun setupMoodDropdown(){
        val spinner: Spinner = binding.spinner

        ArrayAdapter.createFromResource(
            requireContext(), R.array.moods, R.layout.moods_dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.moods_item_dropdown)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateDailyEntryColor(position)
            }
        }

        // Set it to the daily entry's mood!
        spinner.setSelection(promptViewModel.dailyEntry.value!!.mood.id.toInt())
    }

    private fun addListeners(){
        val calendarButton: Button = binding.calendar
        val attachNote: TextView = binding.attachNote
        val attachImage: ImageButton = binding.attachImage
        val submit: ImageButton = binding.submit

        calendarButton.setOnClickListener{
            onCalendarClick()
        }

        submit.setOnClickListener{
            val elapsedUpdateDailyEntry = measureTimeMillis { updateDailyEntry() }
            profiler.profile("update daily entry", elapsedUpdateDailyEntry)
        }

        attachNote.setOnClickListener {
            val dailyEntry = dailyEntryManager.getDailyEntryToday()
            val noteId: Long
            if (dailyEntry.linkedNoteId != null) {
                noteId = dailyEntry.linkedNoteId!!
            }
            else {
                val elapsedCreateLinkedNote = measureTimeMillis {
                    noteId = dailyEntryManager.createLinkedNote(dailyEntry)!!
                }
                profiler.profile("create linked note", elapsedCreateLinkedNote)
            }

            val action = PromptFragmentDirections.actionNavigationPromptToFragmentEditNote(noteId)
            NavHostFragment.findNavController(this).navigate(action)
        }

        setupPromptResponseListener()
        setupMoodDropdown()

        attachImage.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    chooseImageGallery();
                }
            }else{
                chooseImageGallery();
            }
        }

    }

    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(requireContext(),"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateDailyEntryColor(position: Int){
        val prompt: CardView = binding.prompt
        val moodPicker: CardView = binding.moodPicker
        val submit: ImageButton = binding.submit

        val mood = when(position) {
            0 -> Mood.NO_SELECTION
            1 -> Mood.HAPPY
            2 -> Mood.LOVING
            3 -> Mood.EXCITED
            4 -> Mood.NEUTRAL
            5 -> Mood.SAD
            6 -> Mood.ANGRY
            7 -> Mood.DOUBTFUL
            else -> Mood.NO_SELECTION
        }

        promptViewModel.dailyEntry.value?.mood = mood

        val colorId = mood.colour

        val color = ContextCompat.getColor(requireContext(), colorId)

        submit.setColorFilter(color)
        moodPicker.setCardBackgroundColor(color)
        prompt.setCardBackgroundColor(color)

        if (position != 0 && binding.promptAnswer.text.toString() != "") readyToSubmit()
    }

    private fun updateDailyEntry(){
        dailyEntryManager.updateDailyEntry(promptViewModel.dailyEntry.value!!)
        dailyEntryManager.updateModifiedDate(promptViewModel.dailyEntry.value!!)
        submitted()
    }

    private fun onCalendarClick() {
        val action = PromptFragmentDirections.actionNavigationPromptToFragmentCalendar()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    private fun readyToSubmit(){
        if (submitted) return
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(1140, 145)
        binding.spinnerContainer.layoutParams = params
        binding.submit.visibility = View.VISIBLE
    }

    private fun submitted(){
        submitted = true
        binding.submit.visibility = View.GONE

        binding.promptAnswer.inputType = InputType.TYPE_NULL
        binding.attachImage.visibility = View.INVISIBLE
        binding.spinner.isEnabled = false

        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(1355, 145)
        binding.spinnerContainer.layoutParams = params
    }
}
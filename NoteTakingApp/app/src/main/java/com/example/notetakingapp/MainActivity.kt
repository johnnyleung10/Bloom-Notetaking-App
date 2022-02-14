package com.example.notetakingapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notetakingapp.databinding.ActivityMainBinding
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import com.example.notetakingapp.utilities.FileManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        db = DatabaseHelper(this)
//        val noteTwo = NoteModel("Note Two", this)
//        val noteThree = NoteModel("Note Three", this)
//        db.insertNote(noteTwo)
//        db.insertNote(noteThree)
        //db.updateFolder(title = "NEWWWW FOLDER", id = 1)
        //db.insertNote(newNoteModel)
        //val newFolderModel = FolderModel("All Notes", this)
        //db.insertFolder(newFolderModel)
        //db.deleteOneFolder(1)
        //db.close()


        //DatabaseHelper.setApplication(Application())
        //val newNote = NoteFile("My test Note", SpannableStringBuilder("Test"), "DefaultFolder", this)
        //Log.d("DATE TEST", newNote.getDateCreated())
        //db.insertNote(newNote)
        //Log.d("TEST", newNote.noteID.toString())

        val manager = FileManager(this)

        manager.initFiles()
        //val moveNote = manager.createNewNote("move Note")

            for (folder in manager.folderList) {
            Log.d("FOLDER LOG", folder.id.toString() + " " +folder.title)
            for (note in folder.contains) {
                Log.d("FOLDER LOG", note.id.toString() + " " +note.title)
            }
        }
        //manager.moveNote(moveNote, 2)

        for (folder in manager.folderList) {
            Log.d("FOLDER LOG", folder.id.toString() + " " +folder.title)
            for (note in folder.contains) {
                Log.d("FOLDER LOG", note.id.toString() + " " +note.title)
            }
        }
    }

}
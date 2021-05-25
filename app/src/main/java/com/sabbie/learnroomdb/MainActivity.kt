package com.sabbie.learnroomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sabbie.learnroomdb.R
import com.sabbie.learnroomdb.db.note.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel = NoteViewModel
    private lateinit var noteAdapter = NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteRV.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(this) { note, i ->
            showAlertMenu(note)
        }

        noteRV.adapter = noteAdapter

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getNotes()?.observe(this, Observer {
            noteAdapter.setNotes(it)
        })

        override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
            val inflater =  menuInflater
            inflater.inflate(R.menu.main_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
             R.id.addMenu -> showAlertDialogAdd()
            }
            return super.onOptionsItemSelected(item)
        }
    }

    private fun showAlertDialogAdd() {
        val alert = AlertDialog.Builder(this)
        val editText = EditText(applicationContext)

        editText.hint = "Masukkan catatanmu disini"

        alert.setTitle("Catatan Baru")
        alert.setView(editText)

        alert.setPositiveButton("Simpan") { dialog, _ ->
            noteViewModel.insertNote(
                Note(note = editText.text.toString())
            )
            dialog.dismiss()
        }
        alert.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun showAlertMenu(note: Note) {
        val items = arrayOf("Edit", "Hapus")

        val builder =
            AlertDialog.Builder(this)
        builder.setItems(items) { dialog, which ->

            when (which) {
                0 -> {
                    showAlertDialogEdit(note)
                }
                1 -> {
                    noteViewModel.deleteNote(note)
                }
            }
        }
        builder.show()
    }

    private fun showAlertDialogEdit(note: Note) {
        val alert = AlertDialog.Builder(this)

        val editText = EditText(applicationContext)
        editText.setText(note.note)

        alert.setTitle("Edit catatanmu")
        alert.setView(editText)

        alert.setPositiveButton("Update") { dialog, _ ->
            note.note = editText.text.toString()
            noteViewModel.updateNote(note)
            dialog.dismiss()
        }

        alert.setNegativeButton("Batalkan") { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

}
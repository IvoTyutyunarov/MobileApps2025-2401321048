package com.example.simplenotes.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.simplenotes.data.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private ListView listView;
    private ArrayAdapter<Note> adapter;
    private List<Note> notesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        listView = findViewById(R.id.listViewNotes);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddEditDialog(null));

        adapter = new ArrayAdapter<Note>(this, R.layout.item_note, notesList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    // in MainActivity.java, inside the getView method
                    view = getLayoutInflater().inflate(R.layout.item_note, parent, false);

                }

                Note note = getItem(position);
                TextView title = view.findViewById(R.id.textViewTitle);
                TextView content = view.findViewById(R.id.textViewContent);

                if (note != null) {
                    title.setText(note.title);
                    content.setText(note.content);
                }

                view.setOnClickListener(v -> {
                    Note selectedNote = notesList.get(position);
                    showAddEditDialog(selectedNote);
                });

                view.setOnLongClickListener(v -> {
                    Note selectedNote = notesList.get(position);
                    showDeleteDialog(selectedNote);
                    return true;
                });
                return view;
            }
        };
        listView.setAdapter(adapter);


        noteViewModel.getAllNotes().observe(this, notes -> {
            notesList.clear();
            if (notes != null) {
                notesList.addAll(notes);
            }
            adapter.notifyDataSetChanged();
        });








    }

    private void showAddEditDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(note == null ? "Добави бележка" : "Редактирай бележка");

        View dialogView = getLayoutInflater().inflate(R.layout.activity_add_edit_note, null);
        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etContent = dialogView.findViewById(R.id.etContent);

        if (note != null) {
            etTitle.setText(note.title);
            etContent.setText(note.content);
        }

        builder.setView(dialogView);
        builder.setPositiveButton("Запази", (dialog, which) -> {
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();

            if (note == null) {
                noteViewModel.insert(new Note(title, content));
            } else {
                note.title = title;
                note.content = content;
                noteViewModel.update(note);
            }
        });
        builder.setNegativeButton("Отказ", null);
        builder.show();
    }

    private void showDeleteDialog(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Изтриване")
                .setMessage("Наистина ли искате да изтриете бележката?")
                .setPositiveButton("Да", (dialog, which) -> noteViewModel.delete(note))
                .setNegativeButton("Не", null)
                .show();
    }
}

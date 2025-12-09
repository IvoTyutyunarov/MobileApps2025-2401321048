package com.example.simplenotes.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.simplenotes.data.Note;
import com.example.simplenotes.data.NoteRepository;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() { return allNotes; }
    public void insert(Note note) { repository.insert(note); }
    public void update(Note note) { repository.update(note); }
    public void delete(Note note) { repository.delete(note); }
}

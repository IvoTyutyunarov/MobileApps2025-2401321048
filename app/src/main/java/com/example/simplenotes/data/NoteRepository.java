package com.example.simplenotes.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NoteRepository(Application app) {
        NoteDatabase db = NoteDatabase.getDatabase(app);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public void insert(Note note) { executor.execute(() -> noteDao.insert(note)); }
    public void update(Note note) { executor.execute(() -> noteDao.update(note)); }
    public void delete(Note note) { executor.execute(() -> noteDao.delete(note)); }
}

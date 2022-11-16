package com.omaroid.noteapplication.feature_note.data.repository

import com.omaroid.noteapplication.feature_note.domain.model.Note
import com.omaroid.noteapplication.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : NoteRepository {
    private val listNotes = mutableListOf<Note>()
    override fun getNotes(): Flow<List<Note>> {
        return flow { emit(listNotes) }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return listNotes.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        listNotes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        listNotes.remove(note)
    }
}
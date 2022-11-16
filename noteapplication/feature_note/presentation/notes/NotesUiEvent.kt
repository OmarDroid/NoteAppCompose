package com.omaroid.noteapplication.feature_note.presentation.notes

import com.omaroid.noteapplication.feature_note.domain.model.Note
import com.omaroid.noteapplication.feature_note.domain.util.NoteOrder

sealed class NotesUiEvent {
    data class Order(val noteOrder: NoteOrder) : NotesUiEvent()
    data class DeleteNote(val note: Note) : NotesUiEvent()
    object RestoreNote : NotesUiEvent()
    object ToggleOrderSelection : NotesUiEvent()
}

package com.omaroid.noteapplication.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteUiEvent {
    data class EnteredTitle(val value: String) : AddEditNoteUiEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteUiEvent()
    data class EnteredContent(val value: String) : AddEditNoteUiEvent()
    data class ChangeContentFocus(val focusState: FocusState) : AddEditNoteUiEvent()
    data class ChangeColor(val color: Int) : AddEditNoteUiEvent()
    object SaveNoteUi : AddEditNoteUiEvent()
}

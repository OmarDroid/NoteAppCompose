package com.omaroid.noteapplication.feature_note.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omaroid.noteapplication.MainActivity
import com.omaroid.noteapplication.di.AppModule
import com.omaroid.noteapplication.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.omaroid.noteapplication.feature_note.presentation.notes.NotesScreen
import com.omaroid.noteapplication.feature_note.presentation.util.Screen
import com.omaroid.noteapplication.ui.theme.NoteApplicationTheme
import com.omaroid.noteapplication.util.TestTags
import com.omaroid.noteapplication.util.TestTags.NOTE_ITEM
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            NoteApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route
                                + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }
    @Test
    fun saveNewNote_editAfterwards() {
        // Click on FAB to get to add note screen
        composeRule.onNodeWithContentDescription("Add Note").performClick()

        // Enter texts in title and content text fields
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test-title")
        composeRule
            .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("test-content")
        // Save the new
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        // Make sure there is a note in the list with our title and content
        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        // Click on note to edit it
        composeRule.onNodeWithText("test-title").performClick()

        // Make sure title and content text fields contain note title and content
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals("test-title")
        composeRule
            .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .assertTextEquals("test-content")
        // Add the text "2" to the title text field
        composeRule
            .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("2")
        // Update the note
        composeRule.onNodeWithContentDescription("Save Note").performClick()

        // Make sure the update was applied to the list
        composeRule.onNodeWithText("test-title2").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for(i in 1..3) {
            // Click on FAB to get to add note screen
            composeRule.onNodeWithContentDescription("Add Note").performClick()

            // Enter texts in title and content text fields
            composeRule
                .onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput(i.toString())
            composeRule
                .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
                .performTextInput(i.toString())
            // Save the new
            composeRule.onNodeWithContentDescription("Save Note").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort Notes")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[2]
            .assertTextContains("1")
    }
}
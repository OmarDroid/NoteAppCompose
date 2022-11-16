package com.omaroid.noteapplication.di

import android.app.Application
import androidx.room.Room
import com.omaroid.noteapplication.feature_note.data.repository.NoteRepositoryImpl
import com.omaroid.noteapplication.feature_note.data.source.NoteDatabase
import com.omaroid.noteapplication.feature_note.domain.repository.NoteRepository
import com.omaroid.noteapplication.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return Room.inMemoryDatabaseBuilder(
            application,
            NoteDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteOperationUseCases {
        return NoteOperationUseCases(
            getNotesUseCase = GetNotesUseCase(repository = repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository = repository),
            addNoteUseCase = AddNoteUseCase(repository = repository),
            getNoteByIdUseCase = GetNoteByIdUseCase(repository = repository)
        )
    }
}
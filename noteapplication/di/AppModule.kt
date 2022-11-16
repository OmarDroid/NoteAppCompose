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
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteOperationsUseCases(repository: NoteRepository): NoteOperationUseCases {
        return NoteOperationUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteByIdUseCase = GetNoteByIdUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository)
        )
    }

}
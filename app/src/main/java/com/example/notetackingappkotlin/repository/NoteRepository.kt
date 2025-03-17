package com.example.notetackingappkotlin.repository

import com.example.notetackingappkotlin.database.NoteDataBase
import com.example.notetackingappkotlin.model.Note

//Класс репозитория, отвечающий за управление данными.
class NoteRepository(private val db: NoteDataBase) {

    //Вставляет новую заметку в базу данных.
    //Делегирует вызов insertNote из NoteDao
    suspend fun insertNote(note: Note) = db.getNoteDao().insertNote(note)

    //Удаляет заметку из базы данных.
    //Делегирует вызов из NoteDao
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)

    //Обноволяет существующую заметку в базе данных.
    //Делегирует вызов из NoteDao
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)

    //Возвращает все заметки из базы данных в виде LiveData.
    //Делегирует вызов из NoteDao
    fun getAllNotes() = db.getNoteDao().getAllNotes()

    //Выполняет поиск заметок по заголовку или содержимому.
    //Делегирует вызов из NoteDao.
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)
}

/**
 * Репозиторий обеспечивает абстракцию между ViewModel и Room (базой данных).
 * Используя NoteRepository, мы отделяем бизнес-логику приложения от логики доступа к данным.
 * Делегирование:обеспечивает высокую модульность.Например если источник данных изменится с базы данных на
   API,достаточно будет изменить репозиторий не затрагивая ViewModel.
 */
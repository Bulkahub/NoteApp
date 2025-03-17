package com.example.notetackingappkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetackingappkotlin.model.Note
import com.example.notetackingappkotlin.repository.NoteRepository
import kotlinx.coroutines.launch


//Класс для управления данными заметок.
class NoteViewModel(app: Application, private val repository: NoteRepository) :
    AndroidViewModel(app) {

        //Добавление новой заметки в базу данных.
        //Операция будет выполняться в отдельной корутине,чтобы избежать блокировки основного потока.
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

        //Удаление заметки из базы данных.
        //используется viewModelScope, для безопасного выполнения операции на фоне.
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    //Обновляет существующую заметку в базе данных
    //Вызов делегируется слою репозитория (NoteRepository) ,чтобы отделить логику данных  от UI
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    //Возвращает список всех заметок.
    // Данные предоставляются в виде LiveData, что позволяет автоматически обновлять UI при изменении данных.
    fun getAllNotes() = repository.getAllNotes()

    //Выполняет поиск заметок по заголовку или содержимому
    //метод делегируется репозиторием,а резултат передается в виде LiveData
    fun searchNote(query: String?) = repository.searchNote(query)

}

/**
 * ViewModel:используется для хранения и управления данными, связанными с UI,
  что помогает пережить конфигурационные изменения (например, поворот экрана).
 * LiveData: обеспечивает автоматическое обновление пользовательского интерфейса при изменении данных.
 * viewModelScope:операции выполняются асинхронно, чтобы не блокировать основной поток (UI).
 * Делегирует,значит не выполняет логику работы с данными самостоятельно,а передает эту задачу классу NoteRepository.
  Это ключевая идея архитектуры MVVM(Model-View-ViewModel).Разделение ответственности между различными слоями.
 */
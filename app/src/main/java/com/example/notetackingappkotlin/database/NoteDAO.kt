package com.example.notetackingappkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notetackingappkotlin.model.Note
/** Аннотация @Dao указывает, что этот интерфейс является частью Room
 и отвечает за выполнение операций с базой данных**/
@Dao
interface NoteDAO {

    //Вставляет новую заметку в таблицу "notes"
    //Если заметка с  таким id уже существует , то она будет перезаписана.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    //Обновляет существующую заметку в базе данных
    @Update
    suspend fun updateNote(note: Note)

    //Удаляет указанную заметку в базе данных
    @Delete
    suspend fun deleteNote(note: Note)

    // Возвращает все заметки из таблицы "notes", отсортированные по id в порядке убывания.
    //LIveData позволяет автоматическм обновлять UI при изменении данных.
    @Query("SELECT * FROM NOTES ORDER BY id DESC")//Сортируем по индефикатору.
    fun getAllNotes(): LiveData<List<Note>>

    //Выполняет поиск заметок.
    //Поддерживает частичное совпадение благодаря оператору LIKE.
    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE :query OR noteBody LIKE :query")
    fun searchNote(query: String?): LiveData<List<Note>>//Функция будет искать прошлый запрос ,который является заголовком узла или телом.

}

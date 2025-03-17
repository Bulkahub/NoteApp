package com.example.notetackingappkotlin.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Класс Note представляет сущность Room для таблицы "notes".
 * Содержит заголовок, текст заметки и цвет фона.
 * Parcelable позволяет передавать объект между компонентами Android.*/

@Entity(tableName = "notes")
@Parcelize//автоматически переопределяет методы в Parcelable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val noteTitle: String,
    val noteBody: String,
    var backgroundColor: Int = 0
) : Parcelable



package com.example.notetackingappkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notetackingappkotlin.model.Note


@Database(entities = [Note::class], version = 2)
abstract class NoteDataBase : RoomDatabase() {

    //Абстрактная функция для получения DAO
    abstract fun getNoteDao(): NoteDAO

    //companion object используется для реализации шаблона Singleton,
    //который гарантирует ,что будет создан один экземпляр базы данных.
    companion object {

        //@Volatile - гарантирует ,что изменения в instance будут видны всем потокам.
        @Volatile
        private var instance: NoteDataBase? =
            null

        //LOCK - исользуется для синхронизации и предотвращения создания нескольких экземпляров.
        private val LOCK = Any()

        //Функция invoke ооздает или возврщвет существующий экземпляр базы данных.
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            //Если instance равен null,создаем базу данных.
            instance ?: createDataBase(context).also {
                instance = it
            }
        }

        //Функция для создания базы данных с использованием Room.
        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,        //предотврящаем утечку паммяти
                NoteDataBase::class.java,          //указываем класс базы данных
                "note_db"                   //имя файла БД
            ).addMigrations(MIGRATION_1_2)        //добавляем миграцию между версиями
                .build()                          //строим и вохвращаем БД

        //Определение миграции из весии 1 в версию 2.
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Добавляем новый столбец backgroundColor с типом Integer
                database.execSQL("ALTER TABLE notes ADD COLUMN backgroundColor INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}

/**
 * Дополнительное описание важных компонентов:
 *
 * - Any: используется для создания блокировки (LOCK), чтобы гарантировать потокобезопасность при создании базы данных.
 * - synchronized: ключевое слово, используемое для предотвращения создания нескольких экземпляров базы данных
 *   в многопоточной среде. Гарантирует, что только один поток может выполнить блок синхронизации за раз.
 * - @Volatile: делает переменную видимой для всех потоков. Это важно, чтобы изменения instance были доступны сразу.
 * - MIGRATION: помогает обновлять структуру базы данных при изменении версии без потери данных.
 *
 * Этот код демонстрирует классический подход к созданию базы данных с использованием Room и шаблона Singleton.
 */

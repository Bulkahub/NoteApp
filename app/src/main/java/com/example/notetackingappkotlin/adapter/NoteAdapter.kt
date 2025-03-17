package com.example.notetackingappkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notetackingappkotlin.databinding.NoteLayoutBinding
import com.example.notetackingappkotlin.fragments.HomeFragmentDirections
import com.example.notetackingappkotlin.model.Note
import java.util.*

// NoteAdapter - адаптер для RecyclerView, используемый для отображения списка заметок.
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    //// ViewHolder - представляет каждый элемент списка и связывает его с View (NoteLayoutBinding).
    class NoteViewHolder(val itemBinding: NoteLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    // Listener для изменения цвета заметки.
    // Используется для уведомления, если цвет заметки был обновлён
    var onNoteColorChangeListener: ((Note) -> Unit)? = null

    // DiffUtil используется для вычисления изменений в списке (добавление, удаление, обновление).
    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        /*Главная цель DiffUtil - оптимизировать обновление списка RecyclerView.
            Сравнивает старый и новый элементы, чтобы обновлять только те, которые изменились*/


        //Проверим одинаковы ли предметы по (id) и содержэимому.
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteBody == newItem.noteBody &&
                    oldItem.noteTitle == newItem.noteTitle
        }

        // Проверяет, одинаково ли содержимое старого и нового элемента.
        //Если элементы полностью равны,возвращает true
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    //Асинхронное обновление списка.
    val differ = AsyncListDiffer(this, differCallback)

    //Создание ViewHolde для каждого элемента списка.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    //Привязка данных к ViewHolder для отображения на экране.
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        //Установка текста заметки.
        holder.itemBinding.tvNoteBody.text = currentNote.noteBody
        holder.itemBinding.twNoteTitle.text = currentNote.noteTitle

        //Если цвет заметки не установлен (равен 0),генерируем случайный цвет.
        if (currentNote.backgroundColor == 0) {
            currentNote.backgroundColor = Color.rgb(
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
            //Уведомление о том,что цвет заметки был изменен.
            onNoteColorChangeListener?.invoke(currentNote)
        }

        //Случайный класс для генерации случайного числа ,и отобразить случайный цвет
//        val random = Random()
//        //Генерация цветов.
//        val color = Color.argb(
//            255, random.nextInt(256),
//            random.nextInt(256), random.nextInt(256)
//        )

        //Устанавливаем цвет заметки в качестве фона для элемента списка.
        holder.itemView.setBackgroundColor(currentNote.backgroundColor)

        //Обработка нажатия на элемент списка, а так же переход на экран обновленной заметки с передачей текущей заметки.
        holder.itemView.setOnClickListener {
            val direction =
                HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }

    //Возвращает колличество элементов в списке.
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
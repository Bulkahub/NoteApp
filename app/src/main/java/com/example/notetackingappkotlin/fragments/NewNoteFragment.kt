package com.example.notetackingappkotlin.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.notetackingappkotlin.MainActivity
import com.example.notetackingappkotlin.R
import com.example.notetackingappkotlin.adapter.NoteAdapter
import com.example.notetackingappkotlin.databinding.FragmentNewNoteBinding
import com.example.notetackingappkotlin.model.Note
import com.example.notetackingappkotlin.viewmodel.NoteViewModel

//Фрагмент для создания новой заметки.
// Позволяет пользователю добавить заголовок и содержимое заметки и сохранить её в базе данных.
class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    //ViewModel для взаимодействия с данныами заметок.
    private lateinit var notesViewModel: NoteViewModel

    //View для хранения ссылки на корневой элемент интерфейса.
    private lateinit var mView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Добавляем меню для фрагмента.
        setHasOptionsMenu(true)
    }

    // Создаёт View фрагмента и привязывает её к FragmentNewNoteBinding.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root// Возвращаем корневой элемент представления.
    }

    //Метод вызывается когда View создана.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel через активность (MainActivity).
        notesViewModel = (activity as MainActivity).noteViewModel
        mView = view// Сохраняем ссылку на корневой элемент.
    }

    // Функция для сохранения новой заметки.
    private fun saveNote(view: View) {
        val noteTitle = binding.etNoteTitle.text.toString().trim()//Получаем текс заголовка.
        val noteBody = binding.etNoteBody.text.toString().trim()//Получаем текст содержимого.

        if (noteTitle.isNotEmpty()) {
            //Если заголовок не пустой , создаем объект Note и сохраняем его.
            val note =
                Note(0, noteTitle, noteBody)// id = 0, так как Room сгенерирует его автоматически.
            notesViewModel.addNote(note)// Добавляем заметку через ViewModel.

            //Показ уведомления об успешном сохранении.
            Toast.makeText(
                mView.context,
                "Note Saved Successfully",
                Toast.LENGTH_LONG
            ).show()

            //Навигания назад к HomeFragment после сохранения.
            view.findNavController()
                .navigate(R.id.action_newNoteFragment_to_homeFragment)//Возвращаемя назад к домФрагменту.
        } else {
            //Если заголовок пустой,показываем уведомление.
            Toast.makeText(
                mView.context,
                "Please enter note Title",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //Создание меню для фрагмента.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_note, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    //Очистка ресурсов при уничтожении фрагмента.
    override fun onDestroy() {
        super.onDestroy()
        _binding = null// Освобождаем binding для предотвращения утечек памяти.
    }


    //Обработка выбора элементов меню.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                saveNote(mView)//Сохраняем заметку при нажатии на кнопку Save.
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.example.notetackingappkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notetackingappkotlin.MainActivity
import com.example.notetackingappkotlin.R
import com.example.notetackingappkotlin.adapter.NoteAdapter
import com.example.notetackingappkotlin.databinding.FragmentUpdateNoteBinding
import com.example.notetackingappkotlin.model.Note
import com.example.notetackingappkotlin.viewmodel.NoteViewModel

// UpdateNoteFragment - фрагмент для редактирования существующей заметки.
// Позволяет пользователю изменить содержимое заметки или удалить её.
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    // _binding используется для доступа к элементам интерфейса.
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    // ViewModel для взаимодействия с данными заметок.
    private lateinit var notesViewModel: NoteViewModel

    // Текущая заметка, выбранная для редактирования.
    private lateinit var currentNote: Note

    //Фрагмент заметки макета,содержит аргументы в навигационном графике.
    // UpdateNoteFragmentArgs используется для передачи данных между фрагментами.
    private val args: UpdateNoteFragmentArgs by navArgs()


    // Указываем, что фрагмент имеет собственное меню.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Добавляем меню для редактирования заметки.
        setHasOptionsMenu(true)
    }

    // // Создаёт View фрагмента и привязывает её к FragmentUpdateNoteBinding.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root// Возвращаем корневой элемент представления.
    }

    // Метод вызывается, когда View создана.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация ViewModel через активность (MainActivity).
        notesViewModel = (activity as MainActivity).noteViewModel
        // Получение текущей заметки из аргументов.
        currentNote =
            args.note!!

        // Заполняем поля редактирования текущими данными заметки.
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)

        // Кнопка завершения редактирования.
        binding.fabDone.setOnClickListener {
            val title = binding.etNoteTitleUpdate.text.toString().trim()
            val body = binding.etNoteBodyUpdate.text.toString().trim()

            // Проверяем, что заголовок не пустой.
            if (title.isNotEmpty()) {
                // Создаём обновлённую заметку и сохраняем её через ViewModel.
                val note = Note(currentNote.id, title, body)
                notesViewModel.updateNote(note)

                // Навигация обратно на главный экран.
                view.findNavController().navigate(R.id.action_updateNoteFragment_to_homeFragment)
            } else {
                // Если заголовок пустой, показываем уведомление
                Toast.makeText(
                    context,
                    "Please enter note Title",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Функция для удаления заметки.
    // Вызывает диалоговое окно для подтверждения удаления.
    private fun deleteNote() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Note")//Заголовк диалога.
            setMessage("You want to delete this Note?")//Сообщение диалога.
            setPositiveButton("Delete") { _, _ ->//Действие при подтверждении.
                notesViewModel.deleteNote(currentNote)//Удаление заметки.

                // Навигация обратно на главный экран.
                view?.findNavController()?.navigate(
                    R.id.action_updateNoteFragment_to_homeFragment
                )
            }
            setNegativeButton("Cancel", null) // Отмена удаления.
        }.create().show()
    }

    // Создание меню для фрагмента.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_update_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Обработка нажатий на элементы меню.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()// Удаление заметки при выборе опции удаления.
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Освобождает ресурсы, привязанные к binding, при разрушении фрагмента.
    override fun onDestroy() {
        super.onDestroy()
        _binding = null// Очищаем binding для предотвращения утечек памяти.
    }
}
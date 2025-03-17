package com.example.notetackingappkotlin.fragments

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notetackingappkotlin.MainActivity
import com.example.notetackingappkotlin.R
import com.example.notetackingappkotlin.adapter.NoteAdapter
import com.example.notetackingappkotlin.databinding.FragmentHomeBinding
import com.example.notetackingappkotlin.model.Note
import com.example.notetackingappkotlin.viewmodel.NoteViewModel
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar

//HomeFragment - главный экран для отображения списка элементов
// Реализует функционал поиска с использованием SearchView.OnQueryTextListener.
class HomeFragment : Fragment(R.layout.fragment_home),
    SearchView.OnQueryTextListener {//Для начала реализуем поисковое представление.

    //Переменная для привязки представления.
    //_binding используется для безопасного управления жизненным циклом View.
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!//не равен null

    //ViewModel для работы с данными заметок.
    private lateinit var notesViewModel: NoteViewModel

    //Адаптер для RecyclerView который отвечает за отображения списка заметок.
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Указываем что у данного фрагмента есть меню.
        setHasOptionsMenu(true)
    }

    //Создает View для фрагмента и привязывает ее к FragmentHomeBinding.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    //Метод вызывается когда View создано.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Инициализация ViewModel через активность.
        notesViewModel = (activity as MainActivity).noteViewModel

        //Настройка RecyclerView для отображения списка элементов.
        setUpRecyclerView()

        //Обработка нажатия на плавающую кнопку для добавления новой заметки.
        binding.fabAddNote.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_homeFragment_to_newNoteFragment//Переход на экран добавления новой заметки.
            )
        }
    }

    //Настройка RecyclerView и адаптера для отображения заметок в шахматном порядке.
    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()//инициализируем адаптер.

        //Настраиваем RecyclerView.
        binding.recyclerView.apply {//apply используется что бы избавиться от привязки.
            layoutManager = StaggeredGridLayoutManager(
                2,//количество столбцов.
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)//размеры элементов не изменяются
            adapter = noteAdapter
        }

        //Слушатель изменений цвета заметок.
        noteAdapter.onNoteColorChangeListener = { note ->
            notesViewModel.updateNote(note)
        }

        //Наблюдение за списком заметок из ViewModel.
        activity?.let {
            notesViewModel.getAllNotes().observe(
                viewLifecycleOwner, { note ->
                    noteAdapter.differ.submitList(note) {
                        updateUI(note)
                    }
                }
            )
        }
    }

    //Обновляет пользовательский инерфейс в зависимости от наличия заметок.
    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                //Если есть заметки,показываем RecyclerView и скрываем анимацию пустого состояния.
                binding.emptyStateAnimation.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE//
            } else {//Если заметок нет, показываем анимацию пустого состояния.
                binding.emptyStateAnimation.visibility = View.VISIBLE
                binding.emptyStateAnimation.playAnimation()
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    //Создание меню для фрагмента.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        //Анимация для RecyclerView при создании меню
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in)
        binding.recyclerView.startAnimation(animation)

        //Настройка элемента меню для поиска.
        val menuItem = menu.findItem(R.id.menu_search)
        menuItem.isVisible = true
        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false//Отключение кнопки отправки.
        mMenuSearch.setOnQueryTextListener(this)//Установка слушателя.
    }

    //Реагирует на отправку текста в поисковом представлении.
    override fun onQueryTextSubmit(query: String?): Boolean {
        //Здесь можно добавить логику отправки запроса,если нужно.
        return false
    }

    //Реагирует на изменения текста в поисковом представлении.
    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty()) {
            //Анимация RecyclerView при изменении текста.Динамическое управление поиском
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            binding.recyclerView.startAnimation(animation)
            searchNote(newText)//Выполняем поиск по тексту.

        }
        return true
    }

    //Выполняет поиск заметок по введенному запросу.
    private fun searchNote(query: String?) {
        val searchQuery = "%$query"//Форматируем запрос для SQL.
        notesViewModel.searchNote(searchQuery).observe(
            this,
            { list -> noteAdapter.differ.submitList(list) }
        )
    }

    //Освобождает ресурсы,привязанные к binding.
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

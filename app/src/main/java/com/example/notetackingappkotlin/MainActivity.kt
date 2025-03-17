package com.example.notetackingappkotlin

import android.app.WallpaperManager.OnColorsChangedListener
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notetackingappkotlin.adapter.NoteAdapter
import com.example.notetackingappkotlin.database.NoteDataBase
import com.example.notetackingappkotlin.databinding.ActivityMainBinding
import com.example.notetackingappkotlin.repository.NoteRepository
import com.example.notetackingappkotlin.viewmodel.NoteViewModel
import com.example.notetackingappkotlin.viewmodel.NoteViewModelFactory

// Отвечает за инициализацию ViewModel, настройку пользовательского интерфейса и обработку системных отступов.
class MainActivity : AppCompatActivity() {

    // ViewModel для взаимодействия с данными заметок.
    lateinit var noteViewModel: NoteViewModel

    // Binding для привязки к интерфейсу активности.
    lateinit var binding: ActivityMainBinding

    // Toolbar (панель инструментов) для отображения действий и заголовка.
    lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Включаем поддержку Edge-to-Edge дизайна.
        enableEdgeToEdge()

        // Инициализируем View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)// Устанавливаем содержимое активности.

        // Инициализация ViewModel.
        setUpViewModel()

        // Настраиваем Toolbar как ActionBar.
        toolBar = findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    // Настраиваем ViewModel с использованием фабрики.
    private fun setUpViewModel() {
        // Создаём экземпляр репозитория, передавая в него базу данных.
        val noteRepository = NoteRepository(NoteDataBase(this))

        //Используем фабрику и связываемся с Viewmodel,что бы не  погдключать  на прямую , а через фабрику.
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)

        //Создаём экземпляр NoteViewModel.
        noteViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )
            .get(NoteViewModel::class.java)
    }
}
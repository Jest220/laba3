package com.lr3_428_04;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lr3_428_04.database.DBHelper;
import com.lr3_428_04.database.RoomDao;
import com.lr3_428_04.model.RoomItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton add;
    private FloatingActionButton admin;
    private FloatingActionButton deleteAll;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private RoomDao roomDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        initializeDatabase();
        initializeRecycleView();
    }

    private void initializeViews() {
        add = findViewById(R.id.fab_add);
        admin = findViewById(R.id.fab_admin);
        deleteAll = findViewById(R.id.fab_delete_all);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initializeDatabase() {
        dbHelper = new DBHelper(this);
        try {
            dbHelper.createDatabase();
            db = dbHelper.openDatabase();
            roomDao = new RoomDao(db);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при создании базы данных", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeRecycleView() {
        List<RoomItem> rooms = roomDao.getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RoomAdapter(rooms));
    }
}
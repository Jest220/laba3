package com.lr3_428_04;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabAdmin;
    private FloatingActionButton fabDeleteAll;
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
        setupButtons();
    }

    private void initializeViews() {
        fabAdd = findViewById(R.id.fab_add);
        fabAdmin = findViewById(R.id.fab_admin);
        fabDeleteAll = findViewById(R.id.fab_delete_all);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        recyclerView.setAdapter(new RoomAdapter(rooms));
    }

    private void setupButtons() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomEditActivity.class);
            startActivity(intent);
        });
        fabDeleteAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить все помещения?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    deleteAll();
                    dialog.dismiss();
                })
                .setNegativeButton("Отмена", ((dialog, which) -> {
                    dialog.dismiss();
                }))
                .show();
            }
        );
        fabAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }

    public void deleteAll() {
        roomDao.deleteAll();
        initializeRecycleView();
        Toast.makeText(this, "Все помещения удалены", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeRecycleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
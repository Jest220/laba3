package com.lr3_428_04;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lr3_428_04.database.DBHelper;
import com.lr3_428_04.database.SimpleTypeDao;
import com.lr3_428_04.database.SimpleTypeTableName;
import com.lr3_428_04.model.SimpleTypeModel;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Spinner tableSpinner;
    private ListView dataListView;
    private Button btnCancel;
    private Button btnAdd;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private SimpleTypeDao simpleTypeDao;
    private SimpleTypeTableName[] tables;
    List<SimpleTypeModel> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initializeViews();
        initializeDatabase();
        setupSpinner();
        setupButtons();
    }

    private void initializeViews() {
        tableSpinner = findViewById(R.id.tableSpinner);
        dataListView = findViewById(R.id.dataListView);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAdd = findViewById(R.id.btn_add);
    }

    private void initializeDatabase() {
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.openDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при инициализации базы данных", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupSpinner() {
        tables = SimpleTypeTableName.values();
        ArrayAdapter<SimpleTypeTableName> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tables);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(adapter);

        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                simpleTypeDao = new SimpleTypeDao(tables[position], db);
                records = simpleTypeDao.getAll();
                ArrayAdapter<SimpleTypeModel> adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_list_item_1, records);
                dataListView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> {
            TableEditDialogFragment dialog = new TableEditDialogFragment(simpleTypeDao, false);
            dialog.setListener(this::updateListView);
            dialog.show(getSupportFragmentManager(), "popup_dialog");
        });
        dataListView.setOnItemClickListener((parent, view, position, id) -> {
            SimpleTypeModel selected = (SimpleTypeModel) dataListView.getItemAtPosition(position);
            TableEditDialogFragment dialog = new TableEditDialogFragment(simpleTypeDao, selected);
            dialog.setListener(this::updateListView);
            dialog.show(getSupportFragmentManager(), "popup_dialog");
        });
    }

    private void updateListView() {
        records = simpleTypeDao.getAll();
        ArrayAdapter<SimpleTypeModel> adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_list_item_1, records);
        dataListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
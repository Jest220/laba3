package com.lr3_428_04;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.lr3_428_04.database.DBHelper;
import com.lr3_428_04.database.RoomDao;
import com.lr3_428_04.database.SimpleTypeDao;
import com.lr3_428_04.database.SimpleTypeTableName;
import com.lr3_428_04.model.RoomItem;
import com.lr3_428_04.model.SimpleTypeModel;

import java.util.List;

public class RoomEditActivity extends AppCompatActivity {
    private EditText etRoomNumber;
    private Spinner spinnerRoomType;
    private Spinner spinnerEquipType;
    private EditText etEquipBrand;
    private Button btnCancel;
    private Button btnSave;
    private Button btnDelete;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private RoomDao roomDao;
    private List<SimpleTypeModel> roomType;
    private List<SimpleTypeModel> equipType;
    private RoomItem room;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_edit);

        initializeViews();
        initializeDatabase();
        initializeModels();
        setupSpinners();
        setupButtons();
        loadData();
    }

    private void initializeViews() {
        etRoomNumber = findViewById(R.id.etRoomNumber);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        spinnerEquipType = findViewById(R.id.spinnerEquipType);
        etEquipBrand = findViewById(R.id.etEquipBrand);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void initializeDatabase() {
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.openDatabase();
            roomDao = new RoomDao(db);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при инициализации базы данных", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void initializeModels() {
        roomType = new SimpleTypeDao(SimpleTypeTableName.valueOf("ROOM_TYPES"), db).getAll();
        equipType = new SimpleTypeDao(SimpleTypeTableName.valueOf("EQUIP_TYPES"), db).getAll();

    }

    private void setupSpinners() {
        setSpinnerAdapter(spinnerRoomType, roomType);
        setSpinnerAdapter(spinnerEquipType, equipType);
    }

    private void setSpinnerAdapter(Spinner spinner, List<SimpleTypeModel> items) {
        ArrayAdapter<SimpleTypeModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadData() {
        Intent intent = getIntent();
        room = intent.getParcelableExtra("room");

        if (room != null) {
            isEditMode = true;
            etRoomNumber.setText(room.getRoomNumber());
            spinnerRoomType.setSelection(getIndex(spinnerRoomType, room.getRoomType()));
            spinnerEquipType.setSelection(getIndex(spinnerEquipType, room.getEquipType()));
            etEquipBrand.setText(room.getEquipBrand());
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private int getIndex(Spinner spinner, String val) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (val.equals(spinner.getItemAtPosition(i).toString())) {
                return i;
            }
        }
        return 0;
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveRoom());
        btnDelete.setOnClickListener(v -> deleteRoom());

    }

    private void saveRoom() {
        String roomNumber = etRoomNumber.getText().toString().trim();
        int roomTypeId = ((SimpleTypeModel) spinnerRoomType.getSelectedItem()).getId();
        int equipTypeId = ((SimpleTypeModel) spinnerEquipType.getSelectedItem()).getId();
        String equipBrand = etEquipBrand.getText().toString().trim();

        if (roomNumber.isEmpty()) {
            Snackbar.make(etRoomNumber, "Номер помещения не должен быть пустым", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (!roomNumber.matches("^\\d[A-Za-zА-Яа-я0-9]*$")) {
            Snackbar.make(etRoomNumber, "Номер помещения должен начинаться с цифры и может содержать буквы", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (roomNumber.length() > 50) {
            Snackbar.make(etRoomNumber, "Номер помещения должен не превышать 50 символов", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (equipBrand.isEmpty()) {
            Snackbar.make(etEquipBrand, "Марка оборудования не должна быть пустой", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (equipBrand.length() > 50) {
            Snackbar.make(etEquipBrand, "Марка оборудования не должна превышать 50 символов", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            int id = room.getId();
            roomDao.update(id, roomNumber, roomTypeId, equipTypeId, equipBrand);
            Toast.makeText(this, "Помещение обновлено", Toast.LENGTH_SHORT).show();
        } else {
            roomDao.insert(roomNumber, roomTypeId, equipTypeId, equipBrand);
            Toast.makeText(this, "Помещение добавлено", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteRoom() {
        if (isEditMode) {
            roomDao.delete(room.getId());
            Toast.makeText(this, "Помещение удалено", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
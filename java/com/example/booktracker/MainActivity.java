package com.example.booktracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    Toolbar toolbar;
    MyDataBaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages, book_note;
    ArrayList<String> original_book_id, original_book_title, original_book_author, original_book_pages, original_book_note;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDataBaseHelper(MainActivity.this);

        initializeArrayLists();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, book_id, book_title, book_author, book_pages, book_note);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_delete_all) {
            showDeleteAllConfirmationDialog();
            return true;
        } else if (id == R.id.menu_search) {
            showSearchDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            refreshData();
        }
    }

    void initializeArrayLists() {
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        book_note = new ArrayList<>();

        original_book_id = new ArrayList<>();
        original_book_title = new ArrayList<>();
        original_book_author = new ArrayList<>();
        original_book_pages = new ArrayList<>();
        original_book_note = new ArrayList<>();
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
                book_note.add(cursor.getString(4));

                original_book_id.add(cursor.getString(0));
                original_book_title.add(cursor.getString(1));
                original_book_author.add(cursor.getString(2));
                original_book_pages.add(cursor.getString(3));
                original_book_note.add(cursor.getString(4));
            }
        }
    }

    void refreshData() {
        clearAllLists();

        storeDataInArrays();

        customAdapter.notifyDataSetChanged();
    }

    void clearAllLists() {
        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        book_note.clear();

        original_book_id.clear();
        original_book_title.clear();
        original_book_author.clear();
        original_book_pages.clear();
        original_book_note.clear();
    }

    void showDeleteAllConfirmationDialog() {
        if (book_id.isEmpty()) {
            Toast.makeText(this, "No books to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All Books");
        builder.setMessage("Are you sure you want to delete ALL books?\n\nThis will delete " +
                book_id.size() + " book(s) and cannot be undone.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteAllBooks();
                refreshData();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button deleteButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        if (deleteButton != null) {
            deleteButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        if (cancelButton != null) {
            cancelButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
    }

    void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Books");

        final EditText searchEditText = new EditText(this);
        searchEditText.setHint("Enter book title or author name...");
        builder.setView(searchEditText);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    performSearch(searchQuery);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter search text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Show All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetSearch();
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button colors
        Button searchButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button showAllButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        if (searchButton != null) {
            searchButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (showAllButton != null) {
            showAllButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
        if (cancelButton != null) {
            cancelButton.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    void performSearch(String query) {

        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        book_note.clear();

        String lowerQuery = query.toLowerCase();


        for (int i = 0; i < original_book_title.size(); i++) {
            String title = original_book_title.get(i).toLowerCase();
            String author = original_book_author.get(i).toLowerCase();

            if (title.contains(lowerQuery) || author.contains(lowerQuery)) {
                book_id.add(original_book_id.get(i));
                book_title.add(original_book_title.get(i));
                book_author.add(original_book_author.get(i));
                book_pages.add(original_book_pages.get(i));
                book_note.add(original_book_note.get(i));
            }
        }

        customAdapter.notifyDataSetChanged();

        if (book_id.isEmpty()) {
            Toast.makeText(this, "No books found matching '" + query + "'", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Found " + book_id.size() + " book(s)", Toast.LENGTH_SHORT).show();
        }
    }

    void resetSearch() {
        book_id.clear();
        book_title.clear();
        book_author.clear();
        book_pages.clear();
        book_note.clear();

        book_id.addAll(original_book_id);
        book_title.addAll(original_book_title);
        book_author.addAll(original_book_author);
        book_pages.addAll(original_book_pages);
        book_note.addAll(original_book_note);

        customAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Showing all books", Toast.LENGTH_SHORT).show();
    }
}
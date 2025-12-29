package com.example.booktracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {

    EditText editTextBookTitle2, editTextAuthor2, editTextPages2,editTextNotes2;
    Button buttonEditBook, buttonDeleteBook;
    String id, title, author, pages, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buttonEditBook), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextBookTitle2 = findViewById(R.id.editTextBookTitle2);
        editTextAuthor2 = findViewById(R.id.editTextAuthor2);
        editTextPages2 = findViewById(R.id.editTextPages2);
        editTextNotes2 = findViewById(R.id.editTextNotes2);
        buttonEditBook = findViewById(R.id.buttonEditBook);
        buttonDeleteBook = findViewById(R.id.buttonDeleteBook);

        getAndSetIntentData();

        buttonEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = editTextBookTitle2.getText().toString().trim();
                String updatedAuthor = editTextAuthor2.getText().toString().trim();
                String updatedPages = editTextPages2.getText().toString().trim();
                String updatedNote = editTextNotes2.getText().toString().trim();

                if (updatedPages.isEmpty()) {
                    Toast.makeText(UpdateActivity.this, "Please enter pages", Toast.LENGTH_SHORT).show();
                    return;
                }

                showUpdateConfirmationDialog(updatedTitle, updatedAuthor, updatedPages, updatedNote);
            }
        });

        buttonDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("author") && getIntent().hasExtra("pages") && getIntent().hasExtra("note")){

            //get data
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");
            note = getIntent().getStringExtra("note");

            //set data
            editTextBookTitle2.setText(title);
            editTextAuthor2.setText(author);
            editTextPages2.setText(pages);
            editTextNotes2.setText(note);

        }
        else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    void showUpdateConfirmationDialog(String updatedTitle, String updatedAuthor, String updatedPages, String updatedNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Book");
        builder.setMessage("Are you sure you want to update this book?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDataBaseHelper myDb = new MyDataBaseHelper(UpdateActivity.this);
                myDb.updateBook(id, updatedTitle, updatedAuthor, Integer.parseInt(updatedPages), updatedNote);

                finish();
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

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        if (positiveButton != null) {
            positiveButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
    }

    void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Book");
        builder.setMessage("Are you sure you want to delete \"" + title + "\"?\n\nThis action cannot be undone.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDataBaseHelper myDb = new MyDataBaseHelper(UpdateActivity.this);
                myDb.deleteBook(id);

                finish();
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
}
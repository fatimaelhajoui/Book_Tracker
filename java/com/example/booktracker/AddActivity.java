package com.example.booktracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddActivity extends AppCompatActivity {
    EditText editTextBookTitle, editTextAuthor, editTextPages,editTextNotes;
    Button buttonAddBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buttonAddBook), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextBookTitle =findViewById(R.id.editTextBookTitle);
        editTextAuthor =findViewById(R.id.editTextAuthor);
        editTextPages =findViewById(R.id.editTextPages);
        editTextNotes =findViewById(R.id.editTextNotes);
        buttonAddBook =findViewById(R.id.buttonAddBook);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDataBaseHelper myDB= new MyDataBaseHelper(AddActivity.this);
                myDB.addBook(editTextBookTitle.getText().toString().trim(),
                             editTextAuthor.getText().toString().trim(),
                             Integer.parseInt(editTextPages.getText().toString().trim()),
                             editTextNotes.getText().toString().trim()
                             );

            }
        });

    }
}
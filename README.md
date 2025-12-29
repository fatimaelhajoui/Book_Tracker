# Book Tracker - Android Library Manager

A simple Android app for managing your personal book collection with SQLite database.

## Features

- ✅ Add, update, and delete books
- 🔍 Search by title or author
- 📚 View complete library in scrollable list
- 🗑️ Delete individual or all books with confirmation
- 💾 Offline SQLite storage

## Tech Stack

- **Language**: Java
- **Database**: SQLite
- **Architecture**: MVC Pattern
- **UI**: XML Layouts, RecyclerView

## Project Structure

```
app/
├── MainActivity.java          # Main screen with book list
├── AddActivity.java           # Add new books
├── UpdateActivity.java        # Edit/delete books
├── MyDataBaseHelper.java      # SQLite CRUD operations
└── CustomAdapter.java         # RecyclerView adapter
```

## Database Schema

```sql
CREATE TABLE books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    pages INTEGER,
    notes TEXT
);
```

## Code Examples

### Adding a Book

```java
// MyDataBaseHelper.java
public void addBook(String title, String author, int pages, String note ){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_NOTE, note);

        long result= db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            Toast.makeText(context, "Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Added Successfully!",Toast.LENGTH_SHORT).show();
        }
    }
```

### Reading All Books

```java
// MyDataBaseHelper.java
public Cursor readAllData(){
        String query= "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();

        Cursor cursor =null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

```

### Updating a Book

```java
// MyDataBaseHelper.java
public void updateBook(String row_id,String title, String author, int pages, String note){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_NOTE, note);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Updated Successfully!",Toast.LENGTH_SHORT).show();
        }
    }

```

### RecyclerView Adapter

```java
// CustomAdapter.java
@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int currentPosition = position;

        holder.book_id.setText(String.valueOf(position + 1));

        holder.book_title.setText(String.valueOf(book_title.get(position)));
        holder.book_author.setText(String.valueOf(book_author.get(position)));
        holder.book_pages.setText(String.valueOf(book_pages.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);

                intent.putExtra("id", String.valueOf(book_id.get(currentPosition )));
                intent.putExtra("title", String.valueOf(book_title.get(currentPosition )));
                intent.putExtra("author", String.valueOf(book_author.get(currentPosition )));
                intent.putExtra("pages", String.valueOf(book_pages.get(currentPosition )));
                intent.putExtra("note", String.valueOf(book_note.get(currentPosition )));
                activity.startActivityForResult(intent, 1);
            }
        });

    }
```

## Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or device

```bash
git clone https://github.com/yourusername/book-tracker.git
cd book-tracker
```

## Usage

1. Launch app to view your library
2. Tap **+** to add a new book
3. Tap any book to edit or delete
4. Use search icon to find books
5. Use menu to delete all books

## Requirements

- Android Studio
- Min SDK: 21 (Android 5.0)
- Target SDK: 33+

## Author

**Fatima Zahrae EL HAJOUI**

## License

MIT License

---

*A learning project demonstrating Android development with SQLite database management.*}
```

### Updating a Book

```java
// MyDataBaseHelper.java
public void updateData(String id, String title, String author, String pages, String notes) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(COLUMN_TITLE, title);
    cv.put(COLUMN_AUTHOR, author);
    cv.put(COLUMN_PAGES, pages);
    cv.put(COLUMN_NOTES, notes);
    
    long result = db.update(TABLE_NAME, cv, "id=?", new String[]{id});
    if(result == -1) {
        Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
    }
}
```

### RecyclerView Adapter

```java
// CustomAdapter.java
@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.book_title.setText(String.valueOf(book_id.get(position)));
    holder.book_author.setText(String.valueOf(book_title.get(position)));
    holder.book_pages.setText(String.valueOf(book_author.get(position)));
    
    holder.mainLayout.setOnClickListener(v -> {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("id", String.valueOf(book_id.get(position)));
        intent.putExtra("title", String.valueOf(book_title.get(position)));
        intent.putExtra("author", String.valueOf(book_author.get(position)));
        intent.putExtra("pages", String.valueOf(book_pages.get(position)));
        activity.startActivityForResult(intent, 1);
    });
}
```

## Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on emulator or device

```bash
git clone https://github.com/yourusername/book-tracker.git
cd book-tracker
```

## Usage

1. Launch app to view your library
2. Tap **+** to add a new book
3. Tap any book to edit or delete
4. Use search icon to find books
5. Use menu to delete all books

## Requirements

- Android Studio
- Min SDK: 21 (Android 5.0)
- Target SDK: 33+

## Author

**Fatima Zahrae EL HAJOUI**

## License

MIT License

---

*A learning project demonstrating Android development with SQLite database management.*

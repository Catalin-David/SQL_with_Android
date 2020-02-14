package com.example.sqlbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddBookDialog.AddNewBook, BookRecViewAdapter.DeleteBook {
    private static final String TAG = "MainActivity";

    @Override
    public void onDeletingResult(int bookId) {
        Log.d(TAG, "onDeletingResult: deleting book with id" + bookId);

        try{
            databaseHelper.delete(database, bookId);
            DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
            databaseAsyncTask.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAddingNewBookResult(Book book) {
        Log.d(TAG, "onAddingNewBookResult: new Book: " + book.toString());

        try{
            databaseHelper.insert(database, book);
            DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
            databaseAsyncTask.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private RecyclerView recyclerView;
    private BookRecViewAdapter adapter;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private SQLiteDatabase database;

    private ArrayList<Book> allBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recViewBooks);
        adapter = new BookRecViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allBooks = new ArrayList<>();

        DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
        databaseAsyncTask.execute();
    }

    private class DatabaseAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            databaseHelper = new DatabaseHelper(MainActivity.this);
            database = databaseHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                cursor = database.query("books", null, null, null, null, null, null);
                allBooks.clear();
                if(cursor.moveToFirst()){
                    for(int i=0; i<cursor.getCount(); i++){
                        Book book = new Book();
                        for(int j=0; j<cursor.getColumnCount(); j++){
                            switch (cursor.getColumnName(j)){
                                case "_id":
                                    book.set_id(cursor.getInt(j));
                                    break;
                                case "name":
                                    book.setName(cursor.getString(j));
                                    break;
                                case "author":
                                    book.setAuthor(cursor.getString(j));
                                    break;
                                case "language":
                                    book.setLanguage(cursor.getString(j));
                                    break;
                                case "pages":
                                    book.setPages(cursor.getInt(j));
                                    break;
                                case "short_desc":
                                    book.setShort_desc(cursor.getString(j));
                                    break;
                                case "long_desc":
                                    book.setLong_desc(cursor.getString(j));
                                    break;
                                case "image_url":
                                    book.setImage_url(cursor.getString(j));
                                    break;
                                case "isFavorite":
                                    int isFavorite = cursor.getInt(j);
                                    if(isFavorite == 1){
                                        book.setFavorite(true);
                                    }else{
                                        book.setFavorite(false);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        allBooks.add(book);
                        cursor.moveToNext();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            adapter.setBooks(allBooks);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addNewBook){
            AddBookDialog dialog = new AddBookDialog();
            dialog.show(getSupportFragmentManager(), "add new book");
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}

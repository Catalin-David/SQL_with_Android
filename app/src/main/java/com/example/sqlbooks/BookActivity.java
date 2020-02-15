package com.example.sqlbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookActivity extends AppCompatActivity {
    private static final String TAG = "BookActivity";

    private ImageView emptyStar, filledStar, bookImage;
    private TextView name, author, pages, language, shortDesc, longDesc;
    private Book incomingBook;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private boolean hasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initWidgets();
        incomingBook = new Book();

        Intent intent = getIntent();
        try{
            int bookId = intent.getIntExtra("bookId", -1);
            if(bookId != -1){
                GetBookByIdAsyncTask getBookByIdAsyncTask = new GetBookByIdAsyncTask();
                getBookByIdAsyncTask.execute(bookId);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private class GetBookByIdAsyncTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            databaseHelper = new DatabaseHelper(BookActivity.this);
            database = databaseHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            cursor = database.query("books", null, "_id = ?", new String[]{String.valueOf(integers[0])}, null, null, null);

            if(cursor.moveToFirst()){
                for(int i=0; i<cursor.getColumnCount(); i++){
                    switch (cursor.getColumnName(i)){
                        case "_id":
                            incomingBook.set_id(cursor.getInt(i));
                            break;
                        case "name":
                            incomingBook.setName(cursor.getString(i));
                            break;
                        case "author":
                            incomingBook.setAuthor(cursor.getString(i));
                            break;
                        case "language":
                            incomingBook.setLanguage(cursor.getString(i));
                            break;
                        case "pages":
                            incomingBook.setPages(cursor.getInt(i));
                            break;
                        case "short_desc":
                            incomingBook.setShort_desc(cursor.getString(i));
                            break;
                        case "long_desc":
                            incomingBook.setLong_desc(cursor.getString(i));
                            break;
                        case "image_url":
                            incomingBook.setImage_url(cursor.getString(i));
                            break;
                        case "isFavorite":
                            int isFavorite = cursor.getInt(i);
                            if(isFavorite == 1){
                                incomingBook.setFavorite(true);
                            }else{
                                incomingBook.setFavorite(false);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            name.setText(incomingBook.getName());
            author.setText("Author is " + incomingBook.getAuthor());
            language.setText("Book is written in" + incomingBook.getLanguage());
            pages.setText(incomingBook.getPages() + " pages");
            shortDesc.setText(incomingBook.getShort_desc());
            longDesc.setText(incomingBook.getLong_desc());
            Glide.with(BookActivity.this)
                    .asBitmap()
                    .load(incomingBook.getImage_url())
                    .into(bookImage);
            if(incomingBook.isFavorite()){
                filledStar.setVisibility(View.VISIBLE);
                emptyStar.setVisibility(View.GONE);
                filledStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UpdateLikedBookAsyncTask updateLikedBookAsyncTask = new UpdateLikedBookAsyncTask();
                        updateLikedBookAsyncTask.execute(false);
                    }
                });
            }else{
                filledStar.setVisibility(View.GONE);
                emptyStar.setVisibility(View.VISIBLE);
                emptyStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UpdateLikedBookAsyncTask updateLikedBookAsyncTask = new UpdateLikedBookAsyncTask();
                        updateLikedBookAsyncTask.execute(true);
                    }
                });
            }
        }
    }

    private class UpdateLikedBookAsyncTask extends AsyncTask<Boolean, Void, Void>{

        @Override
        protected Void doInBackground(Boolean... booleans) {
            ContentValues contentValues = new ContentValues();
            if(booleans[0]){
                contentValues.put("isFavorite", 1);
            }else{
                contentValues.put("isFavorite", -1);
            }

            try{
                int rowsAffected = database.update("books", contentValues, "_id = ?", new String[]{String.valueOf(incomingBook.get_id())});
                if(rowsAffected > 0){
                    hasChanged = true;
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(hasChanged){
                GetBookByIdAsyncTask getBookByIdAsyncTask = new GetBookByIdAsyncTask();
                getBookByIdAsyncTask.execute(incomingBook.get_id());
            }
        }
    }

    private void initWidgets(){
        emptyStar = findViewById(R.id.emptyStar);
        filledStar = findViewById(R.id.filledStar);
        bookImage = findViewById(R.id.bookImage);

        name = findViewById(R.id.bookName);
        author = findViewById(R.id.authorName);
        pages = findViewById(R.id.pages);
        language = findViewById(R.id.language);
        shortDesc = findViewById(R.id.shortDesc);
        longDesc = findViewById(R.id.longDesc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursor.close();
        database.close();
    }
}

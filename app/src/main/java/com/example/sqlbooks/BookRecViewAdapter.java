package com.example.sqlbooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookRecViewAdapter extends RecyclerView.Adapter<BookRecViewAdapter.ViewHolder>{
    private static final String TAG = "BookRecViewAdapter";

    public interface DeleteBook{
        void onDeletingResult(int bookId);
    }
    private DeleteBook deleteBook;

    private Context context;
    private ArrayList<Book> books = new ArrayList<>();

    public BookRecViewAdapter(Context context) {
        this.context = context;
    }

    public BookRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.bookName.setText(books.get(position).getName());
        holder.bookDesc.setText(books.get(position).getShort_desc());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("bookId", books.get(position).get_id());
                context.startActivity(intent);
            }
        });

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete book")
                        .setMessage("Are you sure you want to delete " + books.get(position).getName() + " ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    deleteBook = (DeleteBook) context;
                                    deleteBook.onDeletingResult(books.get(position).get_id());
                                }catch (ClassCastException e){
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            return true;
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(books.get(position).getImage_url())
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView parent;
        private TextView bookName, bookDesc;
        private ImageView bookImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            bookName = itemView.findViewById(R.id.bookName);
            bookDesc = itemView.findViewById(R.id.bookDesc);
            bookImage = itemView.findViewById(R.id.bookImage);
        }
    }
}

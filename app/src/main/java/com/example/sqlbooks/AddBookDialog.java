package com.example.sqlbooks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddBookDialog extends DialogFragment {
    private static final String TAG = "AddBookDialog";

    private EditText name, author, language, pages, shortDesc, longDesc, imageUrl;
    private Button add, cancel;

    interface AddNewBook{
        void onAddingNewBookResult(Book book);
    }

    private AddNewBook addNewBook;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_new_book, null);
        initWidgets(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Add new book")
                .setView(view);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.setName(name.getText().toString());
                book.setAuthor(author.getText().toString());
                book.setLanguage(language.getText().toString());
                book.setPages(Integer.valueOf(pages.getText().toString()));
                book.setShort_desc(shortDesc.getText().toString());
                book.setLong_desc(longDesc.getText().toString());
                book.setImage_url(imageUrl.getText().toString());
                book.setFavorite(false);

                try{
                    addNewBook = (AddNewBook) getActivity();
                    addNewBook.onAddingNewBookResult(book);
                    dismiss();
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void initWidgets(View view){
        name = view.findViewById(R.id.edtTextName);
        author = view.findViewById(R.id.edtTextAuthor);
        language = view.findViewById(R.id.edtTextLanguage);
        pages = view.findViewById(R.id.edtTextPages);
        shortDesc = view.findViewById(R.id.edtTextShortDesc);
        longDesc = view.findViewById(R.id.edtTextLongDesc);
        imageUrl = view.findViewById(R.id.edtTextImageUrl);
        add = view.findViewById(R.id.btnAdd);
        cancel = view.findViewById(R.id.btnCancel);
    }
}

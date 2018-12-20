package com.chen.interview.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chen.interview.R;
import com.chen.interview.aidl.Book;

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        Uri bookUri = Uri.parse("content://com.chen.interview.provider.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 7);
        values.put("name", "JVM");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (null != bookCursor && bookCursor.moveToNext()) {
            Book book = new Book(bookCursor.getInt(0), bookCursor.getString(1));
            Log.d(TAG, "query book: " + book);
        }
        if (null != bookCursor) {
            bookCursor.close();
        }

        Uri userUri = Uri.parse("content://com.chen.interview.provider.book.provider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (null != userCursor && userCursor.moveToNext()) {
            User user = new User(userCursor.getInt(0), userCursor.getString(1), userCursor.getInt(2) == 1);
            Log.d(TAG, "query user: " + user.toString());
        }
        if (null != userCursor) {
            userCursor.close();
        }

    }
}

package com.chen.interview.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * from 开发艺术探索
 */
public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    public static final String AUTHORITY = "com.chen.interview.provider.book.provider";
    public static final Uri URI_BOOK_CONTENT = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri URI_USER_CONTENT = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int CODE_BOOK_URI = 0;
    public static final int CODE_USER_URI = 1;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", CODE_BOOK_URI);
        sUriMatcher.addURI(AUTHORITY, "user", CODE_USER_URI);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate current thread: " + Thread.currentThread().getName());
        mContext = getContext();
        // 初始化数据库操作，耗时，不建议在这里初始化
        initProviderData();
        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("DELETE FROM " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("DELETE FROM " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("INSERT INTO " + DbOpenHelper.BOOK_TABLE_NAME + " values(3, 'Kotlin')");
        mDb.execSQL("INSERT INTO " + DbOpenHelper.BOOK_TABLE_NAME + " values(5, 'Java')");
        mDb.execSQL("INSERT INTO " + DbOpenHelper.BOOK_TABLE_NAME + " values(2, 'Android')");
        mDb.execSQL("INSERT INTO " + DbOpenHelper.USER_TABLE_NAME + " values(1, 'chan', 1)");
        mDb.execSQL("INSERT INTO " + DbOpenHelper.USER_TABLE_NAME + " values(4, 'zach', 2)");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query, current thread: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (null == table) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(table, projection, selection, selectionArgs, null, null,
                sortOrder, null);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert");
        String table = getTableName(uri);
        if (null == table) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete");
        String table = getTableName(uri);
        if (null == table) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, null, selectionArgs);
        if (0 < count) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        String table = getTableName(uri);
        if (null == table) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.update(table, values, selection, selectionArgs);
        if(0 < count) {
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_BOOK_URI:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case CODE_USER_URI:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}

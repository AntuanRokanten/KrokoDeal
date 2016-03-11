package com.implemica.krokodeal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ant
 */
public class DBHelper extends SQLiteOpenHelper {

   private static final String LOG_TAG = DBHelper.class.getName();
   private Context context;

   private final static int DB_VERSION = 3; // todo later downgrade to 1
   private final static String DB_NAME = "krokodeal.db";
   private final static String WORDS_TABLE_NAME = "words";
   private final static String PLAYERS_TABLE_NAME = "players";
   private final static String WORDS_FILE_NAME = "words.txt";

   public DBHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      this.context = context;
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      Log.d(LOG_TAG, "Creating database: " + DB_NAME);

      db.execSQL("CREATE TABLE " + WORDS_TABLE_NAME + "( word TEXT )");
      db.execSQL("CREATE TABLE " + PLAYERS_TABLE_NAME + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " user_name TEXT," +
            " guesses INTEGER, " +
            " show_success INTEGER, " +
            " show_fail INTEGER )");

      fillWordsTable(db);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + WORDS_TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME);

      onCreate(db);
   }

   private void fillWordsTable(SQLiteDatabase db) {
      List<String> words = getWords();
      Log.d(LOG_TAG, "Populating table " + WORDS_TABLE_NAME + " with " + words.size() + " values");

      ContentValues contentValues;
      for (String word : words) {
         contentValues = new ContentValues();
         contentValues.put("word", word);
         db.insert(WORDS_TABLE_NAME, null, contentValues);
      }
   }

   public String retrieveRandomWord() {
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + WORDS_TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);
      String word = "";
      if (cursor.moveToFirst()) {
         word = cursor.getString(cursor.getColumnIndex("word")); // todo constants
      }
      cursor.close();
      db.close();
      return word;
   }

   private List<String> getWords() {
      List<String> words = new ArrayList<>();

      try {
         InputStream is = context.getAssets().open(WORDS_FILE_NAME);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
         String word;

         while ((word = bufferedReader.readLine()) != null) {
            words.add(word);
         }

      } catch (IOException e) {
         Log.e(LOG_TAG, "Cannot open file with words", e);
      }

      return words;
   }


}

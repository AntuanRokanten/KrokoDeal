package com.implemica.krokodeal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.implemica.krokodeal.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ant
 */
public class DBHelper extends SQLiteOpenHelper {

   private static final String LOG_TAG = DBHelper.class.getName();
   private Context context;

   private final static int DB_VERSION = 7; // todo later downgrade to 1
   private final static String DB_NAME = "krokodeal.db";
   private final static String WORDS_TABLE_NAME = "words";
   private final static String PLAYERS_TABLE_NAME = "players";
   private final static String WORDS_FILE_NAME = "words.txt";

   private final static String WORD_COLUMN = "word";

   private final static String ID_COLUMN = "_id";
   private final static String USER_NAME_COLUMN = "user_name";
   private final static String GUESSES_COLUMN = "guesses";
   private final static String SHOW_SUCCESS_COLUMN = "show_success";
   private final static String SHOW_FAIL_COLUMN = "show_fail";

   public DBHelper(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      this.context = context;
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      Log.d(LOG_TAG, "Creating database: " + DB_NAME);

      db.execSQL("CREATE TABLE " + WORDS_TABLE_NAME + "( " + WORD_COLUMN +  "  TEXT )");
      db.execSQL("CREATE TABLE " + PLAYERS_TABLE_NAME + "( " + ID_COLUMN +  "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " " + USER_NAME_COLUMN + " TEXT," +
            " " + GUESSES_COLUMN +  " INTEGER, " +
            " " + SHOW_SUCCESS_COLUMN + " INTEGER, " +
            " " + SHOW_FAIL_COLUMN + " show_fail INTEGER )");

      fillPlayersTable(db);
      fillWordsTable(db);
   }

   private void fillPlayersTable(SQLiteDatabase db) { // todo delete later
      db.execSQL("INSERT INTO " + PLAYERS_TABLE_NAME + " ( "  + USER_NAME_COLUMN + ", " + GUESSES_COLUMN + ", " + SHOW_SUCCESS_COLUMN + ", " + SHOW_FAIL_COLUMN
      +  " ) VALUES ('Ant', 12, 3, 0)");
      db.execSQL("INSERT INTO " + PLAYERS_TABLE_NAME + " ( "  + USER_NAME_COLUMN + ", " + GUESSES_COLUMN + ", " + SHOW_SUCCESS_COLUMN + ", " + SHOW_FAIL_COLUMN
      +  " ) VALUES ('Pat', 6, 2, 1)");
      db.execSQL("INSERT INTO " + PLAYERS_TABLE_NAME + " ( "  + USER_NAME_COLUMN + ", " + GUESSES_COLUMN + ", " + SHOW_SUCCESS_COLUMN + ", " + SHOW_FAIL_COLUMN
      +  " ) VALUES ('Cob', 1, 2, 4)");
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

   public Observable<Player> getPlayers() {
      return Observable.create(new Observable.OnSubscribe<Player>() {
         @Override
         public void call(Subscriber<? super Player> subscriber) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + PLAYERS_TABLE_NAME, null);

            while (cursor.moveToNext()) {
               String playerName = cursor.getString(cursor.getColumnIndex(USER_NAME_COLUMN));
               subscriber.onNext(new Player(playerName));
            }

            cursor.close();
            db.close();

            subscriber.onCompleted();
         }
      }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
   }

   public Single<String> retrieveRandomWord() {

      return Single.create(new Single.OnSubscribe<String>() {
         @Override
         public void call(SingleSubscriber<? super String> subscriber) {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + WORDS_TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);
            String word = null;
            if (cursor.moveToFirst()) {
               word = cursor.getString(cursor.getColumnIndex("word")); // todo constants
            }
            cursor.close();
            db.close();
            if (word != null) {
               subscriber.onSuccess(word);
            } else {
               subscriber.onError(new Exception("Cannot retrieve word from database"));
            }
         }
      })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
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


   public void saveUsers(List<Player> players) {
      SQLiteDatabase db = getWritableDatabase();
      db.delete(PLAYERS_TABLE_NAME, null, null);

      ContentValues contentValues;
      for (Player player : players) {
         contentValues = new ContentValues();
         contentValues.put(USER_NAME_COLUMN, player.getName());
         contentValues.put(GUESSES_COLUMN, 0);
         contentValues.put(SHOW_SUCCESS_COLUMN, 0);
         contentValues.put(SHOW_FAIL_COLUMN, 0);

         db.insert(PLAYERS_TABLE_NAME, null, contentValues);
      }

   }

   public void incrementShowSuccess(Player host) {
      incrementColumnValue(SHOW_SUCCESS_COLUMN, host.getName());
   }

   public void incrementShowFail(Player host) {
      incrementColumnValue(SHOW_FAIL_COLUMN, host.getName());
   }

   public void incrementGuessesValue(Player winner) {
      incrementColumnValue(GUESSES_COLUMN, winner.getName());
   }

   private void incrementColumnValue(String columnToIncrement, String playerName) {
      SQLiteDatabase db = getWritableDatabase();
      String updateQuery = "UPDATE " + PLAYERS_TABLE_NAME + " SET " + columnToIncrement + " = " + columnToIncrement + " + 1 WHERE " + USER_NAME_COLUMN + " = '" + playerName + "'";
      db.rawQuery(updateQuery, null);
      db.close();
   }
}

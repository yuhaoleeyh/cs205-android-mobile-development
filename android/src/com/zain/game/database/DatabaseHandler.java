package com.zain.game.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresManager";
    private static final String TABLE_SCORES = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SCORE + " TEXT" + ")";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    public void deleteAllRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SCORES);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new score
    public void addScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName()); // Score Name
        values.put(KEY_SCORE, score.getScore()); // Score Score number

        // Inserting Row
        db.insert(TABLE_SCORES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single score
    Score getScore(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCORES, new String[] { KEY_ID,
                        KEY_NAME, KEY_SCORE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Score score = new Score(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        // return score
        return score;
    }

    // code to get all scores in a list view
    public List<Score> getAllScores() {
        List<Score> scoreList = new ArrayList<Score>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setID(Integer.parseInt(cursor.getString(0)));
                score.setName(cursor.getString(1));
                score.setScore(Integer.parseInt(cursor.getString(2)));
                // Adding score to list
                scoreList.add(score);
            } while (cursor.moveToNext());
        }

        // return score list
        Collections.sort(scoreList); // sort by descending order
        return scoreList;
    }

    public void logAllScores() {
        List<Score> scores = getAllScores();
        int i = 0;
        for (Score sc : scores) {
            Log.d("Score " + String.valueOf(i), String.valueOf(sc.getScore()));
            i++;
        }
    }

    public void deleteExceptTopScores() {
        int num_top_scores = 3;
        SQLiteDatabase db = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TABLE_SCORES + " WHERE "
                + KEY_ID + " NOT IN " + " (SELECT " + KEY_ID
                + " FROM " + TABLE_SCORES
                + " ORDER BY " + KEY_SCORE + " DESC"
                + " LIMIT " + String.valueOf(num_top_scores)
                + ")";

        Log.d("deleteQuery", deleteQuery);
        db.execSQL(deleteQuery);

//        List<Score> scores = getAllScores();
//        deleteAllRows();
//
//        int num_top_scores = 3;
//        int num_scores_to_add = Math.min(scores.size(), num_top_scores);
//        for (int i = 0; i < num_scores_to_add; i++) {
//            addScore(scores.get(i));
//        }
    }

    // code to update the single score
    public int updateScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName());
        values.put(KEY_SCORE, score.getScore());

        // updating row
        return db.update(TABLE_SCORES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(score.getID()) });
    }

    // Deleting single score
    public void deleteScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCORES, KEY_ID + " = ?",
                new String[] { String.valueOf(score.getID()) });
        db.close();
    }

    // Getting count of number of scores
    public int getScoresCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        Log.d("Score count:", String.valueOf(cursor.getCount()));
        return cursor.getCount();
    }

    // Get top score
    public int getTopScore() {
        List<Score> scores = getAllScores();

        int top_score = 0;
        for (Score sc : scores) {
            top_score = Math.max(top_score, sc.getScore());
        }
        return top_score;
    }

}

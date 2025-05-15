package com.asiah.formfit.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ExerciseDbHelper manages the local SQLite database for storing exercise data.
 * This implements the local storage component of the hybrid persistence strategy.
 */
public class ExerciseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "formfit.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_ACHIEVEMENTS = "achievements";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TIMESTAMP = "timestamp";

    // USER table columns
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_JOIN_DATE = "join_date";

    // EXERCISE table columns
    private static final String KEY_EXERCISE_NAME = "name";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_FORM_ACCURACY = "form_accuracy";
    private static final String KEY_REPS = "reps";
    private static final String KEY_CALORIES = "calories";
    private static final String KEY_SYNCED = "synced";

    // ACHIEVEMENT table columns
    private static final String KEY_ACHIEVEMENT_NAME = "name";
    private static final String KEY_ACHIEVEMENT_DESC = "description";
    private static final String KEY_ACHIEVEMENT_DATE = "date";

    // Date formatter for consistent date formatting
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public ExerciseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_JOIN_DATE + " TEXT"
                + ")";

        // Create EXERCISES table
        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_EXERCISE_NAME + " TEXT,"
                + KEY_DURATION + " INTEGER,"
                + KEY_FORM_ACCURACY + " REAL,"
                + KEY_REPS + " INTEGER,"
                + KEY_CALORIES + " INTEGER,"
                + KEY_TIMESTAMP + " TEXT,"
                + KEY_SYNCED + " INTEGER DEFAULT 0" // 0 = not synced, 1 = synced
                + ")";

        // Create ACHIEVEMENTS table
        String CREATE_ACHIEVEMENTS_TABLE = "CREATE TABLE " + TABLE_ACHIEVEMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_ACHIEVEMENT_NAME + " TEXT,"
                + KEY_ACHIEVEMENT_DESC + " TEXT,"
                + KEY_ACHIEVEMENT_DATE + " TEXT"
                + ")";

        // Execute CREATE statements
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EXERCISES_TABLE);
        db.execSQL(CREATE_ACHIEVEMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
        onCreate(db);
    }

    /**
     * Add a new user to the database
     */
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_JOIN_DATE, dateFormat.format(user.getJoinDate()));

        // Insert row
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    /**
     * Add an exercise record to the database
     */
    public long addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, exercise.getUserId());
        values.put(KEY_EXERCISE_NAME, exercise.getName());
        values.put(KEY_DURATION, exercise.getDuration());
        values.put(KEY_FORM_ACCURACY, exercise.getFormAccuracy());
        values.put(KEY_REPS, exercise.getReps());
        values.put(KEY_CALORIES, exercise.getCalories());
        values.put(KEY_TIMESTAMP, dateFormat.format(exercise.getTimestamp()));
        values.put(KEY_SYNCED, exercise.isSynced() ? 1 : 0);

        // Insert row
        long id = db.insert(TABLE_EXERCISES, null, values);
        db.close();
        return id;
    }

    /**
     * Add an achievement to the database
     */
    public long addAchievement(Achievement achievement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, achievement.getUserId());
        values.put(KEY_ACHIEVEMENT_NAME, achievement.getName());
        values.put(KEY_ACHIEVEMENT_DESC, achievement.getDescription());
        values.put(KEY_ACHIEVEMENT_DATE, dateFormat.format(achievement.getDate()));

        // Insert row
        long id = db.insert(TABLE_ACHIEVEMENTS, null, values);
        db.close();
        return id;
    }

    /**
     * Get all exercises for a user
     */
    public List<Exercise> getUserExercises(long userId) {
        List<Exercise> exerciseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXERCISES +
                " WHERE " + KEY_USER_ID + " = ?" +
                " ORDER BY " + KEY_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                exercise.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
                exercise.setName(cursor.getString(cursor.getColumnIndex(KEY_EXERCISE_NAME)));
                exercise.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
                exercise.setFormAccuracy(cursor.getFloat(cursor.getColumnIndex(KEY_FORM_ACCURACY)));
                exercise.setReps(cursor.getInt(cursor.getColumnIndex(KEY_REPS)));
                exercise.setCalories(cursor.getInt(cursor.getColumnIndex(KEY_CALORIES)));

                // Parse date
                try {
                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    exercise.setTimestamp(dateFormat.parse(dateStr));
                } catch (ParseException e) {
                    exercise.setTimestamp(new Date()); // Default to current date if parsing fails
                }

                exercise.setSynced(cursor.getInt(cursor.getColumnIndex(KEY_SYNCED)) == 1);

                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exerciseList;
    }

    /**
     * Get exercises that need to be synced to the cloud
     */
    public List<Exercise> getUnsyncedExercises() {
        List<Exercise> exerciseList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXERCISES +
                " WHERE " + KEY_SYNCED + " = 0";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise();
                exercise.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                exercise.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
                exercise.setName(cursor.getString(cursor.getColumnIndex(KEY_EXERCISE_NAME)));
                exercise.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
                exercise.setFormAccuracy(cursor.getFloat(cursor.getColumnIndex(KEY_FORM_ACCURACY)));
                exercise.setReps(cursor.getInt(cursor.getColumnIndex(KEY_REPS)));
                exercise.setCalories(cursor.getInt(cursor.getColumnIndex(KEY_CALORIES)));

                // Parse date
                try {
                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP));
                    exercise.setTimestamp(dateFormat.parse(dateStr));
                } catch (ParseException e) {
                    exercise.setTimestamp(new Date()); // Default to current date if parsing fails
                }

                exercise.setSynced(false); // Already know this is false based on query

                exerciseList.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exerciseList;
    }

    /**
     * Mark an exercise as synced in the database
     */
    public void markExerciseSynced(long exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SYNCED, 1); // 1 = synced

        db.update(TABLE_EXERCISES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(exerciseId)});
        db.close();
    }

    /**
     * Get all achievements for a user
     */
    public List<Achievement> getUserAchievements(long userId) {
        List<Achievement> achievementList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ACHIEVEMENTS +
                " WHERE " + KEY_USER_ID + " = ?" +
                " ORDER BY " + KEY_ACHIEVEMENT_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                achievement.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
                achievement.setName(cursor.getString(cursor.getColumnIndex(KEY_ACHIEVEMENT_NAME)));
                achievement.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ACHIEVEMENT_DESC)));

                // Parse date
                try {
                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_ACHIEVEMENT_DATE));
                    achievement.setDate(dateFormat.parse(dateStr));
                } catch (ParseException e) {
                    achievement.setDate(new Date()); // Default to current date if parsing fails
                }

                achievementList.add(achievement);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return achievementList;
    }
}


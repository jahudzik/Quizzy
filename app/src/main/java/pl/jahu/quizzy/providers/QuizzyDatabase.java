package pl.jahu.quizzy.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
public class QuizzyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quizzy.db";
    private static final int SCHEMA_VERSION = 1;

    public static final String QUESTIONS_TABLE = "questions";
    public static final String ID_COLUMN = "id";
    public static final String QUESTION_COLUMN = "question";
    public static final String ANSWER_COLUMN = "answer";
    public static final String ALL_ANSWERS_COUNT_COLUMN = "all_answers_count";
    public static final String CORRECT_ANSWERS_COLUMN = "correct_answers_count";
    public static final String CATEGORY_COLUMN = "category";


    private static final String CREATE_QUESTIONS_TABLE_QUERY = "CREATE TABLE " + QUESTIONS_TABLE + " ( " +
                                                                                ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                QUESTION_COLUMN + " TEXT, " +
                                                                                ANSWER_COLUMN + " TEXT, " +
                                                                                CATEGORY_COLUMN + " TEXT" +
                                                                                ALL_ANSWERS_COUNT_COLUMN + " INTEGER, " +
                                                                                CORRECT_ANSWERS_COLUMN + " INTEGER)";
    private static final String SELECT_ALL_QUESTIONS_QUERY = "SELECT * FROM " + QUESTIONS_TABLE;

    public QuizzyDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUESTIONS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public List<Question> selectAllQuestions() {
        List<Question> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_QUESTIONS_QUERY, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String question = cursor.getString(1);
                String answer = cursor.getString(2);
                String category = cursor.getString(3);
                int allAnswersCount = cursor.getInt(4);
                int correctAnswersCount = cursor.getInt(5);
                result.add(new Question(id, question, answer, category, allAnswersCount, correctAnswersCount));
            }
        }
        return result;
    }

    public void insertQuestions(List<Question> questions) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Question question : questions) {
                ContentValues cv = new ContentValues();
                cv.put(QUESTION_COLUMN, question.getQuestion());
                cv.put(ANSWER_COLUMN, question.getAnswer());
                cv.put(CATEGORY_COLUMN, question.getCategory());
                cv.put(ALL_ANSWERS_COUNT_COLUMN, question.getAllAnswersCount());
                cv.put(CORRECT_ANSWERS_COLUMN, question.getCorrectAnswersCount());
                db.insert(QUESTIONS_TABLE, null, cv);
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
    }

}

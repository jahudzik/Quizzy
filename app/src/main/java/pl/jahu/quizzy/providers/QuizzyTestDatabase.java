package pl.jahu.quizzy.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-18.
 */
public class QuizzyTestDatabase extends QuizzyDatabase {

    private static List<Question> QUESTIONS = new ArrayList<>();

    private static String ANDROID_CODENAMES_CATEGORY = "Android codenames";
    private static String CAPITAL_CITIES_CATEGORY = "Capital cities";
    private static String SPANISH_WORDS_CATEGORY = "German words";

    static {
        QUESTIONS.add(new Question("Ice Cream Sandwitch", "4.0\nAPI 14-15", ANDROID_CODENAMES_CATEGORY, 2, 5));
        QUESTIONS.add(new Question("Jelly Bean", "4.1-4.3\nAPI 16-18", ANDROID_CODENAMES_CATEGORY, 4, 5));
        QUESTIONS.add(new Question("KitKat", "4.4\nAPI 19", ANDROID_CODENAMES_CATEGORY, 4, 5));
        QUESTIONS.add(new Question("Lollipop", "5.0\nAPI 20-21", ANDROID_CODENAMES_CATEGORY, 5, 5));

        QUESTIONS.add(new Question("Albania", "Tirana", CAPITAL_CITIES_CATEGORY, 4, 6));
        QUESTIONS.add(new Question("Montenegro", "Podgorica", CAPITAL_CITIES_CATEGORY, 1, 7));
        QUESTIONS.add(new Question("Estonia", "Tallinn", CAPITAL_CITIES_CATEGORY, 5, 6));
        QUESTIONS.add(new Question("Kosovo", "Pristina", CAPITAL_CITIES_CATEGORY, 5, 6));
        QUESTIONS.add(new Question("Switzerland", "Bern", CAPITAL_CITIES_CATEGORY, 2, 5));

        QUESTIONS.add(new Question("dog", "el perro", SPANISH_WORDS_CATEGORY, 7, 7));
        QUESTIONS.add(new Question("computer", "el ordenador", SPANISH_WORDS_CATEGORY, 7, 8));
        QUESTIONS.add(new Question("trousers", "los pantalones", SPANISH_WORDS_CATEGORY, 4, 6));
        QUESTIONS.add(new Question("airplane", "el aeroplano", SPANISH_WORDS_CATEGORY, 6, 6));
        QUESTIONS.add(new Question("sandwich", "el bocadillo", SPANISH_WORDS_CATEGORY, 2, 9));
    }

    public QuizzyTestDatabase(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        insertQuestions(db, QUESTIONS);
    }

}

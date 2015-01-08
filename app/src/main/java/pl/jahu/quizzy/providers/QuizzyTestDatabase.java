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

    static {
        String ANDROID_CODENAMES_CATEGORY = "Android codenames";
        QUESTIONS.add(new Question(1, "Apple Pie", "1.0\nAPI 1", ANDROID_CODENAMES_CATEGORY, 7, 5));
        QUESTIONS.add(new Question(2, "Banana Bread", "1.1\nAPI 2", ANDROID_CODENAMES_CATEGORY, 6, 5));
        QUESTIONS.add(new Question(3, "Cupcake", "1.5\nAPI 3", ANDROID_CODENAMES_CATEGORY, 5, 5));
        QUESTIONS.add(new Question(4, "Donut", "1.6\nAPI 4", ANDROID_CODENAMES_CATEGORY, 6, 2));
        QUESTIONS.add(new Question(5, "Eclair", "2.0-2.1\nAPI 5-7", ANDROID_CODENAMES_CATEGORY, 5, 3));
        QUESTIONS.add(new Question(6, "Froyo", "2.2\nAPI 8", ANDROID_CODENAMES_CATEGORY, 6, 4));
        QUESTIONS.add(new Question(7, "Gingerbread", "2.3\nAPI 9-10", ANDROID_CODENAMES_CATEGORY, 8, 5));
        QUESTIONS.add(new Question(8, "Honeycomb", "3.0-3.2\nAPI 11-13", ANDROID_CODENAMES_CATEGORY, 7, 4));
        QUESTIONS.add(new Question(9, "Ice Cream Sandwitch", "4.0\nAPI 14-15", ANDROID_CODENAMES_CATEGORY, 5, 2));
        QUESTIONS.add(new Question(10, "Jelly Bean", "4.1-4.3\nAPI 16-18", ANDROID_CODENAMES_CATEGORY, 5, 4));
        QUESTIONS.add(new Question(11, "KitKat", "4.4\nAPI 19", ANDROID_CODENAMES_CATEGORY, 6, 5));
        QUESTIONS.add(new Question(12, "Lollipop", "5.0\nAPI 20-21", ANDROID_CODENAMES_CATEGORY, 5, 5));

        String CAPITAL_CITIES_CATEGORY = "Capital cities";
        QUESTIONS.add(new Question(13, "Albania", "Tirana", CAPITAL_CITIES_CATEGORY, 7, 6));
        QUESTIONS.add(new Question(14, "Montenegro", "Podgorica", CAPITAL_CITIES_CATEGORY, 7, 1));
        QUESTIONS.add(new Question(15, "Estonia", "Tallinn", CAPITAL_CITIES_CATEGORY, 6, 4));
        QUESTIONS.add(new Question(16, "Kosovo", "Pristina", CAPITAL_CITIES_CATEGORY, 6, 2));
        QUESTIONS.add(new Question(17, "Switzerland", "Bern", CAPITAL_CITIES_CATEGORY, 5, 2));

        String SPANISH_WORDS_CATEGORY = "Spanish words";
        QUESTIONS.add(new Question(18, "dog", "el perro", SPANISH_WORDS_CATEGORY, 0, 0));
        QUESTIONS.add(new Question(19, "computer", "el ordenador", SPANISH_WORDS_CATEGORY, 0, 0));
        QUESTIONS.add(new Question(20, "trousers", "los pantalones", SPANISH_WORDS_CATEGORY, 0, 0));
        QUESTIONS.add(new Question(21, "airplane", "el aeroplano", SPANISH_WORDS_CATEGORY, 0, 0));
        QUESTIONS.add(new Question(22, "sandwich", "el bocadillo", SPANISH_WORDS_CATEGORY, 0, 0));
    }

    public QuizzyTestDatabase(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        insertQuestions(QUESTIONS);
    }

}

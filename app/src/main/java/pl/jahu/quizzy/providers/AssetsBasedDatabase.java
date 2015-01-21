package pl.jahu.quizzy.providers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import pl.jahu.quizzy.models.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2015-01-16.
 */
public class AssetsBasedDatabase extends QuizzyDatabase {

    private static final String ASSETS_LOCATION = "questions";

    private final AssetManager assetManager;

    public AssetsBasedDatabase(Context context) {
        super(context);
        assetManager = context.getAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        List<Question> questions = new ArrayList<>();
        int id = 0;
        String line;
        try {
            String[] categories = assetManager.list(ASSETS_LOCATION);
            for (String category : categories) {
                InputStreamReader in = new InputStreamReader(assetManager.open(ASSETS_LOCATION + "/" + category));
                BufferedReader bufferedReader = new BufferedReader(in);

                String question = null;
                StringBuilder answer = null;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.isEmpty()) {
                        if (question != null) {
                            addQuestion(questions, ++id, category, question, answer);
                            question = null;
                            answer = null;
                        }
                    } else {
                        if (question == null) {
                            question = line;
                        } else {
                            if (answer == null) {
                                answer = new StringBuilder();
                            }
                            answer.append(line).append("\n");
                        }
                    }
                }
                if (question != null) {
                    addQuestion(questions, ++id, category, question, answer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        insertQuestions(questions);
    }

    private int addQuestion(List<Question> questions, int id, String category, String question, StringBuilder answer) {
        String answerValue = (answer != null) ? answer.toString() : "<no answer>";
        questions.add(new Question(id, question, answerValue, category.replace(".txt", ""), 0, 0, 0, 0));
        return id;
    }

}

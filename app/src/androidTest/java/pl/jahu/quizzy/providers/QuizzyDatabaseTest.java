package pl.jahu.quizzy.providers;

import android.app.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import pl.jahu.quizzy.ShadowSQLiteOpenHelper;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Quizzy
 * Created by jahudzik on 2015-01-08.
 */
@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowSQLiteOpenHelper.class})
public class QuizzyDatabaseTest {

    @Test
    public void selectAllQuestions_testWithEmptyDatabase() {
        QuizzyDatabase quizzyDatabase = new QuizzyDatabase(new Activity());
        List<Question> questions = quizzyDatabase.selectAllQuestions();
        checkQuestionsList(0, null, questions);
    }

    @Test
    public void selectAllQuestions_testWithSampleData() {
        QuizzyDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        List<Question> questions = quizzyDatabase.selectAllQuestions();
        checkQuestionsList(22, null, questions);
    }

    @Test
    public void insertQuestions_test() {
        QuizzyDatabase quizzyDatabase = new QuizzyDatabase(new Activity());
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "some question1", "some answer1", "some category", 0, 0));
        questions.add(new Question(2, "some question2", "some answer2", "some category", 0, 0));
        quizzyDatabase.insertQuestions(questions);

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionsList(2, questions, retrievedQuestions);
    }


    /**
     * *****************************************************
     */

    private void checkQuestionsList(int expectedSize, List<Question> expectedQuestions, List<Question> questions) {
        assertNotNull(questions);
        assertEquals(expectedSize, questions.size());
        if (expectedQuestions != null) {
            for (Question expectedQuestion : expectedQuestions) {
                assertTrue(questions.contains(expectedQuestion));
            }
        }
    }

}

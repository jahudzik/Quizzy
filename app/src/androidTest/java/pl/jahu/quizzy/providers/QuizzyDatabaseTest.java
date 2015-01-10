package pl.jahu.quizzy.providers;

import android.app.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import pl.jahu.quizzy.ShadowSQLiteOpenHelper;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        checkQuestionsList(questions, 0, null);
    }

    @Test
    public void selectAllQuestions_testWithSampleData() {
        QuizzyDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        List<Question> questions = quizzyDatabase.selectAllQuestions();
        checkQuestionsList(questions, 22, null);
    }

    @Test
    public void insertQuestions_test() {
        QuizzyDatabase quizzyDatabase = new QuizzyDatabase(new Activity());
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "some question1", "some answer1", "some category", 0, 0));
        questions.add(new Question(2, "some question2", "some answer2", "some category", 0, 0));
        quizzyDatabase.insertQuestions(questions);

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionsList(retrievedQuestions, 2, questions);
    }

    @Test
    public void updateQuestionStats_oneCorrectAnswerTest() {
        QuizzyTestDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        Question question = new Question(18, "dog", "el perro", "Spanish words", 0, 0);
        question.newAnswer(true);

        quizzyDatabase.updateQuestionStats(Arrays.asList(question));

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionExistence(retrievedQuestions, 18, 1, 1);
    }

    @Test
    public void updateQuestionStats_oneIncorrectAnswerTest() {
        QuizzyTestDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        Question question = new Question(18, "dog", "el perro", "Spanish words", 0, 0);
        question.newAnswer(false);

        quizzyDatabase.updateQuestionStats(Arrays.asList(question));

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionExistence(retrievedQuestions, 18, 1, 0);
    }

    @Test
    public void updateQuestionStats_coupleAnswersTest() {
        QuizzyTestDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        Question question = new Question(18, "dog", "el perro", "Spanish words", 0, 0);
        question.newAnswer(false);
        question.newAnswer(false);
        question.newAnswer(false);
        question.newAnswer(true);

        quizzyDatabase.updateQuestionStats(Arrays.asList(question));

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionExistence(retrievedQuestions, 18, 4, 1);
    }

    @Test
    public void updateQuestionStats_noAnswerTest() {
        QuizzyTestDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        Question question = new Question(18, "dog", "el perro", "Spanish words", 0, 0);

        quizzyDatabase.updateQuestionStats(Arrays.asList(question));

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionExistence(retrievedQuestions, 18, 0, 0);
    }

    @Test
    public void updateQuestionStats_coupleQuestionsTest() {
        QuizzyTestDatabase quizzyDatabase = new QuizzyTestDatabase(new Activity());
        Question question1 = new Question(18, "dog", "el perro", "Spanish words", 0, 0);
        question1.newAnswer(false);
        Question question2 = new Question(19, "computer", "el ordenador", "Spanish words", 0, 0);
        question2.newAnswer(true);
        Question question3 = new Question(20, "trousers", "los pantalones", "Spanish words", 0, 0);
        question3.newAnswer(false);
        question3.newAnswer(true);

        quizzyDatabase.updateQuestionStats(Arrays.asList(question1, question2, question3));

        List<Question> retrievedQuestions = quizzyDatabase.selectAllQuestions();
        checkQuestionExistence(retrievedQuestions, 18, 1, 0);
        checkQuestionExistence(retrievedQuestions, 19, 1, 1);
        checkQuestionExistence(retrievedQuestions, 20, 2, 1);
    }


    /**
     * *****************************************************
     */

    private void checkQuestionsList(List<Question> questions, int expectedSize, List<Question> expectedQuestions) {
        assertNotNull(questions);
        assertEquals(expectedSize, questions.size());
        if (expectedQuestions != null) {
            for (Question expectedQuestion : expectedQuestions) {
                assertTrue(questions.contains(expectedQuestion));
            }
        }
    }

    private void checkQuestionExistence(List<Question> questions, int expectedId, int expectedOverallAnswers, int expectedOverallCorrectAnswers) {
        assertNotNull(questions);
        for (Question question : questions) {
            if (Objects.equals(question.getId(), expectedId)) {
                assertEquals(expectedOverallAnswers, question.getOverallAnswers());
                assertEquals(expectedOverallCorrectAnswers, question.getOverallCorrectAnswers());
                return;
            }
        }
        fail("Expected question not found [id=" + expectedId + "]");
    }

}

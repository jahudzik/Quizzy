package pl.jahu.quizzy.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-15.
 */
public class QuestionTest {

    @Test
    public void equals_test1() {
        Question question = new Question("question", "answer", new Category("some category"));
        assertFalse(question.equals(null));
    }

    @Test
    public void equals_test2() {
        Question question = new Question("question", "answer", new Category("some category"));
        String otherObject = "aa";
        assertFalse(question.equals(otherObject));
    }

    @Test
    public void equals_test3() {
        Question question = new Question("question", "answer", new Category("some category"));
        assertTrue(question.equals(question));
    }

    @Test
    public void equals_test4() {
        Question question1 = new Question("question", "answer", new Category("some category"));
        Question question2 = new Question("question", "answer", new Category("some category"));
        assertTrue(question1.equals(question2));
    }

    @Test
    public void equals_test5() {
        Question question1 = new Question("question1", "answer", new Category("some category"));
        Question question2 = new Question("question2", "answer", new Category("some category"));
        assertFalse(question1.equals(question2));
    }

    @Test
    public void equals_test6() {
        Question question1 = new Question("question1", null, new Category("some category"));
        Question question2 = new Question("question2", "answer", new Category("some category"));
        assertFalse(question1.equals(question2));
    }


    @Test
    public void hashCode_test1() {
        Question question1 = new Question("question", "answer", new Category("some category"));
        Question question2 = new Question("question", "answer", new Category("some category"));
        assertEquals(question1.hashCode(), question2.hashCode());
    }

    @Test
    public void hashCode_test2() {
        Question question1 = new Question("question", "answer", new Category("some category1"));
        Question question2 = new Question("question", "answer", new Category("some category2"));
        assertNotEquals(question1.hashCode(), question2.hashCode());
    }

}

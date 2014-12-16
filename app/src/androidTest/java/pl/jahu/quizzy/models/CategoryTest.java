package pl.jahu.quizzy.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-16.
 */
public class CategoryTest {

    @Test
    public void equals_test1() {
        Category category = new Category("some_category");
        assertFalse(category.equals(null));
    }

    @Test
    public void equals_test2() {
        Category category = new Category("some_category");
        assertTrue(category.equals(category));
    }

    @Test
    public void equals_test3() {
        Category category1 = new Category("some_category");
        Category category2 = new Category("some_category");
        assertTrue(category1.equals(category2));
    }

    @Test
    public void equals_test4() {
        Category category1 = new Category("some_category1");
        Category category2 = new Category("some_category2");
        assertFalse(category1.equals(category2));
    }

    @Test
    public void hashCode_test1() {
        Category category1 = new Category("some_category");
        Category category2 = new Category("some_category");
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    public void hashCode_test2() {
        Category category1 = new Category("some_category1");
        Category category2 = new Category("some_category2");
        assertNotEquals(category1.hashCode(), category2.hashCode());
    }

}

package pl.jahu.quizzy.providers;

import pl.jahu.quizzy.models.Category;
import pl.jahu.quizzy.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Quizzy
 * Created by jahudzik on 2014-12-16.
 */
public class SampleDataProvider implements DataProvider {

    private static List<Category> CATEGORIES = new ArrayList<>();
    private static List<Question> QUESTIONS = new ArrayList<>();

    private static Category ANDROID_CODENAMES_CATEGORY = new Category("Android codenames");
    private static Category CAPITAL_CITIES_CATEGORY = new Category("Capital cities");
    private static Category SPANISH_WORDS_CATEGORY = new Category("German words");

    static {
        CATEGORIES.add(ANDROID_CODENAMES_CATEGORY);
//        QUESTIONS.add(new Question("Apple Pie", "1.0\nAPI 1", ANDROID_CODENAMES_CATEGORY));
//        QUESTIONS.add(new Question("Banana Bread", "1.1\nAPI 2", ANDROID_CODENAMES_CATEGORY));
//        QUESTIONS.add(new Question("Cupcake", "1.5\nAPI 3", ANDROID_CODENAMES_CATEGORY));
//        QUESTIONS.add(new Question("Donut", "1.6\nAPI 4", ANDROID_CODENAMES_CATEGORY));
//        QUESTIONS.add(new Question("Eclair", "2.0-2.1\nAPI 5-7", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("Froyo", "2.2\nAPI 8", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("Gingerbread", "2.3\nAPI 9-10", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("Honeycomb", "3.0-3.2\nAPI 11-13", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("Ice Cream Sandwitch", "4.0\nAPI 14-15", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("Jelly Bean", "4.1-4.3\nAPI 16-18", ANDROID_CODENAMES_CATEGORY));
        QUESTIONS.add(new Question("KitKat", "4.4\nAPI 19", ANDROID_CODENAMES_CATEGORY));
//        QUESTIONS.add(new Question("Lollipop", "5.0\nAPI 20-21", ANDROID_CODENAMES_CATEGORY));

        CATEGORIES.add(CAPITAL_CITIES_CATEGORY);
        QUESTIONS.add(new Question("Albania", "Tirana", CAPITAL_CITIES_CATEGORY));
        QUESTIONS.add(new Question("Montenegro", "Podgorica", CAPITAL_CITIES_CATEGORY));
        QUESTIONS.add(new Question("Estonia", "Tallinn", CAPITAL_CITIES_CATEGORY));
        QUESTIONS.add(new Question("Kosovo", "Pristina", CAPITAL_CITIES_CATEGORY));
        QUESTIONS.add(new Question("Switzerland", "Bern", CAPITAL_CITIES_CATEGORY));

        CATEGORIES.add(SPANISH_WORDS_CATEGORY);
        QUESTIONS.add(new Question("dog", "el perro", SPANISH_WORDS_CATEGORY));
        QUESTIONS.add(new Question("computer", "el ordenador", SPANISH_WORDS_CATEGORY));
        QUESTIONS.add(new Question("trousers", "los pantalones", SPANISH_WORDS_CATEGORY));
        QUESTIONS.add(new Question("airplane", "el aeroplano", SPANISH_WORDS_CATEGORY));
        QUESTIONS.add(new Question("sandwich", "el bocadillo", SPANISH_WORDS_CATEGORY));
    }

    @Override
    public List<Category> getAllCategories() {
        return CATEGORIES;
    }

    @Override
    public List<Question> getQuestionsByCategories(List<Category> categories) {
        List<Question> result = new ArrayList<>();
        for (Question question : QUESTIONS) {
            if (categories.contains(question.getCategory())) {
                result.add(question);
            }
        }
        return result;
    }

}

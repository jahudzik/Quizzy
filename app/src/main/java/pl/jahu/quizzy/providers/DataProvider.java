package pl.jahu.quizzy.providers;

import pl.jahu.quizzy.models.Category;
import pl.jahu.quizzy.models.Question;

import java.util.List;

/**
 * Quzzy
 * Created by jahudzik on 2014-12-15.
 */
public interface DataProvider {

    public List<Category> getAllCategories();

    public List<Question> getQuestionsByCategories(List<Category> categories);

}

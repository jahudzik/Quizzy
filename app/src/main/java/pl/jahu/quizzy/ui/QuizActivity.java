package pl.jahu.quizzy.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.utils.Constants;

import javax.inject.Inject;
import java.util.*;

public class QuizActivity extends BaseActivity implements SetupFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener {

    @Inject
    QuizzyDatabase quizzyDatabase;

    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questions = quizzyDatabase.selectAllQuestions();
        Map<String, Integer[]> categoriesSizes = getCategoriesInfo(questions);
        SetupFragment setupFragment = new SetupFragment();
        setupFragment.setCategoriesSizes(categoriesSizes);

        setContentView(R.layout.activity_quiz);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, setupFragment)
                    .commit();
        }
    }

    private Map<String, Integer[]> getCategoriesInfo(List<Question> questions) {
        Map<String, Integer[]> result = new HashMap<>();
        for (Question question : questions) {
            String category = question.getCategory();
            if (!result.containsKey(category)) {
                result.put(category, new Integer[]{0, 0, 0, 0});
            }
            Integer[] sizes = result.get(category);
            sizes[Constants.DIFFICULTY_LEVEL_ALL]++;
            int difficultValue = question.getOverallDifficultValue();
            if (difficultValue < 75) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_75]++;
            }
            if (difficultValue < 50) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_50]++;
            }
            if (difficultValue < 25) {
                sizes[Constants.DIFFICULTY_LEVEL_BELOW_25]++;
            }
            result.put(category, sizes);
        }


        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartQuizButtonClicked(Set<String> chosenCategories, int chosenLevel) {
        List<Question> chosenQuestions = new ArrayList<>();
        for (Question question : questions) {
            if (chosenCategories.contains(question.getCategory()) && question.matchesLevel(chosenLevel)) {
                chosenQuestions.add(question);
            }
        }
        QuizFragment quizFragment = new QuizFragment();
        quizFragment.setQuestions(chosenQuestions);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, quizFragment)
                .commit();
    }

    @Override
    public void onQuizFinished(List<Question> questions) {
        quizzyDatabase.updateQuestionStats(questions);

        SummaryFragment summaryFragment = new SummaryFragment();
        summaryFragment.setQuestions(questions);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, summaryFragment)
                .commit();
    }
}

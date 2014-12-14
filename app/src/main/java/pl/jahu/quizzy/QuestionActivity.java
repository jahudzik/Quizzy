package pl.jahu.quizzy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.fragments.AnswerFragment;
import pl.jahu.quizzy.fragments.StatsFragment;
import pl.jahu.quizzy.models.Question;

import java.util.*;


public class QuestionActivity extends Activity implements AnswerFragment.OnFragmentInteractionListener {

    public static final String LOG_TAG = "Quizzy_QuestionActivity";
    private StatsFragment statsFragment;
    private AnswerFragment answerFragment;
    private TextView questionTextView;
    private List<Question> allQuestions;
    private List<Question> roundQuestions;
    private Set<Question> answeredQuestions;
    private int roundNumber;
    private int questionIndex;
    private int correctAnswersCount;
    private int wrongAnswersCount;

    public QuestionActivity() {
        this.allQuestions = new ArrayList<>();
        allQuestions.add(new Question("API 1", "Apple Pie\n1.0"));
        allQuestions.add(new Question("API 2", "Banana Bread\n1.1"));
        allQuestions.add(new Question("API 3", "Cupcake\n1.5"));
        allQuestions.add(new Question("API 4", "Donut\n1.6"));
        allQuestions.add(new Question("API 5-7", "Eclair\n2.0-2.1"));
        allQuestions.add(new Question("API 8", "Froyo\n2.2"));
        allQuestions.add(new Question("API 9-10", "Gingerbread\n2.3"));
        allQuestions.add(new Question("API 11-13", "Honeycomb\n3.0-3.2"));
        allQuestions.add(new Question("API 14-15", "Ice Cream Sandwitch\n4.0"));
        allQuestions.add(new Question("API 16-18", "Jelly Bean\n4.1-4.3"));
        allQuestions.add(new Question("API 19", "KitKat\n4.4"));
        allQuestions.add(new Question("API 20-21", "Lollipop\n5.0"));

        allQuestions.add(new Question("Apple Pie", "1.0\nAPI 1"));
        allQuestions.add(new Question("Banana Bread", "1.1\nAPI 2"));
        allQuestions.add(new Question("Cupcake", "1.5\nAPI 3"));
        allQuestions.add(new Question("Donut", "1.6\nAPI 4"));
        allQuestions.add(new Question("Eclair", "2.0-2.1\nAPI 5-7"));
        allQuestions.add(new Question("Froyo", "2.2\nAPI 8"));
        allQuestions.add(new Question("Gingerbread", "2.3\nAPI 9-10"));
        allQuestions.add(new Question("Honeycomb", "3.0-3.2\nAPI 11-13"));
        allQuestions.add(new Question("Ice Cream Sandwitch", "4.0\nAPI 14-15"));
        allQuestions.add(new Question("Jelly Bean", "4.1-4.3\nAPI 16-18"));
        allQuestions.add(new Question("KitKat", "4.4\nAPI 19"));
        allQuestions.add(new Question("Lollipop", "5.0\nAPI 20-21"));

        allQuestions.add(new Question("1.0", "Apple Pie\nAPI 1"));
        allQuestions.add(new Question("1.1", "Banana Bread\nAPI 2"));
        allQuestions.add(new Question("1.5", "Cupcake\nAPI 3"));
        allQuestions.add(new Question("1.6", "Donut\nAPI 4"));
        allQuestions.add(new Question("2.0-2.1", "Eclair\nAPI 5-7"));
        allQuestions.add(new Question("2.2", "Froyo\nAPI 8"));
        allQuestions.add(new Question("2.3", "Gingerbread\nAPI 9-10"));
        allQuestions.add(new Question("3.0-3.2", "Honeycomb\nAPI 11-13"));
        allQuestions.add(new Question("4.0", "Ice Cream Sandwitch\nAPI 14-15"));
        allQuestions.add(new Question("4.1-4.3", "Jelly Bean\nAPI 16-18"));
        allQuestions.add(new Question("4.4", "KitKat\nAPI 19"));
        allQuestions.add(new Question("5.0", "Lollipop\nAPI 20-21"));

        roundNumber = 0;
        correctAnswersCount = 0;
        wrongAnswersCount = 0;
        roundQuestions = new ArrayList<>();
        answeredQuestions = new HashSet<>();
        statsFragment = new StatsFragment();
        answerFragment = new AnswerFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.statsPlaceholder, statsFragment)
                    .commit();
            getFragmentManager().beginTransaction()
                    .add(R.id.answerPlaceholder, answerFragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStats();
        startNewRound();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCorrectAnswer() {
        correctAnswersCount++;
        answeredQuestions.add(roundQuestions.get(questionIndex));
        updateStats();
        showNextQuestion();
    }

    @Override
    public void onWrongAnswer() {
        wrongAnswersCount++;
        updateStats();
        showNextQuestion();
    }

    private void updateStats() {
        statsFragment.setValues(allQuestions.size(), correctAnswersCount, wrongAnswersCount);
    }

    private void startNewRound() {
        roundNumber++;
        roundQuestions.clear();
        for (Question question : allQuestions) {
            if (!answeredQuestions.contains(question)) {
                roundQuestions.add(question);
            }
        }
        Collections.shuffle(roundQuestions);
        questionIndex = -1;
        Log.i(LOG_TAG, "Starting round " + roundNumber + ". Remaining " + roundQuestions.size() + " questions.");
        showNextQuestion();
    }

    private void showNextQuestion() {
        questionIndex++;
        if (questionIndex >= roundQuestions.size()) {
            if (answeredQuestions.size() == allQuestions.size()) {
                questionTextView.setText("THE END :)");
            } else {
                startNewRound();
            }
        } else {
            Question question = roundQuestions.get(questionIndex);
            questionTextView.setText(question.getQuestion());
            answerFragment.setAnswer(question.getAnswer());
        }
    }

}

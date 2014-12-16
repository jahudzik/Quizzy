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

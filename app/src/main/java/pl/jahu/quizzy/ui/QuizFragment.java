package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;

import java.util.*;

public class QuizFragment extends Fragment {

    private static final String TOUCH_TO_SEE_MESSAGE = "Touch to see the answer";

    private static final String LOG_TAG = "Quizzy_QuestionFragment";

    private OnFragmentInteractionListener listener;

    private TextView correctCountTextView;
    private TextView wrongCountTextView;
    private TextView questionTextView;

    private List<Question> questions;
    private final List<Question> roundQuestions;
    private final Set<Question> answeredQuestions;

    private int roundNumber;
    private int questionIndex;
    private int correctAnswersCount;
    private int wrongAnswersCount;

    private Button correctAnswerButton;
    private Button wrongAnswerButton;
    private TextView answerTextView;

    public QuizFragment() {
        questions = new ArrayList<>();
        roundQuestions = new ArrayList<>();
        answeredQuestions = new HashSet<>();
        roundNumber = 0;
        correctAnswersCount = 0;
        wrongAnswersCount = 0;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement QuizFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        questionTextView = (TextView) rootView.findViewById(R.id.questionTextView);

        answerTextView = (TextView) rootView.findViewById(R.id.answerTextView);
        answerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && answerTextView.getText().equals(TOUCH_TO_SEE_MESSAGE)) {
                    answerTextView.setText(roundQuestions.get(questionIndex).getAnswer());
                    correctAnswerButton.setEnabled(true);
                    wrongAnswerButton.setEnabled(true);
                }
                return true;
            }
        });
        answerTextView.setText(TOUCH_TO_SEE_MESSAGE);

        correctAnswerButton = (Button) rootView.findViewById(R.id.correctAnswerButton);
        correctAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctAnswersCount++;
                answeredQuestions.add(roundQuestions.get(questionIndex));
                roundQuestions.get(questionIndex).newAnswer(true);
                updateStats();
                showNextQuestion();
            }
        });

        wrongAnswerButton = (Button) rootView.findViewById(R.id.wrongAnswerButton);
        wrongAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongAnswersCount++;
                roundQuestions.get(questionIndex).newAnswer(false);
                updateStats();
                showNextQuestion();
            }
        });

        correctCountTextView = (TextView) rootView.findViewById(R.id.correctCountValue);
        wrongCountTextView = (TextView) rootView.findViewById(R.id.wrongCountValue);

        updateStats();
        startNewRound();
        return rootView;
    }

    private void updateStats() {
        int questionsLeft = questions.size() - correctAnswersCount;
        correctCountTextView.setText(((questionsLeft < 10) ? " " : "") + questionsLeft);
        wrongCountTextView.setText(String.valueOf(wrongAnswersCount));
    }

    private void startNewRound() {
        roundNumber++;
        roundQuestions.clear();
        for (Question question : questions) {
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
            if (answeredQuestions.size() == questions.size()) {
                listener.onQuizFinished(questions);
            } else {
                startNewRound();
            }
        } else {
            questionTextView.setText(roundQuestions.get(questionIndex).getQuestion());
            answerTextView.setText(TOUCH_TO_SEE_MESSAGE);
            correctAnswerButton.setEnabled(false);
            wrongAnswerButton.setEnabled(false);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onQuizFinished(List<Question> questions);
    }

}

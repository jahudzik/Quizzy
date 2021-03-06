package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;

import java.util.*;

public class QuizFragment extends Fragment {

    private static final String QUESTIONS_BUNDLE_KEY = "questions";
    private static final String INTEGER_VALUES_KEY = "integerValues";
    private static final String ROUND_QUESTIONS_KEY = "roundQuestions";
    private static final String ANSWERED_QUESTIONS_KEY = "answeredQuestions";
    private static final String ACTUAL_ANSWER_KEY = "actualAnswer";
    private static final String TOUCH_TO_SEE_MESSAGE = "Touch to see the answer";

    private static final String LOG_TAG = "Quizzy_QuestionFragment";

    private OnFragmentInteractionListener listener;

    private List<Question> questions;
    private List<Question> roundQuestions;
    private Set<Question> answeredQuestions;

    private int roundNumber;
    private int questionIndex;
    private int correctAnswersCount;
    private int wrongAnswersCount;

    private TextView questionsLeftNumberTextView;
    private TextView categoryTextView;
    private TextView questionTextView;
    private TextView answerTextView;

    private Button correctAnswerButton;
    private Button wrongAnswerButton;

    protected static QuizFragment newInstance(List<Question> chosenQuestions) {
        QuizFragment fragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(QUESTIONS_BUNDLE_KEY, new ArrayList<>(chosenQuestions));
        fragment.setArguments(bundle);
        return fragment;
    }

    public QuizFragment() {
        questions = new ArrayList<>();
        roundQuestions = new ArrayList<>();
        answeredQuestions = new HashSet<>();
        roundNumber = 0;
        correctAnswersCount = 0;
        wrongAnswersCount = 0;
        setHasOptionsMenu(true);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        this.questions = getArguments().getParcelableArrayList(QUESTIONS_BUNDLE_KEY);

        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionTextView = (TextView) rootView.findViewById(R.id.questionTextView);

        ScrollView answerScrollView = (ScrollView) rootView.findViewById(R.id.answerScrollView);
        answerScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && answerTextView.getText().equals(TOUCH_TO_SEE_MESSAGE)) {
                    answerTextView.setText(roundQuestions.get(questionIndex).getAnswer());
                    correctAnswerButton.setEnabled(true);
                    wrongAnswerButton.setEnabled(true);
                    return true;
                }
                return false;
            }
        });

        answerTextView = (TextView) rootView.findViewById(R.id.answerTextView);
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

        questionsLeftNumberTextView = (TextView) rootView.findViewById(R.id.questionsLeftNumberTextField);
        categoryTextView = (TextView) rootView.findViewById(R.id.categoryTextView);

        if (savedInstanceState != null) {
            // handling configuration changes
            int[] integerValues = savedInstanceState.getIntArray(INTEGER_VALUES_KEY);
            roundNumber = integerValues[0];
            questionIndex = integerValues[1];
            correctAnswersCount = integerValues[2];
            wrongAnswersCount = integerValues[3];
            roundQuestions = savedInstanceState.getParcelableArrayList(ROUND_QUESTIONS_KEY);
            answeredQuestions = new HashSet<>(savedInstanceState.<Question>getParcelableArrayList(ANSWERED_QUESTIONS_KEY));
            String actualAnswer = savedInstanceState.getString(ACTUAL_ANSWER_KEY);

            // set UI widgets
            Question actQuestion = roundQuestions.get(questionIndex);
            categoryTextView.setText(actQuestion.getCategory());
            questionTextView.setText(actQuestion.getQuestion());
            answerTextView.setText(actualAnswer);
            if (!actualAnswer.equals(TOUCH_TO_SEE_MESSAGE)) {
                correctAnswerButton.setEnabled(true);
                wrongAnswerButton.setEnabled(true);
            }
        } else {
            startNewRound();
        }
        updateStats();

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(INTEGER_VALUES_KEY, new int[]{roundNumber, questionIndex, correctAnswersCount, wrongAnswersCount});
        outState.putParcelableArrayList(ROUND_QUESTIONS_KEY, new ArrayList<>(roundQuestions));
        outState.putParcelableArrayList(ANSWERED_QUESTIONS_KEY, new ArrayList<>(answeredQuestions));
        outState.putString(ACTUAL_ANSWER_KEY, answerTextView.getText().toString());
    }

    private void updateStats() {
        int questionsLeft = questions.size() - answeredQuestions.size();
        questionsLeftNumberTextView.setText(((questionsLeft < 10) ? " " : "") + questionsLeft);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.quiz_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        Toast.makeText(getActivity(), "Round " + roundNumber, Toast.LENGTH_SHORT).show();
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
            Question newQuestion = roundQuestions.get(questionIndex);
            categoryTextView.setText(newQuestion.getCategory());
            questionTextView.setText(newQuestion.getQuestion());
            answerTextView.setText(TOUCH_TO_SEE_MESSAGE);
            correctAnswerButton.setEnabled(false);
            wrongAnswerButton.setEnabled(false);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onQuizFinished(List<Question> questions);
    }

    public List<Question> getQuestions() {
        return questions;
    }

}

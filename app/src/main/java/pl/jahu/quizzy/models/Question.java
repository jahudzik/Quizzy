package pl.jahu.quizzy.models;


import pl.jahu.quizzy.utils.Constants;

import java.util.Comparator;

public class Question {

    private final String id;

    private final String question;

    private final String answer;

    private final String category;

    private final int overallAnswers;

    private final int overallCorrectAnswers;

    private int quizAnswers;

    private int quizCorrectAnswers;

    public Question(String id, String question, String answer, String category, int overallAnswers, int overallCorrectAnswers) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.overallAnswers = overallAnswers;
        this.overallCorrectAnswers = overallCorrectAnswers;
        this.quizAnswers = 0;
        this.quizCorrectAnswers = 0;
    }

    public Question(int id, String question, String answer, String category, int overallAnswers, int overallCorrectAnswers) {
        this(String.valueOf(id), question, answer, category, overallAnswers, overallCorrectAnswers);
    }

    public Question(String question, String answer, String category, int overallAnswers, int overallCorrectAnswers) {
        this(null, question, answer, category, overallAnswers, overallCorrectAnswers);
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }

    public int getOverallAnswers() {
        return overallAnswers;
    }

    public int getOverallCorrectAnswers() {
        return overallCorrectAnswers;
    }

    public int getQuizAnswers() {
        return quizAnswers;
    }

    public int getQuizCorrectAnswers() {
        return quizCorrectAnswers;
    }

    /**
     * Returns difficult value - percentage of correct answers ever.
     * If there were no answers yet, returns 100.
     */
    public int getOverallDifficultValue() {
        return (overallAnswers != 0) ? (overallCorrectAnswers * 100) / overallAnswers : 100;
    }

    /**
     * Returns difficult value - percentage of correct answers in current quiz.
     * If there were no answers yet, returns 100.
     */
    public int getQuizDifficultValue() {
        return (quizAnswers != 0) ? (quizCorrectAnswers * 100) / quizAnswers : 100;
    }

    public boolean matchesLevel(int level) {
        int diffValue = getOverallDifficultValue();
        switch (level) {
            case Constants.DIFFICULTY_LEVEL_ALL:
                return true;
            case Constants.DIFFICULTY_LEVEL_BELOW_75:
                return (diffValue < 75);
            case Constants.DIFFICULTY_LEVEL_BELOW_50:
                return (diffValue < 50);
            case Constants.DIFFICULTY_LEVEL_BELOW_25:
                return (diffValue < 25);
            default:
                return false;
        }
    }

    public void newAnswer(boolean correct) {
        quizAnswers++;
        if (correct) {
            quizCorrectAnswers++;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Question) {
            Question other = (Question)obj;
            return (question.equals(other.question)
                    && answer.equals(other.answer)
                    && category.equals(other.category) );
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + question.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    public static class OverallDifficultValueComparator implements Comparator<Question> {
        @Override
        public int compare(Question first, Question second) {
            int difference = (first.getOverallDifficultValue() - second.getOverallDifficultValue());
            if (difference == 0) {
                return (first.overallAnswers - second.overallAnswers);
            }
            return difference;
        }
    }

    public static class CurrentDifficultValueComparator implements Comparator<Question> {

        @Override
        public int compare(Question first, Question second) {
            int difference = (first.getQuizDifficultValue() - second.getQuizDifficultValue());
            if (difference == 0) {
                return (first.quizAnswers - second.quizAnswers);
            }
            return difference;
        }
    }

}

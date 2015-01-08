package pl.jahu.quizzy.models;


import pl.jahu.quizzy.utils.Constants;

import java.util.Comparator;

public class Question {

    private final int id;

    private final String question;

    private final String answer;

    private final String category;

    private final int allAnswersCount;

    private final int correctAnswersCount;

    private int actAnswersCount;

    private int actCorrectAnswersCount;

    public Question(int id, String question, String answer, String category, int allAnswersCount, int correctAnswersCount) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.allAnswersCount = allAnswersCount;
        this.correctAnswersCount = correctAnswersCount;
        this.actAnswersCount = 0;
        this.actCorrectAnswersCount = 0;
    }

    public Question(String question, String answer, String category, int allAnswersCount, int correctAnswersCount) {
        this(-1, question, answer, category, allAnswersCount, correctAnswersCount);
    }

    public int getId() {
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

    public int getAllAnswersCount() {
        return allAnswersCount;
    }

    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }

    /**
     * Returns difficult value - percentage of correct answers ever.
     * If there were no answers yet, returns 100.
     */
    public int getDifficultValue() {
        return (allAnswersCount != 0) ? (correctAnswersCount * 100) / allAnswersCount : 100;
    }

    /**
     * Returns difficult value - percentage of correct answers in current quiz.
     * If there were no answers yet, returns 100.
     */
    public int getCurrentDifficultValue() {
        return (actAnswersCount != 0) ? (actCorrectAnswersCount * 100) / actAnswersCount : 100;
    }

    public boolean matchesLevel(int level) {
        int diffValue = getDifficultValue();
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
        actAnswersCount++;
        if (correct) {
            actCorrectAnswersCount++;
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

    static class OverallDifficultValueComparator implements Comparator<Question> {
        @Override
        public int compare(Question first, Question second) {
            int difference = (first.getDifficultValue() - second.getDifficultValue());
            if (difference == 0) {
                return (first.allAnswersCount - second.allAnswersCount);
            }
            return difference;
        }
    }

    static class CurrentDifficultValueComparator implements Comparator<Question> {

        @Override
        public int compare(Question first, Question second) {
            int difference = (first.getCurrentDifficultValue() - second.getCurrentDifficultValue());
            if (difference == 0) {
                return (first.actAnswersCount - second.actAnswersCount);
            }
            return difference;
        }
    }

}

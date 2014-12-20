package pl.jahu.quizzy.models;


import pl.jahu.quizzy.utils.Constants;

public class Question {

    private final int id;

    private final String question;

    private final String answer;

    private final String category;

    private int allAnswersCount;

    private int correctAnswersCount;

    public Question(int id, String question, String answer, String category, int allAnswersCount, int correctAnswersCount) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.allAnswersCount = allAnswersCount;
        this.correctAnswersCount = correctAnswersCount;
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

    public int getDifficultValue() {
        return (correctAnswersCount * 100) / allAnswersCount;
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

}

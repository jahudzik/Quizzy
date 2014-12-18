package pl.jahu.quizzy.models;


public class Question {

    private final String question;

    private final String answer;

    private final String category;

    private int allAnswersCount;

    private int correctAnswersCount;

    public Question(String question, String answer, String category, int allAnswersCount, int correctAnswersCount) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.allAnswersCount = allAnswersCount;
        this.correctAnswersCount = correctAnswersCount;
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

    public int getDifficultValue() {
        return correctAnswersCount / (allAnswersCount * 100);
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

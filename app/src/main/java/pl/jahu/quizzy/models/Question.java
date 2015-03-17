package pl.jahu.quizzy.models;


import android.os.Parcel;
import android.os.Parcelable;
import pl.jahu.quizzy.utils.Constants;

import java.util.Comparator;

public class Question implements Parcelable {

    public static QuestionCreator CREATOR = new QuestionCreator();

    private final int id;

    private final String question;

    private final String answer;

    private final String category;

    private final int overallAnswers;

    private final int overallCorrectAnswers;

    private int quizAnswers;

    private int quizCorrectAnswers;

    public Question(int id, String question, String answer, String category, int overallAnswers, int overallCorrectAnswers, int quizAnswers, int quizCorrectAnswers) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.overallAnswers = overallAnswers;
        this.overallCorrectAnswers = overallCorrectAnswers;
        this.quizAnswers = 0;
        this.quizCorrectAnswers = 0;
        this.quizAnswers = quizAnswers;
        this.quizCorrectAnswers = quizCorrectAnswers;
    }

    public Question(int id, String question, String answer, String category, int overallAnswers, int overallCorrectAnswers) {
        this(id, question, answer, category, overallAnswers, overallCorrectAnswers, 0, 0);
    }

    public Question(String question, String answer, String category, int overallAnswers, int overallCorrectAnswers) {
        this(-1, question, answer, category, overallAnswers, overallCorrectAnswers);
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
            case Constants.DIFFICULTY_LEVEL_UNANSWERED:
                return (overallAnswers == 0);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(category);
        dest.writeInt(overallAnswers);
        dest.writeInt(overallCorrectAnswers);
        dest.writeInt(quizAnswers);
        dest.writeInt(quizCorrectAnswers);
    }

    public static class QuestionCreator implements Parcelable.Creator<Question> {

        @Override
        public Question createFromParcel(Parcel source) {
            int id = source.readInt();
            String question = source.readString();
            String answer = source.readString();
            String category = source.readString();
            int overallAnswers = source.readInt();
            int overallCorrectAnswers = source.readInt();
            int quizAnswers = source.readInt();
            int quizCorrectAnswers = source.readInt();
            return new Question(id, question, answer, category, overallAnswers, overallCorrectAnswers, quizAnswers, quizCorrectAnswers);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }

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

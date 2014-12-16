package pl.jahu.quizzy.models;

/**
 * Quzzy
 * Created by jahudzik on 2014-12-15.
 */
public class Category {

    private final String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Category) {
            Category other = (Category)obj;
            return (name.equals(other.name));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        return result;
    }

}

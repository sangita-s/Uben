package generisches.lab.ben;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable{
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answerNr;

    private String difficulty;

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    public Question(String question, String option1, String option2, String option3,
                    int answerNr, String difficulty) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
        this.difficulty = difficulty;
    }

    public Question() {}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    //send b/w classes as bundles - like serialable
    @Override
    public int describeContents() {
        return 0;
    }

    protected Question(Parcel in){
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        answerNr = in.readInt();
        difficulty = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Same order as above
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeInt(answerNr);
        dest.writeString(difficulty);
    }

    public static String[] getAllDifficultyLevels(){
        return new String[]{
          DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD
        };
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}

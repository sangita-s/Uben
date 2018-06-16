package generisches.lab.ben;

import android.provider.BaseColumns;

public final class QuizContract {
    //Precaution to not create sub class or create na instance/object
    public QuizContract() {}

    public static class QuestionsTable implements BaseColumns{
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "questions";
        public static final String COLUMN_OPTION_1 = "option1";
        public static final String COLUMN_OPTION_2 = "option2";
        public static final String COLUMN_OPTION_3 = "option3";
        public static final String COLUMN_ANS_NR = "answer_nr";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        //public static final String _ID = "_id";
    }
}

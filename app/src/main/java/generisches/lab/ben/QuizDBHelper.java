package generisches.lab.ben;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import generisches.lab.ben.QuizContract.QuestionsTable;

public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyQuiz.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase db;

    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Just once till deletion of db
        this.db = db;
        final String sql_create_questions_table = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION_1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION_2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION_3 + " TEXT, " +
                QuestionsTable.COLUMN_ANS_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT" +
                ")";

        db.execSQL(sql_create_questions_table);
        fillQuestionsTable();
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("EASY: What is my name?", "Air", "Fire",
                "Earth", 3, Question.DIFFICULTY_EASY);
        addQuestion(q1);
        Question q2 = new Question("EASY: What is my name?", "Water", "Fire",
                "Earth", 3, Question.DIFFICULTY_EASY);
        addQuestion(q2);
        Question q3 = new Question("EASY: What is my name?", "Air", "Water",
                "Earth", 3, Question.DIFFICULTY_EASY);
        addQuestion(q3);
        Question q4 = new Question("MEDIUM: What is my name?", "Ether", "Fire",
                "Earth", 3, Question.DIFFICULTY_MEDIUM);
        addQuestion(q4);
        Question q5 = new Question("HARD: What is my name?", "Air", "Ether",
                "Earth", 3, Question.DIFFICULTY_HARD);
        addQuestion(q5);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION_1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION_2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION_3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANS_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Change Version number, then..
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
             Question question = new Question();
             question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
             question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_1)));
             question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_2)));
             question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_3)));
             question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANS_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
             questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
    public ArrayList<Question> getQuestions(String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION_3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANS_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}

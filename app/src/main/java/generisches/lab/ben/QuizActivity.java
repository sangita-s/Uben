package generisches.lab.ben;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";

    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvQuestionCount;
    private TextView tvcountdown;
    private TextView tvDifficulty;
    private RadioGroup radioGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button btnConfirmNext;

    //for On save instance state
    private static final String KEY_SCORE = "keyscore";
    private static final String KEY_Q_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_Q_LIST = "keyQuestionList";

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer mCountDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> mQuestionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_crt_score);
        tvQuestionCount = findViewById(R.id.tv_q_count);
        tvDifficulty = findViewById(R.id.tv_diff);
        tvcountdown = findViewById(R.id.tv_countdown);
        radioGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.rb_1);
        rb2 = findViewById(R.id.rb_2);
        rb3 = findViewById(R.id.rb_3);
        btnConfirmNext = findViewById(R.id.btn_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = tvcountdown.getTextColors();

        Intent i = getIntent();
        String diff = i.getStringExtra(StartActivity.EXTRA_DIFFICULTY);

        tvDifficulty.setText("Difficulty : " + diff);

        if(savedInstanceState == null) {
            QuizDBHelper lQuizDBHelper = new QuizDBHelper(this);
            //mQuestionList = lQuizDBHelper.getAllQuestions();
            mQuestionList = lQuizDBHelper.getQuestions(diff);
            questionCountTotal = mQuestionList.size();
            Collections.shuffle(mQuestionList);

            showNextQuestion();
        }
        else
        {
            mQuestionList = savedInstanceState.getParcelableArrayList(KEY_Q_LIST);
            if(mQuestionList == null)
                finish();
            questionCountTotal = mQuestionList.size();
            questionCounter = savedInstanceState.getInt(KEY_Q_COUNT);
            currentQuestion = mQuestionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered) {
                startCountDown();
            }
            else {
                updateCountDownText();
            }
        }

        btnConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()){
                        checkAnswer();
                    }
                    else{
                        Toast.makeText(QuizActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                    }
                }else
                    showNextQuestion();
            }
        });
    }

    private void checkAnswer() {
        answered = true;

        mCountDownTimer.cancel();

        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNr = radioGroup.indexOfChild(rbSelected) + 1;
        if(answerNr == currentQuestion.getAnswerNr()){
            score++;
            tvScore.setText("Score: "+score);
        }
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 3 is correct");
                break;
        }
        if(questionCounter < questionCountTotal){
            btnConfirmNext.setText("Next");
        }else{
            btnConfirmNext.setText("Finish");
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if(questionCounter < questionCountTotal){
            currentQuestion = mQuestionList.get(questionCounter);

            tvQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            questionCounter++;
            tvQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            btnConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }else
        {
            finishQuiz();
        }
    }

    private void startCountDown() {
        mCountDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int)(timeLeftInMillis/1000) / 60;
        int seconds = (int)(timeLeftInMillis/1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tvcountdown.setText(timeFormatted);

        if(timeLeftInMillis < 10000){
            tvcountdown.setTextColor(Color.RED);
        }else{
            tvcountdown.setTextColor(textColorDefaultCd);
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else
        {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_Q_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_Q_LIST, mQuestionList);
    }
}

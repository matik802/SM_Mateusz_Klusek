package com.example.sm_zadanie_1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private Question[] questions = new Question[]{
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, true),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, true)
    };
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private boolean answered = false;
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.SM_Zadanie_1.correctAnswer";
    private static final String KEY_CORRECT_ANSWERS = "correctAnswers";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown;
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                      new ActivityResultContracts.StartActivityForResult(),
                      new ActivityResultCallback<ActivityResult>() {
                          @Override
                          public void onActivityResult(ActivityResult activityResult) {
                                int result = activityResult.getResultCode();
                                Intent data = activityResult.getData();
                          }
                      }
            );
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
            correctAnswers = savedInstanceState.getInt(KEY_CORRECT_ANSWERS);
        }
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        promptButton = findViewById(R.id.prompt_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        }));
        falseButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        }));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questions.length;
                answerWasShown = false;
                setNextQuestion();
            }
        });
        promptButton.setOnClickListener((v) -> {
            Intent intent = new Intent (MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
            //activityResultLauncher.launch(intent);
        });
        setNextQuestion();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#019361"));
        actionBar.setBackgroundDrawable(colorDrawable);
        setTitle("Quiz");
        Log.d("onCreate!", "onCreate override!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart!", "onStart override!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause!", "onPause override!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume!", "onResume override!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop!", "onStop override!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy!", "onDestroy override!");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstanceState!", "onSaveInstance ovveride!");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
        outState.putInt(KEY_CORRECT_ANSWERS, correctAnswers);
    }

    protected void checkAnswerCorrectness(boolean userAnswer) {
        if (answered) {
            return;
        }
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
                ++correctAnswers;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        if (currentIndex == questions.length - 1) {
            Toast.makeText(this, this.getString(R.string.summary) + " " + correctAnswers + "/" + questions.length, Toast.LENGTH_SHORT).show();
            correctAnswers = 0;
        }
        answered = true;
    }

    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
        answered = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}
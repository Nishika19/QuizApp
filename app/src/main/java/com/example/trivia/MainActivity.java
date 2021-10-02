package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestionIndex=0;
    private int currentScore = 0;
    private int correctAns = 0;
    private int incorrectAns = 0;
    private Score score;
    private Prefs prefs;
    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        score = new Score();
        prefs = new Prefs(MainActivity.this);
        currentQuestionIndex = prefs.getLastIndex();
        binding.highestTextview.setText(MessageFormat.format("Highest Score: {0}", prefs.getHighestScore()));
//        overriding processFinished method
        questionList = new Repository().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                binding.questionTextview.setText(questionList.get(currentQuestionIndex)
                        .getAnswer());
                binding.questionNoTextview.setText(MessageFormat
                        .format("Question: {0}/{1}", currentQuestionIndex + 1, questionList.size()));
            }
        });

         binding.submitButton.setOnClickListener(v -> {
             Intent intent = new Intent(MainActivity.this,ShowScore.class);
             intent.putExtra("Incorrect",incorrectAns);
             intent.putExtra("Correct",correctAns);
             intent.putExtra("Current", score.getScore());
             intent.putExtra("highest", prefs.getHighestScore());
             startActivity(intent);
         });

         binding.restartButton.setOnClickListener(v -> {
             correctAns=0;
             incorrectAns=0;
             currentScore = 0;
             score.setScore(currentScore);
             binding.currentTextview.setText(MessageFormat.format("Current Score: {0}", score.getScore()));
            currentQuestionIndex = 0;
            binding.questionTextview.setText(questionList.get(0).getAnswer());
            binding.questionNoTextview.setText(MessageFormat.format("Question: {0}/{1}", currentQuestionIndex + 1, questionList.size()));

        });
         binding.nextButton.setOnClickListener(v -> {
            nextQuestion();
        });
         binding.trueButton.setOnClickListener(v -> {
                checkAnswer(true);
                updateQuestion();
         });
         binding.falseButton.setOnClickListener(v -> {
                checkAnswer(false);
                updateQuestion();
         });
    }

    private void checkAnswer(Boolean answerGiven) {
        boolean answer = questionList.get(currentQuestionIndex).getAnswerTrue();
        if(answerGiven==answer){
            correctAnswer();
            fadeAnimation();
            incrementCurrentScore();
            Toast.makeText(MainActivity.this,R.string.correct,Toast.LENGTH_SHORT)
                    .show();
        }else{
            incorrectAnswer();
            decrementScore();
            shakeAnimation();
            Toast.makeText(MainActivity.this,R.string.incorrect,Toast.LENGTH_SHORT)
                    .show();
        }
        nextQuestion();

    }

    private void incorrectAnswer(){
        incorrectAns++;
    }

    private void correctAnswer(){
        correctAns++;
    }

    private void nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
        updateText();
    }

    private void incrementCurrentScore() {
        currentScore++;
        score.setScore(currentScore);
        binding.currentTextview.setText(MessageFormat.format("Current Score: {0}", score.getScore()));
    }

    private void decrementScore(){
        if(currentScore!=0){
            currentScore--;
            score.setScore(currentScore);
        }
        binding.currentTextview.setText(MessageFormat.format("Current Score: {0}", score.getScore()));
    }

    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(4.0f,0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.CYAN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void updateText() {
        String update = "Question: "+ (currentQuestionIndex+1)+"/"+questionList.size();
        binding.questionNoTextview.setText(update);
    }

    private void updateQuestion() {
    String question = questionList.get(currentQuestionIndex).getAnswer();
    binding.questionTextview.setText(question);
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.continue_quiz(currentQuestionIndex);
        super.onPause();
    }
}
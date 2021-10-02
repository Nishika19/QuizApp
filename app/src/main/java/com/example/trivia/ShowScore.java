package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;

public class ShowScore extends AppCompatActivity {
    private TextView correctNumber;
    private TextView incorrectNumber;
    private ImageButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        correctNumber = findViewById(R.id.correct_textview);
        incorrectNumber = findViewById(R.id.incorrect_textview);
        shareButton = findViewById(R.id.imageButton);

        Bundle details = getIntent().getExtras();
        int current = details.getInt("Current");
        int highest = details.getInt("highest");
         if(details!=null) {
            correctNumber.setText(MessageFormat.format("Correct: {0}", details.getInt("Correct")));
            incorrectNumber.setText(MessageFormat.format("Incorrect: {0}", details.getInt("Incorrect")));
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Current Score: "+ current + "\n" +"Highest Score: "+ highest);
                startActivity(intent);
            }
        });

    }
}
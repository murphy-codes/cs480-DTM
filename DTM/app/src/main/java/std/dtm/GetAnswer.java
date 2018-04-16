/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/15/2018
Purpose: GetAnswer Displays the movie that the app believes to be the answer to the question and the poster if available,
    Main Menu Button returns user to MainActivity
 */
package std.dtm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static std.dtm.AskQuestion.EXTRA_MESSAGE;

public class GetAnswer extends AppCompatActivity {

    //returns user to MainActivity
    private Button endButton;
    private String guess;
    private TextView answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_answer);

        //get this intent to unpackage the guess from the user
        Intent thisIntent = getIntent();
        guess = thisIntent.getStringExtra(EXTRA_MESSAGE);

        answerTextView = (TextView) findViewById(R.id.answerTextview);

        if(guess.isEmpty()){
            guess = "Too bad you gave up!";
        }

        answerTextView.setText(guess);

        endButton = (Button) findViewById(R.id.endbutton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    //clear activity stack and go to MainActivity
    private void goToMainMenu() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

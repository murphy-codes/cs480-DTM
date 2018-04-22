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
//ad stuff
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static std.dtm.AskQuestion.EXTRA_MESSAGE;

public class GetAnswer extends AppCompatActivity {

    //returns user to MainActivity
    private Button endButton;
    private String guess;
    private String displayResultString;
    private TextView answerTextView;
    //points to be earned if game is won
    private int earned=100;
    private TextView displayTextView;

    //ad stuff
    private String admob_app_ID;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_answer);

        //get this intent to unpackage the guess from the user
        Intent thisIntent = getIntent();
        guess = thisIntent.getStringExtra(EXTRA_MESSAGE);

        String answer = GetClues.currentMovie.getTitle();

        answerTextView = (TextView) findViewById(R.id.answerTextview);
        displayTextView = (TextView) findViewById(R.id.displaytextview);

        //if the guess string is contained in the answer string, and is at least one fourth the length of the answer, you are correct
        if(guess.isEmpty()){
            displayResultString = "Too bad you gave up!\n The answer is: "+answer;
        } else if(answer.toLowerCase().contains(guess.toLowerCase()) && guess.length() > answer.length() /4){
            displayResultString = "YOU WON!\nIt was "+answer+"!!!\nYou earned $"+earned;
            MainActivity.user.addBalance(earned);
        } else {
            displayResultString = "Oh, actually we thought it was "+answer+".";
        }

        //displayTextView.setText(MainActivity.user.getDisplayString());

        answerTextView.setText(displayResultString);

        endButton = (Button) findViewById(R.id.endbutton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });

        //ad stuff
        //initilize the ads with our unit id.
        admob_app_ID = getString(R.string.admob_app_ID);
        MobileAds.initialize(this, admob_app_ID);
        //populate the adspace with an ad.
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //TODO: this line to be removed for production release
                .build();
        mAdView.loadAd(adRequest);

    }

    //clear activity stack and go to MainActivity
    private void goToMainMenu() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

/*
Authors: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/28/2018
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
//poster
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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

    //poster
    private ImageView posterIMG;
    private String posterURL;

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
        posterURL = GetClues.currentMovie.getPoster();

        answerTextView = (TextView) findViewById(R.id.answerTextview);
        displayTextView = (TextView) findViewById(R.id.displaytextview);
        posterIMG = (ImageView) findViewById(R.id.imageViewPoster);

        //if the guess string is contained in the answer string, and is at least one fourth the length of the answer, you are correct
        if(guess.isEmpty()){
            displayResultString = "Too bad you gave up!\n The answer is: "+answer;
        } else if(answer.toLowerCase().contains(guess.toLowerCase()) && guess.length() > answer.length() /4){
            displayResultString = "YOU WON!\nIt was "+answer+"!!!";
            //If free play is not set, you win $100
            if(!MainActivity.settings.isFreePlaySet()) {
                MainActivity.user.addBalance(earned);
                displayResultString += "\nYou earned $" + earned;
            }
        } else {
            displayResultString = "Oh, actually we thought it was "+answer+".";
        }

        new DownloadImageTask(posterIMG).execute();
        //android:visibility="gone"
        //posterIMG.setVisibility(View.VISIBLE);

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
        final Intent intent = new Intent(this, AskQuestion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //attempt to create/load an image via our 'Poster URL'
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        //This class is adapted from code presented by user Satheeshkumar Somu via stackoverflow
        //https://stackoverflow.com/a/34354709
        ImageView posterIMG;

        private DownloadImageTask(ImageView posterIMG) {
            this.posterIMG = posterIMG;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap poster = null;
            try {
                InputStream in = new java.net.URL(posterURL).openStream();
                poster = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return poster;
        }

        protected void onPostExecute(Bitmap result) {
            posterIMG.setImageBitmap(result);
        }
    }
}

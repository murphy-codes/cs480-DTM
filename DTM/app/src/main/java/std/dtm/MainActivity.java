/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/30/2018
Purpose: MainActivity is the main menu for the DTM application
    DTM is the Don't Tell Me game where you pose a question about a movie, the app finds it for you and presents clues
    Reclaim your outsourced memory, ask, don't tell me.

    'Movie' button launches AskQuestion
 */
package std.dtm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //launch Ask question when you play
    private ImageButton playButtonMovies;
    private ImageButton settingsButton;
    private TextView displayTextView;
    public static GameSettings settings;
    public static User user;

    //permissions
    private static final int PERMISSION_REQUEST_ALL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new GameSettings();
        user = new User("L33T69GAMER69");

        displayTextView = (TextView) findViewById(R.id.displaytextview);
        displayTextView.setText(user.getDisplayString());

        playButtonMovies = (ImageButton) findViewById(R.id.playbuttonMovies);
        settingsButton = (ImageButton) findViewById(R.id.settingsbutton);

        playButtonMovies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                checkPermission();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });
    }

    private void goToSettings(){
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }

    private void goToAskQuestion(){
        Intent askQuestionIntent = new Intent(this, AskQuestion.class);
        startActivity(askQuestionIntent);

    }

    //before we launch, check we have permissions.
    private void checkPermission() {
        // Check if the AUDIO and INTERNET permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start DTM!
            goToAskQuestion();
        }
        else {
            // Permission is missing and must be requested.
            requestPermission();
        }
    }

    //Make new Permission request
    private void requestPermission() {
        // if permission not granted, show error.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET) )
        {
            Toast.makeText(getApplicationContext(), "permission not granted!", Toast.LENGTH_LONG).show();

        }
        else {
            // Request the permission. Result received in onRequestPermissionsResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET}, PERMISSION_REQUEST_ALL);
        }
    }

    //handle the request for permission: start or deny.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_ALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start.
                goToAskQuestion();
            } else {
                // Permission request was denied.
                Toast.makeText(getApplicationContext(), "permission for camera was denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

}//end mainactivity

//store settings information for the play session
class GameSettings {
    private boolean timer;
    private boolean freePlay;

    GameSettings() {
        timer = true;
        freePlay = false;
    }

    public boolean isTimerSet() {
        return timer;
    }

    public boolean isFreePlaySet() {
        return freePlay;
    }

    public void setTimer(boolean timer){
        this.timer = timer;
    }

    public void setFreePlay(boolean freePlay) {
        this.freePlay = freePlay;
    }
}

//store user information for the play session
class User{
    private int bankBalance;
    private String username;

    User(String username){
        this.bankBalance = 500;
        this.username = username;
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setBankBalance(int newBalance) {
        this.bankBalance = newBalance;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getDisplayString() {
        return "Username: "+username+"   Bank Balance: $"+bankBalance;
    }

    public void subtractBalance(int spent){
        bankBalance-=spent;
    }

    public void addBalance(int earned) {
        bankBalance+= earned;
    }
}

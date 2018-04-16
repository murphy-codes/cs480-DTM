/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/15/2018
Purpose: MainActivity is the main menu for the DTM application
    DTM is the Don't Tell Me game where you pose a question about a movie, the app finds it for you and presents clues
    Reclaim your outsourced memory, ask, don't tell me.

    PLAY! button launches AskQuestion
 */
package std.dtm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //launch Ask question when you play
    private Button playButton;
    private ImageButton settingsButton;
    private TextView displayTextView;
    public static GameSettings settings;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new GameSettings();
        user = new User("L33T69GAMER69");

        displayTextView = (TextView) findViewById(R.id.displaytextview);
        displayTextView.setText(user.getDisplayString());

        playButton = (Button) findViewById(R.id.playbutton);
        settingsButton = (ImageButton) findViewById(R.id.settingsbutton);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                goToAskQuestion();
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
}

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

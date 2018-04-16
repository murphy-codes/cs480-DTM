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

public class MainActivity extends AppCompatActivity {

    //launch Ask question when you play
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playbutton);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                goToAskQuestion();
            }
        });
    }

    private void goToAskQuestion(){
        Intent askQuestionIntent = new Intent(this, AskQuestion.class);
        startActivity(askQuestionIntent);

    }
}

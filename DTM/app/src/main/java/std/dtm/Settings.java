/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/30/2018
Purpose: Settings the view with which users select their settings
 */
package std.dtm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    private Switch freeplayswitch;
    private Switch timerswitch;
    private ImageButton mainMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        freeplayswitch = (Switch) findViewById(R.id.freeplayswitch);
        timerswitch = (Switch) findViewById(R.id.timerswitch);
        mainMenuButton = (ImageButton) findViewById(R.id.buttonMainMenu);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });

        if(MainActivity.settings.isFreePlaySet()) {
            freeplayswitch.setChecked(true);
        }

        if(MainActivity.settings.isTimerSet()) {
            timerswitch.setChecked(true);
        }

        freeplayswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.settings.setFreePlay(freeplayswitch.isChecked());
            }
        });

        timerswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.settings.setTimer(timerswitch.isChecked());
            }
        });
    }

    //set onclick Listener to return to MainActivity
    private void goToMainMenu() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

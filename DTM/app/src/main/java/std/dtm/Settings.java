package std.dtm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    private Switch freeplayswitch;
    private Switch timerswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        freeplayswitch = (Switch) findViewById(R.id.freeplayswitch);
        timerswitch = (Switch) findViewById(R.id.timerswitch);

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
}

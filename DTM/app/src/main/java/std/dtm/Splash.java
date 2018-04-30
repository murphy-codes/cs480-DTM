/*
Authors: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/28/2018
Purpose: Splash displays the DTM (splash) logo for a short period of time, before transitioning to MainActivity
 */
package std.dtm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private ImageView splashLogo;
    private ImageView developedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLogo = (ImageView) findViewById(R.id.imageViewSplashLogo);
        developedBy = (ImageView) findViewById(R.id.imageViewDevelopedBy);
        Animation fadeInLogo = AnimationUtils.loadAnimation(this, R.anim.splash_transition_in);
        Animation fadeInDevBy = AnimationUtils.loadAnimation(this, R.anim.dev_by_transition_in);
        //final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.splash_transition_out);
        // For implementing a fade out to follow the fade in, later.
        // Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.splash_transition_out);
        splashLogo.startAnimation(fadeInLogo);
        developedBy.startAnimation(fadeInDevBy);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            public void run (){
                try {
                    //sleep(4000);
                    //splashLogo.startAnimation(fadeOut);
                    //developedBy.startAnimation(fadeOut);
                    //sleep(1500);
                    sleep(5500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}

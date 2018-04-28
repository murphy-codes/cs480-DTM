/*
Authors: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/28/2018
Purpose: GetAnswer Displays the movie that the DTM logo for a short period of time, before transitioning to MainActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLogo = (ImageView) findViewById(R.id.imageViewSplashLogo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_transition_in);
        // For implementing a fade out to follow the fade in, later.
        // Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.splash_transition_out);
        splashLogo.startAnimation(fadeIn);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(4500);
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

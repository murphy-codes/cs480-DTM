/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/15/2018
Purpose: AskQuestion can launch a microphone Intent to get voice input from user, then the DTM button will
    launch GetClues passing the stringified user input.
 */
package std.dtm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class AskQuestion extends AppCompatActivity {

    //micButton launches Intent to get voice input and turn into a string.
    private Button micButton;
    //dtmButton launches PresentClues
    private Button dtmButton;
    //askTextView holds instructions for user until voice input overrides them
    private TextView askTextView;
    //code for speech input
    private final int REQ_CODE_SPEECH_INPUT = 99;
    //string waiting to hold user input
    private String userInput;
    //path to message variable
    public static final String EXTRA_MESSAGE = "std.dtm.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        micButton = (Button) findViewById(R.id.micbutton);
        dtmButton = (Button) findViewById(R.id.dtmbutton);
        askTextView = (TextView) findViewById(R.id.asktextview);

        //initially userInput is empty, so we know not to launch dtm until user asks a question
        userInput = "";

        micButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });

        dtmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToClues();
            }
        });
    }

    //place the current guess into an intent and then launch GetClues
    private void goToClues() {
        Intent goToClueIntent = new Intent(this, GetClues.class);
        if(!userInput.isEmpty()){
            goToClueIntent.putExtra(EXTRA_MESSAGE, userInput);
            startActivity(goToClueIntent);
        } else {
            Toast.makeText(this, "you must first ask a question", Toast.LENGTH_SHORT).show();
        }

    }

    //launches voice input activity
    private void askSpeechInput() {
        //Sets up intent and starts activity for speech to text
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Listening...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receives results of speech to text and puts them in voiceInput TextView
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //set userInput, so we know we can launch DTM
                    userInput = result.get(0);
                    askTextView.setText(userInput);
                }
                break;
            }

        }
    }
}

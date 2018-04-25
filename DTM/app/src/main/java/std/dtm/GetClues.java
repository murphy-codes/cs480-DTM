/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/15/2018
Purpose: GetClues launches PresentClue activity on clue button press, and can cycle through movies the clues are dependant on,
    as well as launch GetAnswer or GiveUp.
 */
package std.dtm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static std.dtm.AskQuestion.EXTRA_MESSAGE;

public class GetClues extends AppCompatActivity {

    private Button nextMovieButton;
    private Button submitButton;
    private Button guessButton;
    private Button clueTwoButton;
    private Button clueThreeButton;
    private Button clueFourButton;
    private Button clueFiveButton;
    private Button clueSixButton;
    private Button clueOneButton;
    private TextView guessTextView;
    private String searchTerm;
    private String userInput;
    private String searchFor;
    private String search_api_key;
    //movieGuessiter is the array index of movie responses from google
    private int movieGuessIter;
    //responseArray stores google search results
    private JSONArray responseArray;
    public static movieResult currentMovie;
    private final int REQ_CODE_SPEECH_INPUT = 99;
    //Base URL of our API call
    static final String API_URL = "http://www.omdbapi.com/?";
    //If you're reusing our code, please use your own API key.
    private String omdb_api_key;
    //display username and bank balance
    private TextView displayTextView;
    //displays current size of prize pool
    private TextView prizeTextView;

    public static int clueOneValue = 100;
    public static int clueTwoValue = 75;
    public static int clueThreeValue = 50;
    public static int clueFourValue = 25;
    public static int clueFiveValue = 10;
    public static int clueSixValue = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_clues);

        guessTextView = (TextView) findViewById(R.id.guesstextview);
        displayTextView = (TextView) findViewById(R.id.displaytextview);


        displayTextView.setText(MainActivity.user.getDisplayString());

        //initially we want the first movie result from google
        movieGuessIter=0;

        //if freeplay enabled, disable cost
        if(MainActivity.settings.isFreePlaySet()){
            clueOneValue = 0;
            clueTwoValue = 0;
            clueThreeValue = 0;
            clueFourValue = 0;
            clueFiveValue = 0;
            clueSixValue = 0;
        }

        //api keys
        omdb_api_key = "&apikey="+getString(R.string.omdb_search_api_key);
        search_api_key = getString(R.string.google_search_api_key);

        //get this intent to unpackage the guess from the user
        Intent thisIntent = getIntent();
        searchTerm = thisIntent.getStringExtra(EXTRA_MESSAGE);

        googleSearch(searchTerm);

        //userInput is initially empty, so submitting will be giving up when empty, and guessing when not empty
        userInput = "";

        //find buttons
        nextMovieButton = (Button) findViewById(R.id.nextmoviebutton);
        submitButton = (Button) findViewById(R.id.submitbutton);
        guessButton = (Button) findViewById(R.id.guessmoviebutton);
        clueOneButton = (Button) findViewById(R.id.clueonebutton);
        clueTwoButton = (Button) findViewById(R.id.cluetwobutton);
        clueThreeButton = (Button) findViewById(R.id.cluethreebutton);
        clueFourButton = (Button) findViewById(R.id.cluefourbutton);
        clueFiveButton = (Button) findViewById(R.id.cluefivebutton);
        clueSixButton = (Button) findViewById(R.id.cluesixbutton);

        //disable clue buttons until their textviews have content
        clueOneButton.setEnabled(false);
        clueTwoButton.setEnabled(false);
        clueThreeButton.setEnabled(false);
        clueFourButton.setEnabled(false);
        clueFiveButton.setEnabled(false);
        clueSixButton.setEnabled(false);

        clueOneButton.setText("Summary $"+clueOneValue);
        clueTwoButton.setText("Cast $"+clueTwoValue);
        clueThreeButton.setText("Writer/Director $"+clueThreeValue);
        clueFourButton.setText("Production/Release $"+clueFourValue);
        clueFiveButton.setText("Genre/Rating $"+clueFiveValue);
        clueSixButton.setText("Awards $"+clueSixValue);

        //set onclick Listeners for buttons
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAnswer();
            }
        });

        clueOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueOneButton.setEnabled(false);
                }
                presentClue("1");
                MainActivity.user.subtractBalance(clueOneValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });
        clueTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueTwoButton.setEnabled(false);
                }
                presentClue("2");
                MainActivity.user.subtractBalance(clueTwoValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });
        clueThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueThreeButton.setEnabled(false);
                }
                presentClue("3");
                MainActivity.user.subtractBalance(clueThreeValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });
        clueFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueFourButton.setEnabled(false);
                }
                presentClue("4");
                MainActivity.user.subtractBalance(clueFourValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });
        clueFiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueFiveButton.setEnabled(false);
                }
                presentClue("5");
                MainActivity.user.subtractBalance(clueFiveValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });
        clueSixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.settings.isFreePlaySet()){
                    clueSixButton.setEnabled(false);
                }
                presentClue("6");
                MainActivity.user.subtractBalance(clueSixValue);
                displayTextView.setText(MainActivity.user.getDisplayString());
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });

        nextMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOmdbApi();
            }
        });
    }

    //place answer in intent and go to GetAnswer
    private void goToAnswer() {
        Intent goToAnswerIntent = new Intent(this, GetAnswer.class);
        goToAnswerIntent.putExtra(EXTRA_MESSAGE, userInput);
        startActivity(goToAnswerIntent);
    }

    //launch presentClue activity
    private void presentClue(String btnNumber) {
        Intent presentClueIntent = new Intent(this, PresentClue.class);
        presentClueIntent.putExtra(EXTRA_MESSAGE, btnNumber);
        startActivity(presentClueIntent);
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
                    guessTextView.setText("Current Guess: "+userInput);
                    //change submitButton text from "give up" to "submit" when an answer is provided
                    submitButton.setText("Submit");
                    goToAnswer();
                }
                break;
            }

        }
    }

    void googleSearch(String searchTerm ) {
        //instance the requestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        //grab the API key from secret.xml
        String url = "https://www.googleapis.com/customsearch/v1?key=" + search_api_key + "&cx=015559890765402091894:0yoxulceyae&q="+ searchTerm;

        //request a string response from provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //here we do our work to convert to a json array, then walk it and grab the titles.
                        try {
                            //convert json object into json_array.
                            responseArray = response.getJSONArray("items");
                            //get imdb information with current top result from google search
                            runOmdbApi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                String errorString = "No Response";
                guessTextView.setText(errorString);
            }
        });
        //fire request
        queue.add(jsonObjectRequest);
    }

    void runOmdbApi() {
        //disable buttons while changing the text for each clue
        clueOneButton.setEnabled(false);
        clueTwoButton.setEnabled(false);
        clueThreeButton.setEnabled(false);
        clueFourButton.setEnabled(false);
        clueFiveButton.setEnabled(false);
        clueSixButton.setEnabled(false);
        try{
            //instance the results we want to return.
            JSONObject jsonobj = responseArray.getJSONObject(movieGuessIter);
            String gMovieTitle = jsonobj.getString("title");
            //int movieTitleLength = movieTitle.length();
            //String releaseYear = movieTitle.substring(movieTitleLength - 12, movieTitleLength - 8);
            //movieTitle = movieTitle.substring(0, movieTitleLength - 14);
            String movieTitle = validateTitle(gMovieTitle);

            int movieTitleLength = movieTitle.length();

            for (int i = 0; i < movieTitleLength; i++) {
                if (movieTitle.substring(i, i + 1).equals(" ")) {
                    movieTitle = movieTitle.substring(0, i) + '+' + movieTitle.substring(i + 1, movieTitleLength);
                }
            }
            //create our search string, w/ our apikey included & the movie title
            //searchFor = API_URL + omdb_api_key + "&t=" + movieTitle + "&y=" + releaseYear; //keeping this here to re-implement release year
            searchFor = API_URL + omdb_api_key + "&t=" + movieTitle;

            new RetrieveFeedTask().execute();
            movieGuessIter++;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(searchFor);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR 1", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            //guessTextView.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

            try {
                JSONObject movieObject = new JSONObject(response);
                String title = movieObject.getString("Title");
                String rated = movieObject.getString("Rated");
                String released = movieObject.getString("Released");
                String runtime = movieObject.getString("Runtime");
                String genre = movieObject.getString("Genre");
                String director = movieObject.getString("Director");
                String writer = movieObject.getString("Writer");
                String actors = movieObject.getString("Actors");
                String plot = movieObject.getString("Plot");
                String awards = movieObject.getString("Awards");
                String poster = movieObject.getString("Poster");
                String production = movieObject.getString("Production");

                //set the currentMovie to our omdb result
                currentMovie = new movieResult(title, plot, writer, director, actors, genre,released, production, poster, awards, rated, runtime);
                clueOneButton.setEnabled(true);
                clueTwoButton.setEnabled(true);
                clueThreeButton.setEnabled(true);
                clueFourButton.setEnabled(true);
                clueFiveButton.setEnabled(true);
                clueSixButton.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String validateTitle(String movieTitle) {
        //clean our movie titles of everything after title
        boolean keepSearching = true;
        int i = 1;
        int end = movieTitle.length();
        boolean chop = false;
        //search for parentheses after the 1st char
        while (keepSearching) {
            if (i >= end-1) { keepSearching = false; } else {
                if (!movieTitle.substring(i, i + 1).equals("(")) { i++; } else { keepSearching = false; chop = true;}
            }
        }
        if (chop) { movieTitle = movieTitle.substring(0, i-1); }
        return movieTitle;
    }
}

class movieResult {
    //save our results
    //public ArrayList<String> arrayTitleResults = new ArrayList<String>();
    private String title;
    private String summary;
    private String writtenBy;
    private String directedBy;
    private String genre;
    private String releaseDate;
    private String actors;
    private String production;
    private String poster;
    private String awards;
    private String rated;
    private String runtime;

    public movieResult(String title, String summary, String writtenBy, String directedBy, String actors,
                       String genre, String releaseDate, String production, String poster, String awards, String rated, String runtime) {
        this.title = title;
        this.summary = summary;
        this.writtenBy = writtenBy;
        this.directedBy = directedBy;
        this.actors = actors;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.production = production;
        this.poster = poster;
        this.awards = awards;
        this.rated = rated;
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public String getDirectedBy() {
        return writtenBy;
    }

    public String getActors() {
        return actors;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getProduction() {
        return production;
    }

    public String getPoster() {
        return poster;
    }

    public String getAwards() {
        return awards;
    }

    public String getRated() {
        return rated;
    }

    public String getRuntime() {
        return runtime;
    }
}

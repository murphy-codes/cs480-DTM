package com.example.tmx42.omdb_api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText inputTitle;
    EditText inputYear;
    ProgressBar progressBar;
    TextView message;
    Button submit;
    TextView results;

    //Base URL of our API call
    static final String API_URL = "http://www.omdbapi.com/?";
    //If you're reusing our code, please use your own API key.
    static final String API_KEY = "apikey=########";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTitle = (EditText) findViewById(R.id.editTextTitle);
        inputYear = (EditText) findViewById(R.id.editTextYear);
        message = (TextView) findViewById(R.id.textViewMessage);
        results = (TextView) findViewById(R.id.textViewResults);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        submit = (Button) findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    new RetrieveFeedTask().execute();
                }
            }
        });
    }

    public boolean validateInput() {
        //------------------ BEGIN VALIDATION ------------------//
        String searchFor = inputTitle.getText().toString();
        String searchYear = inputYear.getText().toString();
        String titleMssg = "";
        String yearMssg = "";
        boolean year = false;
        boolean validated = false;

        if (searchFor.equals("")) {
            //Check that user has at least attempted to input a movie title.
            //Beyond that we are naively trusting our users to correctly enter movie titles
            titleMssg = "Enter a movie title. ";
        }
        if (searchYear.equals("")) {
            //Do nothing, this is simply a logic catch because we want the year input to be optional
        } else {
            //If user attempted to input a year, check that is is between 1888-2028.
            if (!searchYear.matches("^[0-9]{4}$")) {
                //This rejects all input that is not a 4-digit number
                yearMssg = "Check year format. ";
            } else {
                //Year is between 1000-9999. Let's narrow that down to between 1888-2028
                //1888 for the Roundhay Garden Scene, 2028 because I figure 10 years is enough forward time
                if (Integer.parseInt(searchYear) > 2028) {
                    //year is too late
                    yearMssg = "Choose an earlier year. ";
                } else if (Integer.parseInt(searchYear) < 1888) {
                    //year is too early
                    yearMssg = "Choose a later year. ";
                } else {
                    //all seems well with our user's year
                    year = true;
                }
            }
        }
        if (!titleMssg.equals("") || !yearMssg.equals("")) {
            //If either of the above checks failed, output an error message.
            message.setText(titleMssg + yearMssg + "Try again.");
            results.setText("");
        } else {
            //All seems well, do not display (an error) message
            message.setText("");
            //replace spaces (' ') with '+'
            for (int i = 0; i < searchFor.length(); i++) {
                if (searchFor.substring(i, i + 1).equals(" ")) {
                    searchFor = searchFor.substring(0, i) + '+' + searchFor.substring(i + 1, searchFor.length());
                }
            }
            //create our search string, w/ our apikey included & the movie title
            searchFor = API_URL + API_KEY + "&t=" + searchFor;
            if (year) {
                //The user (correctly) inputted a year, so append that to our search string
                searchFor = searchFor + "&y=" + searchYear;
            }
            //display our search string, for debug purposes
            results.setText(searchFor);
            validated = true;
        }
        return validated;
        //------------------ END VALIDATION ------------------//
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {

            String searchFor = results.getText().toString();

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
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            results.setText(response);
            // TODO: check this.exception
            // TODO: do something with the feed

                try {
                    //JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                      //String requestID = object.getString("requestId");
                      //int likelihood = object.getInt("likelihood");
                      //JSONArray photos = object.getJSONArray("photos");
                    JSONObject movieObject = new JSONObject(response);
                    String title = movieObject.getString("Title");
                    String year = movieObject.getString("Year");
                    String rated = movieObject.getString("Rated");
                    String released = movieObject.getString("Released");
                    String runtime = movieObject.getString("Runtime");
                    String genre = movieObject.getString("Genre");
                    String director = movieObject.getString("Director");
                    String writer = movieObject.getString("Writer");
                    String actors = movieObject.getString("Actors");
                    String plot = movieObject.getString("Plot");
                    String language = movieObject.getString("Language");
                    String country = movieObject.getString("Country");
                    String awards = movieObject.getString("Awards");
                    String poster = movieObject.getString("Poster");
                    //String ratings = movieObject.getString("Ratings");
                    //String r0 = movieObject.getString("0");
                    //String r1 = movieObject.getString("1");
                    //String r2 = movieObject.getString("2");
                    String metascore = movieObject.getString("Metascore");
                    String imdbRating = movieObject.getString("imdbRating");
                    String imdbVotes = movieObject.getString("imdbVotes");
                    String imdbID = movieObject.getString("imdbID");
                    String type = movieObject.getString("Type");
                    String DVD = movieObject.getString("DVD");
                    String boxOffice = movieObject.getString("BoxOffice");
                    String production = movieObject.getString("Production");
                    String website = movieObject.getString("Website");
                    String responseTF = movieObject.getString("Response");

                    results.setText("Title: " + title + "\n" +
                            "Year: " + year + "\n" +
                            "Rated: " + rated + "\n" +
                            "Released: " + released + "\n" +
                            "Runtime: " + runtime + "\n" +
                            "Genre: " + genre + "\n" +
                            "Director: " + director + "\n" +
                            "Actors: " + actors + "\n" +
                            "Plot: " + plot + "\n" +
                            "Language: " + language + "\n" +
                            "Country: " + country + "\n" +
                            "Awards: " + awards + "\n" +
                            "Poster: " + poster + "\n" +
                            "Metascore: " + metascore + "\n" +
                            "imdbRating: " + imdbRating + "\n" +
                            "imdbVotes: " + imdbVotes + "\n" +
                            "imdbID: " + imdbID + "\n" +
                            "Type: " + type + "\n" +
                            "DVD release date: " + DVD + "\n" +
                            "Box Office: " + boxOffice + "\n" +
                            "Production: " + production + "\n" +
                            "Website: " + website + "\n" +
                            "Response: " + responseTF);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }
}

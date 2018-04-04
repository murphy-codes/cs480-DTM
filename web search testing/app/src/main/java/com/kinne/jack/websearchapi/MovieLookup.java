package com.kinne.jack.websearchapi;
//This activity will run with a loading display and perform a google search on the user input,
//  returning imdb.com results in JSON form
import android.app.SearchManager;
import android.content.Intent;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MovieLookup extends AppCompatActivity {

    EditText eText;
    Button btn;
    TextView resultTextView;

    String searchTerm = "matrix";
    String returnString;

    //send a RESTful request for google custom search
    void googleSearch() {
        //instance the requestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBuwIMBG5YjqxxkDsQEjz-el2xHw15RfFQ&cx=015559890765402091894:0yoxulceyae&q="+ searchTerm;

        //request a string response from provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //display the first 500 characters of the response string.
                        String temp = "Response is: " + response.toString();
                        resultTextView.setText(temp);
                        returnString = temp;
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                String temp2 = "didn't work!";
                resultTextView.setText(temp2);
            }
        });

        queue.add(jsonObjectRequest);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_lookup);

        Intent intent = getIntent();
        searchTerm = intent.getStringExtra(VoiceInput.EXTRA_MESSAGE);

        googleSearch();
        /*
        eText = (EditText) findViewById(R.id.editTextInput);
        btn = (Button) findViewById(R.id.button1);
        resultTextView = (TextView) findViewById(R.id.textView1);
        //set textview as scrollable
        resultTextView.setMovementMethod(new ScrollingMovementMethod());

        // button onClick
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String searchString = eText.getText().toString();

                //resultTextView.setText("Searching for : " + searchString);
                //onSearchClick(v);

                //call to custom search
                googleSearch();

            }
        });*/

    }

    public void onSearchClick(View v)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            String term = eText.getText().toString();
            intent.putExtra(SearchManager.QUERY, term);
            startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }



}
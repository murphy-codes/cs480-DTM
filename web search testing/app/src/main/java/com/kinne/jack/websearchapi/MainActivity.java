package com.kinne.jack.websearchapi;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    instancedGoogleResults test;


    public class GoogleResults {
        //save our results
        public ArrayList<String> arrayTitleResults = new ArrayList<String>();
    }
    //ui
    EditText eText;
    Button btn;
    TextView resultTextView;

    //api key
    String search_api_key;

    //send a RESTful request for google custom search
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
                //display the first 500 characters of the response string.
                String jsonString = response.toString();
                resultTextView.setText(jsonString);

                //here we do our work to convert to a json array, then walk it and grab the titles.
                try {
                    //convert json object into json_array.
                    JSONArray responseArray = response.getJSONArray("items");

                    //instance the results we want to return.
                    GoogleResults instancedGoogleResults = new GoogleResults();

                    //loop through contacts
                    for(int i = 0 ; i < responseArray.length(); i++){
                        //convert object from the array one by one back into a single json object
                        JSONObject c = responseArray.getJSONObject(i);

                        //save our results; pushing the strings onto our array list
                        String titleString = c.getString("title");
                        instancedGoogleResults.arrayTitleResults.add(0, titleString );

                        Toast.makeText(getApplicationContext(), titleString,
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                String temp2 = "didn't work!";
                resultTextView.setText(temp2);
            }
        });
        //fire request
        queue.add(jsonObjectRequest);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eText = (EditText) findViewById(R.id.editTextInput);
        btn = (Button) findViewById(R.id.button1);
        resultTextView = (TextView) findViewById(R.id.textView1);

        //set textview as scrollable.  not currently used.
        //resultTextView.setMovementMethod(new ScrollingMovementMethod());

        //api key
        String google_search_api_key = getString(R.string.google_search_api_key);
        search_api_key = google_search_api_key;

        // button onClick to begin our search
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String searchString = eText.getText().toString();

                resultTextView.setText("Searching for : " + searchString);

                //call to custom search
                googleSearch(searchString);

                //show our results
                String showtitle = instancedGoogleResults.arrayTitleResults.get(0);
                resultTextView.setText(showtitle);

            }
        });
    }

    //not used in this project
    //create a google search call, but send the user to a google browser window.
    /*
    public void onSearchClick(View v)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            String term = eText.getText().toString();
            intent.putExtra(SearchManager.QUERY, term);
            startActivity(intent);
        } catch (Exception e) {
         //
        }
    }
    */
}



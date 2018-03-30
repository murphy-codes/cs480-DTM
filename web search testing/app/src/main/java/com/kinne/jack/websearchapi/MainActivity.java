package com.kinne.jack.websearchapi;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText eText;
    Button btn;
    TextView resultTextView;

    String searchTerm = "matrix";
    String returnString;
    String inputString;

    //saved values from our search
    private HashMap<String,String> resultsSearch;

    //send a RESTful request for google custom search
    void googleSearch(String searchTerm ) {

        //hashmap return
        //final HashMap <String,String> resultsSearch = new HashMap<String,String>();

        //instance the requestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        //grab the API key from secret.xml
        String url = "https://www.googleapis.com/customsearch/v1?key=" + inputString + "&cx=015559890765402091894:0yoxulceyae&q="+ searchTerm;

        //request a string response from provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //display the first 500 characters of the response string.
                String temp = "Response is: " + response.toString();
                resultTextView.setText(temp);
                returnString = temp;

                //save our JSON object
                JSONObject resultsObject = new JSONObject((Map) response);

                //we'll need to walk the map , possibly with a fore each?

                //catch some strings, store in an unordered map.
                try {
                    String title = resultsObject.getString("Title");
                    resultsSearch.put("title", title);
                    String description = resultsObject.getString("description");
                    resultsSearch.put("description", description);
                    //keywords
                    //genre
                    //release_date
                    //runtime
                    //person  ..  [name]
                    //directed_by

                    Toast.makeText(getApplicationContext(), title,
                            Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), description,
                            Toast.LENGTH_SHORT).show();

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
        //set textview as scrollable
        resultTextView.setMovementMethod(new ScrollingMovementMethod());

        //api key
        String google_search_api_key = getString(R.string.google_search_api_key);
        inputString = google_search_api_key;

        // button onClick
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String searchString = eText.getText().toString();

                resultTextView.setText("Searching for : " + searchString);

                //call to google window search
                //onSearchClick(v);

                //call to custom search
                googleSearch(searchString);

            }
        });

    }

    //create a google search call, but send the user to a google browser window.
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



package com.kinne.jack.websearchapi;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText eText;
    Button btn;
    TextView resultTextView;




    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eText = (EditText) findViewById(R.id.editTextInput);
        btn = (Button) findViewById(R.id.button1);
        resultTextView = (TextView) findViewById(R.id.textView1);

        // button onClick
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String searchString = eText.getText().toString();

                //resultTextView.setText("Searching for : " + searchString);
                onSearchClick(v);


            }
        });

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



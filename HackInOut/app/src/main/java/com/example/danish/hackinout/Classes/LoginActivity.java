package com.example.danish.hackinout.Classes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.danish.hackinout.MainActivity;
import com.example.danish.hackinout.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Danish on 13-Aug-16.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "WwLwfvPVELWG9zUSNJxHOdY9DESHqrVJf4wf11vx", "bI4Ap7SCWNXJ80L16w16xrvLuWBLulh1IFBYEzbl");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        progress = new ProgressDialog(this);
        progress.setMessage("Signing in... ");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.show();

        Button button=(Button)findViewById(R.id.button);
        Button button2=(Button)findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null)
        {
            progress.dismiss();
          startNextActivity();
        }
        else
        {
            progress.dismiss();
        }
    }

    public void startNextActivity()
    {
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button)
        {
            try {
                progress.show();
                ParseUser user = ParseUser.logIn("danishgoel", "danish");
                progress.dismiss();
                startNextActivity();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(v.getId()==R.id.button2)
        {
            try {
                progress.show();
                ParseUser user = ParseUser.logIn("sarthakahuja", "sarthak");
                progress.dismiss();
                startNextActivity();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

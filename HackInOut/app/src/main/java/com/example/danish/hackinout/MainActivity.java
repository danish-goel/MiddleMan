package com.example.danish.hackinout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<ParseObject> messages = new ArrayList<>();
    EditText comments_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Parse.initialize(this, "WwLwfvPVELWG9zUSNJxHOdY9DESHqrVJf4wf11vx", "bI4Ap7SCWNXJ80L16w16xrvLuWBLulh1IFBYEzbl");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getOtherUserName());
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        Log.d("user", ParseUser.getCurrentUser().getString("Name"));
        comments_input = (EditText) findViewById(R.id.comments_input);
        FloatingActionButton Submit = (FloatingActionButton) findViewById(R.id.Submit);
        Submit.setOnClickListener(this);

        comments_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable mEdit) {
                String text = mEdit.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ConversationList(messages, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        new FetchMessagesTask().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Submit) {
            writeNewMessage(comments_input.getText().toString());
            comments_input.setText("");

        }
    }

    public class FetchMessagesTask extends AsyncTask<Void, Void, Void> {


        public FetchMessagesTask() {

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            query.include("sender");
            query.orderByAscending("updatedAt");
            try {
                messages.addAll(query.find());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mAdapter = new ConversationList(messages, MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
//            Log.d("mssa",fetchedMessages.size()+"");
            super.onPostExecute(result);
        }

    }

    public boolean writeNewMessage(String data) {
        ParseObject message = new ParseObject("Message");
        message.put("data", data);
        message.put("sender", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        message.put("recipient", ParseObject.createWithoutData("_User", getRecpeintObjectID()));
        message.saveInBackground();
        message.put("sender",ParseUser.getCurrentUser());
        messages.add(message);
        mAdapter.notifyItemInserted(messages.size() - 1);
        return true;

    }

    //Hardcoded data
    String getRecpeintObjectID() {
        if (ParseUser.getCurrentUser().getUsername() == "danishgoel") {
            return "YlfLHR5uNz";
        } else {
            return "PYvZtP3MDl";
        }
    }
    //Hardcoded data
    String getOtherUserName()
    {
        if(ParseUser.getCurrentUser().getString("username").equals("danishgoel"))
        {
            return "Sarthak Ahuja";
        }
        else
        {
            return "Danish Goel";
        }
    }
}

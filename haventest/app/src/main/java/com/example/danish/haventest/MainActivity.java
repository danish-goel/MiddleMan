package com.example.danish.haventest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.HODErrorCode;
import hod.response.parser.HODErrorObject;
import hod.response.parser.HODResponseParser;

public class MainActivity extends AppCompatActivity implements IHODClientCallback {

    HODClient hodClient;
    HODResponseParser hodParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hodClient = new HODClient("74e993bb-0893-4b4a-bcf0-b23d7f142f61", this);
        hodParser = new HODResponseParser();

        useHODClient();
    }

    private void useHODClient() {
        String hodApp = HODApps.ENTITY_EXTRACTION;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("url", "http://www.cnn.com");
        List<String> entities = new ArrayList<String>();
        entities.add("people_eng");
        entities.add("places_eng");
        params.put("entity_type", entities);
        params.put("unique_entities", "true");

        hodClient.GetRequest(params, hodApp, HODClient.REQ_MODE.SYNC);
    }

    // define a custom response class
    public class EntityExtractionResponse {
        public List<Entity> entities;

        public class AdditionalInformation
        {
            public List<String> person_profession;
            public String person_date_of_birth;
            public String wikipedia_eng;
            public Long place_population;
            public String place_country_code;
            public Double place_elevation;
        }
        public class Entity
        {
            public String normalized_text;
            public String type;
            public AdditionalInformation additional_information;
        }
    }

    // implement callback functions

    @Override
    public void requestCompletedWithContent(String response) {
        EntityExtractionResponse resp = (EntityExtractionResponse) hodParser.ParseCustomResponse(EntityExtractionResponse.class, response);
        if (resp != null) {
            String values = "";
            for (EntityExtractionResponse.Entity ent : resp.entities) {
                values += ent.type + "\n";
                values += ent.normalized_text + "\n";
                if (ent.type.equals("places_eng")) {
                    values += ent.additional_information.place_country_code + "\n";
                    values += ent.additional_information.place_elevation + "\n";
                    values += ent.additional_information.place_population + "\n";
                } else if (ent.type.equals("people_eng")) {
                    values += ent.additional_information.person_date_of_birth + "\n";
                    values += ent.additional_information.person_profession + "\n";
                    values += ent.additional_information.wikipedia_eng + "\n";
                }
            }
            // print the values
            Log.d("values",values);
        } else {
            List<HODErrorObject> errors = hodParser.GetLastError();
            String errorMsg = "";
            for (HODErrorObject err : errors) {
                errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
                if (err.detail != null)
                    errorMsg += "Error detail: " + err.detail + "\n";
                // handle error message
            }
        }
    }

    @Override
    public void requestCompletedWithJobID(String response) {

    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        // handle error if any
    }
}

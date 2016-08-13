package com.example.sarthak.haventesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hod.api.hodclient.HODApps;
import hod.api.hodclient.HODClient;
import hod.api.hodclient.IHODClientCallback;
import hod.response.parser.HODErrorCode;
import hod.response.parser.HODErrorObject;
import hod.response.parser.HODResponseParser;
import hod.response.parser.OCRDocumentResponse;

public class MainActivity extends AppCompatActivity implements IHODClientCallback {



        HODClient hodClient;
        HODResponseParser hodParser;
        String hodApp = "";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            hodClient = new HODClient("your-apikey", this);
            hodParser = new HODResponseParser();
            useHODClient();
        }

        private void useHODClient() {
            hodApp = HODApps.OCR_DOCUMENT;
            Map<String, Object> params = new HashMap<String, Object>();

            params.put("file", "path/and/filename");
            params.put("mode", "document_photo");

            hodClient.PostRequest(params, hodApp, HODClient.REQ_MODE.ASYNC);
        }

        // implement delegated functions

        /**************************************************************************************
         * An async request will result in a response with a jobID. We parse the response to get
         * the jobID and send a request for the actual content identified by the jobID.
         **************************************************************************************/
        @Override
        public void requestCompletedWithJobID(String response) {
            String jobID = hodParser.ParseJobID(response);
            if (jobID.length() > 0)
                hodClient.GetJobStatus(jobID);
        }

        @Override
        public void requestCompletedWithContent(String response) {
            OCRDocumentResponse resp = hodParser.ParseOCRDocumentResponse(response);
            if (resp != null) {
                String text = "";
                for (OCRDocumentResponse.TextBlock block : resp.text_block) {
                    text += block.text + "\n";
                }
                // print recognized text.
            } else {
                List<HODErrorObject> errors = hodParser.GetLastError();
                String errorMsg = "";
                for (HODErrorObject err: errors) {
                    if (err.error == HODErrorCode.QUEUED) {
                        // sleep for a few seconds then check the job status again
                        hodClient.GetJobStatus(err.jobID);
                        return;
                    } else if (err.error == HODErrorCode.IN_PROGRESS) {
                        // sleep for for a while then check the job status again
                        hodClient.GetJobStatus(err.jobID);
                        return;
                    } else {
                        errorMsg += String.format("Error code: %d\nError Reason: %s\n", err.error, err.reason);
                        if (err.detail != null)
                            errorMsg += "Error detail: " + err.detail + "\n";
                    }
                    // print error message.
                }
            }
        }

        @Override
        public void onErrorOccurred(String errorMessage) {
            // handle error if any
        }
    }
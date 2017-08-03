package com.example.heber.w3d3_ex02;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String MESSAGE_EXTRA = "com.example.heber.w3d3_ex02.MESSAGE_EXTRA";
    private TextView resultTV;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message = msg.getData().getString(MESSAGE_EXTRA);
            String result = String.format(getString(R.string.lbl_result_1_s), message);
            resultTV.setText(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTV = (TextView) findViewById(R.id.tv_result);
        getHttpResult();
    }


    public void getHttpResult(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    StringBuilder result = new StringBuilder();
                    try{
                        URL url = new URL("https://www.google.com");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        String line;
                        while((line = reader.readLine()) != null){
                            result.append(line);
                        }
                    }catch(MalformedURLException mue){
                        mue.printStackTrace();
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                    Message msg = handler.obtainMessage();
                    Bundle data = new Bundle();
                    data.putString(MESSAGE_EXTRA, result.toString());
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            }
        });
        thread.start();
    }
}

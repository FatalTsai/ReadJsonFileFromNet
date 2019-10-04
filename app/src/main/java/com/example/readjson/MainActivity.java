package com.example.readjson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;


import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.security.NetworkSecurityPolicy;

public class MainActivity extends AppCompatActivity {

    TextView txv;
    JSONObject jsonObject;
    String data="fuck";
    JSONArray jsonArray;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.e("data",isCleartextTrafficPermitted());
        txv = (TextView) findViewById(R.id.txv);
        //http://enginebai.logdown.com/posts/196784/android-asnyctask-get-results-oo-methods
        //a better way to deal with async task
        try {
            data = new GoodTask().execute("http://192.168.151.10:3000/home/=getvideo").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Log.d("data2",data);
            jsonArray = new JSONArray(data);

            //REF:https://stackoverflow.com/questions/3395729/convert-json-array-to-normal-java-list
            //method: JSONArray to ArrayList
            list = new ArrayList<String>();

            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(jsonArray.get(i).toString());
                }
            }

            Log.d("item", list.get(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }





    //Ref :http://codenamker.pixnet.net/blog/post/161818334-%E3%80%90android%E3%80%91%E7%95%B0%E6%AD%A5%E5%9F%B7%E8%A1%8C%E7%B7%92asynctask-android-studio
    class GoodTask extends AsyncTask<String, Integer, String> {
        // <傳入參數, 處理中更新介面參數, 處理後傳出參數>
        private static final int TIME_OUT = 1000;

        String jsonString1 = "";

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... countTo) {
            // TODO Auto-generated method stub
            // 再背景中處理的耗時工作
            try {
                HttpURLConnection conn = null;
                URL url = new URL(countTo[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                // 讀取資料
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8"));
                jsonString1 = reader.readLine();
                reader.close();

                if (Thread.interrupted()) {
                    throw new InterruptedException();

                }
                if (jsonString1.equals("")) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "網路中斷" + e;
            }
            //Log.e("data",jsonString1);
            return jsonString1;
        }
        public void onPostExecute(String result )
        { super.onPreExecute();
            // 背景工作處理完"後"需作的事
            txv.setText("JSON:\r\n"+ result);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // 背景工作處理"中"更新的事

        }
    }



}

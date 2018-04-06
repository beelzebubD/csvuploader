package com.singh.divyanshu.omnia_tap_assignment;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main2Activity extends AppCompatActivity {

    public JSONObject jsonObject = new JSONObject();
    public File file;
    public JSONArray jsonArray = new JSONArray();
    private Button upload;
    public String Url;
    public List<String> headings;
    Map<String, Object> map;
    private Uri uri;
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle = getIntent().getExtras();
        Url = bundle.getString("URL");
        uri = (Uri) bundle.get("URI");
        Log.d("URL", Url);
        upload = findViewById(R.id.upload);
        headings = new ArrayList<>();

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Url != null || checkHeadingFormat() || !(Url.contains(".csv"))) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("CSVfileJsonObject");
                    try {
                        Log.d("upload1", Main2Activity.this.jsonArray.length() + "");
                        int j = 0;
                        for (int i = 1; i < jsonArray.length(); i++) {
                            String s = Main2Activity.this.jsonArray.get(i).toString();
                            Log.d("upload2", Main2Activity.this.jsonArray.get(i).toString());
                            map = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<HashMap<String, Object>>() {
                            }.getType());
                            myRef.child("Row" + j++).setValue(map);
                        }
                        Toast.makeText(Main2Activity.this, "UPLOADED :-)", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.d("upload3", e.getMessage());
                    }
                } else {
                    Log.d("Toast1", "URL, null");
                    Toast.makeText(Main2Activity.this, "URL is NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(!(Url.isEmpty()||!(Url.contains(".csv")))){
        BufferedReader mBufferedReader = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            mBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] words;
        int flag = 1;
        try {
            while ((line = mBufferedReader.readLine()) != null) {
                this.jsonObject = new JSONObject();
                words = line.split(",");
                if (flag == 1) {
                    for (int j = 0; j < words.length; j++) {
                        headings.add(words[j]);
                        jsonObject.put(words[j], words[j]);
                        Log.d("LLLLL: ", words[j]);
                        flag = 0;
                    }
                } else {
                    for (int j = 0; j < words.length; j++) {
                        this.jsonObject.put(headings.get(j), words[j]);
                        Log.d("LLLLL: ", words[j]);
                    }
                }
                this.jsonArray.put(jsonObject);
                Log.e("JsonObjectMain2Activity", this.jsonObject.toString());
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.e("JsonArray2Main2Activity", this.jsonArray.get(i).toString());
            }
            mBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d("JSONObject", e.getMessage());
        }
    }else{
            Toast.makeText(this,"Wrong File Format",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }
    public boolean checkHeadingFormat(){
        for(int i=0;i<headings.size();i++)
        {
            if(!headings.isEmpty()) {
                if (headings.get(i).contains(".") || headings.get(i).contains("/") || headings.get(i).contains("$") || headings.get(i).contains("[") || headings.get(i).contains("]") || headings.get(i).contains("{") || headings.get(i).contains("}") || headings.get(i).contains("(") || headings.get(i).contains(")"))
                return false;
                return false;
            }
        }
        return true;
    }
}

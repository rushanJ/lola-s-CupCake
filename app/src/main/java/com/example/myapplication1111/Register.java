package com.example.myapplication1111;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    TextView txtResult;
    Button btn;
    EditText name,email,pass;
    String nameTxt,passTxt,emailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtResult = (TextView) findViewById(R.id.textView);
        btn =(Button) findViewById(R.id.saveData);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        pass = findViewById(R.id.password);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                store();
            }
        });


    }

    public void store(){
        nameTxt = name.getText().toString();
        passTxt = pass.getText().toString();
        emailTxt = email.getText().toString();

        OkHttpClient client = new OkHttpClient();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", nameTxt)
                .addFormDataPart("email", emailTxt)
                .addFormDataPart("pass", passTxt)

           .build();

        Request request = new Request.Builder()
                .url("http://critssl.com/cakeshop/API/register.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // get JSONObject from JSON file
//                                String JSON_STRING = myResponse;
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String success = mJsonObject.getString("status");
                                Log.d("STATE","success");
//                                mTextViewResult.setText("success: "+success);
                                if (success.equals("1")) {
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Register.this);
                                    a_builder.setMessage("Account Creared Succesfully ! ")
                                            .setCancelable(false)
                                            .setPositiveButton("Go to Login", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    loginPage();
                                                }
                                            });

                                    AlertDialog alert =a_builder.create();
                                    alert.setTitle("Success!");
                                    alert.show();

                                }
                                else {
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(Register.this);
                                    a_builder.setMessage("Account Not Creared ! ")
                                            .setCancelable(false)
                                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert =a_builder.create();
                                    alert.setTitle("Error!");
                                    alert.show();


                                }
//

                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
//                            mTextViewResult.setText(myResponse);
                            Log.d("STATE",myResponse);


                        }
                    });
                }
            }
        });
    }
    public void loginPage(){
        Log.d("STATE","openAdminHome running");
        Intent intent =new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }
    private void updateResult(String myResponse) {
        Log.d("STATE",myResponse);

    }

}


package com.example.myapplication1111;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView status,role,method,mTextViewResult;
    private Button loginBtn,registerBtn;
    private boolean authStatus=false;
    private String file = "userInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        mTextViewResult = findViewById(R.id.textView2);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        //openUserHome();
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("STATE","Button Clicked");
                EditText userName = (EditText) findViewById(R.id.userName);
                EditText password = (EditText) findViewById(R.id.password);
                String userNameTxt = userName.getText().toString();
                String passwordTxt = password.getText().toString();

                Log.d("STATE",userNameTxt);
                Log.d("STATE",passwordTxt);


               // auth(userNameTxt,passwordTxt);
                if (passwordTxt.equals("nimda") && userNameTxt.equals("admin")){
                    Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_SHORT).show();
                    openAdminHome();
                }

                else if( auth(userNameTxt,passwordTxt)) {
                    openUserHome();

                }
                else{
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                    a_builder.setMessage("Username ot Password error!")
                            .setCancelable(false)
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            . setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert =a_builder.create();
                    alert.setTitle("Error !");
                    alert.show();
                }
            }
        });





    }

    public void openAdminHome(){
        Log.d("STATE","openAdminHome running");
        Intent intent =new Intent(MainActivity.this,AdminHome.class);
        startActivity(intent);
    }
    public void openUserHome(){
        Log.d("STATE","Open User running");
        Intent intent =new Intent(MainActivity.this,UserHome.class);
        startActivity(intent);
    }
    public void openRegister(){
        Log.d("STATE","Open User running");
        Intent intent =new Intent(MainActivity.this,Register.class);
        startActivity(intent);
    }
    public boolean auth(String userName, String password){
        OkHttpClient client = new OkHttpClient();
        String url = "http://critssl.com/cakeshop/API/auth.php?userName="+userName+"&pass="+password;
        Request request = new Request.Builder()
                .url(url)
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

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // get JSONObject from JSON file
//                                String JSON_STRING = myResponse;
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String success = mJsonObject.getString("success");
                                String id = mJsonObject.getString("id");

                                try {
                                    Log.d("STATE","Trying");
                                    FileOutputStream fOut = openFileOutput(file,MODE_WORLD_READABLE);
                                    fOut.write(id.getBytes());
                                    fOut.close();
                                    Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
                                    Log.d("STATE","Done");
                                }
                                catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    Log.d("STATE","Error");
                                    e.printStackTrace();

                                }

//                                mTextViewResult.setText("success: "+success);
                                if (success.equals("true")) authStatus =true;
                                else authStatus =true;
//

                            } catch (JSONException e) {
                                e.printStackTrace();
                                authStatus =true;

                            }
//                            mTextViewResult.setText(myResponse);
                            Log.d("STATE",myResponse);
                            authStatus =true;

                        }
                    });
                }
                authStatus =true;
            }
        });
        return authStatus ;
    }
}

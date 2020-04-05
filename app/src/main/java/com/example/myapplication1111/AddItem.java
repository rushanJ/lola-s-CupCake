package com.example.myapplication1111;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddItem extends AppCompatActivity {
    TextView txtResult;
    Button btn;
    EditText name,price,des;
    String nameTxt,priceTxt,desTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        txtResult = (TextView) findViewById(R.id.textView);
        btn =(Button) findViewById(R.id.saveData);
        des = findViewById(R.id.des);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                store();
            }
        });


    }
    public static void writeToFile(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("This is the string contained in the file to be uploaded, Yiiiiiihaaaaaa!!!".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void store(){
        nameTxt = name.getText().toString();
        desTxt = des.getText().toString();
        priceTxt = price.getText().toString();

        OkHttpClient client = new OkHttpClient();
        File file = new File(getCacheDir(), "fileToUpload.txt");
        if (!file.exists())
            writeToFile(file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", nameTxt)
                .addFormDataPart("price", priceTxt)
                .addFormDataPart("description", desTxt)
//                .addFormDataPart("img", "file.txt", RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .addFormDataPart("img", "file.txt", RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://critssl.com/cakeshop/API/newItem.php")
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

                    AddItem.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // get JSONObject from JSON file
//                                String JSON_STRING = myResponse;
                                String JSON_STRING = "["+myResponse+"]";
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String success = mJsonObject.getString("success");
//                                mTextViewResult.setText("success: "+success);
                                if (success.equals("true")) {

                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(AddItem.this);
                                    a_builder.setMessage("Item Added successfully !")
                                            .setCancelable(false)

                                            . setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert =a_builder.create();
                                    alert.setTitle("Error !");
                                    alert.show();
                                    Log.d("STATE", "Done");
                                }
                                else {
                                    AlertDialog.Builder a_builder = new AlertDialog.Builder(AddItem.this);
                                    a_builder.setMessage("Unable to add item !")
                                            .setCancelable(false)

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
//
//

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("STATE", e.toString());

                            }
//                            mTextViewResult.setText(myResponse);
                            Log.d("STATE",myResponse);


                        }
                    });
                }
            }
        });
    }
    private void updateResult(String myResponse) {
        Log.d("STATE",myResponse);

    }
}

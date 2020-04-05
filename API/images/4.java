package com.example.myapplication1111;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserHome extends AppCompatActivity {


    private String TAG = UserHome.class.getSimpleName();
    private ListView lv;
    private String file = "userInfo";
    String temp="";
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        try {
            FileInputStream fin = openFileInput(file);
            int c;

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            Log.d("STATE",temp);;
            Toast.makeText(getBaseContext(),"file read",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Log.d("STATE","error");;
        }

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(UserHome.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://10.0.2.2/cakeshop/API/test.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String price = c.getString("price");
                        String description = c.getString("description");

                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("price", price);
                        contact.put("description", description);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(UserHome.this, contactList,
                    R.layout.list_item, new String[]{ "name","price","description"},
                    new int[]{R.id.name, R.id.price, R.id.des});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String a=""+position;
                    System.out.println(contactList.get(position).get("id"));

                   Log.d("STATE", a);
                    Log.d("STATE", temp);
                    orderItems(temp,contactList.get(position).get("id"));
//                    Toast.makeText(UserHome.this,contactList, Toast.LENGTH_SHORT).show();
                }
            });
        }
       public void orderItems(String user, String item){
           OkHttpClient client = new OkHttpClient();
           String url = "http://10.0.2.2/cakeshop/API/orderItem.php?user="+user+"&item="+item;
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

                       UserHome.this.runOnUiThread(new Runnable() {
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
                                   if (success.equals("true")) Log.d("STATE", "Done");
                                   else Log.d("STATE", "Error");
//

                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   Log.d("STATE", "Error");

                               }
//                            mTextViewResult.setText(myResponse);
                               Log.d("STATE",myResponse);


                           }
                       });
                   }
                   Log.d("STATE","Error");
               }
           });
       }
    }
}
package com.example.myapplication1111;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdminHome extends AppCompatActivity {
    Button newItemBtn,viewOrdersBtn,logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        newItemBtn = (Button) findViewById(R.id.newItem);
        viewOrdersBtn = (Button) findViewById(R.id.viewOrders);
        logoutBtn = (Button) findViewById(R.id.logout);
        //openNewItem();
        newItemBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openNewItem();
            }
        });
        viewOrdersBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openOrderList();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void openNewItem(){
        Log.d("STATE","openAdminHome running");
        Intent intent =new Intent(AdminHome.this,AddItem.class);
        startActivity(intent);
    }
    public void openOrderList(){
        Log.d("STATE","openAdminHome running");
        Intent intent =new Intent(AdminHome.this,ViewItem.class);
        startActivity(intent);
    }
    public void logout(){
        Log.d("STATE","openAdminHome running");
        Intent intent =new Intent(AdminHome.this,MainActivity.class);
        startActivity(intent);
    }
}

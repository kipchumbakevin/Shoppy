package com.example.kipchu.ecommerceapp;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;



public class ProductListActivity extends AppCompatActivity {

    public static ArrayList<Product> mProductArrayList = new ArrayList<>();

    RecyclerView mRecyclerView;
    int position=78;

    private String[] productNames = {"Hoverboard v-4 s","Natural light googles","Black Lether Wallet","Nike supra 3","Beat wireless earphones","Khaki handbags","Brown Rubber"};
    private String[] productStrikedPrice={"20000","1500","180000","4000","2000","1700","3000"};
    private String[] productPrices={"17000","800","1500","3500","1500","1200","2200"};
    private String[] productDescription={"gg","hh","jj", "dd", "yy", "gf", "gf"};
    private int[] productImages = {R.drawable.hover_board,R.drawable.googles,R.drawable.wallet,R.drawable.nike,R.drawable.beat_headphones,R.drawable.h_bag,R.drawable.rubber_shoe};

    ProductListAdapter mProductListAdapter ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);
        if (savedInstanceState!=null){
            position=savedInstanceState.getInt("position");
        }

        mRecyclerView=(RecyclerView) findViewById(R.id.product_list_recyclerView);

        mProductListAdapter=new ProductListAdapter(ProductListActivity.this,mProductArrayList);



        populateRecyclerView();

        mRecyclerView.setAdapter(mProductListAdapter);


        mRecyclerView.setLayoutManager(new GridLayoutManager(ProductListActivity.this,getResources().getInteger(R.integer.products_grid_span)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.ic_add);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent i=new Intent(ProductListActivity.this,ProductActivity.class);

                startActivity(i);

            }

        });
        Log.d("Start", "Activity onCreate and position variable = "+ position);

    }

    public  void populateRecyclerView(){

        mProductArrayList.clear();

        int index;
        for(index = 0; index<productNames.length; index++){

            Product product=new Product();

            product.setName(productNames[index]);

            product.setPrice(productPrices[index]);
            product.setStrikedPrice(productStrikedPrice[index]);
            product.setDescription(productDescription [index]);

            product.setImage(productImages[index]);

            mProductArrayList.add(product);

        }

        mProductListAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Start", "Activity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Start", "Activity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        position= 80;
        Log.d("Start", "Activity onPause and position variable= "+position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Start", "Activity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Start", "Activity onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",position);
    }

   // @Override
    //public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.main_menu,menu);
        //return super.onCreateOptionsMenu(menu);
    //}
}


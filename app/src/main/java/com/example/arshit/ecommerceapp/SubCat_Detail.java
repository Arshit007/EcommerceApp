package com.example.arshit.ecommerceapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.arshit.ecommerceapp.Model.SubCategory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class SubCat_Detail extends AppCompatActivity {

    private DatabaseReference Rootref;
    String selected_user_id;

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    private FirebaseUser firebaseUser;
    ElegantNumberButton numberButton;
    FirebaseDatabase database;
String currentUserId;
    FirebaseAuth mAuth;
    private StorageReference imgStorageReference;
//    Food currentFood;
Intent intent;
    String CategoryId,SubCategoryId;
    String SubCatImage,SubCatName,SubCatDescription,SubCatPrice;
    SubCategory subCategory;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat__detail);

        intialise();

    }

    private void intialise() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        linearLayout = findViewById(R.id.cart_product_ll);
//        linearLayout.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        imgStorageReference = FirebaseStorage.getInstance().getReference();
        intent= getIntent();

//        SubCategoryName =  intent.getStringExtra("SubCategoryName");

        CategoryId = intent.getStringExtra("CategoryId");

        SubCategoryId = intent.getStringExtra("selected_user_id");



        Rootref = FirebaseDatabase.getInstance().getReference("SubCategory").child(CategoryId).child(SubCategoryId);


        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        AddToCart();
            }
        });


        food_description = (TextView) findViewById(R.id.food_description);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
//        collapsingToolbarLayout.setTitleEnabled(false);


        getDetailFood();


    }

    private void AddToCart() {

        String    cartId = UUID.randomUUID().toString();
//        Rootref = FirebaseDatabase.getInstance().getReference("Cart").child("UserView").child("Products").child(currentUserId).child(cartId);
//      DatabaseReference  dbref = FirebaseDatabase.getInstance().getReference("Cart").child("AdminView").child("Products").child(currentUserId).child(cartId);

        Rootref = FirebaseDatabase.getInstance().getReference("Cart").child("UserView").child("Products").child(currentUserId).child(cartId);
        //DatabaseReference  dbref = FirebaseDatabase.getInstance().getReference("Cart").child("AdminView").child("Products").child(cartId);



        int Quantity = Integer.parseInt(numberButton.getNumber());
        int Price = Integer.parseInt(subCategory.getPrice());
        int TotalPrice = Quantity*Price;

//        Toast.makeText(this, String.valueOf(TotalPrice), Toast.LENGTH_SHORT).show();

        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("UserId ",currentUserId);
        hashMap.put("ProductName",SubCatName);
        hashMap.put("ProductQuantity",numberButton.getNumber());
        hashMap.put("ProductImage",SubCatImage);
        hashMap.put("CartId",cartId);
        hashMap.put("Price", String.valueOf(TotalPrice));

        //dbref.setValue(hashMap);
          Rootref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {

                  if (task.isSuccessful()){

                      Toast.makeText(SubCat_Detail.this, "Items Added to cart ", Toast.LENGTH_SHORT).show();
//                      linearLayout.setVisibility(View.INVISIBLE);

                  }

              }
          });

    }


    private void getDetailFood() {

        Rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                subCategory = dataSnapshot.getValue(SubCategory.class);


                 SubCatImage = dataSnapshot.child("imageSubCat").getValue().toString();
                 SubCatName = dataSnapshot.child("Food Name").getValue().toString();

                 SubCatDescription = dataSnapshot.child("Description").getValue().toString();

                 SubCatPrice = dataSnapshot.child("Price").getValue().toString();


                Picasso.get().load(SubCatImage).placeholder(R.drawable.profile_avatar_small).into(food_image);


                        collapsingToolbarLayout.setTitle(SubCatName);

                        food_price.setText(SubCatPrice);

                        food_name.setText(SubCatName);

                        food_description.setText(SubCatDescription);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }



}
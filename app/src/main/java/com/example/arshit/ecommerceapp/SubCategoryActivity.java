package com.example.arshit.ecommerceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiodegio.msv.MaterialSearchView;
import com.example.arshit.ecommerceapp.Model.SubCategory;
import com.example.arshit.ecommerceapp.Practise.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCategoryActivity extends AppCompatActivity {


    FirebaseRecyclerAdapter adapter;
    FirebaseRecyclerAdapter searchadapter;


    private DatabaseReference Rootref;
    String selected_user_id;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String currentUserId;
    EditText txt_category, edit_text_price_discount, edit_food_cat, edit_text_sub_cat, edit_text_price_subcat;
    Button btnSelect, btnUpload;
    private static int Gallery_Pick = 1;
    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;
    Uri imageURI;
    RecyclerView recycler_sub_category,search_sub_category_rv;
    String subcatdiscount, subcatName, subcatdescription, subcatprice,CategoryName;
    Button btn_category, btn_subcat;
    Intent intent;
    String CategoryId;
    String FoodName,Description,Key,Price,userImage;
    EditText searchView;
    ArrayList<String> suggestedList;

    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);


//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                NotificationChannel channel = new NotificationChannel("MY NOTIFICATION","MY NOTIFICATION",NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Message succesful";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
////                        Toast.makeText(SubCat_Detail.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//


    intialise();
        toolbar();
    }

    private void toolbar() {


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);
            ImageView imageView = findViewById(R.id.toolbar_img);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(CategoryName);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    private void intialise() {

//
//        search_sub_category_rv =findViewById(R.id.search_sub_category_rv);
//
//     searchView = findViewById(R.id.searchBar);
//
//        searchView.addTextChangedListener(new TextWatcher() {
//    @Override
//    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable editable) {
//
//        if (!editable.toString().isEmpty()){
//
//            search(editable.toString());
//        }
//
//        else {
//
//            search("");
//        }
//
//    }
//});






         imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Rootref = FirebaseDatabase.getInstance().getReference();

        intent= getIntent();
        CategoryId =  intent.getStringExtra("selected_user_id");

        CategoryName = intent.getStringExtra("CategoryName");


//        btn_subcat = findViewById(R.id.btn_sub_category);


        recycler_sub_category = findViewById(R.id.recycler_sub_category);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 2);
        recycler_sub_category.setLayoutManager(glm);





//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
//        search_sub_category_rv.setLayoutManager(gridLayoutManager);

    }


//    private void search(String s) {
//
//        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("SubCategory").child(CategoryId);
////        Query query = dbref.orderByChild("Food Name").startAt(s).endAt(s+"\uf8ff");
//
//        dbref.keepSynced(true);
//
//        FirebaseRecyclerOptions<SubCategory> options = new FirebaseRecyclerOptions.Builder<SubCategory>()
//                .setQuery(dbref.orderByChild("Food Name").startAt(s).endAt(s+"\uf8ff"), SubCategory.class).build();
//
//        searchadapter = new FirebaseRecyclerAdapter<SubCategory, UsersViewHolder>(options) {
//
//            @Override
//            protected void onBindViewHolder(@NonNull final SubCategoryActivity.UsersViewHolder holder, int position, @NonNull final SubCategory subCategory) {
//
//                selected_user_id = getRef(position).getKey();
//
//                dbref.child(selected_user_id).addValueEventListener(new ValueEventListener() {
//
//
//                    @Override
//                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//suggestedList.clear();
//                        if (dataSnapshot.hasChild("imageSubCat")){
//
//                            Key = dataSnapshot.getKey();
//                            FoodName = dataSnapshot.child("Food Name").getValue().toString();
//
//                            suggestedList.add(FoodName);
//
//                            holder.uprice.setText(Key);
//                            holder.uprice.setVisibility(View.INVISIBLE);
//                    }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//            }
//
//
//
//
//            @NonNull
//            @Override
//            public SubCategoryActivity.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
//
//                return new SubCategoryActivity.UsersViewHolder(view);
//
//            }
//
//        } ;
//
//
//        search_sub_category_rv.setAdapter(searchadapter);
//        searchadapter.startListening();
//
//
//    }


    @Override
    protected void onStart() {
        super.onStart();



        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("SubCategory").child(CategoryId);
        database.keepSynced(true);

        FirebaseRecyclerOptions<SubCategory> options = new FirebaseRecyclerOptions.Builder<SubCategory>()
                .setQuery(database, SubCategory.class).build();

adapter = new FirebaseRecyclerAdapter<SubCategory, UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final SubCategoryActivity.UsersViewHolder holder, int position, @NonNull final SubCategory subCategory) {

                   selected_user_id = getRef(position).getKey();


//                Toast.makeText(SubCategoryActivity.this, "selected id "+ selected_user_id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(SubCategoryActivity.this, "Category Id " + CategoryId, Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(SubCategoryActivity.this," Value - "+(CharSequence) database.child(CategoryId).child(selected_user_id), Toast.LENGTH_SHORT).show();

//                database.child(selected_user_id).orderByChild("MenuId").equalTo(0);



                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                        Key = dataSnapshot.getKey();

                        if (dataSnapshot.hasChild("imageSubCat")){

//                            Key = database.child(CategoryId).child(selected_user_id).push().getKey();
                          Key = dataSnapshot.getKey();
//                            Toast.makeText(SubCategoryActivity.this, selected_user_id, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(SubCategoryActivity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            FoodName = dataSnapshot.child("Food Name").getValue().toString();
//                            Description = dataSnapshot.child("Description").getValue().toString();
//
//                            Price = dataSnapshot.child("Price").getValue().toString();

                            holder.uName.setText(FoodName);
                            holder.uprice.setText(Key);
                            holder.uprice.setVisibility(View.INVISIBLE);




                           userImage = dataSnapshot.child("imageSubCat").getValue().toString();

                            Picasso.get().load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.profile_avatar_small).into(holder.profileImg, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
                                }
                            });



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(SubCategoryActivity.this, SubCat_Detail.class);
                        profileIntent.putExtra("selected_user_id", String.valueOf(holder.uprice.getText().toString()));
                        profileIntent.putExtra("CategoryId", CategoryId);
                        profileIntent.putExtra("SubCategoryName", String.valueOf(holder.uName.getText().toString()));



                        startActivity(profileIntent);

                    }
                });

            }




            @NonNull
            @Override
            public SubCategoryActivity.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

                return new SubCategoryActivity.UsersViewHolder(view);

            }

        } ;


   recycler_sub_category.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder  {
        public ImageView profileImg;

        TextView uName,uStatus,uprice;
        View  mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            uprice = mView.findViewById(R.id.uprice);
            uName = mView.findViewById(R.id.category_name);
            profileImg = mView.findViewById(R.id.category_image);
        }


    }





}



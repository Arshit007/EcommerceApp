package com.example.arshit.ecommerceapp.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahamed.multiviewadapter.SimpleRecyclerAdapter;
import com.example.arshit.ecommerceapp.Adapter.CategoryBinder;
import com.example.arshit.ecommerceapp.Model.Category;
import com.example.arshit.ecommerceapp.Model.Token;
import com.example.arshit.ecommerceapp.Practise.MainActivity;
import com.example.arshit.ecommerceapp.R;
import com.example.arshit.ecommerceapp.SubCategoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Home extends Fragment {
     RecyclerView recycler_category;
     View view;
    String currentUserId;
    FirebaseAuth mAuth;

     ArrayList<Category> accdata;
    FirebaseRecyclerAdapter adapter;
    private DatabaseReference database;
     public Fragment_Home(){


     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     view = inflater.inflate(R.layout.fragment_home, container, false);
    intialise();

   return view;

     }
    private void intialise() {

//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("MY NOTIFICATION","MY NOTIFICATION",NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//
//
//
//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Message succesful";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
//                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        final DatabaseReference dbrf = FirebaseDatabase.getInstance().getReference("Token");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                Token data = new Token(deviceToken,false);
                dbrf.child(currentUserId).setValue(data);

            }
        });


        recycler_category = view.findViewById(R.id.recycler_category);


        SimpleRecyclerAdapter<Category, CategoryBinder> adapter = new SimpleRecyclerAdapter<>(new CategoryBinder());
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);

        recycler_category.setLayoutManager(glm);

    }


    @Override
    public void onStart() {
        super.onStart();

        database = FirebaseDatabase.getInstance().getReference().child("Category");
        database.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database, Category.class).build();

         adapter = new FirebaseRecyclerAdapter<Category, Fragment_Home.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final Fragment_Home.UserViewHolder holder, int position, @NonNull final Category category) {

                final String selected_user_id = getRef(position).getKey();

                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("imageURL")){

                            final String userImage = dataSnapshot.child("imageURL").getValue().toString();
                            String category1 = dataSnapshot.child("Category").getValue().toString();




                            holder.uName.setText(category1);

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


//                Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);




//                Set event for component
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(getContext(), SubCategoryActivity.class);
                        profileIntent.putExtra("selected_user_id",selected_user_id);
                        profileIntent.putExtra("CategoryName", String.valueOf(holder.uName.getText().toString()));

                        startActivity(profileIntent);
                    }
                });

            }

            @NonNull
            @Override
            public Fragment_Home.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

                return new Fragment_Home.UserViewHolder(view);

            }

        } ;


//        all_user_rv = findViewById(R.id.all_user_recyclerview);
        recycler_category.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
       ImageView profileImg;

        TextView uName,uStatus;
        View  mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            uName =  mView.findViewById(R.id.category_name);
            profileImg = mView.findViewById(R.id.category_image);
        }

    }


}

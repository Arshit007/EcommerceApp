package com.example.arshit.ecommerceapp.Practise;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.ecommerceapp.Fragment.Fragment_Cart;
import com.example.arshit.ecommerceapp.Fragment.Fragment_Home;
import com.example.arshit.ecommerceapp.Model.Category;
import com.example.arshit.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference Rootref;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String currentUserId;
    RecyclerView recycler_category;
    private DatabaseReference database;
    View view;
    ArrayList<Category> accdata;

    ViewPager viewPager;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_shopping_cart_black_24dp,

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void intialise() {




        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.addFragment(new Fragment_Home(), "Home");
        viewPagerAdapter.addFragment(new Fragment_Cart(), "Cart");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

//        recycler_category = findViewById(R.id.recycler_category);
////        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//
//
//        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 1);
////
////
////        int spanCount = 2; // 3 columns
////        int spacing = 2; // 50px
////        boolean includeEdge = true;
////        recycler_category.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//
////        recycler_category.addItemDecoration(
////                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
////        recycler_category.addItemDecoration(
////                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
//
//        SimpleRecyclerAdapter<Category, CategoryBinder> adapter = new SimpleRecyclerAdapter<>(new CategoryBinder());
//
//        recycler_category.setLayoutManager(glm);
////        recycler_category.setAdapter(adapter);
////        glm.setSpanSizeLookup(adapter.getSpanSizeLookup());
////        adapter.setSpanCount(2);
////        adapter.setData(accdata);


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        database = FirebaseDatabase.getInstance().getReference().child("Category");
//        database.keepSynced(true);
//
//        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
//                .setQuery(database, Category.class).build();
//
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Category, UserViewHolder>(options) {
//
//
//            @Override
//            protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final Category category) {
//
//                final String selected_user_id = getRef(position).getKey();
//
//                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.hasChild("imageURL")){
//
//                            final String userImage = dataSnapshot.child("imageURL").getValue().toString();
//                            String category1 = dataSnapshot.child("Category").getValue().toString();
//
//
//
//
//                            holder.uName.setText(category1);
//
//                            Picasso.get().load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
//                                    .placeholder(R.drawable.profile_avatar_small).into(holder.profileImg, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//
//                                    Picasso.get().load(userImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
//                                }
//                            });
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
////                Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
//
//
//
//
////                Set event for component
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent profileIntent = new Intent(MainActivity.this, SubCategoryActivity.class);
//                        profileIntent.putExtra("selected_user_id",selected_user_id);
//                        profileIntent.putExtra("CategoryName", String.valueOf(holder.uName.getText().toString()));
//
//                        startActivity(profileIntent);
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
//
//                return new UserViewHolder(view);
//
//            }
//
//        } ;
//
//
////        all_user_rv = findViewById(R.id.all_user_recyclerview);
//        recycler_category.setAdapter(adapter);
//        adapter.startListening();
//    }


//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        public CircleImageView profileImg;
//
//        TextView uName,uStatus;
//        View  mView;
//
//        public UserViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//
//            uName = mView.findViewById(R.id.category_name);
//            profileImg = mView.findViewById(R.id.category_image);
//        }
//
//    }


    private void toolbar() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Ecommerce App");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> mfragments;
        private final  ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            this.mfragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }


        @Override
        public int getCount() {
            return mfragments.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mfragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

}
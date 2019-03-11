package com.example.arshit.ecommerceapp.Practise;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arshit.ecommerceapp.Model.Cart;
import com.example.arshit.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartActivity extends AppCompatActivity {

    private StorageReference imgStorageReference;
    private DatabaseReference dbref;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String currentUserId;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recycler_cart;
 String ProductPrice,ProductName,ProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



initialise();



    }

    private void initialise() {

        imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        recycler_cart = findViewById(R.id.recycler_cart);

        dbref = FirebaseDatabase.getInstance().getReference();



    }

    @Override
    protected void onStart() {
        super.onStart();



        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Cart").child(currentUserId);
        database.keepSynced(true);

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(database, Cart.class).build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartActivity.UsersViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final CartActivity.UsersViewHolder holder, int position, @NonNull final Cart cart) {

                      String     selected_user_id = getRef(position).getKey();
                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.hasChild("ProductImage")){


                            ProductName = dataSnapshot.child("ProductName").getValue().toString();
                            ProductPrice = dataSnapshot.child("ProductPrice").getValue().toString();

                            holder.uName.setText(ProductName);
                            holder.uprice.setText(ProductPrice);






                            ProductImage = dataSnapshot.child("ProductImage").getValue().toString();

                            Picasso.get().load(ProductImage).networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.profile_avatar_small).into(holder.profileImg, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                    Picasso.get().load(ProductImage).placeholder(R.drawable.profile_avatar_small).into(holder.profileImg);
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



                    }
                });

            }




            @NonNull
            @Override
            public CartActivity.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

                return new CartActivity.UsersViewHolder(view);

            }

        } ;


        recycler_cart.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder  {
        public CircleImageView profileImg;

        TextView uName,uStatus,uprice;
        View  mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            uprice = mView.findViewById(R.id.orginal_price);
            uName = mView.findViewById(R.id.cart_product_name);
            profileImg = mView.findViewById(R.id.cart_product_image);
        }


    }

}

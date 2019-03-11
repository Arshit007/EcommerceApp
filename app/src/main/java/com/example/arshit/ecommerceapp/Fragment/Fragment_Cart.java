package com.example.arshit.ecommerceapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.ecommerceapp.Model.Cart;
import com.example.arshit.ecommerceapp.Model.OrderModel;
import com.example.arshit.ecommerceapp.Model.Token;
import com.example.arshit.ecommerceapp.Notification.APIService;
import com.example.arshit.ecommerceapp.Notification.Common;
import com.example.arshit.ecommerceapp.Notification.MyResponse;
import com.example.arshit.ecommerceapp.Notification.Notification;
import com.example.arshit.ecommerceapp.Notification.Sender;
import com.example.arshit.ecommerceapp.R;
import com.example.arshit.ecommerceapp.SubCat_Detail;
import com.example.arshit.ecommerceapp.SubCategoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;


public class Fragment_Cart extends Fragment {

    private StorageReference imgStorageReference;
    private DatabaseReference dbref;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    Button btnOrder;
    String selected_user_id;
    private String currentUserId;
    FirebaseRecyclerAdapter adapter;
    RecyclerView recycler_cart;
    String ProductPrice, ProductName,ProductImage,ProductQuantity,ProductOrginal,CartId;
    View view;
    TextView total,cart_product_orginal_price,cart_product_quantity;
    static int amount;
    ImageView remove_item_cart;
    EditText edit_contact,edit_address,edit_city,edit_country;
    String address,city,country,contact,id;
    HashMap<String,Object> hashMap;
     DatabaseReference RootRefe;
     LinearLayout linearLayout;
     RelativeLayout relativeLayout;
     Button btnbook;

     APIService mService;
    public Fragment_Cart() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initialise();
        return view;

    }

    private void initialise() {

        hashMap = new HashMap<>();

        relativeLayout =view.findViewById(R.id.linear_layout_group);
        relativeLayout.setVisibility(View.INVISIBLE);
        imgStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        total = view.findViewById(R.id.total);
      btnOrder = view.findViewById(R.id.btnPlaceOrder);
        linearLayout = view.findViewById(R.id.cart_product_ll);
        linearLayout.setVisibility(View.VISIBLE);

        recycler_cart = view.findViewById(R.id.recycler_cart);
        remove_item_cart = view.findViewById(R.id.remove_item_cart);
        btnbook = view.findViewById(R.id.btnbook);

        mService = Common.getFCMService();

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(getContext(),Fragment_Home.class);
                 startActivity(Intent);

            }
        });


        GridLayoutManager glm = new GridLayoutManager(getContext(), 1);

        recycler_cart.setLayoutManager(glm);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  showDialog();

            }
        });

    }


    private void showDialog(){
        relativeLayout.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Order Detail : ");
        builder.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View custom_layout = inflater.inflate(R.layout.custom_dialog_order,null);



        edit_address = custom_layout.findViewById(R.id.edit_address);
        edit_contact = custom_layout.findViewById(R.id.edit_contact);
        edit_city = custom_layout.findViewById(R.id.edit_city);
        edit_country = custom_layout.findViewById(R.id.edit_country);


        builder.setView(custom_layout);

        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        builder.setPositiveButton("Order ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                address = edit_address.getText().toString();
                contact = edit_contact.getText().toString();
                city = edit_city.getText().toString();
                country = edit_country.getText().toString();

                if (TextUtils.isEmpty(address) && TextUtils.isEmpty(city)
                        && TextUtils.isEmpty(contact) && TextUtils.isEmpty(country)){

                    Toast.makeText(getContext(), "Enter All the Detail", Toast.LENGTH_SHORT).show();
                }

                else {

                    uploadDetail();

                }

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });



        builder.show();
    }


    private void uploadDetail(){



        RootRefe  = FirebaseDatabase.getInstance().getReference("Order");//

        hashMap.put("Address",address);
        hashMap.put("Contact",contact);
        hashMap.put("City",city);
        hashMap.put("CurrentId",currentUserId);
        hashMap.put("Country",country);
        hashMap.put("TotalAmount",String.valueOf(amount));
         id = UUID.randomUUID().toString();



        RootRefe.child(id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);

                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cart").child("UserView").child("Products").child(currentUserId);

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot snapshot:dataSnapshot.getChildren())
                            {   Cart cart = snapshot.getValue(Cart.class);
                                HashMap<String, Object> hashmap3 = new HashMap<>();
                                hashmap3.put("Price", cart.getPrice());
                                hashmap3.put("ProductName",cart.getProductName());
                                hashmap3.put("ProductQuantity", cart.getProductQuantity());


                                RootRefe.child(id).child("products").push().setValue(hashmap3);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Cart").child("AdminView").child("Products").child(currentUserId);

                    sendNotification(id);

                      db.removeValue();
                      dbref.removeValue();


                }

            }
        });




    }

    private void sendNotification(final String id) {

        final DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Token");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Token serverToken = snapshot.getValue(Token.class);
                    Notification notification = new Notification("You Have a new Order \n "+id,"E-commerce App");

//                    Toast.makeText(getContext(), "worrrrrrrrrking 22222", Toast.LENGTH_SHORT).show();

                    Sender content = new Sender(serverToken.getToken(),notification);

                    mService.sendNotification(content)
                            .enqueue(new retrofit2.Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if (response.code() == 200) {

                                        if (response.body().success == 1) {

                                            Toast.makeText(getContext(), "Thank You,Order PLACEd", Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                    Log.e("ERROR",t.getMessage());


                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();


        amount = 0;
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Cart").child("UserView").child("Products").child(currentUserId);
        database.keepSynced(true);

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(database, Cart.class).build();

        adapter = new FirebaseRecyclerAdapter<Cart, Fragment_Cart.UsersViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final Fragment_Cart.UsersViewHolder holder, int position, @NonNull final Cart cart) {

               selected_user_id = getRef(position).getKey();
//           id = selected_user_id;

                database.child(selected_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.hasChild("ProductImage")) {
                            linearLayout.setVisibility(View.INVISIBLE);

                            relativeLayout.setVisibility(View.VISIBLE);

                            CartId = dataSnapshot.child("CartId").getValue().toString();
                            ProductName = dataSnapshot.child("ProductName").getValue().toString();
                            ProductPrice = dataSnapshot.child("Price").getValue().toString();
                            ProductQuantity = dataSnapshot.child("ProductQuantity").getValue().toString();
                            ProductOrginal = cart.getPrice();



//                            hashMap.put("CArt Id",CartId);

                            int orginal = (Integer.valueOf(ProductOrginal))/Integer.valueOf(ProductQuantity);
                            holder.uorginal.setText(String.valueOf(orginal));

                            holder.uquant.setText(ProductQuantity);


                            holder.uName.setText(ProductName);

                            holder.uprice.setText(ProductOrginal);

                            ProductImage = dataSnapshot.child("ProductImage").getValue().toString();


                            amount = amount + (Integer.valueOf(cart.getPrice()));

                            total.setText(String.valueOf(amount));


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




            }


            @NonNull
            @Override
            public Fragment_Cart.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

                return new Fragment_Cart.UsersViewHolder(view);

            }

        };


        recycler_cart.setAdapter(adapter);
        adapter.startListening();

        adapter.notifyDataSetChanged();
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public CircleImageView profileImg;

        TextView uName, uStatus, uprice,uquant,uorginal;
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            mView.setOnCreateContextMenuListener(this);

            uprice = mView.findViewById(R.id.cart_product_total_price);

            uquant = mView.findViewById(R.id.cart_product_quantity);

            uorginal = mView.findViewById(R.id.cart_product_orginal_price);

            uName = mView.findViewById(R.id.cart_product_name);

            profileImg = mView.findViewById(R.id.cart_product_image);


        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "DELETE");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("DELETE")) {

            DeleteDialog(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);


    }

    private void DeleteDialog(String ref) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Cart").child("AdminView").child("Products").child(currentUserId);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Cart").child("UserView").child("Products").child(currentUserId);

        database.child(ref).removeValue();
        db.child(ref).removeValue();
        onResume();
        relativeLayout.setVisibility(View.INVISIBLE);

        total.setText(String.valueOf(amount));


    }




}

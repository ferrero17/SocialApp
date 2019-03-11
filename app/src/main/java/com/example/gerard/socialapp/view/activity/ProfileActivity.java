package com.example.gerard.socialapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gerard.socialapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    TextView tvUsername;
    ImageView ivUserAvatar;
    //
    TextView tvCifraPostsTotal;
    TextView tvCifraFavs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.username);
        ivUserAvatar = findViewById(R.id.useravatar);
        tvCifraPostsTotal = findViewById(R.id.cifraPublicados);
        tvCifraFavs = findViewById(R.id.cifraFav);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            tvUsername.setText(firebaseUser.getDisplayName());
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl().toString())
                    .into(ivUserAvatar);


            findViewById(R.id.sign_out2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AuthUI.getInstance()
                            .signOut(ProfileActivity.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
            });

            FirebaseDatabase.getInstance().getReference().child("posts").child("user-posts").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String total = String.valueOf(dataSnapshot.getChildrenCount());
                    tvCifraPostsTotal.setText(total);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });



            FirebaseDatabase.getInstance().getReference().child("posts").child("user-likes").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String totalFavs = String.valueOf(dataSnapshot.getChildrenCount());
                    tvCifraFavs.setText(totalFavs);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }
    }
}

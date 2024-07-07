package com.example.peprm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_user);

        //take userID, print it, yay
        Intent intent = getIntent();
        //int userId = intent.getIntExtra("USER_ID", -1);
        String userEmail = intent.getStringExtra("USER_EMAIL");
        String userFirstName = intent.getStringExtra("USER_FIRST_NAME");
        String userLastName = intent.getStringExtra("USER_LAST_NAME");
        String userAvatar = intent.getStringExtra("USER_AVATAR");

        TextView textViewUserName = findViewById(R.id.text_view_user_name);
        TextView textViewUserEmail = findViewById(R.id.text_view_user_email);
        ImageView imageViewUserAvatar = findViewById(R.id.image_view_user_avatar);

        textViewUserName.setText(userFirstName + " " + userLastName);
        textViewUserEmail.setText(userEmail);
        Glide.with(this).load(userAvatar).into(imageViewUserAvatar); // Using Glide to load user avatar


        Button BackBtn = findViewById(R.id.BackBtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailUserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
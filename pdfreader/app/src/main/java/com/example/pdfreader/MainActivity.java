package com.example.pdfreader;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase Database initialization
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Write a message to the database
        myRef.setValue("Hello, World!");

        // Find the TextView and Button
        TextView textView = findViewById(R.id.textView);
        Button logoutButton = findViewById(R.id.logoutButton);

        // Read the value from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the value from the database and update the TextView
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Log an error if the read is cancelled
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Handle logout button click
        logoutButton.setOnClickListener(v -> {
            // Sign out from Firebase Auth
            firebaseAuth.signOut();

            // Navigate back to Login activity
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}

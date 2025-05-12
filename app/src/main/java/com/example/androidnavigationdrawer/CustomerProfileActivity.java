package com.example.androidnavigationdrawer;



import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidnavigationdrawer.DatabaseHelper;
import com.example.androidnavigationdrawer.Client;

public class CustomerProfileActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private FrameLayout frameLayoutProfile; // Changed from ImageView to FrameLayout
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Customer Profile");
        }

        // Initialize views
        textViewName = findViewById(R.id.textViewProfileName);
        textViewEmail = findViewById(R.id.textViewProfileEmail);
        textViewPhone = findViewById(R.id.textViewProfilePhone);
        frameLayoutProfile = findViewById(R.id.imageViewProfile); // Changed variable name to match type

        // Get client ID from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("client_id")) {
            int clientId = intent.getIntExtra("client_id", -1);
            if (clientId != -1) {
                loadClientData(clientId);
            }
        }
    }

    private void loadClientData(int clientId) {
        Client client = databaseHelper.getClient(clientId);
        if (client != null) {
            textViewName.setText(client.getName());
            textViewEmail.setText(client.getEmail());
            textViewPhone.setText(client.getPhone());

            // Set the first letter of the client's name as the profile image
            if (client.getName() != null && !client.getName().isEmpty()) {
                TextView textViewInitial = findViewById(R.id.textViewInitial);
                textViewInitial.setText(String.valueOf(client.getName().charAt(0)).toUpperCase());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
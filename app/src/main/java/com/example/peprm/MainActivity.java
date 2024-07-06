package com.example.peprm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import Adapter.UsersAdapter;
import Api.ApiClient;
import Api.ApiService;
import models.UsersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<UsersResponse.User> userList = new ArrayList<>();
    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this, userList);
        recyclerView.setAdapter(usersAdapter);

        editTextName = findViewById(R.id.editTextText);
        editTextEmail = findViewById(R.id.editTextText2);
        buttonSearch = findViewById(R.id.button);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                searchUsers(name, email);
            }
        });

        fetchUsers();
    }

    private void fetchUsers() {
        ApiClient apiClient = new ApiClient();
        ApiService apiService = apiClient.getApiService();

        apiService.getUsers(2).enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    UsersResponse usersResponse = response.body();
                    if (usersResponse != null) {
                        userList.addAll(usersResponse.getData());
                        usersAdapter.updateList(userList);
                    } else {
                        Log.e(TAG, "UsersResponse is null");
                    }
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Log.e(TAG, "Request failed: " + t.getMessage(), t);
            }
        });
    }

    private void searchUsers(String name, String email) {
        Log.d(TAG, "Search called with name: " + name + ", email: " + email);

        if (name.isEmpty() && email.isEmpty()) {
            Log.d(TAG, "Both fields empty, displaying all users");
            usersAdapter.resetList();
            return;
        }

        List<UsersResponse.User> searchedList = new ArrayList<>();

        for (UsersResponse.User user : userList) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            boolean matchesName = !name.isEmpty() && fullName.toLowerCase().contains(name.toLowerCase());
            boolean matchesEmail = !email.isEmpty() && user.getEmail().toLowerCase().contains(email.toLowerCase());

            Log.d(TAG, "User: " + fullName + ", Email: " + user.getEmail());
            Log.d(TAG, "Matches name: " + matchesName + ", Matches email: " + matchesEmail);

            if ((name.isEmpty() || matchesName) && (email.isEmpty() || matchesEmail)) {
                searchedList.add(user);
            }
        }
        Log.d(TAG, "Search results: " + searchedList.size());
        usersAdapter.updateList(searchedList);
    }
}


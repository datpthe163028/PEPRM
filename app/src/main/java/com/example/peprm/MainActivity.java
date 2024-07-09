package com.example.peprm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private Spinner spinnerPage;
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<UsersResponse.User> userList = new ArrayList<>();
    private EditText editTextName;
    private EditText editTextEmail;
    private Button buttonSearch;
    private int itemsPerPage = 2;
    private int count = 0;
    private DatabaseHelper databaseHelper;
    private Button buttonDeleteCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        fetchUsers();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this, userList);
        recyclerView.setAdapter(usersAdapter);

        editTextName = findViewById(R.id.editTextText);
        editTextEmail = findViewById(R.id.editTextText2);
        buttonSearch = findViewById(R.id.button);
        buttonDeleteCache = findViewById(R.id.button2);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                searchUsers(name, email);
            }
        });
        buttonDeleteCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.clearCache();
            }
        });
    }

    private void fetchUsers() {
        if (databaseHelper.isCacheAvailable()) {
            Log.i("Caching", "Loading data from cache");
            userList.clear();
            userList.addAll(databaseHelper.getAllUsers());
            populateSpinner((int)Math.ceil((double) userList.size() / itemsPerPage))
            ;
        } else {
            Log.i("Caching", "Fetching data from API");
            ApiClient apiClient = new ApiClient();
            ApiService apiService = apiClient.getApiService();

            apiService.getUsers(2).enqueue(new Callback<UsersResponse>() {
                @Override
                public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                    if (response.isSuccessful()) {
                        UsersResponse usersResponse = response.body();
                        if (usersResponse != null) {
                            userList.addAll(usersResponse.getData());

                            for (UsersResponse.User user : usersResponse.getData()) {
                                databaseHelper.addUser(user);
                            }

                            populateSpinner((int)Math.ceil((double) userList.size() / itemsPerPage));
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsersResponse> call, Throwable t) {
                    Log.e(TAG, "API call failed: " + t.getMessage());
                }
            });
        }
    }

    private void pagination(int startIndex, int endIndex){
        List<UsersResponse.User> list = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            list.add(userList.get(i));
        }
        usersAdapter.updateList(list);
    }


    private void populateSpinner(int countPage) {
        Spinner spinner = findViewById(R.id.spinner);
        List<String> pages = new ArrayList<>();

        for(int i = 1; i <= countPage; i++)
            pages.add(i + "");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPage = parentView.getItemAtPosition(position).toString();
                int startIndex = (Integer.parseInt(selectedPage) - 1 ) * itemsPerPage;
                int endIndex = Math.min(startIndex + itemsPerPage, userList.size());
                pagination(startIndex, endIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không làm gì cả
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


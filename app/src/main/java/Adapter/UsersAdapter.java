package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.peprm.DetailUserActivity;
import com.example.peprm.R;

import java.util.ArrayList;
import java.util.List;

import models.UsersResponse;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private Context context;
    private List<UsersResponse.User> userList;
    private List<UsersResponse.User> originalUserList;

    public UsersAdapter(Context context, List<UsersResponse.User> userList) {
        this.context = context;
        this.userList = new ArrayList<>(userList);
        this.originalUserList = new ArrayList<>(userList);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UsersResponse.User user = userList.get(position);
        holder.textViewName.setText(user.getFirstName() + " " + user.getLastName());
        holder.textViewEmail.setText(user.getEmail());
        Glide.with(context).load(user.getAvatar()).into(holder.imageViewAvatar);
        holder.b1.setId(user.getId());
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("USER_ID", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<UsersResponse.User> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }

    public void resetList() {
        userList.clear();
        userList.addAll(originalUserList);
        notifyDataSetChanged();
    }
}


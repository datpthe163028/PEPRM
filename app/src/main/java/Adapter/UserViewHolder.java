package Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peprm.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView textViewName;
    TextView textViewEmail;
    ImageView imageViewAvatar;

    public UserViewHolder(View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.text_view_name);
        textViewEmail = itemView.findViewById(R.id.text_view_email);
        imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
    }
}
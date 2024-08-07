package Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peprm.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView textViewName;
    ImageView imageViewAvatar;
    Button b1;

    public UserViewHolder(View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.text_view_name);
        imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
        b1 = itemView.findViewById(R.id.buttondat);
    }
}
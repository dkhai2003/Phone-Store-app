package com.example.duan1.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.MemberGroup2;

import java.util.List;

public class AboutSlideAdapter extends RecyclerView.Adapter<AboutSlideAdapter.MemViewHolder> {
    private final List<MemberGroup2> mList;

    public AboutSlideAdapter(List<MemberGroup2> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_about_us, parent, false);
        return new MemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemViewHolder holder, int position) {
        MemberGroup2 mem = mList.get(position);
        if (mem == null) {
            return;
        }
        holder.imgPhoto.setImageResource(mem.getImg());
        holder.item_name.setText(String.format("Name: %s", mem.getName()));
        holder.item_address.setText(String.format("Address: %s", mem.getAddress()));
        holder.item_dob.setText(String.format("Date of birth: %s", mem.getDob()));
        holder.item_spe.setText(String.format("Specialized: %s", mem.getSpe()));
        holder.item_role.setText(String.format("Role: %s", mem.getRole()));
        holder.btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mem.getLink());
                view.getContext().startActivities(new Intent[]{new Intent(Intent.ACTION_VIEW, uri)});
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public static class MemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPhoto;
        private TextView item_name, item_dob, item_address, item_spe, item_role;
        private Button btnFB;

        public MemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_slide);
            item_name = itemView.findViewById(R.id.item_name);
            item_dob = itemView.findViewById(R.id.item_dob);
            item_address = itemView.findViewById(R.id.item_address);
            item_spe = itemView.findViewById(R.id.item_spe);
            item_role = itemView.findViewById(R.id.item_role);
            btnFB = itemView.findViewById(R.id.btnFB);
        }
    }
}

package com.example.duan1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.HoaDon;
import com.example.duan1.model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HistoryAdapter  extends FirebaseRecyclerAdapter<HoaDon, HistoryAdapter.myViewHolder> {

    private final IClickHistory iClickHistory;

    public interface IClickHistory {
        void onClickShowListProduct(HoaDon hoaDon);
    }


    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<HoaDon> options, IClickHistory iClickHistory) {
        super(options);
        this.iClickHistory = iClickHistory;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull HoaDon model) {
        holder.maHoaDon.setText("Mã hóa đơn: "+model.getMaHoaDon().toUpperCase());
        holder.gia.setText("Tổng giá: "+model.getToTal());
        holder.ngay.setText("Thời gian: "+model.getDate());
        holder.soLuong.setText("Tổng sản phẩm: "+model.getSoLuong()+"");

        holder.card_view_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickHistory.onClickShowListProduct(model);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);


        return new HistoryAdapter.myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView maHoaDon , ngay, soLuong, gia;
        CardView  card_view_history;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            maHoaDon = itemView.findViewById(R.id.tvMaHoaDon);
            ngay = itemView.findViewById(R.id.tvDateHistory);
            gia = itemView.findViewById(R.id.tvTotalHistory);
            soLuong = itemView.findViewById(R.id.tvAmountHistory);
            card_view_history = itemView.findViewById(R.id.card_view_history);
        }
    }
}

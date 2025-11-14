package com.lr3_428_04;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr3_428_04.model.RoomItem;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<RoomItem> rooms;

    public RoomAdapter(List<RoomItem> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomItem room = rooms.get(position);
        holder.tvRoom.setText(room.getRoomType() + " " + room.getRoomNumber());
        holder.tvEquipType.setText(room.getEquipType());
        holder.tvEquipBrand.setText(room.getEquipBrand());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RoomEditActivity.class);
            intent.putExtra("room", room);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoom;
        TextView tvEquipType;
        TextView tvEquipBrand;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvEquipType = itemView.findViewById(R.id.tv_equip_type);
            tvEquipBrand = itemView.findViewById(R.id.tv_equip_brand);
        }
    }
}

package org.techtown.ThreeMate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>
        implements OnFoodItemClickListener {
    ArrayList<FD> items = new ArrayList<FD>();

    OnFoodItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.food_diary, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FD fd = items.get(position);
        viewHolder.setItem(fd);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FD fd) {
        items.add(fd);
    }
    public void removeItem(){
        items.clear();
    }
    public void removeItem2(int position){
        items.remove(position);
    }

    public void setItems(ArrayList<FD> items) {
        this.items = items;
    }

    public FD getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, FD fd) {
        items.set(position, fd);
    }

    public void setOnItemClickListener(OnFoodItemClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);

        }
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        ImageView iconImageView;

        public ViewHolder(View itemView, final OnFoodItemClickListener listener) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(FD fd) {

            if (fd.getIcon().equals("null") || fd.getIcon().equals("")){
                Glide.with(itemView).load("https://cdn.pixabay.com/photo/2018/02/24/10/03/orange-3177693_960_720.png").override(300).into(iconImageView);
            }else {
                Glide.with(itemView).load(fd.getIcon()).override(300).into(iconImageView);
            }
            textView.setText(fd.getName());
            textView2.setText(fd.getKcal());
            textView3.setText(fd.getCarbs());
            textView4.setText(fd.getProtein());
            textView5.setText(fd.getFat());
        }

    }

}

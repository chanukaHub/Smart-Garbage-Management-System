package com.usj.smartgarbagemanagementsystem.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.usj.smartgarbagemanagementsystem.R;
import com.usj.smartgarbagemanagementsystem.model.Bin;

public class BinAdapter extends FirebaseRecyclerAdapter<Bin,BinAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BinAdapter(@NonNull FirebaseRecyclerOptions<Bin> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Bin model) {
        Drawable empty_drawable = ContextCompat.getDrawable(holder.imageView1.getContext(),R.drawable.empty_bin);
        Drawable full_drawable = ContextCompat.getDrawable(holder.imageView1.getContext(),R.drawable.full_bin);
        Drawable half_drawable = ContextCompat.getDrawable(holder.imageView1.getContext(),R.drawable.half_bin);
        Drawable unlock_drawable = ContextCompat.getDrawable(holder.imageView2.getContext(),R.drawable.unlock_icon);
        Drawable lock_drawable = ContextCompat.getDrawable(holder.imageView2.getContext(),R.drawable.lock_icon);

        holder.textView1.setText(String.valueOf(model.id));

        switch (model.getFillingPercentage()){
            case 100:
                Glide.with(holder.imageView1.getContext()).load(full_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is 100% Full");
                break;
            case 90:
                Glide.with(holder.imageView1.getContext()).load(full_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is 90% Full");
                break;
            case 75:
                Glide.with(holder.imageView1.getContext()).load(half_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is 75% Full");
                break;
            case 50:
                Glide.with(holder.imageView1.getContext()).load(half_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is 50% Full");
                break;
            case 25:
                Glide.with(holder.imageView1.getContext()).load(empty_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is 25% Full");
                break;
            case 0:
                Glide.with(holder.imageView1.getContext()).load(empty_drawable).into(holder.imageView1);
                holder.textView2.setText("Bin is Empty");
                break;
        }

        switch (model.lock){
            case 1:
                Glide.with(holder.imageView2.getContext()).load(lock_drawable).into(holder.imageView2);
                break;
            case 0:
                Glide.with(holder.imageView2.getContext()).load(unlock_drawable).into(holder.imageView2);
                break;

        }

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_bin,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2;
        ImageView imageView1,imageView2;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);
            imageView1 = itemView.findViewById(R.id.image_view1);
            imageView2 = itemView.findViewById(R.id.image_view2);
        }
    }
}

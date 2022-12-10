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
        Drawable empty_drawable = ContextCompat.getDrawable(holder.imageView.getContext(),R.drawable.empty_bin);
        Drawable full_drawable = ContextCompat.getDrawable(holder.imageView.getContext(),R.drawable.full_bin);
        Drawable half_drawable = ContextCompat.getDrawable(holder.imageView.getContext(),R.drawable.half_bin);

        holder.textView.setText(String.valueOf(model.getFillingPercentage()));

        switch (model.getFillingPercentage()){
            case 100:
            case 90:
                Glide.with(holder.imageView.getContext()).load(full_drawable).into(holder.imageView);
                break;
            case 75:
            case 50:
                Glide.with(holder.imageView.getContext()).load(half_drawable).into(holder.imageView);
                break;
            case 25:
            case 0:
                Glide.with(holder.imageView.getContext()).load(empty_drawable).into(holder.imageView);
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
        TextView textView;
        ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view1);
            imageView = itemView.findViewById(R.id.image_view1);
        }
    }
}

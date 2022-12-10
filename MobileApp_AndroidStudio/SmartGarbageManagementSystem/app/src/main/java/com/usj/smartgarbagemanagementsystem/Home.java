package com.usj.smartgarbagemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.FirebaseDatabase;
import com.usj.smartgarbagemanagementsystem.adapters.BinAdapter;
import com.usj.smartgarbagemanagementsystem.model.Bin;


public class Home extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private BinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView =findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseRecyclerOptions<Bin> options =
                new FirebaseRecyclerOptions.Builder<Bin>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bins"), Bin.class)
                        .build();
        adapter = new BinAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}
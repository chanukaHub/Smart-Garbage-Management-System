package com.usj.smartgarbagemanagementsystem;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.smartgarbagemanagementsystem.model.Bin;

import java.util.List;

public class BinPage extends AppCompatActivity {

    private DatabaseReference reference;
    Bin bin;
    TextView textView1,textView2,textViewId,textViewPercentage,textViewStatus,textViewLatitude,textViewLongitude;
    ImageView imageView1,imageView2;
    Button locateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_page);

        Drawable empty_drawable = getResources().getDrawable(R.drawable.empty_bin);
        Drawable full_drawable = getResources().getDrawable(R.drawable.full_bin);
        Drawable half_drawable = getResources().getDrawable(R.drawable.half_bin);
        Drawable unlock_drawable = getResources().getDrawable(R.drawable.unlock_icon);
        Drawable lock_drawable = getResources().getDrawable(R.drawable.lock_icon);

        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        imageView1 = findViewById(R.id.image_view1);
        imageView2 =findViewById(R.id.image_view2);
        textViewId =findViewById(R.id.text_view_id);
        textViewPercentage =findViewById(R.id.text_view_filling_percentage);
        textViewStatus =findViewById(R.id.text_view_lock_status);
        textViewLatitude =findViewById(R.id.text_view_latitude);
        textViewLongitude =findViewById(R.id.text_view_longitude);
        locateBtn =findViewById(R.id.locate_btn);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        reference= FirebaseDatabase.getInstance().getReference("Bins/"+id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = dataSnapshot.child("id").getValue(String.class);
                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                int duration = dataSnapshot.child("duration").getValue(Integer.class);
                int lock = dataSnapshot.child("lock").getValue(Integer.class);

                bin = new Bin(id,latitude,longitude,duration,lock);

                textView1.setText(String.valueOf(id));

                switch (bin.getFillingPercentage()){
                    case 100:
                        Glide.with(imageView1.getContext()).load(full_drawable).into(imageView1);
                        textView2.setText("Bin is 100% Full");
                        textViewPercentage.setText("100%");
                        break;
                    case 90:
                        Glide.with(imageView1.getContext()).load(full_drawable).into(imageView1);
                        textView2.setText("Bin is 90% Full");
                        textViewPercentage.setText("90%");
                        break;
                    case 75:
                        Glide.with(imageView1.getContext()).load(half_drawable).into(imageView1);
                        textView2.setText("Bin is 75% Full");
                        textViewPercentage.setText("75%");
                        break;
                    case 50:
                        Glide.with(imageView1.getContext()).load(half_drawable).into(imageView1);
                        textView2.setText("Bin is 50% Full");
                        textViewPercentage.setText("50%");
                        break;
                    case 25:
                        Glide.with(imageView1.getContext()).load(empty_drawable).into(imageView1);
                        textView2.setText("Bin is 25% Full");
                        textViewPercentage.setText("25%");
                        break;
                    case 0:
                        Glide.with(imageView1.getContext()).load(empty_drawable).into(imageView1);
                        textView2.setText("Bin is Empty");
                        textViewPercentage.setText("0%");
                        break;
                }

                switch (lock){
                    case 1:
                        textViewStatus.setText("Lock");
                        Glide.with(imageView2.getContext()).load(lock_drawable).into(imageView2);
                        break;
                    case 0:
                        textViewStatus.setText("Unlock");
                        Glide.with(imageView2.getContext()).load(unlock_drawable).into(imageView2);
                        break;

                }
                textViewId.setText(id);
                textViewLatitude.setText(String.valueOf(latitude));
                textViewLongitude.setText(String.valueOf(longitude));

                locateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String label ="Bin ID: "+id;
                        String uriBegin = "geo:"+latitude+","+longitude;
                        String query = latitude+","+longitude+"(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery;
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}
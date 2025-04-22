package com.example.doan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.Adapter;

import java.util.ArrayList;
import java.util.List;

public class Dishes_View extends AppCompatActivity {
    RecyclerView recyclerView;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_view);

        recyclerView = findViewById(R.id.recycler_view);

        AddAllTitles();
        AddAllImages();

        adapter = new Adapter(this, titles, images, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void AddAllTitles()
    {
        titles = new ArrayList<>();
        titles.add("Celery Soup");
        titles.add("Dimsum Soup");
        titles.add("Kale Soup");
        titles.add("Mushroom soup");
        titles.add("Pumpkin Soup");
        titles.add("Tomato Soup");
    }

    private void AddAllImages()
    {
        images = new ArrayList<>();
        images.add(R.drawable.celery_soup);
        images.add(R.drawable.dimsum_soup);
        images.add(R.drawable.kale_soup);
        images.add(R.drawable.mashroom_soup);
        images.add(R.drawable.pumpkin_soup);
        images.add(R.drawable.tomato_soup);
    }
}
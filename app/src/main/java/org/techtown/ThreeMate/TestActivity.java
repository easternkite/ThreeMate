package org.techtown.ThreeMate;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PersonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonAdapter();
       adapter.addItem(new Item(R.drawable.ic_icon1, "dd", "dd", "dd", "dd"));
       adapter.addItem(new Item(R.drawable.ic_icon1, "dd", "dd", "dd", "dd"));
       adapter.addItem(new Item(R.drawable.ic_icon1, "dd", "dd", "dd", "dd"));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(PersonAdapter.ViewHolder holder, View view, int position) {

            }
        });
    }
}
package com.zzu.luanchuan.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.zzu.luanchuan.R;
import com.zzu.luanchuan.adapter.EvaluateAdapter;
import com.zzu.luanchuan.beans.EvaluateItem;

import java.util.ArrayList;

@SuppressWarnings("all")
public class Main extends Base {
    private Toolbar toobar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        ArrayList<Uri> uri_list = null;
        String text = null;
        if (intent != null) {
          text =   intent.getStringExtra("text");
            uri_list = intent.getParcelableArrayListExtra("image");

        }
        initialize();
        EvaluateItem evi = null;
        ArrayList<EvaluateItem> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            evi = new EvaluateItem();
            evi.setEvaluate_img_uris(uri_list);
            evi.setEvaluate_content(text);
            list.add(evi);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EvaluateAdapter ava = new EvaluateAdapter(list,Main.this);
        recyclerView.setAdapter(ava);


    }

    private void initialize() {

        toobar = $(R.id.toolbar);
        toobar.setTitle("用户评价");
        recyclerView = $(R.id.recycler_view);
        setSupportActionBar(toobar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(Main.this, "haah", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

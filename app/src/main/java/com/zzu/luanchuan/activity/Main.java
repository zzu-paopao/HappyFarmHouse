package com.zzu.luanchuan.activity;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.zzu.luanchuan.R;
@SuppressWarnings("all")
public class Main extends Base{
    private Toolbar toobar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialize();
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

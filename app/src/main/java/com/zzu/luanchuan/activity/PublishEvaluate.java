package com.zzu.luanchuan.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.ImageEngine;
import com.zzu.luanchuan.R;

public class PublishEvaluate extends Base {
    private static final int REQUEST_ADD_IMAGE = 2;
    EditText edit_evaluate_content;
    MultiPictureView multiPictureView;
    private int MY_PERMISSIONS_REQUEST = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_evaluate);

        Toolbar toolbar =  $(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        edit_evaluate_content = $(R.id.edit_evaluate_content);
        multiPictureView =  $(R.id.multiple_image);
        // 如果不想显示删除按钮，可以这样写
//        multiPictureView.setDeleteResource(0);
        multiPictureView.setDeleteResource(R.drawable.delete);
        multiPictureView.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                addImage();
            }
        });

        FloatingActionButton fab = $(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishEvaluate.this,Main.class);
                intent.putExtra("text", edit_evaluate_content.getText().toString());
                intent.putParcelableArrayListExtra("image", multiPictureView.getList());
//                setResult(RESULT_OK,intent);
//                onBackPressed();

                startActivity(intent);
            }
        });
    }

    void addImage() {
        if (ContextCompat.checkSelfPermission(PublishEvaluate.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        } else {
            Matisse.from(PublishEvaluate.this)
                    .choose(MimeType.allOf())
                    .maxSelectable(9 - multiPictureView.getCount())
                    .thumbnailScale(0.85f)
                    .theme(R.style.my_zhihu_theme)
                    .imageEngine(new ImageEngine() {
                        @Override
                        public void loadThumbnail(Context context, int i, Drawable drawable, ImageView imageView, Uri uri) {
                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadAnimatedGifThumbnail(Context context, int i, Drawable drawable, ImageView imageView, Uri uri) {

                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadImage(Context context, int i, int i1, ImageView imageView, Uri uri) {

                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadAnimatedGifImage(Context context, int i, int i1, ImageView imageView, Uri uri) {

                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.loading)
                                    .into(imageView);
                        }

                        @Override
                        public boolean supportAnimatedGif() {
                            return false;
                        }
                    })
                    .forResult(REQUEST_ADD_IMAGE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_IMAGE && resultCode == RESULT_OK) {
            multiPictureView.addItem(Matisse.obtainResult(data));
        }
    }

}

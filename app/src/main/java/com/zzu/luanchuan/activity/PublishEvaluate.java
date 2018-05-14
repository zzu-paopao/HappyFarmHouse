package com.zzu.luanchuan.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.ImageEngine;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.constant.Constants;
import com.zzu.luanchuan.utils.MyToast;
import com.zzu.luanchuan.utils.StorageUtils;

import java.util.ArrayList;

public class PublishEvaluate extends Base {

    EditText edit_evaluate_content;
    MultiPictureView multiPictureView;
    private Uri temp_imgUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_evaluate);

        Toolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        View view = getLayoutInflater().inflate(
                R.layout.select_img, null);
        final Dialog dialog = new Dialog(this,
                R.style.transparentFrameWindowStyle);
        //为view中控件添加点击相应
        Button btnCancle = view.findViewById(R.id.btn_photochoose_cancle);
        Button btn_photochoose_photobox = view.findViewById(R.id.btn_photochoose_photobox);
        Button btn_photochoose_take = view.findViewById(R.id.btn_photochoose_take);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_photochoose_photobox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addImage();

            }
        });
        btn_photochoose_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                MyToast.showToast(PublishEvaluate.this, "Build.VERSION.SDK_INT"+Build.VERSION.SDK_INT);
                if ((ContextCompat.checkSelfPermission(PublishEvaluate.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)  || (ContextCompat.checkSelfPermission(PublishEvaluate.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(PublishEvaluate.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA},
                            Constants.REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA_MOUNT_UNMOUNT_FILESYSTEMS);
                } else {
                   /*shouldShowRequestPermissionRationale();*/
                    temp_imgUri = StorageUtils.get_temp_photo_uri();
                    startActivityForResult(get_take_photo_intent(temp_imgUri), Constants.REQUEST_TAKE_PHOTO);
                }

            }
        });

        // 传入界面
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();//得到分层窗口
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = LinearLayout.LayoutParams.MATCH_PARENT;
        wl.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);


        edit_evaluate_content = $(R.id.edit_evaluate_content);
        multiPictureView = $(R.id.multiple_image);
        // 如果不想显示删除按钮，可以这样写
//        multiPictureView.setDeleteResource(0);
        multiPictureView.setDeleteResource(R.drawable.delete);
        multiPictureView.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                dialog.show();

            }
        });

        FloatingActionButton fab = $(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishEvaluate.this, ShowEvaluate.class);
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
                    Constants.REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Matisse.from(PublishEvaluate.this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
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
                    .forResult(Constants.REQUEST_ADD_IMAGE);
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
        if (requestCode == Constants.REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addImage();
            }
        } else if (requestCode == Constants.REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA_MOUNT_UNMOUNT_FILESYSTEMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                temp_imgUri = StorageUtils.get_temp_photo_uri();
                startActivityForResult(get_take_photo_intent(temp_imgUri), Constants.REQUEST_TAKE_PHOTO);
            }else {
                MyToast.showToast(this,"meiyouquanxian"+grantResults[0]+grantResults[1]);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_ADD_IMAGE && resultCode == RESULT_OK) {
            multiPictureView.addItem(Matisse.obtainResult(data));
        } else if (requestCode == Constants.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            startActivityForResult(
                    cropPhoto(temp_imgUri, temp_imgUri, 300, 400),
                    Constants.REQUEST_CROP_PHOTO);

        }else if(requestCode == Constants.REQUEST_CROP_PHOTO && resultCode == RESULT_OK){
            ArrayList<Uri> list = new ArrayList<Uri>();
            list.add(temp_imgUri);
            multiPictureView.addItem(list);
        }
    }

    private Intent get_take_photo_intent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;

    }

    private  Intent cropPhoto(Uri uri, Uri cropUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
        intent.putExtra("aspectX", 1); //裁剪的宽比例
        intent.putExtra("aspectY", 1);  //裁剪的高比例
        intent.putExtra("outputX", outputX); //裁剪的宽度
        intent.putExtra("outputY", outputY);  //裁剪的高度
        intent.putExtra("scale", true); //支持缩放
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);  //将裁剪的结果输出到指定的Uri
        intent.putExtra("return-data", true); //若为true则表示返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁剪成的图片的格式
        intent.putExtra("noFaceDetection", true);  //启用人脸识别
        return intent;
    }
}

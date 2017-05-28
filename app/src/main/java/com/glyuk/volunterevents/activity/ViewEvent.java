package com.glyuk.volunterevents.activity;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.adpter.ImgAdapter;
import com.glyuk.volunterevents.model.ImageItem;
import com.glyuk.volunterevents.model.ModelEvents;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class ViewEvent extends AppCompatActivity {

    private TextView tvEventDetailName, tvEventDetailDescription, tvEventDetailDate, tvEventDetailNumberMember;
    private ImageView ivEventDetailImage;
    private FloatingActionButton fab;
    private ModelEvents eventDetailsModel =new ModelEvents();

    private ListView listView;
    ArrayList<String> filePath = new ArrayList<String>();
    private Button btnGallery;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.relative);
        tabSpec.setIndicator("Описание");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.liner);
        tabSpec.setIndicator("Фотографии");
        tabHost.addTab(tabSpec);


        getAllWidgets();
        getDataFromPreviousActivity();
        setDataInWidgets();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Спасибо. Ждем Вас на событии. Скоро с Вами свяжется куратор!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.lv_gallery);
        btnGallery = (Button) findViewById(R.id.btn_gallery);

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            // do your stuff..

            btnGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filePath.clear();
                    FilePickerBuilder.getInstance().setMaxCount(15)
                            .setSelectedFiles(filePath)
                            .setActivityTheme(R.style.AppTheme)
                            .pickPhoto(ViewEvent.this);            }
            });

        } else {
            System.out.print("MY error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    filePath = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    ImageItem item;
                    ArrayList<ImageItem> imageItems = new ArrayList<>();

                    try {
                        for (String path : filePath) {
                            item = new ImageItem();
                            //item.setNameImg(path.substring(path.lastIndexOf("/") + 1));

                            item.setUriImg(Uri.fromFile(new File(path)));
                            imageItems.add(item);
                        }

                        listView.setAdapter(new ImgAdapter(this,imageItems));
                        Toast.makeText(ViewEvent.this, "Total = " + String.valueOf(imageItems.size()), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void getAllWidgets() {

        fab = (FloatingActionButton) findViewById(R.id.fab);
        ivEventDetailImage = (ImageView) findViewById(R.id.view_image);
        tvEventDetailName = (TextView) findViewById(R.id.view_name);
        tvEventDetailDescription = (TextView) findViewById(R.id.view_description);
        tvEventDetailDate = (TextView) findViewById(R.id.view_date);
        tvEventDetailNumberMember = (TextView) findViewById(R.id.view_member);
    }

    private void getDataFromPreviousActivity() {
        int personID = getIntent().getIntExtra("PersonID", -1);
        eventDetailsModel =MainActivity.getInstance().searchPerson(personID);
    }

    private void setDataInWidgets() {
        ivEventDetailImage.setImageURI(Uri.parse(eventDetailsModel.getImage().toString()));

        tvEventDetailName.setText(eventDetailsModel.getName());
        tvEventDetailDescription.setText(getString(R.string.event_description, eventDetailsModel.getDescription()));

        tvEventDetailDate.setText(getString(R.string.event_address, eventDetailsModel.getAddress()));
        tvEventDetailNumberMember.setText(getString(R.string.event_member, String.valueOf(eventDetailsModel.getMember())));
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {

        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff






                } else {
                    Toast.makeText(ViewEvent.this, "GET_IMG Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

}

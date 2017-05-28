package com.glyuk.volunterevents.adpter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.model.ImageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImgAdapter extends BaseAdapter {

    Context contextImg;
    ArrayList<ImageItem> imageItems;

    public ImgAdapter(Context contextImg, ArrayList<ImageItem> imageItems) {
        this.contextImg = contextImg;
        this.imageItems = imageItems;
    }

    @Override
    public int getCount() {
        return imageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return imageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView= LayoutInflater.from(contextImg).inflate(R.layout.gallery_model,parent,false);
        }

        final ImageItem imageItem = (ImageItem) this.getItem(position);

        //TextView nameImg = (TextView) convertView.findViewById(R.id.name_img_gallery);
        ImageView galleryImg = (ImageView) convertView.findViewById(R.id.iv_gallery);

        //nameImg.setText(imageItem.getNameImg());
        Picasso.with(contextImg).load(imageItem.getUriImg()).placeholder(R.drawable.placeholder).into(galleryImg);

/*        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contextImg, imageItem.getNameImg(), Toast.LENGTH_SHORT).show();
            }
        });*/
        return convertView;
    }
}
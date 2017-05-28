/*
package com.glyuk.volunterevents.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.activity.MainActivity;
import com.glyuk.volunterevents.activity.ViewEvent;
import com.glyuk.volunterevents.model.ModelEvents;

import java.util.zip.Inflater;

public class OtherTab extends Fragment {

    private TextView tvEventDetailName, tvEventDetailDescription, tvEventDetailDate, tvEventDetailNumberMember;
    private ImageView ivEventDetailImage;
    FloatingActionButton fab;
    private ModelEvents eventDetailsModel =new ModelEvents();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other, container, false);

        ivEventDetailImage = (ImageView) v.findViewById(R.id.view_image);
        tvEventDetailName = (TextView) v.findViewById(R.id.view_name);
        tvEventDetailDescription = (TextView) v.findViewById(R.id.view_description);
        tvEventDetailDate = (TextView) v.findViewById(R.id.view_date);
        tvEventDetailNumberMember = (TextView) v.findViewById(R.id.view_member);

        Intent intent = getActivity().getIntent();
        int personID = intent.getIntExtra("PersonID", -1);
        eventDetailsModel =MainActivity.getInstance().searchPerson(personID);

        tvEventDetailName.setText(eventDetailsModel.getName());
        tvEventDetailDescription.setText(getString(R.string.event_description, eventDetailsModel.getDescription()));

        tvEventDetailDate.setText(getString(R.string.event_address, eventDetailsModel.getAddress()));
        tvEventDetailNumberMember.setText(getString(R.string.event_member, String.valueOf(eventDetailsModel.getMember())));


        //getDataFromPreviousActivity();
        //setDataInWidgets();

        return v;
    }

    */
/*private void getDataFromPreviousActivity() {

        Intent intent = getActivity().getIntent();
        int personID = intent.getIntExtra("PersonID", -1);
        eventDetailsModel =MainActivity.getInstance().searchPerson(personID);
    }

    private void setDataInWidgets() {
        Intent intent = getActivity().getIntent();



        //ivEventDetailImage.setImageURI(Uri.parse(eventDetailsModel.getImage().toString()));

        tvEventDetailName.setText(eventDetailsModel.getName());
        tvEventDetailDescription.setText(getString(R.string.event_description, eventDetailsModel.getDescription()));

        tvEventDetailDate.setText(getString(R.string.event_address, eventDetailsModel.getAddress()));
        tvEventDetailNumberMember.setText(getString(R.string.event_member, String.valueOf(eventDetailsModel.getMember())));
    }*//*


}
*/

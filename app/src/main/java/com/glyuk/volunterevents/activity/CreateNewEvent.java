package com.glyuk.volunterevents.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.adpter.RVAdapter;
import com.glyuk.volunterevents.model.ModelEvents;
import com.glyuk.volunterevents.utility.Utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;

public class CreateNewEvent extends AppCompatActivity implements View.OnClickListener {

    static EditText dateEdit;

    private static int id = 1;
    private RVAdapter rvAdapter;
    private static ArrayList<ModelEvents> eventDetailsModelArrayList = new ArrayList<>();


    ImageView btnImageEvent;
    Button btnAdd;

    EditText etAddEventName;
    EditText etAddEventDescription;
    EditText etAddEventAddress;
    EditText etAddEventMember;

    private Realm mRealm;

    static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRealm = Realm.getInstance(this);

        dateEdit = (EditText) findViewById(R.id.date_and_time);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonTimePickerDialog(v);
                showTruitonDatePickerDialog(v);
            }
        });

        addOrUpdateEventDetailsDialog(null, getIntent().getIntExtra("position", -1));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_add:
                if (!Utility.isBlankField(etAddEventName) && !Utility.isBlankField(etAddEventDescription)
                        && !Utility.isBlankField(etAddEventAddress)
                        && !Utility.isBlankField(etAddEventMember)) {
                    ModelEvents eventDetailsModel = new ModelEvents();
                    eventDetailsModel.setImage(btnImageEvent.getTag().toString());
                    eventDetailsModel.setName(etAddEventName.getText().toString());
                    eventDetailsModel.setDescription(etAddEventDescription.getText().toString());
                    eventDetailsModel.setAddress(etAddEventAddress.getText().toString());
                    eventDetailsModel.setMember(Integer.parseInt(etAddEventMember.getText().toString()));

                    addDataToRealm(eventDetailsModel);

                    Intent intent=new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.btn_cancel:
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
        }
    }

    public void addOrUpdateEventDetailsDialog(final ModelEvents model, final int position) {


        btnImageEvent = (ImageView) findViewById(R.id.imageView);
        etAddEventName = (EditText) findViewById(R.id.name_event);
        etAddEventDescription = (EditText) findViewById(R.id.discription);
        etAddEventAddress = (EditText) findViewById(R.id.date_and_time);
        etAddEventMember = (EditText) findViewById(R.id.number_of_participants);

        //This "if" run, if user click edit card
/*        if (model != null) {
            btnImageEvent.setImageURI(Uri.parse(model.getImage()));
            etAddEventName.setText(model.getName());
            etAddEventDescription.setText(model.getDescription());
            etAddEventAddress.setText(model.getAddress());
            etAddEventMember.setText(String.valueOf(model.getMember()));
        }*/

    }

    private void addDataToRealm(ModelEvents model) {
        mRealm.beginTransaction();

        ModelEvents eventDetailsModel = mRealm.createObject(ModelEvents.class);
        eventDetailsModel.setId(id);
        eventDetailsModel.setImage(model.getImage());
        eventDetailsModel.setName(model.getName());
        eventDetailsModel.setDescription(model.getDescription());
        eventDetailsModel.setAddress(model.getAddress());
        eventDetailsModel.setMember(model.getMember());
        eventDetailsModelArrayList.add(eventDetailsModel);

        mRealm.commitTransaction();
        //rvAdapter.notifyDataSetChanged();
        id++;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                btnImageEvent.setImageBitmap(selectedImage);
                btnImageEvent.setTag(imageUri.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }









    public void showTruitonDatePickerDialog (View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        dateEdit.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        dateEdit.setText(dateEdit.getText() + " -" + hourOfDay + ":" + minute);
    }
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }


}

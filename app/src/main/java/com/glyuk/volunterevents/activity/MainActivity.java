package com.glyuk.volunterevents.activity;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.facebook.stetho.Stetho;
import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.adpter.RVAdapter;
import com.glyuk.volunterevents.model.ModelEvents;
import com.glyuk.volunterevents.utility.Utility;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


/*    private RecyclerView rv;
    private Realm mRealm;
    private static MainActivity instance;

    private static int id = 1;

    FloatingActionButton fab;
    private static ArrayList<ModelEvents> eventDetailsModelArrayList = new ArrayList<>();
    private RVAdapter rvAdapter;*/

   /*********************************************/

    private static final int RESULT_LOAD_IMG = 1;
    private static int id = 1;
    private FloatingActionButton fabAddEvent;

    private ImageButton btnImageEvent;

    private Realm mRealm;
    private RecyclerView rv;
    private static ArrayList<ModelEvents> eventDetailsModelArrayList = new ArrayList<>();
    private RVAdapter rvAdapter;
    private static MainActivity instance;
    private AlertDialog.Builder subDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);


        mRealm = Realm.getInstance(MainActivity.this);
        instance = this;

        //Realm.init(this);
/*        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("Database.realm") .schemaVersion(8) .build();
        Realm.setDefaultConfiguration(realmConfiguration);*/


        getAllWidgets();
        bindWidgetsWithEvents();
        setEventDetailsAdapter();
        getAllEvents();

    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void getAllWidgets() {
        fabAddEvent = (FloatingActionButton) findViewById(R.id.fab);
        rv=(RecyclerView)findViewById(R.id.rv);
    }

    private void bindWidgetsWithEvents() {
        fabAddEvent.setOnClickListener(this);
    }

    private void setEventDetailsAdapter() {
        rvAdapter = new RVAdapter(eventDetailsModelArrayList, MainActivity.this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                addOrUpdateEventDetailsDialog(null,-1);
                break;
        }
    }

    public void addOrUpdateEventDetailsDialog(final ModelEvents model, final int position) {

        //subdialog
        subDialog =  new AlertDialog.Builder(MainActivity.this)
                .setMessage("Please enter all the details!!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });

        //maindialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.prompt_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
        mainDialog.setView(promptsView);

        btnImageEvent = (ImageButton) promptsView.findViewById(R.id.btnImageEvent);
        final EditText etAddEventName = (EditText) promptsView.findViewById(R.id.name_event);
        final EditText etAddEventDescription = (EditText) promptsView.findViewById(R.id.discription);
        final EditText etAddEventDate = (EditText) promptsView.findViewById(R.id.date_and_time);
        final EditText etAddEventMember = (EditText) promptsView.findViewById(R.id.number_of_participants);

        //This "if" run, if user click edit card
        if (model != null) {
            btnImageEvent.setImageURI(Uri.parse(model.getImage()));
            etAddEventName.setText(model.getName());
            etAddEventDescription.setText(model.getDescription());
            etAddEventDate.setText(model.getAddress());
            etAddEventMember.setText(String.valueOf(model.getMember()));
        }

        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = mainDialog.create();
        dialog.show();

        btnImageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddEventName) && !Utility.isBlankField(etAddEventDescription)
                        && !Utility.isBlankField(etAddEventDate)
                        && !Utility.isBlankField(etAddEventMember)) {
                    ModelEvents eventDetailsModel = new ModelEvents();
                    eventDetailsModel.setImage(btnImageEvent.getTag().toString());
                    eventDetailsModel.setName(etAddEventName.getText().toString());
                    eventDetailsModel.setDescription(etAddEventDescription.getText().toString());
                    eventDetailsModel.setAddress(etAddEventDate.getText().toString());
                    eventDetailsModel.setMember(Integer.parseInt(etAddEventMember.getText().toString()));

                    if (model == null)
                        addDataToRealm(eventDetailsModel);
                    else
                        updatePersonDetails(eventDetailsModel, position, model.getId());

                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
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
        rvAdapter.notifyDataSetChanged();
        id++;
    }

    public void deletePerson(int personId, int position) {
        RealmResults<ModelEvents> results = mRealm.where(ModelEvents.class).equalTo("id", personId).findAll();

        mRealm.beginTransaction();
        results.remove(0);
        mRealm.commitTransaction();

        eventDetailsModelArrayList.remove(position);
        rvAdapter.notifyDataSetChanged();
    }

    public ModelEvents searchPerson(int personId) {
        RealmResults<ModelEvents> results = mRealm.where(ModelEvents.class).equalTo("id", personId).findAll();

        mRealm.beginTransaction();
        mRealm.commitTransaction();

        return results.get(0);
    }

    public void updatePersonDetails(ModelEvents model, int position, int personID) {
        ModelEvents editPersonDetails = mRealm.where(ModelEvents.class).equalTo("id", personID).findFirst();
        mRealm.beginTransaction();
        editPersonDetails.setImage(model.getImage());
        editPersonDetails.setName(model.getName());
        editPersonDetails.setDescription(model.getDescription());
        editPersonDetails.setAddress(model.getAddress());
        editPersonDetails.setMember(model.getMember());
        mRealm.commitTransaction();

        eventDetailsModelArrayList.set(position, editPersonDetails);
        rvAdapter.notifyDataSetChanged();
    }

    private void getAllEvents() {
        RealmResults<ModelEvents> results = mRealm.where(ModelEvents.class).findAll();

        mRealm.beginTransaction();

        for (int i = 0; i < results.size(); i++) {
            eventDetailsModelArrayList.add(results.get(i));
        }

        if(results.size()>0)
            id = mRealm.where(ModelEvents.class).max("id").intValue() + 1;
        mRealm.commitTransaction();
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventDetailsModelArrayList.clear();
        mRealm.close();
    }








    /*    public void openAdd() {

        Intent intent=new Intent(this, CreateNewEvent.class);
        startActivity(intent);
    }*/
}

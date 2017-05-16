package com.glyuk.volunterevents.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.glyuk.volunterevents.R;
import com.glyuk.volunterevents.model.ModelEvents;

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewEvent extends AppCompatActivity {

    TextView mName, mDescription;
    Realm mRealm;
    private RealmResults<ModelEvents> mEvents;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (TextView) findViewById(R.id.view_name);
        mDescription = (TextView) findViewById(R.id.view_description);
        mRealm = Realm.getInstance(this);

        int pos = (int) rv.getTag();
        mName.setText(mEvents.get(pos).getName().toString());
        //mName.setText(getString((mEvents.getId())));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void change(String textTitle, String textData) {
        mName.setText(textTitle);
        mDescription.setText(textData);
    }
}

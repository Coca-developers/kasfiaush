package info.fandroid.navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import info.fandroid.navdrawer.fragments.FragmentShare;

public class SingleProduct extends AppCompatActivity {

    TextView getUserName;
    TextView  getUserPass;
    TextView getUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getUserName = (TextView) findViewById(R.id.getUserName);
        getUserPass = (TextView) findViewById(R.id.getUserPass);
        getUserID = (TextView) findViewById(R.id.getUserID);
        Intent intent = getIntent();
        String nume_product = intent.getStringExtra("nameProduct");
        String id_product = intent.getStringExtra("IdProducT");
        String Password = intent.getStringExtra("Password");
            //efoiwejfpqdpaokdaw
        getUserName.setText(nume_product);
        getUserPass.setText(Password);
        getUserID.setText(id_product);

        Toast.makeText(this, "sefsdok pwkerp",
                Toast.LENGTH_LONG).show();


    }



}

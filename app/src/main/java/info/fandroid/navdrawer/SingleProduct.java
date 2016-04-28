package info.fandroid.navdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import info.fandroid.navdrawer.fragments.FragmentShare;

public class SingleProduct extends AppCompatActivity {

    GoogleMap googleMap;
    TextView getUserName;
    TextView  getUserPass;
    TextView getUserID;
    ImageView SingePrImage;
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
        SingePrImage = (ImageView) findViewById(R.id.SingePrImage);
        Intent intent = getIntent();
        String nume_product = intent.getStringExtra("nameProduct");
        String id_product = intent.getStringExtra("IdProducT");
        String Password = intent.getStringExtra("Password");
        String CurentImage = intent.getStringExtra("ImageCurent");

        Bitmap my = decodeThumbnail(CurentImage);
        SingePrImage.setImageBitmap(my);
        getUserName.setText(nume_product);
        getUserPass.setText(Password);
        getUserID.setText(id_product);
        createMapView();
        addMarker();
    }
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }
    private void addMarker(){

        /** Make sure that the map has been initialised **/
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(0, 0))
                            .title("Marker")
                            .draggable(true)
            );
        }
    }
    private Bitmap decodeThumbnail(String thumbData) {
        byte[] bytes = Base64.decode(thumbData, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

}

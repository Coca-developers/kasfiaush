package info.fandroid.navdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class SingleProduct extends AppCompatActivity implements View.OnClickListener {
    private static final String JSON_ARRAY = "result";
    GoogleMap googleMap;
    TextView getUserName;
    TextView getUserPass;
    TextView getProductYear;
    ImageView SingePrImage;
    private JSONArray locations = null;
    private static final String LAT = "lat";
    private static final String LONG = "long";
    private int TRACK = 0;
    Double Lat;
    Double Long;
    TextView rateOneStar;
    TextView rateTwoStars;
    TextView ratethreeStars;
    TextView rateFourStars;
    TextView rateFiveStars;
    String YearProduct;
    TextView totalVotes;
    String IdProducT;
    TextView totalRate;
    TextView addedAt;
    TextView ProducerView;

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
        getProductYear = (TextView) findViewById(R.id.getUserID);
        SingePrImage = (ImageView) findViewById(R.id.SingePrImage);
        addedAt = (TextView) findViewById(R.id.addedAt);
        ProducerView = (TextView) findViewById(R.id.ProducerView); ;


        Intent intent = getIntent();


        /*
          intent.putExtra("nameProduct", nameProduct);
                        intent.putExtra("descriptionProduct", descriptionProduct);
                        intent.putExtra("producer", producer);
                        intent.putExtra("IdProducT", IdProducT);
                        intent.putExtra("DataAdaugarii", DataAdaugarii);
                        intent.putExtra("anulProducerii", anulProducerii);

         */
        String Producer = intent.getStringExtra("producer");
         String dataadaugarii = intent.getStringExtra("DataAdaugarii");
        IdProducT = intent.getStringExtra("IdProducT");
        String nume_product = intent.getStringExtra("nameProduct");
        YearProduct = intent.getStringExtra("anulProducerii");
        String ProductDesc = intent.getStringExtra("descriptionProduct");
        String CurentImage = intent.getStringExtra("ImageCurent");
        addedAt.setText(dataadaugarii);
        ProducerView.setText(Producer);

        Bitmap my = decodeThumbnail(CurentImage);
        SingePrImage.setImageBitmap(my);
        getUserName.setText(nume_product);
        getUserPass.setText(ProductDesc);
        getProductYear.setText(YearProduct);
        createMapView();

        //addMarker();


        final Response.Listener<String> getLocations = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    j = new JSONObject(response);
                    locations = j.getJSONArray(JSON_ARRAY);
                    for (TRACK = 0; TRACK < locations.length(); TRACK++) {

                        JSONObject jsonObject = locations.getJSONObject(TRACK);
                        Lat = Double.parseDouble(jsonObject.getString(LAT));
                        Long = Double.parseDouble(jsonObject.getString(LONG));
                        addMarker(Lat, Long);
                    }
                    //showData();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ProductLocations productLocations = new ProductLocations(IdProducT, getLocations);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(productLocations);

        rateOneStar = (TextView) findViewById(R.id.Onestar);
        rateOneStar.setOnClickListener(this);
        rateTwoStars = (TextView) findViewById(R.id.Twostars);
        rateTwoStars.setOnClickListener(this);
        ratethreeStars = (TextView) findViewById(R.id.ThreeStars);
        ratethreeStars.setOnClickListener(this);
        rateFourStars = (TextView) findViewById(R.id.FourStars);
        rateFourStars.setOnClickListener(this);
        rateFiveStars = (TextView) findViewById(R.id.FiveStars);
        rateFiveStars.setOnClickListener(this);
        totalVotes = (TextView) findViewById(R.id.totalVotes);
        totalRate = (TextView) findViewById(R.id.totalRate);


        final Response.Listener<String> responseProductRating = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    final String FinalRate = jsonResponse.getString("FinalRate");
                    final String NumberOfVotes = jsonResponse.getString("NumberOfVotes");
                    totalVotes.setText(NumberOfVotes);
                    totalRate.setText(FinalRate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetProductRating getProductRating = new GetProductRating(IdProducT, responseProductRating);
        RequestQueue queuety = Volley.newRequestQueue(this);
        queuety.add(getProductRating);

    }

    private void me() {
        //CameraUpdate center=
        //      CameraUpdateFactory.newLatLng(new LatLng(47.0255966,28.8288095));
        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        // googleMap.moveCamera(center);
        // googleMap.animateCamera(zoom);
        try{
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),
                    "am ajuns si aici",Toast.LENGTH_SHORT).show();
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("you are here!"));
        }catch (NullPointerException exception){

        }

    }


    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();
                googleMap.setMyLocationEnabled(true);
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }
    private void addMarker(Double Latitudine, Double Longitudine ){

        /** Make sure that the map has been initialised **/
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitudine, Longitudine)).title(" ").draggable(true));



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



    final Response.Listener<String> VotingR = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject voteResponse = new JSONObject(response);
                final  String action = voteResponse.getString("action");
                Toast.makeText(getApplicationContext(), action,Toast.LENGTH_SHORT).show();
                final String FinalRate = voteResponse.getString("FinalRate");
                final String NumberOfVotes = voteResponse.getString("NumberOfVotes");
                totalVotes.setText(NumberOfVotes);
                totalRate.setText(FinalRate);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }

    };
    Profile profile = Profile.getCurrentProfile();
    String user_id = profile.getName();

    public void vote(int gr){

        RateThisProduct rateOne = new RateThisProduct(user_id ,IdProducT, gr,  VotingR);
        RequestQueue queue5 = Volley.newRequestQueue(this);
        queue5.add(rateOne);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Onestar:
                    vote(1);
                break;

            case R.id.Twostars:
                    vote(2);
                break;

            case R.id.ThreeStars:
                    vote(3);
                break;
            case R.id.FourStars:
                    vote(4);
                break;
            case R.id.FiveStars:
                    vote(5);
                break;

            default:
                break;


        }


    }
}

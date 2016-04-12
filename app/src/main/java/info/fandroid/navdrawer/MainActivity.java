package info.fandroid.navdrawer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import info.fandroid.navdrawer.fragments.FragmentGallery;
import info.fandroid.navdrawer.fragments.FragmentImport;
import info.fandroid.navdrawer.fragments.FragmentSend;
import info.fandroid.navdrawer.fragments.FragmentShare;
import info.fandroid.navdrawer.fragments.FragmentSlideshow;
import info.fandroid.navdrawer.fragments.FragmentTools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.onSomeeventListener, View.OnClickListener {



    FragmentImport fimport;
    FragmentGallery fgallery;
    FragmentSend fsend;
    FragmentShare fshare;
    FragmentSlideshow fshow;
    FragmentTools ftools;
    TextView User_name = null;
    ImageView PrifileImage = null;
    Button logoutButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fgallery = new FragmentGallery();
        fimport = new FragmentImport();
        fsend = new FragmentSend();
        fshare = new FragmentShare();
        fshow = new FragmentSlideshow();
        ftools = new FragmentTools();

        View headerView = navigationView.getHeaderView(0);
         User_name = (TextView) headerView.findViewById(R.id.User_name);
        PrifileImage = (ImageView) headerView.findViewById(R.id.PrifileImage);
        logoutButton = (Button) headerView.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(this);

        ifIsLoged();

    }
    private void logout() {
        LoginManager.getInstance().logOut();

     //   User_name.setText("");
      //  String ht = null;
       // Picasso.with(getApplicationContext())
         //       .load(ht)
         //       .into(PrifileImage);

        Intent i = new Intent(MainActivity.this, InitLogin.class);

        startActivity(i);
    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void ifIsLoged(){
        if (isLoggedIn()) {

            } else {

           Intent i = new Intent(MainActivity.this, InitLogin.class);

            startActivity(i);
            }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();

        if (id == R.id.nav_camara) {
            ftrans.replace(R.id.Container, fimport);
        } else if (id == R.id.nav_gallery) {
            ftrans.replace(R.id.Container, fgallery);

        } else if (id == R.id.nav_slideshow) {
            ftrans.replace(R.id.Container, fshow);

        } else if (id == R.id.nav_manage) {
            ftrans.replace(R.id.Container, ftools);

        } else if (id == R.id.nav_share) {
            ftrans.replace(R.id.Container, fshare);

        } else if (id == R.id.nav_send) {
            ftrans.replace(R.id.Container, fsend);

        } ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void someEvent(String a, String b) {
        User_name.setText("aici se seteaza textul");

        Picasso.with(getApplicationContext())
                .load(b)
                .into(PrifileImage);

    }

    @Override
    public void onPlayerCancel() {

    }

    @Override
    public void onClick(View v) {
        logout();
    }
}

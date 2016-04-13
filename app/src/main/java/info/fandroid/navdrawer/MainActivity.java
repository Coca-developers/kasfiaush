package info.fandroid.navdrawer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
        implements NavigationView.OnNavigationItemSelectedListener,  View.OnClickListener {

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
        Intent intent = getIntent();
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

        setD();
        IfIsLoged();
    }
    private void logout() {
        LoginManager.getInstance().logOut();
        Intent i = new Intent(MainActivity.this, InitLogin.class);
        startActivity(i);
    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setD(){
        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        ftrans.replace(R.id.Container, fimport);
        ftrans.commit();

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ftrans = getFragmentManager().beginTransaction();
        switch (id){
            default: ftrans.replace(R.id.Container, fimport);
                break;
            case R.id.nav_camara :
                ftrans.replace(R.id.Container, fimport);
                break;
            case R.id.nav_gallery :
                ftrans.replace(R.id.Container, fgallery);
                break;
            case R.id.nav_manage :
                ftrans.replace(R.id.Container, ftools);
                break;
            case R.id.nav_share :
                ftrans.replace(R.id.Container, fshare);
                break;
            case R.id.nav_send :
                ftrans.replace(R.id.Container, fsend);
                break;
            case R.id.nav_slideshow :
                ftrans.replace(R.id.Container, fshow);
                break;

        }

         ftrans.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void IfIsLoged() {
       if (isLoggedIn()) {
            Profile profile = Profile.getCurrentProfile();
            String myPhoto = profile.getProfilePictureUri(400, 400).toString();
            String getUser = profile.getName();
            Toast.makeText(this, getUser,
                    Toast.LENGTH_LONG).show();
            User_name.setText(getUser);
            Picasso.with(getApplicationContext())
                    .load(myPhoto)
                    .into(PrifileImage);
        } else {
            Intent i = new Intent(MainActivity.this, InitLogin.class);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        logout();
    }

}

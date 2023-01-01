package com.nilscreation.dailyfacts;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.nilscreation.dailyfacts.R;

public class About extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Navigation Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        drawerLayout = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.navigationview);
        navigationView.setCheckedItem(R.id.about);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                // Navigation drawer item click listener
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(About.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.about:
                        intent = new Intent(About.this, About.class);
                        startActivity(intent);
                        break;

                    case R.id.privacy:
                        String url = "https://khandeshcentral101.blogspot.com/p/privacy-policy.html";
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        break;

                    case R.id.share:
                        String appUrl = "For More Wallpapers download the app now " + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

                        Intent sharing = new Intent(Intent.ACTION_SEND);
                        sharing.setType("text/plain");
                        sharing.putExtra(Intent.EXTRA_SUBJECT, "Download Now");
                        sharing.putExtra(Intent.EXTRA_TEXT, appUrl);
                        startActivity(Intent.createChooser(sharing, "Share via"));
                        break;

                    case R.id.exit:

                        finishAffinity();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
}
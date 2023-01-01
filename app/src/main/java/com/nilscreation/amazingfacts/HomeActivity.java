package com.nilscreation.amazingfacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    SwitchCompat switchMode;
    networkChangListener networkChangListener = new networkChangListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottonNavigationView);
        switchMode = findViewById(R.id.switchMode);

        //default fragment
        loadFragment(new MainFragment());

        nightMode();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    loadFragment(new MainFragment());
                } else if (id == R.id.settings) {
                    loadFragment(new SettingsFragment());
                } else if (id == R.id.favourite) {
                    loadFragment(new FavouriteFragment());
                } else {
                    loadFragment(new CategoryFragment());
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainContainer, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        nightMode();
        super.onResume();
    }

    private void nightMode() {
        //NIGHT MODE
        // Saving state of our app using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // When user reopens the app after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchMode.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchMode.setChecked(false);
        }

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchMode.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    // it will set isDarkModeOn
                    // boolean to false
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragInstance = fm.findFragmentById(R.id.mainContainer);

        if (fragInstance instanceof CategoryFragment) {
            loadFragment(new MainFragment());
            bottomNavigation.setSelectedItemId(R.id.home);
        } else if (fragInstance instanceof FavouriteFragment) {
            loadFragment(new MainFragment());
            bottomNavigation.setSelectedItemId(R.id.home);
        } else if (fragInstance instanceof SettingsFragment) {
            loadFragment(new MainFragment());
            bottomNavigation.setSelectedItemId(R.id.home);
        } else if (fragInstance instanceof CategorySearchFragment) {
            loadFragment(new CategoryFragment());
        } else {
//            super.onBackPressed();
//            finish();
            callDialog();

        }

//        if (bottomNavigation.getSelectedItemId() == R.id.home) {
//            super.onBackPressed();
//            finish();
//        } else {
//            bottomNavigation.setSelectedItemId(R.id.home);
//        }
    }

    private void callDialog() {

        //Dialog
        AlertDialog.Builder exitdialog = new AlertDialog.Builder(HomeActivity.this);
        exitdialog.setTitle("Exit");
        exitdialog.setIcon(R.drawable.ic_info);
        exitdialog.setMessage("Do you really want to exit?");

        exitdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        exitdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        exitdialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        exitdialog.show();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangListener);
        super.onStop();
    }

    private boolean isConnected(HomeActivity homeActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }

    public class networkChangListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //Internet connection
            if (!isConnected(HomeActivity.this)) {

                Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.internet_dialog);
                dialog.setCancelable(false);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                Button btnOk = dialog.findViewById(R.id.btn_retry);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onReceive(HomeActivity.this, intent);
                    }
                });

                dialog.show();
            }

        }
    }
}
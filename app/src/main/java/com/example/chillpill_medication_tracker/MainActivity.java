package com.example.chillpill_medication_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up the drawer
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // start with the current rx page
        // this prevents destroying and re-displaying the current activity caused by rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CurrentRxFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_current_rx);
        }
    }


    // draw item selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_current_rx:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CurrentRxFragment()).commit();
                break;
            case R.id.nav_past_rx:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PastRxFragment()).commit();
                break;
            case R.id.nav_insurance_cards:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InsuranceCardsFragment()).commit();
                break;
            case R.id.nav_reminders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RemindersListFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

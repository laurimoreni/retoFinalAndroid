package com.example.retofinalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected Modelo mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get model data
        mod = (Modelo) getApplication();

        // add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Creates the Action Bar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.userProfile:
                Intent userIntent = new Intent(this, UserProfile.class);
                startActivity(userIntent);
                break;
            case R.id.logout:
                mod.setLoggedUser(null);
                Intent logoutIntent = new Intent(this, Login.class);
                startActivity(logoutIntent);
                break;
            case R.id.config:
                Intent configIntent = new Intent(this, Settings.class);
                startActivity(configIntent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                break;
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

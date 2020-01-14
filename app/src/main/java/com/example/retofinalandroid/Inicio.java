package com.example.retofinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;



public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        boolean a;

    }

    private class descargarJSOn extends AsyncTask<Void, Void, Boolean> {
        private String url = "188.213.5.150";
        private String user = "ldmj";
        private String pass = "ladamijo";

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {


            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {

        }
    }
}

package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences settings = getSharedPreferences("User", MODE_PRIVATE);
        if (!settings.getString("token", "").isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_start, new SignInFragment())
                    .commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Object item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setToolbarTitle(String title) {

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;

        if ( v.getId() == R.id.registration_button ) {
            fragment = new SignUpFragment();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_start, fragment)
                .addToBackStack(null)
                .commit();
    }
}


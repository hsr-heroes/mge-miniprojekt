package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_start, new SignInFragment())
                .commit();
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
        switch (v.getId()) {
            case R.id.registration_button:
                fragment = new SignUpFragment();
                break;
            case R.id.signin_button:
                fragment = new SignInFragment();
                break;
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_start, fragment)
                .commit();
    }
}


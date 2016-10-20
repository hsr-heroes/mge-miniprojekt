package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
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

public class StartActivity extends AppCompatActivity implements OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private TextInputLayout serverLayout;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_start, new SignInFragment())
                .commit();

        spinner = (Spinner) findViewById(R.id.server_spinner);
        address = (EditText) findViewById(R.id.server);
        serverLayout = (TextInputLayout) findViewById(R.id.server_layout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.server_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);
        switch (selection) {
            case "Localhost":
                address.setText("http://localhost");
                serverLayout.setVisibility(View.INVISIBLE);
                break;
            case "MG1":
                address.setText("http://mge1.dev.ifs.hsr.ch");
                serverLayout.setVisibility(View.INVISIBLE);
                break;
            default:
                serverLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


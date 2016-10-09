package ch.hsr.mge.gadgeothek;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private OnFragmentInteractionListener mListener;
    private Spinner spinner;
    private TextInputLayout serverLayout;
    private EditText address;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        spinner = (Spinner) root.findViewById(R.id.server_spinner);
        address = (EditText) root.findViewById(R.id.server);
        serverLayout = (TextInputLayout) root.findViewById(R.id.server_layout);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.server_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selection = (String) parent.getItemAtPosition(position);
        Log.d("TAG", selection);
        switch (selection) {
            case "Localhost":
                address.setText("http://localhost/public");
                serverLayout.setVisibility(View.INVISIBLE);
                break;
            case "MG1":
                address.setText("http://mge1.dev.ifs.hsr.ch/public");
                serverLayout.setVisibility(View.INVISIBLE);
                break;
            default:
                serverLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {

    }
}

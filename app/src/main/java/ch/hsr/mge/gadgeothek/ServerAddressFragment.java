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

public class ServerAddressFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private TextInputLayout serverLayout;
    private EditText address;
    private OnFragmentInteractionListener mListener;

    public ServerAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_server_address, container, false);
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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selection = (String) parent.getItemAtPosition(position);
        if (selection.equals("Custom")) {
            serverLayout.setVisibility(View.VISIBLE);
        } else {
            serverLayout.setVisibility(View.INVISIBLE);
            address.setText("http://" + selection.toLowerCase() + ".dev.ifs.hsr.ch");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


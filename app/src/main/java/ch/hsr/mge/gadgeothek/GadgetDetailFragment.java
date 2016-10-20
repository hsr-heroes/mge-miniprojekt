package ch.hsr.mge.gadgeothek;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import ch.hsr.mge.gadgeothek.domain.Gadget;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GadgetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GadgetDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "ARG_ITEM_ID";

    private Gadget mGadget;
    private OnFragmentInteractionListener mListener;

    public static GadgetDetailFragment newInstance(Gadget gadget) {
        GadgetDetailFragment fragment = new GadgetDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, gadget);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGadget = (Gadget) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gadget_detail, container, false);
        initUI(v);
        return v;
    }

    private void initUI(View v) {
        mListener.setToolbarTitle(mGadget.getName());
        TextView name = (TextView) v.findViewById(R.id.gadget_name);
        name.setText(mGadget.getName());
        TextView manufacturer = (TextView) v.findViewById(R.id.gadget_manufacturer);
        manufacturer.setText(mGadget.getManufacturer());
        TextView price = (TextView) v.findViewById(R.id.gadget_price);
        price.setText(String.format(Locale.getDefault(), "%f", mGadget.getPrice()));
        TextView condition = (TextView) v.findViewById(R.id.gadget_condition);
        condition.setText(mGadget.getCondition().toString());
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

}

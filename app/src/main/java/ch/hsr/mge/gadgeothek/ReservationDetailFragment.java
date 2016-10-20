package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.mge.gadgeothek.domain.Reservation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "ARG_ITEM_ID";

    private Reservation mReservation;
    private OnFragmentInteractionListener mListener;

    public static ReservationDetailFragment newInstance(Reservation reservation) {
        ReservationDetailFragment fragment = new ReservationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, reservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReservation = (Reservation) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reservation_detail, container, false);
        mListener.setToolbarTitle("Reservation Details");
        // TODO: 20.10.2016 initUI
        return v;
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

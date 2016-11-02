package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ReservationListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;
    private MyReservationRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReservationListFragment() {
    }

    @SuppressWarnings("unused")
    public static ReservationListFragment newInstance(int columnCount) {
        ReservationListFragment fragment = new ReservationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        View view = rView.findViewById(R.id.reservation_list);

        mListener.setToolbarTitle("My Reservations");
        // Set the adapter
        if (view instanceof EmptyRecyclerView) {
            Context context = view.getContext();
            final EmptyRecyclerView recyclerView = (EmptyRecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                    getActivity()
            ));
            View emptyView = rView.findViewById(R.id.reservation_list_empty);
            recyclerView.setEmptyView(emptyView);
            Callback<List<Reservation>> callback = new Callback<List<Reservation>> () {
                @Override
                public void onCompletion(List<Reservation> input) {
                    adapter = new MyReservationRecyclerViewAdapter(input, mListener);
                    recyclerView.setAdapter(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(adapter.getItemTouchHelperCallback(getResources()));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
                @Override
                public void onError(String message) {
                    Log.d("onError not implemented", message);
                }
            };
            LibraryService.getReservationsForCustomer(callback);

        }
        return rView;
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

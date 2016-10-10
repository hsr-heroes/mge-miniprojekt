package ch.hsr.mge.gadgeothek;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Reservation;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Reservation} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyReservationRecyclerViewAdapter extends RecyclerView.Adapter<MyReservationRecyclerViewAdapter.ViewHolder> {

    private final List<Reservation> mValues;
    private final OnFragmentInteractionListener mListener;

    public MyReservationRecyclerViewAdapter(List<Reservation> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getReservationId());
        holder.mContentView.setText(mValues.get(position).getGadget().getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Reservation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.reservation_text);
            mContentView = (TextView) view.findViewById(R.id.reservation_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
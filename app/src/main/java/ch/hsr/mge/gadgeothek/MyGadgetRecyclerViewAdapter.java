package ch.hsr.mge.gadgeothek;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Gadget;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Gadget} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 */
public class MyGadgetRecyclerViewAdapter extends RecyclerView.Adapter<MyGadgetRecyclerViewAdapter.ViewHolder> {

    private final List<Gadget> mValues;
    private final OnFragmentInteractionListener mListener;

    public MyGadgetRecyclerViewAdapter(List<Gadget> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gadget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mManufacturerView.setText(mValues.get(position).getManufacturer());
        holder.mPriceView.setText("CHF " + String.format("%.2f", mValues.get(position).getPrice()));

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
        public final TextView mNameView;
        public final TextView mManufacturerView;
        public final TextView mPriceView;
        public Gadget mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.gadget_item_name);
            mManufacturerView = (TextView) view.findViewById(R.id.gadget_item_manufacturer);
            mPriceView = (TextView) view.findViewById(R.id.gadget_item_price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}

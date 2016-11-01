package ch.hsr.mge.gadgeothek;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Loan;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Loan} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLoanRecyclerViewAdapter extends RecyclerView.Adapter<MyLoanRecyclerViewAdapter.ViewHolder> {

    private final List<Loan> mValues;
    private final OnFragmentInteractionListener mListener;

    public MyLoanRecyclerViewAdapter(List<Loan> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_loan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        boolean returned = mValues.get(position).getReturnDate() != null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        holder.mItem = mValues.get(position);
        holder.mGadget.setText(mValues.get(position).getGadget().getName());
        holder.mDate.setText(dateFormat.format(mValues.get(position).getPickupDate()) +
                (returned ? " - " + dateFormat.format(mValues.get(position).getReturnDate()) : ""));
        holder.mStatus.setText( returned ? "returned" : "open");

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
        public final TextView mGadget;
        public final TextView mDate;
        public final TextView mStatus;
        public Loan mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGadget = (TextView) view.findViewById(R.id.loan_item_gadget);
            mDate = (TextView) view.findViewById(R.id.loan_item_date);
            mStatus = (TextView) view.findViewById(R.id.loan_item_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGadget.getText() + "'";
        }
    }
}

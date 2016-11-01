package ch.hsr.mge.gadgeothek;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Reservation} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyReservationRecyclerViewAdapter extends RecyclerView.Adapter<MyReservationRecyclerViewAdapter.ViewHolder> {

    private final List<Reservation> mValues;
    private final OnFragmentInteractionListener mListener;
    private Resources resources = null;

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
        holder.mGadgetView.setText(mValues.get(position).getGadget().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        holder.mDate.setText(dateFormat.format(mValues.get(position).getReservationDate()));
        holder.mFinished.setText(mValues.get(position).getFinished() ? "finished" : "open");

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

    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mValues.size());
    }

    public ItemTouchHelper.SimpleCallback getItemTouchHelperCallback(Resources r) {
        resources = r;
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                LibraryService.deleteReservation(mValues.get(position), new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        removeItem(position);
                    }
                    @Override
                    public void onError(String message) {
                    }
                });
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background,p);
                    icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete_white);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mGadgetView;
        public final TextView mDate;
        public final TextView mFinished;
        public Reservation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGadgetView = (TextView) view.findViewById(R.id.reservation_item_gadget);
            mDate = (TextView) view.findViewById(R.id.reservation_item_date);
            mFinished = (TextView) view.findViewById(R.id.reservation_item_finished);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGadgetView.getText() + "' on '" + mDate.getText();
        }
    }
}

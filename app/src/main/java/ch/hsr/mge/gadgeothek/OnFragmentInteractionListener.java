package ch.hsr.mge.gadgeothek;

import android.net.Uri;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.domain.Reservation;

public interface OnFragmentInteractionListener {
    void onListFragmentInteraction(Object item);
    void onFragmentInteraction(Uri uri);
    void setToolbarTitle(String title);
}

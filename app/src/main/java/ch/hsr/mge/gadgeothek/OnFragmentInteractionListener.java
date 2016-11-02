package ch.hsr.mge.gadgeothek;

import android.net.Uri;
import android.view.View;

public interface OnFragmentInteractionListener extends View.OnClickListener {
    void onListFragmentInteraction(Object item);
    void onFragmentInteraction(Uri uri);
    void setToolbarTitle(String title);
}

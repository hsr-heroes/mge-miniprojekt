package ch.hsr.mge.gadgeothek;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.domain.Reservation;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class MainActivity extends Activity implements OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LibraryService.setServerAddress("http://mge1.dev.ifs.hsr.ch/public");

        View signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, GadgetListFragment.newInstance(1))
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Object item) {
        Fragment fragment;
        if (item instanceof Gadget) {
            fragment = GadgetDetailFragment.newInstance(((Gadget) item).getInventoryNumber());
        } else if (item instanceof Loan) {
            fragment = LoanDetailFragment.newInstance(((Loan) item).getLoanId());
        } else if (item instanceof Reservation) {
            fragment = ReservationDetailFragment.newInstance(((Reservation) item).getReservationId());
        } else {
            throw new RuntimeException("item is not an instance of Gadget, Loan or Reservation");
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}

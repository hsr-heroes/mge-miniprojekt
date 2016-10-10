package ch.hsr.mge.gadgeothek;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ch.hsr.mge.gadgeothek.domain.Gadget;
import ch.hsr.mge.gadgeothek.domain.Loan;
import ch.hsr.mge.gadgeothek.domain.Reservation;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerGadgets:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container , GadgetListFragment.newInstance(1))
                        .commit();
                break;
            case R.id.drawerMyLoans :
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container , LoanListFragment.newInstance(1))
                        .commit();
                break;
            case R.id.drawerMyReservations :
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container , ReservationListFragment.newInstance(1))
                        .commit();
                break;
            case R.id.drawerNewReservation :
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container , ReservationListFragment.newInstance(1))
                        .commit();
                break;
        }
        item.setChecked(true);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
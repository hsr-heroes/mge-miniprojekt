package ch.hsr.mge.gadgeothek;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

import static android.Manifest.permission.READ_CONTACTS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    private OnFragmentInteractionListener mListener;
    private AutoCompleteTextView emailInputView;
    private View progressBarView;
    private View signupFormView;
    private EditText passwordInputView;
    private EditText studentIdInputView;
    private EditText nameInputView;
    private InputValidationHelper inputValidationHelper;
    private View submitButtonView;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        progressBarView = v.findViewById(R.id.signup_progress);
        signupFormView = v.findViewById(R.id.signup_form);
        emailInputView = (AutoCompleteTextView) v.findViewById(R.id.email);
        passwordInputView = (EditText) v.findViewById(R.id.password);
        nameInputView = (EditText) v.findViewById(R.id.name);
        studentIdInputView = (EditText) v.findViewById(R.id.student_id);
        submitButtonView = v.findViewById(R.id.signup_button);
        v.findViewById(R.id.signin_button).setOnClickListener(mListener);

        populateAutoComplete();

        inputValidationHelper = new InputValidationHelper();

        submitButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser(v);
            }
        });

        return v;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(emailInputView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign up the user.
     *
     * If there are form errors, the the first error is presented and no actual signup attempt is made.
     */
    private void signUpUser(View view) {
        // Get values at the time of the signup attempt.
        final String name = nameInputView.getText().toString();
        final String studentId = studentIdInputView.getText().toString();
        final String email = emailInputView.getText().toString();
        final String password = passwordInputView.getText().toString();

        // Input validation.

        if (inputValidationHelper.isEmpty(name)) {
            nameInputView.setError(getString(R.string.error_field_required));
            nameInputView.requestFocus();
            return;
        } else {
            nameInputView.setError(null);
        }

        if (inputValidationHelper.isEmpty(studentId) || !inputValidationHelper.isNumeric(studentId)) {
            studentIdInputView.setError(getString(R.string.error_field_required));
            studentIdInputView.requestFocus();
            return;
        } else {
            studentIdInputView.setError(null);
        }

        if (!inputValidationHelper.isValidEmail(email)) {
            emailInputView.setError(getString(R.string.error_field_required));
            emailInputView.requestFocus();
            return;
        } else {
            emailInputView.setError(null);
        }

        if (inputValidationHelper.isEmpty(password)) {
            passwordInputView.setError(getString(R.string.error_field_required));
            passwordInputView.requestFocus();
            return;
        } else {
            passwordInputView.setError(null);
        }

        EditText serverAddressView = (EditText) view.findViewById(R.id.server);

        // Todo: Validate server address.
        final String serverAddress = serverAddressView.getText().toString();

        // Show a progress spinner, and kick off the async signup task.
        showProgress(true);

        LibraryService.setServerAddress(serverAddress);
        LibraryService.register(email, password, name, studentId, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean success) {
                showProgress(false);
                Log.d(getString(R.string.app_name), "Registration action complete. Success: " + success);
                LibraryService.login(email, password, new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        SharedPreferences settings = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putString("server", serverAddress);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("studentId", studentId);

                        editor.apply();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        Log.d("RESULT", "Sign in done");
                    }

                    @Override
                    public void onError(String message) {
                        showProgress(false);
                        Log.d(getString(R.string.app_name), "Login error: " + message);
                        Snackbar.make(submitButtonView, message, Snackbar.LENGTH_INDEFINITE);
                    }
                });
            }

            @Override
            public void onError(String message) {
                showProgress(false);
                Log.d(getString(R.string.app_name), "Registration error: " + message);
                Snackbar.make(submitButtonView, message, Snackbar.LENGTH_INDEFINITE);
            }
        });
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

    /**
     * Shows the progress UI and hides the registration form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very easy animations.
        // If available, use these APIs to fade-in the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            signupFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(Boolean.compare(show, false))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            progressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBarView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(Boolean.compare(show, false))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            /*
             * The ViewPropertyAnimator APIs are not available, so simply show
             * and hide the relevant UI components.
             */
            progressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
            signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), SignUpFragment.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                /*
                 * Show primary email addresses first.
                 *
                 * Note that there won't be a primary email address if the user hasn't specified one.
                 */
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(SignUpFragment.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailInputView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }
}

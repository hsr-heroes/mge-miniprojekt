package ch.hsr.mge.gadgeothek;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

import static android.Manifest.permission.READ_CONTACTS;

public class SignUpActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.

    private AutoCompleteTextView emailInputView;
    private EditText passwordInputView;
    private EditText studentIdInputView;
    private EditText nameInputView;
    private View progressBarView;
    private View signupFormView;

    private InputValidationHelper inputValidationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Set up the registration form.
        progressBarView = findViewById(R.id.signup_progress);
        signupFormView = findViewById(R.id.signup_form);
        emailInputView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordInputView = (EditText) findViewById(R.id.password);
        nameInputView = (EditText) findViewById(R.id.name);
        studentIdInputView = (EditText) findViewById(R.id.student_id);

        populateAutoComplete();

        inputValidationHelper = new InputValidationHelper();

        View signupButtonView = findViewById(R.id.signup_button);
        signupButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
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
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
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
    private void signUpUser() {
        // Get values at the time of the signup attempt.
        String name = nameInputView.getText().toString();
        String studentId = studentIdInputView.getText().toString();
        String email = emailInputView.getText().toString();
        String password = passwordInputView.getText().toString();

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

        // Show a progress spinner, and kick off the async signup task.
        showProgress(true);

        // Todo: Use correct server address.
        // Todo: Get server address from settings.
        LibraryService.setServerAddress("http://mge8.dev.ifs.hsr.ch");
        LibraryService.register(email, password, name, studentId, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean success) {
                // Todo: Do something.
                showProgress(false);
                Log.d(getString(R.string.app_name), "Registration action complete. Success: " + success);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                showProgress(false);
                Log.d(getString(R.string.app_name), "Registration error: " + message);

                View submitButtonView = findViewById(R.id.signup_button);

                Snackbar.make(submitButtonView, message, Snackbar.LENGTH_INDEFINITE);
            }
        });
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
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), SignUpActivity.ProfileQuery.PROJECTION,

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
            emails.add(cursor.getString(SignUpActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignUpActivity.this,
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

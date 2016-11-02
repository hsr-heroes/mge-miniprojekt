package ch.hsr.mge.gadgeothek;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private View progressBarView;
    private View signupFormView;
    private TextInputEditText emailInputView;
    private TextInputEditText passwordInputView;
    private TextInputEditText studentIdInputView;
    private TextInputEditText nameInputView;
    private InputValidationHelper inputValidationHelper;
    private View submitButtonView;
    private TextInputEditText serverInputView;

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
        emailInputView = (TextInputEditText) v.findViewById(R.id.email);
        passwordInputView = (TextInputEditText) v.findViewById(R.id.password);
        nameInputView = (TextInputEditText) v.findViewById(R.id.name);
        studentIdInputView = (TextInputEditText) v.findViewById(R.id.student_id);
        submitButtonView = v.findViewById(R.id.signup_button);
        serverInputView = (TextInputEditText) v.findViewById(R.id.server_fragment).findViewById(R.id.server);

        inputValidationHelper = new InputValidationHelper();

        submitButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser(v);
            }
        });

        return v;
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

        if (name.isEmpty()) {
            nameInputView.setError(getString(R.string.error_field_required));
            nameInputView.requestFocus();
            return;
        } else {
            nameInputView.setError(null);
        }

        if (studentId.isEmpty() || !inputValidationHelper.isNumeric(studentId)) {
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

        if (password.isEmpty()) {
            passwordInputView.setError(getString(R.string.error_field_required));
            passwordInputView.requestFocus();
            return;
        } else {
            passwordInputView.setError(null);
        }

        EditText serverAddressView = (EditText) view.findViewById(R.id.server);

        // Todo: Validate server address.
        final String serverAddress = serverInputView.getText().toString();

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
}

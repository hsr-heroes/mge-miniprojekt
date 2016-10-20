package ch.hsr.mge.gadgeothek;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private AutoCompleteTextView emailInputView;
    private EditText passwordInputView;
    private View progressBarView;
    private View signupFormView;
    private InputValidationHelper inputValidationHelper;

    public SignInFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        progressBarView = v.findViewById(R.id.sign_in_progress);
        signupFormView = v.findViewById(R.id.sign_in_form);
        emailInputView = (AutoCompleteTextView) v.findViewById(R.id.sign_in_email);
        passwordInputView = (EditText) v.findViewById(R.id.sign_in_password);
        inputValidationHelper = new InputValidationHelper();

        View signupButtonView = v.findViewById(R.id.email_sign_in_button);
        signupButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private void signInUser() {
        // Get values at the time of the signup attempt.
        String email = emailInputView.getText().toString();
        String password = passwordInputView.getText().toString();

        // Input validation.
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
        LibraryService.login(email,password, new Callback<Boolean>() {
            @Override
            public void onCompletion(Boolean success) {
                showProgress(false);
                Log.d(getString(R.string.app_name), "Login action complete. Success: " + success);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                showProgress(false);
                Log.d(getString(R.string.app_name), "Login error: " + message);

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

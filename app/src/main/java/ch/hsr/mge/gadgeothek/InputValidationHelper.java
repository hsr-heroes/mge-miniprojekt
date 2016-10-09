package ch.hsr.mge.gadgeothek;

import android.text.TextUtils;

class InputValidationHelper {
    boolean isValidEmail(String string) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches();
    }

    boolean isEmpty(String string) {
        return TextUtils.isEmpty(string);
    }

    boolean isNumeric(String string) {
        return TextUtils.isDigitsOnly(string);
    }
}

package ch.hsr.mge.gadgeothek;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @todo Maybe use parameterized test instead.
 */
public class InputValidationHelperTest {
    private InputValidationHelper helper;

    @Before
    public void setUp() {
        helper = new InputValidationHelper();
    }

    @After
    public void tearDown() {
        helper = null;
    }

    @Test
    public void isValidEmail() {
        assertTrue(helper.isValidEmail("john@doe.com"));
        assertFalse(helper.isValidEmail("john@doe"));
        assertFalse(helper.isValidEmail("johndoe"));
        assertFalse(helper.isValidEmail("johndoe.com"));
    }

    @Test
    public void isNumeric() {
        assertTrue(helper.isNumeric("0"));
        assertTrue(helper.isNumeric("123"));
        assertFalse(helper.isNumeric(""));
        assertFalse(helper.isNumeric("foo"));
        assertFalse(helper.isNumeric("1a"));
        assertFalse(helper.isNumeric("1 "));
    }
}
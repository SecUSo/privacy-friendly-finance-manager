package org.secuso.privacyfriendlyfinance.helpers;

public class KeyStoreHelperException extends Exception {
    public KeyStoreHelperException(String message) {
        super(message);
    }

    public KeyStoreHelperException(Exception ex) {
        super(ex);
    }
}

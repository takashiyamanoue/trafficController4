package org.yamaLab.pukiwikiCommunicator.connector;

public interface AuthDialogListener {
    public void whenLoginButtonClicked(AuthDialog x);
    public void whenCancelButtonClicked(AuthDialog x);
}

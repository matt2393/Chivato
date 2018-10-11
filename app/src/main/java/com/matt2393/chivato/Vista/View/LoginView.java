package com.matt2393.chivato.Vista.View;

public interface LoginView {

    void showProgress();

    void hideProgress();

    void onError(String mess);

    void onUsernameError();

    void onPasswordError();

    void onSuccess();
}

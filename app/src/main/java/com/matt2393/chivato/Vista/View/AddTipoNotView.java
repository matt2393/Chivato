package com.matt2393.chivato.Vista.View;

public interface AddTipoNotView {

    void showProgress();
    void hideProgress();
    void onSuccess();
    void onError(String error);
    void onErrorEmptyName();
}

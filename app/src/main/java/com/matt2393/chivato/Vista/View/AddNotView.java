package com.matt2393.chivato.Vista.View;

public interface AddNotView {

    void showProgress();
    void hideProgress();
    void onSuccess(String key);
    void onError(String e);
    void onErrorEmptyTit();
    void onErrorEmptyDesc();
}

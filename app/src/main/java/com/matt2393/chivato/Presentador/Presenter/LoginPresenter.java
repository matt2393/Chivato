package com.matt2393.chivato.Presentador.Presenter;

import com.matt2393.chivato.Presentador.Interactor.LoginInteractor;
import com.matt2393.chivato.Vista.View.LoginView;

public class LoginPresenter implements LoginInteractor.OnLoginFinishedListener {


    private LoginView loginView;
    private LoginInteractor loginInteractor;

    public LoginPresenter (LoginView loginView, LoginInteractor loginInteractor){
        this.loginView=loginView;
        this.loginInteractor=loginInteractor;
    }

    public void login(String username,String pass){
        if(loginView!=null){
            loginView.showProgress();
        }
        loginInteractor.login(username,pass,this);
    }

    public void onDestroy(){
        loginView=null;
    }



    @Override
    public void usernameError() {
        if(loginView!=null){
            loginView.onUsernameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void passwordError() {
        if(loginView!=null){
            loginView.onPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void error(String mess) {
        if(loginView!=null){
            loginView.onError(mess);
            loginView.hideProgress();
        }
    }

    @Override
    public void success() {
        if(loginView!=null){
            loginView.onSuccess();
            loginView.hideProgress();
        }
    }
}

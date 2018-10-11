package com.matt2393.chivato.Presentador.Presenter;

import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.UserInitInteractor;
import com.matt2393.chivato.Vista.View.UserInitView;

import java.util.ArrayList;

public class UserInitPresenter implements UserInitInteractor.OnFinishLoadUser {

    private UserInitView userInitView;
    private UserInitInteractor userInitInteractor;

    public UserInitPresenter(UserInitView userInitView, UserInitInteractor userInitInteractor) {
        this.userInitView = userInitView;
        this.userInitInteractor = userInitInteractor;
    }

    public void loadRoles(){
        userInitInteractor.loadRoles(this);
    }

    public void loadUser(String key){
        userInitInteractor.loadUser(key,this);
    }

    @Override
    public void onLoadRoles(ArrayList<RolUsuario> roles) {
        userInitView.loadRol(roles);
    }

    @Override
    public void onLoadUser(Usuario us) {
        userInitView.loadUser(us);
    }

    @Override
    public void errorRoles(String mess) {
        userInitView.errorRoles(mess);
    }

    @Override
    public void errorUser(String mess) {
        userInitView.errorUser(mess);
    }


    public void addListenerRoles(){
        userInitInteractor.addListenerRoles();
    }
    public void removeListenerRoles(){
        userInitInteractor.removeListenerRoles();
    }

    public void addListenerUser(){
        userInitInteractor.addListenerUser();
    }
    public void removeListenerUser(){
        userInitInteractor.removeListenerUser();
    }
}

package com.matt2393.chivato.Presentador.Presenter;

import android.util.Log;
import android.view.ViewGroup;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.AddRolInteractor;
import com.matt2393.chivato.Vista.View.AddRolView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAddContRol;

import java.util.HashMap;

public class AddRolPresenter implements AddRolInteractor.OnLoadSaveRol {
    private AddRolView addRolView;
    private AddRolInteractor addRolInteractor;

    public AddRolPresenter(AddRolView addRolView, AddRolInteractor addRolInteractor) {
        Log.e("MATT","rol_presenter");
        this.addRolView = addRolView;
        this.addRolInteractor = addRolInteractor;
    }

    public void loadConten(){
        addRolInteractor.loadConten(this);
    }
    public void guardar(RolUsuario rolUsuario, HashMap<String, Boolean> lectura){
        if(addRolView!=null)
            addRolView.showProgress();
        addRolInteractor.guardar(rolUsuario,lectura,this);
    }
    public void editar(String key,RolUsuario rolUsuario, HashMap<String, Boolean> lectura){
        if(addRolView!=null)
            addRolView.showProgress();
        addRolInteractor.edit(key,rolUsuario,lectura,this);
    }


    public void addListener(){
        addRolInteractor.addListener();
    }
    public void removeListener(){
        addRolInteractor.removeListener();
    }
    public FirebaseRecyclerAdapterMatt<TipoNoticia,ViewHolderAddContRol> getAdapter(){
        return addRolInteractor.getAdapter();
    }

    @Override
    public void populateViewHolder(ViewHolderAddContRol v, TipoNoticia tn, int pos) {
        if(addRolView!=null)
            addRolView.onPopulateViewHolder(v, tn, pos);
    }

    @Override
    public ViewHolderAddContRol OnCreateViewHolder(ViewGroup v, int typeView) {
        return addRolView.OnCreateViewHolder(v, typeView);
    }

    @Override
    public void success() {
        if(addRolView!=null) {
            addRolView.hideProgress();
            addRolView.onSuccess();
        }
    }

    @Override
    public void error(String e) {
        if(addRolView!=null) {

            addRolView.hideProgress();
            addRolView.onError(e);
        }
    }

    @Override
    public void errorEmptyTit() {
        if(addRolView!=null)
            addRolView.onErrorEmptyTit();
    }

    @Override
    public void errorEmptyPermisos() {
        if(addRolView!=null)
            addRolView.onErrorEmptyPermisos();
    }
}

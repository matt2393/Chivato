package com.matt2393.chivato.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.UserInteractor;
import com.matt2393.chivato.Vista.View.UsersView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class UserPresenter implements UserInteractor.OnLoadChangeViewListener{

    private UsersView usersView;
    private UserInteractor userInteractor;

    public UserPresenter(UsersView usersView, UserInteractor userInteractor) {
        this.usersView = usersView;
        this.userInteractor = userInteractor;
    }

    public void loadData(){
        userInteractor.loadData(this);
    }

    public void startListeners(){
        userInteractor.starListeners();
    }
    public void stopLosteners(){
        userInteractor.stopListeners();
    }



    @Override
    public ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return usersView.loadViewHolder(parent, viewType);
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, Usuario u, int pos) {
        usersView.populateViewHolder(v, u, pos);
    }

    public FirebaseRecyclerAdapterMatt<Usuario,ViewHolderAdminGen> getAdapter(){
        return userInteractor.getAdapter();
    }
}


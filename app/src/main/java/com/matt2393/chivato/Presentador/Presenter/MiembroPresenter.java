package com.matt2393.tercerojo.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.tercerojo.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.tercerojo.Modelo.Miembro;
import com.matt2393.tercerojo.Presentador.Interactor.MiembroInteractor;
import com.matt2393.tercerojo.Vista.View.MiembroView;
import com.matt2393.tercerojo.Vista.ViewHolder.ViewHolderAdminGen;

public class MiembroPresenter implements MiembroInteractor.OnFinishMiembro{
    private MiembroView miembroView;
    private MiembroInteractor miembroInteractor;

    public MiembroPresenter(MiembroView miembroView, MiembroInteractor miembroInteractor) {
        this.miembroView = miembroView;
        this.miembroInteractor = miembroInteractor;
    }

    public void loadData(){
        miembroInteractor.loadData(this);
    }

    public void startListeners(){
        miembroInteractor.starListeners();
    }
    public void stopLosteners(){
        miembroInteractor.stopListeners();
    }



    @Override
    public ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return miembroView.loadViewHolder(parent, viewType);
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, Miembro m, int pos) {
        miembroView.populateViewHolder(v, m, pos);
    }

    public FirebaseRecyclerAdapterMatt<Miembro,ViewHolderAdminGen> getAdapter(){
        return miembroInteractor.getAdapter();
    }
}

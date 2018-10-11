package com.matt2393.chivato.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.TipoNotInteractor;
import com.matt2393.chivato.Vista.View.TipoNotView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class TipoNotPresenter implements TipoNotInteractor.OnLoadChangeViewListener{

    private TipoNotView tipoNotView;
    private TipoNotInteractor tipoNotInteractor;

    public TipoNotPresenter(TipoNotView tipoNotView, TipoNotInteractor tipoNotInteractor) {
        this.tipoNotView = tipoNotView;
        this.tipoNotInteractor = tipoNotInteractor;
    }

    public void loadData(){
        tipoNotInteractor.loadData(this);
    }

    public void startListeners(){
        tipoNotInteractor.starListeners();
    }
    public void stopLosteners(){
        tipoNotInteractor.stopListeners();
    }



    @Override
    public ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return tipoNotView.loadViewHolder(parent, viewType);
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, TipoNoticia tipoNoticia, int pos) {
        tipoNotView.populateViewHolder(v, tipoNoticia, pos);
    }

    public FirebaseRecyclerAdapterMatt<TipoNoticia,ViewHolderAdminGen> getAdapter(){
        return tipoNotInteractor.getAdapter();
    }
}

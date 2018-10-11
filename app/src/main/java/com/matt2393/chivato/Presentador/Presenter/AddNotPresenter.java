package com.matt2393.chivato.Presentador.Presenter;

import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Presentador.Interactor.AddNotInteractor;
import com.matt2393.chivato.Presentador.Interactor.AddTipoNotInteractor;
import com.matt2393.chivato.Vista.View.AddNotView;

public class AddNotPresenter implements AddNotInteractor.OnFinishSaveNot {

    private AddNotView addNotView;
    private AddNotInteractor addNotInteractor;

    public AddNotPresenter(AddNotView addNotView, AddNotInteractor addNotInteractor) {
        this.addNotView = addNotView;
        this.addNotInteractor = addNotInteractor;
    }

    public void guardar(Noticia noticia){
        if(addNotView!=null)
            addNotView.showProgress();
        addNotInteractor.guardar(noticia,this);
    }

    @Override
    public void success(String key) {

        if(addNotView!=null){
            addNotView.hideProgress();
            addNotView.onSuccess(key);
        }
    }

    @Override
    public void error(String e) {

        if(addNotView!=null){
            addNotView.hideProgress();
            addNotView.onError(e);
        }
    }

    @Override
    public void errorEmptyTitulo() {
        if(addNotView!=null){
            addNotView.hideProgress();
            addNotView.onErrorEmptyTit();
        }
    }

    @Override
    public void errorEmptyDesc() {

        if(addNotView!=null){
            addNotView.hideProgress();
            addNotView.onErrorEmptyDesc();
        }
    }
}

package com.matt2393.chivato.Presentador.Presenter;

import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.AddTipoNotInteractor;
import com.matt2393.chivato.Vista.View.AddTipoNotView;

public class AddTipoNotPresenter implements AddTipoNotInteractor.OnFinishSaveAddTipoNot{

    private AddTipoNotInteractor addTipoNotInteractor;
    private AddTipoNotView addTipoNotView;

    public AddTipoNotPresenter(AddTipoNotInteractor addTipoNotInteractor, AddTipoNotView addTipoNotView) {
        this.addTipoNotInteractor = addTipoNotInteractor;
        this.addTipoNotView = addTipoNotView;
    }

    public void guardar(TipoNoticia tipoNoticia){
        if(addTipoNotView!=null)
            addTipoNotView.showProgress();

        addTipoNotInteractor.guardar(tipoNoticia,this);
    }
    public void editar(String key,TipoNoticia tipoNoticia){
        if(addTipoNotView!=null)
            addTipoNotView.showProgress();

        addTipoNotInteractor.editar(key,tipoNoticia,this);
    }

    @Override
    public void success() {
        if(addTipoNotView!=null) {
            addTipoNotView.hideProgress();
            addTipoNotView.onSuccess();
        }

    }

    @Override
    public void error(String e) {
        if(addTipoNotView!=null) {
            addTipoNotView.hideProgress();
            addTipoNotView.onError(e);
        }
    }

    @Override
    public void errorNombre() {
        if(addTipoNotView!=null) {
            addTipoNotView.hideProgress();
            addTipoNotView.onErrorEmptyName();
        }
    }
}

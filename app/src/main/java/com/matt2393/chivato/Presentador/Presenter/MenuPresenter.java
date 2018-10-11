package com.matt2393.chivato.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.MenuInteractor;
import com.matt2393.chivato.Vista.View.MenuView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderMenu;

import java.util.ArrayList;

public class MenuPresenter implements MenuInteractor.OnFinishLoad {

    private MenuView menuView;
    private MenuInteractor menuInteractor;

    public MenuPresenter(MenuView menuView, MenuInteractor menuInteractor) {
        this.menuView = menuView;
        this.menuInteractor = menuInteractor;
    }

    public void loadMenu(String keyRol,boolean isAdmin){
        menuInteractor.loadView(keyRol,this,isAdmin);
    }
    public void loadMenuAux(String keyRol,boolean isAdmin){
        menuInteractor.loadViews(keyRol,this,isAdmin);
    }

    @Override
    public void onPopulateViewH(@NonNull ViewHolderMenu v, TipoNoticia not, int pos) {
        menuView.populateViewHolderMenu(v,not,pos);
    }

    @Override
    public ViewHolderMenu onCreateViewHolder(ViewGroup vg, int typeView) {
        return menuView.OnCreateViewHolder(vg,typeView);
    }

    @Override
    public void error() {
        menuView.errorMenu();
    }

    @Override
    public void changeData(ArrayList<TipoNoticia> tipo_not) {
        if(menuView!=null)
            menuView.changeDataMenu(tipo_not);
    }

    public void addListener(){
        menuInteractor.addListeners();
    }
    public void removeListener(){
        menuInteractor.removeListeners();
    }

    public FirebaseRecyclerAdapterMatt<TipoNoticia,ViewHolderMenu> getAdapter(){
        return menuInteractor.getAdapter();
    }
}

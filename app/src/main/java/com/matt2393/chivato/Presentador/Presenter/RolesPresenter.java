package com.matt2393.chivato.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Presentador.Interactor.RolesInteractor;
import com.matt2393.chivato.Vista.View.RolesView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class RolesPresenter implements RolesInteractor.OnLoadChangeViewListener{

        private RolesView rolesView;
        private RolesInteractor rolesInteractor;

        public RolesPresenter(RolesView rolesView, RolesInteractor rolesInteractor) {
                this.rolesView = rolesView;
                this.rolesInteractor = rolesInteractor;
        }

        public void loadData(){
                rolesInteractor.loadData(this);
        }

        public void startListeners(){
                rolesInteractor.starListeners();
        }
        public void stopLosteners(){
                rolesInteractor.stopListeners();
        }



        @Override
        public ViewHolderAdminGen getViewHolder(@NonNull ViewGroup parent, int viewType) {
                return rolesView.loadViewHolder(parent, viewType);
        }

        @Override
        public void populateViewHolder(ViewHolderAdminGen v, RolUsuario rol, int pos) {
                rolesView.populateViewHolder(v, rol, pos);
        }

        public FirebaseRecyclerAdapterMatt<RolUsuario,ViewHolderAdminGen> getAdapter(){
                return rolesInteractor.getAdapter();
        }
}

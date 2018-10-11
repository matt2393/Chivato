package com.matt2393.chivato.Vista.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.chivato.Filter;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Presentador.Interactor.RolesInteractor;
import com.matt2393.chivato.Presentador.Presenter.RolesPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Vista.Dialog.DialogAddRol;
import com.matt2393.chivato.Vista.View.RolesView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class FragmentRoles extends Fragment implements RolesView , Filter{

    public final static String TAG = "FragmentRoles";
    private RecyclerView rec;
    private FloatingActionButton add;

    private RolesPresenter rolesPresenter;

    public static FragmentRoles newInstance() {
        return new FragmentRoles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_generico, container, false);

        rec = view.findViewById(R.id.recycler_admin_gen);
        add = view.findViewById(R.id.add_admin);

        rolesPresenter = new RolesPresenter(this, new RolesInteractor());

        rolesPresenter.loadData();
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(rolesPresenter.getAdapter());
        add.setOnClickListener(v -> {
            DialogAddRol.newInstance(null)
                    .show(getActivity().getSupportFragmentManager(),DialogAddRol.TAG);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        rolesPresenter.startListeners();

    }

    @Override
    public void onPause() {
        super.onPause();

        rolesPresenter.stopLosteners();
    }

    @Override
    public ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminGen(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_admin_gen, parent, false));
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, RolUsuario rol, int pos) {
        final RolUsuario rolU=rol;
        if(rolU.getTitulo()!=null)
            v.titulo.setText(rolU.getTitulo());
        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_edit,null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.edit);
        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_delete_forever,null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.elim);

        v.edit.setOnClickListener(v1 -> {
            DialogAddRol.newInstance(rol).show(getActivity().getSupportFragmentManager(),DialogAddRol.TAG);
        });
        v.elim.setOnClickListener(v1 -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Quiere borrar este rol: "+ rolU.getTitulo())
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("RolUsuario")
                                .child(rolU.getKey())
                                .removeValue();
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancerlar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void errorData() {

    }

    @Override
    public void emptyData() {

    }

    @Override
    public void filter(String dat) {
        if(rolesPresenter!=null)
            rolesPresenter.getAdapter().filtrar(dat);
    }

    @Override
    public void startFilter() {

        if(rolesPresenter!=null)
            rolesPresenter.getAdapter().startFilter();
    }

    @Override
    public void stopFilter() {

        if(rolesPresenter!=null)
            rolesPresenter.getAdapter().stopFilter();
    }
}

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
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.chivato.Filter;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.UserInteractor;
import com.matt2393.chivato.Presentador.Presenter.UserPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.Dialog.DialogAddUser;
import com.matt2393.chivato.Vista.Dialog.DialogEditUserRol;
import com.matt2393.chivato.Vista.View.UsersView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class FragmentUsers extends Fragment implements UsersView, Filter {

    public final static String TAG = "FragmentUsers";
    private RecyclerView rec;
    private FloatingActionButton add;
    private UserPresenter userPresenter;

    public static FragmentUsers newInstance() {
        return new FragmentUsers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_generico, container, false);

        rec = view.findViewById(R.id.recycler_admin_gen);
        add = view.findViewById(R.id.add_admin);

        userPresenter = new UserPresenter(this, new UserInteractor());
        userPresenter.loadData();
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(userPresenter.getAdapter());
        add.setOnClickListener(v -> {
            DialogAddUser.newInstence().show(getActivity().getSupportFragmentManager(), DialogAddUser.TAG);
        });


        return view;
    }

    @Override
    public ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminGen(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_admin_gen, parent, false));
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, Usuario usuario, int pos) {
        final Usuario us = usuario;
        v.titulo.setText(usuario.getNombre() + " " + usuario.getApellido());

        v.elim.setBackgroundResource(us.isActivo()?R.drawable.fondo_activo:R.drawable.fondo_elim);

        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_round_email, null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.edit);
        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_outline_person_outline, null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.elim);

        v.elim.setOnClickListener(v1 -> {
            AlertDialog.Builder aler = new AlertDialog.Builder(getActivity());
            aler.setTitle("¿Desea "+ (us.isActivo()?"Desactivar":"Activar") +" al usuario "+usuario.getNombre()+"?");
            aler.setPositiveButton("Aceptar", (dialog, which) ->
                    FirebaseDatabase.getInstance().getReference()
                    .child("Usuario")
                    .child(us.getKey())
                    .child("activo").setValue(!us.isActivo())
            );
            aler.setNegativeButton("Cancelar", (dialog, which) -> {

            });
            aler.show();

        });
        if (Tools.rolUsuario != null && Tools.rolUsuario.isAdmin()) {
            v.edit.setVisibility(View.VISIBLE);
        } else {
            v.edit.setVisibility(View.GONE);
        }
        if (Tools.usuario != null && Tools.usuario.isSuAdmin()) {
            v.elim.setVisibility(View.VISIBLE);
        } else
            v.elim.setVisibility(View.GONE);
        if(Tools.rolUsuario!=null && Tools.rolUsuario.isAdmin() && !usuario.isAdmin())
            v.aux_img.setVisibility(View.VISIBLE);
        else
            v.aux_img.setVisibility(View.GONE);

        if(us.getEmail()!=null){
            v.aux.setVisibility(View.VISIBLE);
            v.aux.setText(us.getEmail());
        }
        else
            v.aux.setVisibility(View.GONE);

        v.aux_img.setVisibility(View.VISIBLE);
        v.aux_img.setOnClickListener(v1 -> {
            DialogEditUserRol.newInstance(us)
                    .show(getActivity().getSupportFragmentManager(),DialogEditUserRol.TAG);
        });

        v.edit.setOnClickListener(v1 -> {

            AlertDialog.Builder aler = new AlertDialog.Builder(getActivity());
            aler.setTitle("¿Desea enviar un correo para reestablecer la contraseña?");
            aler.setPositiveButton("Aceptar", (dialog, which) -> {
                        FirebaseInit.getAuth()
                                .sendPasswordResetEmail(us.getEmail())
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Se envió un correo de restablecimiento de contraseña", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Ocurrio un error en la restauracion de contraseña", Toast.LENGTH_LONG).show();
                                    }
                                });
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    });

            aler.show();

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userPresenter.startListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        userPresenter.startListeners();
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
        if (userPresenter != null)
            userPresenter.getAdapter().filtrar(dat);
    }

    @Override
    public void startFilter() {

        if (userPresenter != null)
            userPresenter.getAdapter().startFilter();
    }

    @Override
    public void stopFilter() {

        if (userPresenter != null)
            userPresenter.getAdapter().stopFilter();
    }
}

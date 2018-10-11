package com.matt2393.tercerojo.Vista.Fragment;

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
import com.matt2393.tercerojo.Filter;
import com.matt2393.tercerojo.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.tercerojo.GlideApp;
import com.matt2393.tercerojo.Modelo.Miembro;
import com.matt2393.tercerojo.Presentador.Interactor.MiembroInteractor;
import com.matt2393.tercerojo.Presentador.Presenter.MiembroPresenter;
import com.matt2393.tercerojo.R;
import com.matt2393.tercerojo.Vista.Dialog.DialogMiembro;
import com.matt2393.tercerojo.Vista.View.MiembroView;
import com.matt2393.tercerojo.Vista.ViewHolder.ViewHolderAdminGen;

public class FragmentMiembros extends Fragment implements MiembroView, Filter {

    public final static String TAG = "FragmentRoles";
    private final static String TIPO="TIPO";
    public final static int PUBLICO=0;
    public final static int ADMIN=1;

    private RecyclerView rec;
    private FloatingActionButton add;
    private int tipo;

    private MiembroPresenter miembtoPresenter;

    private FirebaseRecyclerAdapterMatt<Miembro,ViewHolderAdminGen> adapter;

    public static FragmentMiembros newInstance(int tipo) {
        Bundle bn=new Bundle();
        bn.putInt(TIPO,tipo);
        FragmentMiembros fr=new FragmentMiembros();
        fr.setArguments(bn);
        return fr;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_generico, container, false);

        rec = view.findViewById(R.id.recycler_admin_gen);
        add = view.findViewById(R.id.add_admin);


        if(getArguments()!=null)
            tipo=getArguments().getInt(TIPO,0);
        else
            tipo=0;

        miembtoPresenter = new MiembroPresenter(this, new MiembroInteractor());

        miembtoPresenter.loadData();
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=miembtoPresenter.getAdapter();

        rec.setAdapter(adapter);

        if(tipo==ADMIN)
            add.setVisibility(View.VISIBLE);
        else
            add.setVisibility(View.GONE);
        add.setOnClickListener(v -> {
            DialogMiembro.newInstance(null)
                    .show(getActivity().getSupportFragmentManager(),DialogMiembro.TAG);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        miembtoPresenter.startListeners();

    }

    @Override
    public void onPause() {
        super.onPause();
        miembtoPresenter.stopLosteners();
        if(adapter!=null){
            cleanImg();
        }
    }
    private void cleanImg(){
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        adapter.getDatos().clear();

    }
    @Override
    public ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminGen(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_admin_gen, parent, false));
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, Miembro miembro, int pos) {
        final Miembro miembroM=miembro;
        if(miembroM.getNombre()!=null) {
            v.titulo.setVisibility(View.VISIBLE);
            v.titulo.setText(miembroM.getNombre());
        }
        else {
            v.titulo.setVisibility(View.GONE);
        }

        if(miembroM.getGrado()!=null) {
            v.aux.setVisibility(View.VISIBLE);
            v.aux.setText(miembroM.getGrado());
        }
        else{
            v.aux.setVisibility(View.GONE);
        }

        if(tipo==0){
            v.edit.setVisibility(View.GONE);
            v.elim.setVisibility(View.GONE);
        }
        else {
            v.edit.setVisibility(View.VISIBLE);
            v.elim.setVisibility(View.VISIBLE);
            GlideApp.with(getActivity())
                    .load(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_edit, null))
                    .apply(RequestOptions.circleCropTransform())
                    .into(v.edit);
            GlideApp.with(getActivity())
                    .load(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_delete_forever, null))
                    .apply(RequestOptions.circleCropTransform())
                    .into(v.elim);
        }

        v.edit.setOnClickListener(v1 -> {
            DialogMiembro.newInstance(miembroM)
                    .show(getActivity().getSupportFragmentManager(),DialogMiembro.TAG);
        });
        v.elim.setOnClickListener(v1 -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Quiere borrar este miembro: "+ miembroM.getNombre())
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Miembro")
                                .child(miembroM.getKey())
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
    public void filter(String dat) {
        if(miembtoPresenter!=null)
            miembtoPresenter.getAdapter().filtrar(dat);
    }

    @Override
    public void startFilter() {

        if(miembtoPresenter!=null)
            miembtoPresenter.getAdapter().startFilter();
    }

    @Override
    public void stopFilter() {

        if(miembtoPresenter!=null)
            miembtoPresenter.getAdapter().stopFilter();
    }

}

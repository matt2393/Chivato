package com.matt2393.chivato.Vista.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.chivato.Filter;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.TipoNotInteractor;
import com.matt2393.chivato.Presentador.Presenter.TipoNotPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Vista.Dialog.DialogAddTipoNot;
import com.matt2393.chivato.Vista.View.TipoNotView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAdminGen;

public class FragmentTipoNot extends Fragment implements TipoNotView, Filter {

    public final static String TAG = "FragmentTipoNot";
    private RecyclerView rec;
    private FloatingActionButton add;

    private TipoNotPresenter tipoNotPresenter;

    public static FragmentTipoNot newInstance() {
        return new FragmentTipoNot();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_generico, container, false);

        rec = view.findViewById(R.id.recycler_admin_gen);
        add = view.findViewById(R.id.add_admin);

        tipoNotPresenter=new TipoNotPresenter(this,new TipoNotInteractor());
        tipoNotPresenter.loadData();

        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setAdapter(tipoNotPresenter.getAdapter());

        add.setOnClickListener(v -> {
            DialogAddTipoNot.newInstance(null)
                    .show(getActivity().getSupportFragmentManager(),DialogAddTipoNot.TAG);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tipoNotPresenter.startListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        tipoNotPresenter.stopLosteners();
    }

    @Override
    public ViewHolderAdminGen loadViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderAdminGen(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_admin_gen, parent, false));
    }

    @Override
    public void populateViewHolder(ViewHolderAdminGen v, TipoNoticia tipoNoticia, int pos) {
        final TipoNoticia tt= tipoNoticia;
        if(tt.getNombre()!=null)
            v.titulo.setText(tt.getNombre());
        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_edit,null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.edit);
        GlideApp.with(getActivity())
                .load(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_delete_forever,null))
                .apply(RequestOptions.circleCropTransform())
                .into(v.elim);
        v.edit.setOnClickListener(v1->{
            DialogAddTipoNot.newInstance(tt)
                    .show(getActivity().getSupportFragmentManager(),DialogAddTipoNot.TAG);
        });
        v.elim.setOnClickListener(v1 -> {
            FirebaseDatabase.getInstance().getReference()
                    .child("TipoNoticia")
                    .child(tt.getKey())
                    .removeValue();
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
        if(tipoNotPresenter!=null)
            tipoNotPresenter.getAdapter().filtrar(dat);
    }

    @Override
    public void startFilter() {
        if(tipoNotPresenter!=null)
            tipoNotPresenter.getAdapter().startFilter();

    }

    @Override
    public void stopFilter() {
        if(tipoNotPresenter!=null)
            tipoNotPresenter.getAdapter().stopFilter();

    }
}

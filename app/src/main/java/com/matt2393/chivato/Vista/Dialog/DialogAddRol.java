package com.matt2393.chivato.Vista.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.AddRolInteractor;
import com.matt2393.chivato.Presentador.Presenter.AddRolPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Vista.View.AddRolView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderAddContRol;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogAddRol extends DialogFragment implements AddRolView {

    public final static String TAG="DialogAddRol";
    public final static String EDIT="model_edit";
    private TextInputEditText titulo;
    private RecyclerView rec;
    private CheckBox admin;
    private Button guardar;
    private TextView colorSelect;

    private AddRolPresenter addRolPresenter;

    private HashMap<String, Boolean> lectura;
    private HashMap<String, Boolean> escritura;

    private LinearLayoutManager linearLayoutManager;

    private RolUsuario rolUs;
    private DialogLoad dialogLoad;

    private int colorInt;

    public static DialogAddRol newInstance(RolUsuario rolUsuario){
        Bundle bn=new Bundle();
        bn.putParcelable(EDIT,rolUsuario);
        DialogAddRol d=new DialogAddRol();
        d.setArguments(bn);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_add_rol, null);

        dialogLoad=new DialogLoad();
        dialogLoad.setCancelable(false);
        titulo = view.findViewById(R.id.add_titulo_rol);
        admin = view.findViewById(R.id.admin_add_rol);
        rec = view.findViewById(R.id.reycler_add_rol);
        colorSelect=view.findViewById(R.id.add_color_rol);

        guardar = view.findViewById(R.id.guardar_add_rol);

        if(getArguments()!=null)
            rolUs=getArguments().getParcelable(EDIT);


        linearLayoutManager=new LinearLayoutManager(getActivity());

        rec.setLayoutManager(linearLayoutManager);
        addRolPresenter = new AddRolPresenter(this, new AddRolInteractor());
        addRolPresenter.loadConten();
        rec.setAdapter(addRolPresenter.getAdapter());

        lectura = new HashMap<>();
        escritura = new HashMap<>();



        admin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                rec.setVisibility(View.GONE);
                for (TipoNoticia tt:addRolPresenter.getAdapter().getDatos()){
                    lectura.put(tt.getKey(),true);
                    escritura.put(tt.getKey(),true);
                }
            }
            else{
                rec.setVisibility(View.VISIBLE);
                for (TipoNoticia tt:addRolPresenter.getAdapter().getDatos()){
                    lectura.remove(tt.getKey());
                    escritura.remove(tt.getKey());
                }
            }
        });

        if(rolUs!=null){
            titulo.setText(rolUs.getTitulo());
            titulo.setSelection(rolUs.getTitulo().length());
            admin.setChecked(rolUs.isAdmin());
            if(rolUs.getColor()!=0){
                colorInt=rolUs.getColor();
                colorSelect.setBackgroundColor(colorInt);
                colorSelect.setTextColor(Color.WHITE);
            }
        }

        guardar.setOnClickListener(v->{
            RolUsuario rol=new RolUsuario();
            rol.setTitulo(titulo.getText().toString());
            rol.setAdmin(admin.isChecked());
            rol.setContenido(escritura);
            rol.setLectura(lectura);
            rol.setColor(colorInt);
            if(rolUs==null)
                addRolPresenter.guardar(rol, lectura);
            else{
                addRolPresenter.editar(rolUs.getKey(),rol, lectura);
            }
        });

        colorSelect.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(getActivity())
                    .setTitle("Elija un color que identifique a este rol")
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("Aceptar", (dialogInterface, i, integers) -> {
                        colorSelect.setBackgroundColor(i);
                        colorSelect.setTextColor(Color.WHITE);
                        colorInt=i;
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dismiss();
                    })
                    .build()
                    .show();

        });

        rec.setNestedScrollingEnabled(false);


        addRolPresenter.addListener();
        alert.setView(view);
        return alert.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(getDialog().getWindow()!=null)
            getDialog().getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnim;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addRolPresenter.removeListener();
    }

    /**
     * Métodos de la interface AddRolView
     */

    @Override
    public void onSuccess() {
        dismiss();

    }

    @Override
    public void onError(String e) {

    }

    @Override
    public void onErrorEmptyTit() {

    }

    @Override
    public void onErrorEmptyPermisos() {

    }

    @Override
    public void onPopulateViewHolder(ViewHolderAddContRol viewHolder, TipoNoticia tn, int pos) {
        final TipoNoticia t = tn;
        if (tn.getNombre() != null)
            viewHolder.titulo.setText(tn.getNombre());
        viewHolder.lectura.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                lectura.put(t.getKey(), true);
            else
                lectura.remove(t.getKey());

        });
        viewHolder.escritura.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                escritura.put(t.getKey(), true);
            else
                escritura.remove(t.getKey());

        });
        if(rolUs!=null && rolUs.getContenido()!=null && rolUs.getContenido().get(t.getKey())!=null){
            viewHolder.escritura.setChecked(rolUs.getContenido().get(t.getKey()));
        }
        if(rolUs!=null && rolUs.getLectura()!=null && rolUs.getLectura().get(t.getKey())!=null){
            viewHolder.lectura.setChecked(rolUs.getLectura().get(t.getKey()));
        }
        ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)rec.getLayoutParams();
        params.height=params.height+viewHolder.titulo.getLayoutParams().height;
        rec.setLayoutParams(params);
    }

    @Override
    public ViewHolderAddContRol OnCreateViewHolder(ViewGroup v, int typeView) {
        return new ViewHolderAddContRol(LayoutInflater.from(v.getContext())
                .inflate(R.layout.recycler_add_tiponot_rol, v, false));
    }

    @Override
    public void showProgress() {
        dialogLoad.show(getActivity().getSupportFragmentManager(),DialogLoad.TAG);
    }

    @Override
    public void hideProgress() {
        dialogLoad.dismiss();
    }
}

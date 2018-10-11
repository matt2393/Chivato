package com.matt2393.ind.Vista.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.ind.Modelo.RolUsuario;
import com.matt2393.ind.Modelo.Usuario;
import com.matt2393.ind.R;
import com.matt2393.ind.Tools.Tools;

import java.util.ArrayList;

public class DialogEditUserRol extends DialogFragment{


    public final static String TAG="DialogEditUserRol";
    private final static String USER="USER";

    private TextView titulo;
    private RadioGroup roles;
    private Button guardar;

    private ArrayList<RolUsuario> rolesArray;
    private Usuario usuario;
    private DialogLoad dialogLoad;

    public static DialogEditUserRol newInstance(Usuario us){
        Bundle bn=new Bundle();
        bn.putParcelable(USER,us);
        DialogEditUserRol dd=new DialogEditUserRol();
        dd.setArguments(bn);
        return dd;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());

        View view=getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_editar_user_roles,null);

        titulo=view.findViewById(R.id.titulo_user_edit_rol);
        roles=view.findViewById(R.id.radiog_edit_user_rol);
        guardar=view.findViewById(R.id.guardar_edit_us_rol);

        dialogLoad=DialogLoad.newInstance();
        RadioGroup.LayoutParams lay =
                new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(getArguments()!=null)
            usuario=getArguments().getParcelable(USER);

        if(usuario!=null && usuario.getNombre()!=null)
            titulo.setText(usuario.getNombre());

        rolesArray = Tools.roles;
        for (int i = 0; i < rolesArray.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setLayoutParams(lay);
            radioButton.setId(i);
            radioButton.setText(rolesArray.get(i).getTitulo());
            if(usuario!=null && usuario.getKeyRol().equals(rolesArray.get(i).getKey()))
                radioButton.setChecked(true);
            roles.addView(radioButton);
        }

        guardar.setOnClickListener(v -> {
            dialogLoad.show(getActivity().getSupportFragmentManager(), DialogLoad.TAG);
            String keyRol=Tools.roles.get(roles.getCheckedRadioButtonId()).getKey();

            FirebaseDatabase.getInstance()
                    .getReference().child("Usuario")
                    .child(usuario.getKey())
                    .child("keyRol")
                    .setValue(keyRol)
            .addOnCompleteListener(task -> {
                dialogLoad.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Se guardo correctamente",Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else{
                    Toast.makeText(getActivity(),"Ocurrio un error al guardar",Toast.LENGTH_SHORT).show();
                }
            });

        });


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
}

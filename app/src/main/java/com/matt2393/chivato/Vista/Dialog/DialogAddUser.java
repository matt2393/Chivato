package com.matt2393.chivato.Vista.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;

import java.util.ArrayList;

public class DialogAddUser extends DialogFragment {

    public final static String TAG = "DialogAddUser";
    private final static String ROLESARR = "roles";
    private final static String ADDDD = "admin";
    private TextInputEditText email, pass, nom, apell;
    private Button guardar;
    private RadioGroup roles;
    private ArrayList<RolUsuario> rolesArray;
    private Usuario admin;
    private DialogLoad dialogLoad;
    private CheckBox suAdmin;

    public static DialogAddUser newInstence() {
        Bundle bn = new Bundle();
        DialogAddUser dd = new DialogAddUser();
        // bn.putParcelableArrayList(ROLESARR,roles);
        // bn.putParcelable(ADDDD,admin);
        dd.setArguments(bn);
        return dd;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_add_usuario, null);

        dialogLoad = DialogLoad.newInstance();
        dialogLoad.setCancelable(false);
        email = view.findViewById(R.id.email_add_user);
        pass = view.findViewById(R.id.password_add_user);
        nom = view.findViewById(R.id.nombre_add_user);
        apell = view.findViewById(R.id.apell_add_user);
        guardar = view.findViewById(R.id.guardar_nuevo_us);
        roles = view.findViewById(R.id.radiog_add_user);
        suAdmin=view.findViewById(R.id.suadmin_add_user);

        RadioGroup.LayoutParams lay =
                new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        //if(getArguments()!=null) {
        if(Tools.usuario.isSuAdmin())
            suAdmin.setVisibility(View.VISIBLE);
        else
            suAdmin.setVisibility(View.GONE);
        rolesArray = Tools.roles;
        for (int i = 0; i < rolesArray.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setLayoutParams(lay);
            radioButton.setId(i);
            radioButton.setText(rolesArray.get(i).getTitulo());
            roles.addView(radioButton);
        }
        //admin=getArguments().getParcelable(ADDDD);
        /*}
        else{
            dismiss();
        }*/

        guardar.setOnClickListener(v -> {
            dialogLoad.show(getActivity().getSupportFragmentManager(), DialogLoad.TAG);
            final Usuario uuu = new Usuario();
            uuu.setNombre(nom.getText().toString());
            uuu.setApellido(apell.getText().toString());
            uuu.setEmail(email.getText().toString());
            uuu.setPassword(pass.getText().toString());
            uuu.setActivo(true);
            uuu.setSuAdmin(suAdmin.isChecked());
            uuu.setKeyRol(Tools.roles.get(roles.getCheckedRadioButtonId()).getKey());
            uuu.setAdmin(Tools.roles.get(roles.getCheckedRadioButtonId()).isAdmin());
            FirebaseInit.userA=FirebaseInit.getUser();
            FirebaseInit.getAuth().signOut();
            FirebaseInit.setUser();
            FirebaseInit.getAuth()
                    .createUserWithEmailAndPassword(email.getText().toString(),
                            pass.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        FirebaseInit.setUser();
                        FirebaseDatabase.getInstance().getReference()
                                .child("Usuario").child(FirebaseInit.getUser().getUid())
                                .setValue(uuu)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()) {
                                        FirebaseInit.getAuth().signOut();
                                        FirebaseInit.setUser();
                                        FirebaseInit.getAuth().signInWithEmailAndPassword(Tools.usuario.getEmail(), Tools.usuario.getPassword())
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        FirebaseInit.setUser();
                                                        dialogLoad.dismiss();
                                                        dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(getActivity(),
                                                                "Ocurrio un error, vuelve a iniciar sesión",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                    else{
                                        if(FirebaseInit.getUser()!=null) {
                                            FirebaseInit.getUser().delete();
                                            Toast.makeText(getActivity(),
                                                    "Ocurrio un error al crear el usuario, se cerro su sesión",
                                                    Toast.LENGTH_LONG).show();
                                            getActivity().finish();
                                        }
                                    }
                                });

                    })
            .addOnFailureListener(e -> {
                Toast.makeText(getActivity(),
                        "Ocurrio un error al crear el usuario, se cerro su sesión",
                        Toast.LENGTH_LONG).show();
                getActivity().finish();
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

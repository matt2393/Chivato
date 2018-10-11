package com.matt2393.ind.Vista.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.matt2393.ind.GlideApp;
import com.matt2393.ind.Modelo.Noticia;
import com.matt2393.ind.Modelo.Usuario;
import com.matt2393.ind.R;
import com.matt2393.ind.Tools.Const;
import com.matt2393.ind.Tools.FirebaseInit;
import com.matt2393.ind.Tools.Tools;

import java.util.ArrayList;

public class DialogEditUsuario extends DialogFragment {

    public final static String TAG = "DialogEditUsuario";

    private TextInputEditText nombre, apellido;
    private ImageView img_user;
    private CardView card_img_user;
    private Button guardar;

    private Uri uriImg;
    private String pathImg;
    private Usuario usuario;

    private DialogLoad dialogLoad;

    private Query query1;
    private ValueEventListener valueEventListener;

    public static DialogEditUsuario newInstance() {
        DialogEditUsuario dd = new DialogEditUsuario();

        return dd;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_edit_usuario, null);

        nombre = view.findViewById(R.id.nombre_edit_user);
        apellido = view.findViewById(R.id.apell_edit_user);
        card_img_user = view.findViewById(R.id.card_img_user);
        img_user = view.findViewById(R.id.imagen_user_edit);
        guardar = view.findViewById(R.id.guardar_edit_us);


        usuario = new Usuario();
        usuario.setNombre(Tools.usuario.getNombre());
        usuario.setApellido(Tools.usuario.getApellido());
        usuario.setKeyRol(Tools.usuario.getKeyRol());
        usuario.setUrlImage(Tools.usuario.getUrlImage());
        usuario.setKey(Tools.usuario.getKey());
        usuario.setEmail(Tools.usuario.getEmail());
        usuario.setActivo(Tools.usuario.isActivo());
        usuario.setEmail(FirebaseInit.getUser().getEmail());
        usuario.setPassword(Tools.usuario.getPassword());
        dialogLoad = DialogLoad.newInstance();
        dialogLoad.setCancelable(false);

        nombre.setText(usuario.getNombre());
        nombre.setSelection(usuario.getNombre().length());
        apellido.setText(usuario.getApellido());
        apellido.setSelection(usuario.getApellido().length());

        if (usuario.getUrlImage() != null) {
            GlideApp.with(getActivity())
                    .load(FirebaseInit.getStorageRef(usuario.getUrlImage()))
                    .into(img_user);
        }

        card_img_user.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            in.setType("image/*");
            startActivityForResult(Intent.createChooser(in, "Selecciona imagenes"), Const.CODE_IMAGE_EDIT_USER);
        });

        guardar.setOnClickListener(v -> {
            dialogLoad.show(getActivity().getSupportFragmentManager(), dialogLoad.getTag());


            usuario.setNombre(nombre.getText().toString());
            usuario.setApellido(apellido.getText().toString());

            final DatabaseReference dRef = FirebaseDatabase.getInstance().getReference()
                    .child("Usuario")
                    .child(usuario.getKey());
            if (uriImg != null) {
                if (pathImg == null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
                    Cursor cursor = getActivity().getContentResolver().query(uriImg, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    pathImg = cursor.getString(columnIndex);
                    cursor.close();
                }
                if (pathImg == null) {
                    pathImg = "imagen_user" + System.currentTimeMillis() + "jpg";
                }
                StorageReference ref = FirebaseInit.getReference()
                        .child("Usuario")
                        .child(usuario.getKey() + " " + usuario.getNombre())
                        .child(pathImg);
                ref.putFile(uriImg)
                        .continueWithTask(task ->
                                ref.getDownloadUrl()
                        )
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                usuario.setUrlImage(task.getResult().toString());
                                editarNoticias(usuario);
                                dRef.setValue(usuario)
                                        .addOnSuccessListener(aVoid -> {
                                            dialogLoad.dismiss();
                                            dismiss();
                                        })
                                        .addOnFailureListener(e -> {
                                            dialogLoad.dismiss();

                                        });
                            } else
                                dialogLoad.dismiss();
                        });

            } else {
                editarNoticias(usuario);
                dRef.setValue(usuario).addOnSuccessListener(aVoid -> {
                    dialogLoad.dismiss();
                    dismiss();
                })
                        .addOnFailureListener(e -> {
                            dialogLoad.dismiss();
                        });
            }


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

    private void editarNoticias(Usuario us) {
        final Usuario uuuu=new Usuario();
        uuuu.setKey(us.getKey());
        uuuu.setNombre(us.getNombre());
        uuuu.setApellido(us.getApellido());
        uuuu.setEmail(us.getEmail());
        if(us.getUrlImage()!=null)
            uuuu.setUrlImage(us.getUrlImage());
        query1=FirebaseDatabase.getInstance().getReference().child("Noticia");
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dd:dataSnapshot.getChildren()){
                    Noticia no=dd.getValue(Noticia.class);
                    if(dd.getKey()!=null && no!=null && no.getUsuario()!=null
                            && no.getUsuario().getKey()!=null
                            && no.getUsuario().getKey().equals(uuuu.getKey())){
                        FirebaseDatabase.getInstance()
                                .getReference().child("Noticia")
                                .child(dd.getKey())
                                .child("usuario").setValue(uuuu);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query1.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.CODE_IMAGE_EDIT_USER) {
            uriImg = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pathImg = cursor.getString(columnIndex);
            cursor.close();
            img_user.setImageURI(uriImg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(query1!=null && valueEventListener!=null)
            query1.removeEventListener(valueEventListener);
    }
}

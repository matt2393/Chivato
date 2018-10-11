package com.matt2393.chivato.Vista.Dialog;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Presentador.Interactor.AddTipoNotInteractor;
import com.matt2393.chivato.Presentador.Presenter.AddTipoNotPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Vista.View.AddTipoNotView;

public class DialogAddTipoNot extends DialogFragment implements AddTipoNotView {

    public final static String TAG = "DialogAddTipoNot";
    public final static String EDIT = "model_edit";
    private TextInputEditText nombre;
    private Button guardar;
    private CheckBox publico;
    private TextView add_icon;
    private ImageView icon;

    private AddTipoNotPresenter addTipoNotPresenter;
    private TipoNoticia tipoNoti;
    private DialogLoad dialogLoad;

    private Uri uriImg;
    private String pathImg;

    public static DialogAddTipoNot newInstance(TipoNoticia t) {
        Bundle b = new Bundle();
        b.putParcelable(EDIT, t);
        DialogAddTipoNot d = new DialogAddTipoNot();
        d.setArguments(b);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_ad_tipo_noticia, null);

        dialogLoad = DialogLoad.newInstance();
        dialogLoad.setCancelable(false);
        nombre = view.findViewById(R.id.nom_add_tipo_not);
        guardar = view.findViewById(R.id.guardar_add_tipo_not);
        publico = view.findViewById(R.id.publico_add_tipo_not);
        add_icon = view.findViewById(R.id.icono_text_add_tipo_not);
        icon = view.findViewById(R.id.icono_add_tipo_not);


        if (getArguments() != null)
            tipoNoti = getArguments().getParcelable(EDIT);

        if(tipoNoti!=null){
            nombre.setText(tipoNoti.getNombre());
            nombre.setSelection(tipoNoti.getNombre().length());
            publico.setChecked(tipoNoti.isPublico());
            if(tipoNoti.getUrlIcon()!=null){
                icon.setVisibility(View.VISIBLE);
                GlideApp.with(getActivity())
                        .load(FirebaseInit.getStorageRef(tipoNoti.getUrlIcon()))
                        .into(icon);
            }
            else
                icon.setVisibility(View.GONE);
        }

        addTipoNotPresenter = new AddTipoNotPresenter(new AddTipoNotInteractor(), this);


        add_icon.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            in.setType("image/*");
            startActivityForResult(Intent.createChooser(in, "Seleccione una imagen"), Const.CODE_IMAGE_TIPO_NOT);
        });

        guardar.setOnClickListener(v -> {
            TipoNoticia tipoNoticia = new TipoNoticia();
            tipoNoticia.setNombre(nombre.getText().toString());
            tipoNoticia.setPublico(publico.isChecked());



            if (uriImg != null && pathImg!=null) {
                final StorageReference sRef = FirebaseInit.getReference()
                        .child("TipoNoticia")
                        .child(tipoNoticia.getNombre())
                        .child(pathImg);

                sRef.putFile(uriImg)
                        .continueWithTask(task ->
                                sRef.getDownloadUrl()
                        )
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                tipoNoticia.setUrlIcon(task.getResult().toString());
                                if(tipoNoti==null)
                                    addTipoNotPresenter.guardar(tipoNoticia);
                                else{
                                    addTipoNotPresenter.editar(tipoNoti.getKey(),tipoNoticia);
                                }
                            }
                            else{
                                Toast.makeText(getActivity(), "Ocurrio un error al subir el icono, " +
                                        "intente nuevamente",Toast.LENGTH_LONG).show();
                                dialogLoad.dismiss();
                            }
                        });
            } else {
                if(tipoNoti==null)
                    addTipoNotPresenter.guardar(tipoNoticia);
                else{
                    addTipoNotPresenter.editar(tipoNoti.getKey(),tipoNoticia);
                }
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


    /**
     * Métodos sobreescritos de la interface AddTipoNotView
     */

    @Override
    public void showProgress() {
        dialogLoad.show(getActivity().getSupportFragmentManager(), DialogLoad.TAG);
    }

    @Override
    public void hideProgress() {
        dialogLoad.dismiss();
    }

    @Override
    public void onSuccess() {
        dialogLoad.dismiss();
        dismiss();
    }

    @Override
    public void onError(String error) {
        dialogLoad.dismiss();
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorEmptyName() {
        Toast.makeText(getActivity(), "Ingrese un nombre", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.CODE_IMAGE_TIPO_NOT) {
            uriImg = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            pathImg = cursor.getString(columnIndex);
            cursor.close();
            icon.setImageURI(uriImg);
            icon.setVisibility(View.VISIBLE);
        }

    }
}

package com.matt2393.chivato.Vista;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.matt2393.chivato.Modelo.Imagen;
import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Modelo.Notificacion;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.AddNotInteractor;
import com.matt2393.chivato.Presentador.Presenter.AddNotPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.Adapter.AdapterImage;
import com.matt2393.chivato.Vista.Dialog.DialogLoad;
import com.matt2393.chivato.Vista.View.AddNotView;
import com.matt2393.chivato.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddNotActivity extends AppCompatActivity implements AddNotView {


    private final static int IMG = 111;
    private final static int DOC = 112;
    private TextInputEditText titulo, descr;
    private Button guardar;
    private TextView newDoc, newImg, doc;
    private RecyclerView recImg;
    private ImageView img_doc;


    private AddNotPresenter addNotPresenter;

    private ArrayList<String> imagesEncodedList;
    private ArrayList<Uri> imagenes;
    private ArrayList<String> nameImage;
    private ArrayList<Uri> imgUriFirebase;
    private HashMap<String, Imagen> imageFireB;
    private Uri documento, docUriFirebase;
    private String doc_path;

    private ArrayList<StorageReference> refsUps;

    private AdapterImage adapterImage;

    private TipoNoticia tipoNoticia;
    private Usuario usuario;
    StorageReference refUp;
    StorageReference refUpDoc;
    private String exDoc;
    private ArrayList<String> exImgs;
    private int i, cantImgUp, refsIndex;
    private DialogLoad dialogLoad;

    private RadioGroup prioridad;
    private CheckBox notif_push;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_noticia);
        titulo = findViewById(R.id.add_titulo_not);
        descr = findViewById(R.id.add_desc_not);
        guardar = findViewById(R.id.guarda_noticia);
        newDoc = findViewById(R.id.new_doc_add_not);
        newImg = findViewById(R.id.new_image_add_not);
        doc = findViewById(R.id.nom_doc_not);
        recImg = findViewById(R.id.recycler_add_not);
        prioridad = findViewById(R.id.prioridad_group);
        notif_push = findViewById(R.id.notificacion_push);
        img_doc = findViewById(R.id.img_doc_not);

        tipoNoticia = getIntent().getParcelableExtra(Const.TIPO_N);
        usuario = getIntent().getParcelableExtra(Const.USER);

        dialogLoad = DialogLoad.newInstance();
        dialogLoad.setCancelable(false);
        exImgs = new ArrayList<>();

        imagenes = new ArrayList<>();
        nameImage = new ArrayList<>();
        imgUriFirebase = new ArrayList<>();
        refsUps = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recImg.setLayoutManager(linearLayoutManager);
        adapterImage = new AdapterImage(imagenes);
        recImg.setAdapter(adapterImage);

        newDoc.setOnClickListener(v -> {
            Intent in = new Intent();
            in.setType("*/*");
            in.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(in, "Selecciona un documento"), DOC);
        });
        newImg.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            in.setType("image/*");
            startActivityForResult(Intent.createChooser(in, "Selecciona imagenes"), IMG);
        });

        addNotPresenter = new AddNotPresenter(this, new AddNotInteractor());

        guardar.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(titulo.getText().toString()) && !TextUtils.isEmpty(descr.getText().toString())) {
                dialogLoad.show(getSupportFragmentManager(), DialogLoad.TAG);
                Noticia not = new Noticia();
                not.setTitulo(titulo.getText().toString());
                not.setDescripcion(descr.getText().toString());
                not.setHora_fecha(new Date().getTime());

                if (prioridad.getCheckedRadioButtonId() == R.id.alta_prio)
                    not.setPrio(1);
                else if (prioridad.getCheckedRadioButtonId() == R.id.normal_prio)
                    not.setPrio(0);
                else if (prioridad.getCheckedRadioButtonId() == R.id.baja_prio)
                    not.setPrio(-1);
                else
                    not.setPrio(0);

                if (Tools.usuario != null) {
                    Usuario uus = new Usuario();
                    uus.setKey(Tools.usuario.getKey());
                    uus.setNombre(Tools.usuario.getNombre());
                    uus.setEmail(Tools.email);
                    uus.setApellido(Tools.usuario.getApellido());
                    uus.setUrlImage(Tools.usuario.getUrlImage());
                    not.setUsuario(uus);
                }
                if (Tools.rolUsuario != null) {
                    not.setColorRol(Tools.rolUsuario.getColor());
                }

                final StorageReference sRef = FirebaseInit.getReference().child(tipoNoticia.getNombre())
                        .child(not.getTitulo());

                cantImgUp = 0;
                if (documento != null) {
                    refUpDoc = sRef.child(doc_path);
                    refUpDoc.putFile(documento)
                            .continueWithTask(task ->
                                    refUpDoc.getDownloadUrl()
                            )
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    docUriFirebase = task.getResult();
                                    refsIndex = -1;
                                    if (imagenes.size() == 0) {
                                        addNotPresenter.guardar(not);
                                        if (notif_push.isChecked())
                                            enviarPush(not);
                                    } else {
                                        for (i = 0; i < imagenes.size(); i++) {
                                            final StorageReference rrr = sRef.child(nameImage.get(i));
                                            rrr.putFile(imagenes.get(i))
                                                    .continueWithTask(task1 -> {
                                                                refsIndex++;
                                                                return rrr.getDownloadUrl();
                                                            }
                                                    )
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Uri downloadUri = task1.getResult();
                                                            imgUriFirebase.add(downloadUri);
                                                            cantImgUp++;
                                                            if (cantImgUp == imagenes.size()) {
                                                                addNotPresenter.guardar(not);
                                                                if (notif_push.isChecked())
                                                                    enviarPush(not);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
                } else {
                    refsIndex = -1;
                    if (imagenes.size() == 0) {
                        addNotPresenter.guardar(not);
                        if (notif_push.isChecked())
                            enviarPush(not);
                    } else {
                        for (i = 0; i < imagenes.size(); i++) {

                            final StorageReference rrr = sRef.child(nameImage.get(i));
                            rrr.putFile(imagenes.get(i))
                                    .continueWithTask(task -> {
                                        refsIndex++;
                                        return rrr.getDownloadUrl();
                                    })
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            imgUriFirebase.add(downloadUri);
                                            cantImgUp++;


                                            if (cantImgUp == imagenes.size()) {
                                                addNotPresenter.guardar(not);
                                                if (notif_push.isChecked())
                                                    enviarPush(not);

                                            }
                                        }
                                    });
                        }
                    }
                }

            } else
                Toast.makeText(AddNotActivity.this, "Ingrese titulo y descripción", Toast.LENGTH_LONG).show();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                switch (requestCode) {
                    case IMG:
                        imagenes.add(data.getData());
                        adapterImage.setImages(imagenes);

                        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
                        Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        nameImage.add(picturePath);
                        exImgs.add(picturePath.substring(picturePath.lastIndexOf(".") + 1));
                        break;
                    case DOC:
                        documento = data.getData();
                        doc.setVisibility(View.VISIBLE);

                        String[] filePathColumn1 = {MediaStore.Files.FileColumns.DISPLAY_NAME};
                        Cursor cursor1 = getContentResolver().query(documento, filePathColumn1, null, null, null);
                        cursor1.moveToFirst();
                        int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                        String docPath = cursor1.getString(columnIndex1);
                        cursor1.close();
                        doc_path = docPath;

                        doc.setText(docPath);
                        exDoc = docPath.substring(docPath.lastIndexOf(".") + 1);

                        img_doc.setVisibility(View.VISIBLE);
                        switch (exDoc) {
                            case "doc":
                                img_doc.setImageResource(R.drawable.doc);
                                doc.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.doc, 0, 0, 0);
                                break;
                            case "docx":
                                img_doc.setImageResource(R.drawable.docx);
                                break;
                            case "xls":
                            case "xlsx":
                                img_doc.setImageResource(R.drawable.excel);
                                break;
                            case "pdf":
                                img_doc.setImageResource(R.drawable.img_pdf);
                                break;
                            default:
                                img_doc.setImageResource(R.drawable.file);
                        }

                        break;
                    default:
                        Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("MATt", "error doc");
            }

        } else {
            Log.e("MATt", "cancelado");
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        dialogLoad.dismiss();
    }

    @Override
    public void onSuccess(String key) {

        for (int j = 0; j < imgUriFirebase.size(); j++) {
            Imagen im = new Imagen();
            im.setPos(j);
            im.setUrl(imgUriFirebase.get(j).toString());
            im.setExt(exImgs.get(j));
            FirebaseDatabase.getInstance().getReference()
                    .child("Noticia")
                    .child(key).child("imagenes")
                    .push().setValue(im);
        }
        if (documento != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("Noticia")
                    .child(key).child("urlDoc")
                    .setValue(docUriFirebase.toString());
            FirebaseDatabase.getInstance().getReference()
                    .child("Noticia")
                    .child(key).child("extDoc")
                    .setValue(exDoc);
        }

        FirebaseDatabase.getInstance().getReference()
                .child("Noticia")
                .child(key)
                .child(tipoNoticia.getKey())
                .setValue(true);

        finish();
    }

    @Override
    public void onError(String e) {
        Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorEmptyTit() {

    }

    @Override
    public void onErrorEmptyDesc() {

    }

    public void enviarPush(Noticia n) {


        Notificacion nnn = new Notificacion();
        nnn.setKey(n.getKey());
        nnn.setKeyTipo(tipoNoticia.getKey());
        nnn.setTitulo(n.getTitulo());
        nnn.setDescripcion(n.getDescripcion());
        if (n.getImagenes() != null && n.getImagenes().size() > 0) {
            ArrayList<Imagen> imm = new ArrayList<>(n.getImagenes().values());
            nnn.setImagen(imm.get(0).getUrl());
        }
        notif no = new notif();
        no.setTo("/topics/" + tipoNoticia.getKey());
        no.setData(nnn);


        Gson gson = new Gson();
        try {
            JSONObject obj = new JSONObject(gson.toJson(no));
            Log.e("JSON", obj.toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "https://fcm.googleapis.com/fcm/send",
                    obj, response -> {

            },
                    error -> {

                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "key=AAAA6DzIKRo:APA91bEKAhSzRWCMctEy5GM7cWKIyJ3x1-ugOwDI5M3k1_Qt9Kbmnd4vfmdEUYIHSwthZnH3DlA_w1jxSMm_tN6lSHH4RWDysWdWZDtvumlRiQGO1fDidHS2e4_a-98JDMM8ge6HwSEbY32bh2XOZ4AhAcBVG379zw");
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext())
                    .addRequest(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private class notif {
        private String to;
        private Notificacion data;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public Notificacion getData() {
            return data;
        }

        public void setData(Notificacion data) {
            this.data = data;
        }


        public notif() {
        }


    }
}

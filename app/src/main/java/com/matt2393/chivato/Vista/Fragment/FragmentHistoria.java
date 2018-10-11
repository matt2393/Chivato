package com.matt2393.tercerojo.Vista.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.matt2393.tercerojo.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.tercerojo.GlideApp;
import com.matt2393.tercerojo.Modelo.Imagen;
import com.matt2393.tercerojo.R;
import com.matt2393.tercerojo.Tools.Const;
import com.matt2393.tercerojo.Tools.FirebaseInit;
import com.matt2393.tercerojo.Vista.Dialog.DialogLoad;
import com.matt2393.tercerojo.Vista.ViewHolder.ViewHolderImgSplash;

public class FragmentHistoria extends Fragment {

    public final static String TAG="FragmentHistoria";
    private final static String TIPO="TIPO";
    public final static int PUBLICO=0;
    public final static int ADMIN=1;

    private RecyclerView rec;
    private FloatingActionButton add;
    private TextView titulo;

    private Query query;
    private FirebaseRecyclerAdapterMatt<Imagen,ViewHolderImgSplash> adapter;

    private DatabaseReference ref;

    private DialogLoad dialogLoad;

    private int tipo;

    public static FragmentHistoria newInstance(int ti){
        Bundle bn=new Bundle();
        bn.putInt(TIPO,ti);
        FragmentHistoria ff=new FragmentHistoria();
        ff.setArguments(bn);
        return ff;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_splash,container,false);

        rec=view.findViewById(R.id.recycler_imagen_splash);
        add=view.findViewById(R.id.add_img_splash);
        titulo=view.findViewById(R.id.titulo_imagenes_splash);

        titulo.setVisibility(View.GONE);

        if(getArguments()!=null){
            tipo=getArguments().getInt(TIPO,PUBLICO);
        }
        else
            tipo=PUBLICO;

        ref= FirebaseDatabase.getInstance()
                .getReference().child("Historia");

        dialogLoad=DialogLoad.newInstance();
        initQuery();

        rec.setLayoutManager(new LinearLayoutManager(getContext()));
        rec.setAdapter(adapter);

        add.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            in.setType("image/*");
            startActivityForResult(Intent.createChooser(in, "Seleccione una imagen"), Const.CODE_IMAGE_SPLASH);
        });
        return view;
    }

    private void initQuery(){
        query=ref;
        adapter=new FirebaseRecyclerAdapterMatt<Imagen, ViewHolderImgSplash>(
                Imagen.class,ViewHolderImgSplash.class,
                query,true) {
            @Override
            protected void populateViewHolder(ViewHolderImgSplash viewHolder, Imagen model, int position) {
                final String key=model.getKey();
                if(model.getUrl()!=null) {

                    CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams) rec.getLayoutParams();


                    viewHolder.img.setVisibility(View.VISIBLE);
                    viewHolder.img.setMinimumHeight(params.width/2);
                    viewHolder.img.setMinimumWidth(params.width/2);

                    if(tipo==PUBLICO)
                        viewHolder.elim.setVisibility(View.GONE);
                    else
                        viewHolder.elim.setVisibility(View.VISIBLE);
                    Log.e("IMG","imgg: "+model.getUrl());
                    GlideApp.with(getActivity())
                            .load(FirebaseInit.getStorageRef(model.getUrl()))
                            .into(viewHolder.img);

                }
                else{
                    viewHolder.img.setVisibility(View.GONE);
                    viewHolder.elim.setVisibility(View.GONE);
                }

                viewHolder.elim.setOnClickListener(v -> {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("¿Seguro que quiere eliminar esta imagen?")
                            .setPositiveButton("Aceptar", (dialog, which) -> {
                                ref.child(key)
                                        .removeValue()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful())
                                                Toast.makeText(getActivity(),"Se eliminó",Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getActivity(),"Ocurrio un problema al eliminar",Toast.LENGTH_SHORT).show();
                                        });
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                });
            }

            @NonNull
            @Override
            public ViewHolderImgSplash onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolderImgSplash(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.recycler_img_splash,parent,false));
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Const.CODE_IMAGE_SPLASH && resultCode== Activity.RESULT_OK){
            Uri uriImg = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String pathImg = cursor.getString(columnIndex);
            cursor.close();

            String  ext=pathImg.substring(pathImg.lastIndexOf(".") + 1);
            dialog(uriImg,pathImg,ext);


        }
    }

    private void dialog(Uri uriImg,String path,String ext){
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        alert.setTitle("¿Desea guardar esta imagen?");
        ImageView img=new ImageView(alert.getContext());
        img.setImageURI(uriImg);
        alert.setView(img);
        alert.setPositiveButton("Guardar", (dialog, which) -> {
            dialogLoad.show(getActivity().getSupportFragmentManager(),DialogLoad.TAG);
            final StorageReference sRef=FirebaseInit.getReference()
                    .child("Slider")
                    .child(path);
            sRef.putFile(uriImg)
                    .continueWithTask(task ->
                            sRef.getDownloadUrl()
                    )
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Imagen imm=new Imagen();
                            imm.setUrl(task.getResult().toString());
                            imm.setExt(ext);
                            ref.push().setValue(imm)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Se subio con exito", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            dialogLoad.dismiss();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Ocurrio un problema al subir la imagen", Toast.LENGTH_SHORT).show();
                                            dialogLoad.dismiss();
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(getActivity(),"Ocurrio un problema al subir la imagen",Toast.LENGTH_SHORT).show();
                            dialogLoad.dismiss();
                        }
                    });
        });
        alert.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        alert.show();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(query!=null && adapter!=null) {
            //cleanImg();
            adapter.addListeners();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(query!=null && adapter!=null) {
            adapter.cleanListeners();
            cleanImg();
        }
    }


    private void cleanImg(){
        adapter.notifyItemRangeRemoved(0,adapter.getItemCount());
        adapter.getDatos().clear();

    }
}

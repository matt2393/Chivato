package com.matt2393.chivato.Vista.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.matt2393.chivato.Filter;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.Imagen;
import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.NoticiaInteractor;
import com.matt2393.chivato.Presentador.Presenter.NoticiaPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.AddNotActivity;
import com.matt2393.chivato.Vista.ImageActivity;
import com.matt2393.chivato.Vista.View.LoginView;
import com.matt2393.chivato.Vista.View.NoticiaView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderNoticia;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class FragmentNoticia extends Fragment implements NoticiaView , Filter{

    public final static String TAG = "FragmentNoticia";
    public final static String KEY_CONTENIDO="key_contenido";
    public final static String ROL="Rol";
    public final static String USUARIO="Usuario";
    private RecyclerView rec;
    private FloatingActionButton add;
    private NoticiaPresenter noticiaPresenter;
    private String keycont;
    private TipoNoticia tipoNoticia;
    private RolUsuario rolUsuario;
    private Usuario usuario;



    private SimpleDateFormat simpleDateFormat;
    public static FragmentNoticia newInstance(TipoNoticia tipoN, RolUsuario rol, Usuario usuario) {
        FragmentNoticia fragmentNoticia = new FragmentNoticia();
        Bundle bn = new Bundle();
        bn.putParcelable(KEY_CONTENIDO, tipoN);
        bn.putParcelable(ROL,rol);
        bn.putParcelable(USUARIO,usuario);
        fragmentNoticia.setArguments(bn);
        return fragmentNoticia;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);
        rec = view.findViewById(R.id.recycler_noticia);
        add = view.findViewById(R.id.add_not);


        noticiaPresenter = new NoticiaPresenter(this, new NoticiaInteractor(),
                getActivity().getResources().getDisplayMetrics());

        simpleDateFormat=new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        add.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddNotActivity.class)
                .putExtra(Const.TIPO_N,tipoNoticia)
                .putExtra(Const.USER,usuario));
        });
        if (getArguments() != null) {
            tipoNoticia = getArguments().getParcelable(KEY_CONTENIDO);
            rolUsuario=getArguments().getParcelable(ROL);
            usuario=getArguments().getParcelable(USUARIO);

            if((rolUsuario!=null && rolUsuario.isAdmin()) || (rolUsuario!=null && rolUsuario.getContenido()!=null && rolUsuario.getContenido().get(tipoNoticia.getKey())!=null && rolUsuario.getContenido().get(tipoNoticia.getKey())) )
                add.setVisibility(View.VISIBLE);
            else
                add.setVisibility(View.GONE);
            noticiaPresenter.loadData(tipoNoticia.getKey());
            rec.setAdapter(noticiaPresenter.getAdapter());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        noticiaPresenter.startListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        clearRec();
        Log.e("MAtt","fragment_pause");
        noticiaPresenter.stopLosteners();
    }

    private void clearRec(){
        if(noticiaPresenter!=null && noticiaPresenter.getAdapter()!=null) {
            Log.e("AAAA","AAAAAAAAA");
            int cant = noticiaPresenter.getAdapter().getDatos().size();
            noticiaPresenter.getAdapter().getDatos().clear();
            noticiaPresenter.getAdapter().notifyItemRangeRemoved(0, cant);
        }
    }

    @Override
    public ViewHolderNoticia loadViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderNoticia(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_noticia, parent, false));
    }

    @Override
    public void populateViewHolder(ViewHolderNoticia v, Noticia not, int pos) {
        final Noticia nn=not;
        if (not.getTitulo() != null) {
            v.titulo.setText(not.getTitulo());
        }
        if(not.getDescripcion() != null) {
            v.descr.setText(not.getDescripcion());
        }

        if(not.getColorRol()!=0){
            v.conten_img_user.setCardBackgroundColor(not.getColorRol());
        }
        else{
            v.conten_img_user.setCardBackgroundColor(Color.WHITE);
        }

        if(not.getUsuario()!=null) {
            if (not.getUsuario().getUrlImage() != null) {
                GlideApp.with(getActivity())
                        .load(FirebaseInit.getStorageRef(not.getUsuario().getUrlImage()))
                        .apply(RequestOptions.circleCropTransform())
                        .into(v.img_user);
            } else
                v.img_user.setImageResource(R.drawable.person_black);
            if(not.getUsuario().getNombre()!=null)
                v.nom_user.setText(not.getUsuario().getNombre());
        }
        if (not.getImagenes()!=null && not.getImagenes().size() > 0) {
            ArrayList<Imagen> imgs = new ArrayList<>(not.getImagenes().values());
            Imagen imm[] = new Imagen[imgs.size()];
            imm = imgs.toArray(imm);
            Arrays.sort(imm);
            for (int i = 0; i < imm.length; i++)
                imgs.set(i, imm[i]);

            GlideApp.with(getActivity())
                    .load(FirebaseInit.getStorageRef(imgs.get(0).getUrl()))
                    .into(v.img1);
            if (imgs.size() > 1) {
                GlideApp.with(getActivity())
                        .load(FirebaseInit.getStorageRef(imgs.get(1).getUrl()))
                        .into(v.img2);
                if (imgs.size() > 2) {
                    GlideApp.with(getActivity())
                            .load(FirebaseInit.getStorageRef(imgs.get(2).getUrl()))
                            .into(v.img3);
                }
            }
            /*v.contenAllImg.setOnClickListener(v1 -> {
                abrirImages(imgs);
            });*/
            v.img1.setOnClickListener(v1 -> {
                abrirImages(imgs,v.img1,0);
            });
            v.img2.setOnClickListener(v1 -> {
                abrirImages(imgs,v.img2,1);
            });
            v.img3.setOnClickListener(v1 -> {
                abrirImages(imgs,v.img3,2);
            });
        }
        if (not.getUrlDoc()!=null){
            v.conten_arch.setVisibility(View.VISIBLE);
            v.descarga.setVisibility(View.VISIBLE);
            v.img_ext.setVisibility(View.VISIBLE);
            switch (not.getExtDoc()){
                case "doc": v.img_ext.setImageResource(R.drawable.doc);break;
                case "docx": v.img_ext.setImageResource(R.drawable.docx);break;
                case "xls":
                case "xlsx": v.img_ext.setImageResource(R.drawable.excel);break;
                case "pdf": v.img_ext.setImageResource(R.drawable.img_pdf);break;
                default: v.img_ext.setImageResource(R.drawable.file);
            }
        }
        else {
            v.conten_arch.setVisibility(View.GONE);
            v.descarga.setVisibility(View.GONE);
            v.img_ext.setVisibility(View.GONE);
        }
        v.descarga.setMaxProgress(0.339f);
        v.descarga.setOnClickListener(v1 -> {
            descargarDoc(v.descarga,not.getUrlDoc(),not.getExtDoc());

        });

        if((rolUsuario!=null && rolUsuario.isAdmin()) || (rolUsuario!=null && rolUsuario.getContenido()!=null && rolUsuario.getContenido().get(tipoNoticia.getKey())))
            v.elim.setVisibility(View.VISIBLE);
        else
            v.elim.setVisibility(View.GONE);
        v.elim.setOnClickListener(v1 -> {
            AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
            dialog.setTitle("¿Esta seguro que desea eliminar la noticia "+nn.getTitulo()+"?");
            dialog.setPositiveButton("Aceptar", (dialog1, which) -> {
                FirebaseDatabase.getInstance().getReference()
                        .child("Noticia")
                        .child(nn.getKey())
                        .removeValue()
                .addOnSuccessListener(aVoid -> dialog1.dismiss());
            })
                    .setNegativeButton("Cancelar", (dialog12, which) -> {
                        dialog12.dismiss();
                    });

            dialog.create().show();
        });
        if(not.getHora_fecha()!=0){
            v.fecha.setVisibility(View.VISIBLE);
            Date dddd=new Date(not.getHora_fecha());
            String fff=simpleDateFormat.format(dddd);
            v.fecha.setText(fff);
        }
        else
            v.fecha.setVisibility(View.GONE);
    }

    private void abrirImages(ArrayList<Imagen> im, ImageView imageView,int pos){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(getActivity(), ImageActivity.class);
            intent.putExtra(Const.IMAGES,im);
            intent.putExtra(Const.POS_IMG,pos);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), imageView, Const.IMAGE_VIEW);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(new Intent(getActivity(), ImageActivity.class).putExtra(Const.IMAGES,im));
        }

    }

    private void descargarDoc(final LottieAnimationView anim,String url,String ext){

        anim.playAnimation();
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "IND" + File.separator+File.separator+"DOC");
        file.mkdirs();
        File localFile = new File(file,"IND_DOC_"+new Date().getTime()+"."+ ext);
        FirebaseInit.getStorageRef(url)
                .getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(getActivity(),"Se descargo",Toast.LENGTH_SHORT).show();
                    anim.setMinProgress(0.64f);
                    anim.setMaxProgress(1f);
                    anim.playAnimation();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(getActivity(),"Ocurrio un error en la descarga",Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    if(taskSnapshot.getTotalByteCount()>0 && anim.getProgress()>0.337f
                            && anim.getProgress()<0.64f){
                        float aux=0.64f-0.337f;
                        float aux2=(float)taskSnapshot.getBytesTransferred()/(float)taskSnapshot.getTotalByteCount()*100f;
                        anim.setProgress(anim.getProgress()+(aux2*aux/100f));
                    }
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
        if(noticiaPresenter!=null)
        noticiaPresenter.getAdapter().filtrar(dat);
    }

    @Override
    public void startFilter() {
        if(noticiaPresenter!=null)
            noticiaPresenter.getAdapter().startFilter();
    }

    @Override
    public void stopFilter() {

        if(noticiaPresenter!=null)
            noticiaPresenter.getAdapter().stopFilter();
    }
}

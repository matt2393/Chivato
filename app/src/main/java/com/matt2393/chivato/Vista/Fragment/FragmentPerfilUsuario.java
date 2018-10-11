package com.matt2393.ind.Vista.Fragment;

import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.matt2393.ind.GlideApp;
import com.matt2393.ind.MainAct;
import com.matt2393.ind.Modelo.TipoNoticia;
import com.matt2393.ind.R;
import com.matt2393.ind.Tools.Const;
import com.matt2393.ind.Tools.FirebaseInit;
import com.matt2393.ind.Tools.Tools;
import com.matt2393.ind.Vista.Dialog.DialogEditUsuario;

public class FragmentPerfilUsuario extends Fragment{

    public final static String TAG="FragmentPerfilUsuario";

    private ImageView img;
    private AppCompatTextView edit,nom,apell,email, logout;
    private AppCompatTextView menu,roles,usuarios,img_slider;


    private Fragment fr;

    public static FragmentPerfilUsuario newInstance(){
        return new FragmentPerfilUsuario();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_perfil_usuario,container,false);
        img=view.findViewById(R.id.imagen_perfil_user);
        edit=view.findViewById(R.id.editar_perfil_us);
        nom=view.findViewById(R.id.nombre_perfil_us);
        apell=view.findViewById(R.id.apellido_perfil_us);
        email=view.findViewById(R.id.email_perfil_us);
        logout=view.findViewById(R.id.logout_perfil_us);

        menu=view.findViewById(R.id.items_menu_app);
        roles=view.findViewById(R.id.roles_us_admin);
        usuarios=view.findViewById(R.id.usuarios_admin);
        img_slider=view.findViewById(R.id.slider_admin);

        if((Tools.rolUsuario!=null && Tools.rolUsuario.isAdmin()) || (Tools.usuario!=null && Tools.usuario.isSuAdmin())){
            menu.setVisibility(View.VISIBLE);
            roles.setVisibility(View.VISIBLE);
            usuarios.setVisibility(View.VISIBLE);
            img_slider.setVisibility(View.VISIBLE);
        }
        else{
            menu.setVisibility(View.GONE);
            roles.setVisibility(View.GONE);
            usuarios.setVisibility(View.GONE);
            img_slider.setVisibility(View.GONE);
        }


        menu.setOnClickListener(v -> {
            fr=FragmentTipoNot.newInstance();
            addFrag();
        });
        roles.setOnClickListener(v -> {
            fr=FragmentRoles.newInstance();
            addFrag();
        });
        usuarios.setOnClickListener(v -> {
            fr=FragmentUsers.newInstance();
            addFrag();
        });

        img_slider.setOnClickListener(v -> {
            fr=FragmentImgSplash.newInstance();
            addFrag();
        });

        logout.setOnClickListener(v -> {
            FirebaseInit.getAuth().signOut();
            FirebaseInit.setUser();
            Tools.usuario=null;
            Tools.rolUsuario=null;

            if(Tools.tiposNoticias!=null && Tools.tiposNoticias.size()>0){
                for (TipoNoticia tt: Tools.tiposNoticias) {
                    FirebaseMessaging.getInstance()
                            .unsubscribeFromTopic(tt.getKey());
                }
            }

            getActivity().setResult(Const.CODE_LOGOUT);
            getActivity().finish();
        });



        edit.setOnClickListener(v -> {
            DialogEditUsuario.newInstance()
                    .show(getActivity().getSupportFragmentManager(),DialogEditUsuario.TAG);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Tools.usuario!=null){

            if (Tools.usuario.getUrlImage()!=null){
                GlideApp.with(this)
                        .load(FirebaseInit.getStorageRef(Tools.usuario.getUrlImage()))
                        .into(img);
            }
            nom.setText(Tools.usuario.getNombre());
            apell.setText(Tools.usuario.getApellido());
            email.setText(Tools.usuario.getEmail());
        }

    }

    private void addFrag(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_frag, R.anim.exit_frag, R.anim.enter_frag, R.anim.exit_frag)
                .replace(R.id.contenedor_login,fr)
                .addToBackStack(null)
                .commit();
        ((MainAct)getActivity()).addFrag(fr);
    }
}

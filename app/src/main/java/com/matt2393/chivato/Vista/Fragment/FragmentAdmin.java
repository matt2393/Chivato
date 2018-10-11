package com.matt2393.chivato.Vista.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.matt2393.chivato.Filter;
import com.matt2393.chivato.MainAct;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.Dialog.DialogEditUsuario;

public class FragmentAdmin extends Fragment implements Filter{

    public final static String TAG="FragmentAdmin";
    private TextView tipoNot,rolU,user;
    private TextView editUs;
    private Fragment fr;
    public static FragmentAdmin newInstence(){
        return new FragmentAdmin();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin,container,false);
        tipoNot=view.findViewById(R.id.tipo_not_admin);
        rolU=view.findViewById(R.id.rol_admin);
        user=view.findViewById(R.id.users_admin);
        editUs=view.findViewById(R.id.edit_perfil_user);

        if(Tools.rolUsuario.isAdmin()){
            tipoNot.setVisibility(View.VISIBLE);
            rolU.setVisibility(View.VISIBLE);
            user.setVisibility(View.VISIBLE);
        }
        else{
            tipoNot.setVisibility(View.GONE);
            rolU.setVisibility(View.GONE);
            user.setVisibility(View.GONE);
        }
        tipoNot.setOnClickListener(v -> {
            fr=FragmentTipoNot.newInstance();
            addFrag();
        });
        rolU.setOnClickListener(v->{
            fr=FragmentRoles.newInstance();
            addFrag();
        });
        user.setOnClickListener(v->{
            fr=FragmentUsers.newInstance();
            addFrag();
        });
        editUs.setOnClickListener(v -> {
            DialogEditUsuario.newInstance()
                    .show(getActivity().getSupportFragmentManager(),DialogEditUsuario.TAG);
        });

        return view;
    }

    private void addFrag(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_main,fr)
                .addToBackStack(null)
                .commit();
        ((MainAct)getActivity()).addFrag(fr);
    }

    @Override
    public void filter(String dat) {

    }

    @Override
    public void startFilter() {

    }

    @Override
    public void stopFilter() {

    }
}

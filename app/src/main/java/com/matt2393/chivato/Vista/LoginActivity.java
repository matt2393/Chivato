package com.matt2393.chivato.Vista;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.matt2393.chivato.MainAct;
import com.matt2393.chivato.MainActivity;
import com.matt2393.chivato.Presentador.Interactor.LoginInteractor;
import com.matt2393.chivato.Presentador.Presenter.LoginPresenter;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.Dialog.DialogLoad;
import com.matt2393.chivato.Vista.Fragment.FragmentLogin;
import com.matt2393.chivato.Vista.Fragment.FragmentPerfilUsuario;
import com.matt2393.chivato.Vista.View.LoginView;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements MainAct{

    private ArrayList<Fragment> fragments;
    private Fragment fr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseInit.getUser()!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_login, FragmentPerfilUsuario.newInstance(),FragmentPerfilUsuario.TAG)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_login, FragmentLogin.newInstence(),FragmentLogin.TAG)
                    .commit();
        }
        fragments=new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        if(fragments.size()>1) {
            fragments.remove(fragments.size()-1);
            fr=fragments.get(fragments.size()-1);
        }
        super.onBackPressed();
    }

    @Override
    public void addFrag(Fragment ff) {
        fr=ff;
        fragments.add(ff);
    }
}

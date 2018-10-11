package com.matt2393.ind.Vista.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.matt2393.ind.Presentador.Interactor.LoginInteractor;
import com.matt2393.ind.Presentador.Presenter.LoginPresenter;
import com.matt2393.ind.R;
import com.matt2393.ind.Tools.Tools;
import com.matt2393.ind.Vista.Dialog.DialogLoad;
import com.matt2393.ind.Vista.View.LoginView;

public class FragmentLogin extends Fragment implements LoginView{

    public final static String TAG="FragmentLogin";
    private TextInputEditText username,password;
    private Button sing;
    private LoginPresenter loginPresenter;
    private DialogLoad dialogLoad;

    public static FragmentLogin newInstence(){
        return new FragmentLogin();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);

        username=view.findViewById(R.id.email_login);
        password=view.findViewById(R.id.password_login);
        sing=view.findViewById(R.id.button_login);
        dialogLoad=DialogLoad.newInstance();


        loginPresenter=new LoginPresenter(this,new LoginInteractor());
        sing.setOnClickListener(v->
                loginPresenter.login(username.getText().toString(),password.getText().toString())
        );


        return view;
    }




    @Override
    public void showProgress() {
        dialogLoad.setCancelable(false);
        dialogLoad.show(getActivity().getSupportFragmentManager(),DialogLoad.TAG);
    }

    @Override
    public void hideProgress() {
        dialogLoad.dismiss();
    }

    @Override
    public void onError(String mess) {
        Toast.makeText(getActivity(),mess,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUsernameError() {
        Toast.makeText(getActivity(),"Ingrese un Correo Electrónico",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordError() {
        Toast.makeText(getActivity(),"Ingrese un Correo Electrónico",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Tools.email=username.getText().toString();
        Tools.pass=password.getText().toString();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}

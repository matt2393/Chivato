package com.matt2393.chivato.Presentador.Presenter;

import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.matt2393.chivato.Firebase.FirebaseRecyclerAdapterMatt;
import com.matt2393.chivato.Modelo.Noticia;
import com.matt2393.chivato.Presentador.Interactor.NoticiaInteractor;
import com.matt2393.chivato.Vista.View.NoticiaView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderNoticia;

public class NoticiaPresenter implements NoticiaInteractor.OnLoadChangeViewListener{

    private NoticiaView noticiaView;
    private NoticiaInteractor noticiaInteractor;

    private DisplayMetrics displayMetrics;

    public NoticiaPresenter(NoticiaView noticiaView,NoticiaInteractor noticiaInteractor, DisplayMetrics displayMetrics){
        this.noticiaView=noticiaView;
        this.noticiaInteractor=noticiaInteractor;
        this.displayMetrics=displayMetrics;
    }
    public void loadData(String keyCont){
        noticiaInteractor.loadData(keyCont,this);
    }

    public void startListeners(){
        noticiaInteractor.starListeners();
    }
    public void stopLosteners(){
        noticiaInteractor.stopListeners();
    }


    @Override
    public ViewHolderNoticia getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return noticiaView.loadViewHolder(parent,viewType);
    }

    @Override
    public void populateViewHolder(ViewHolderNoticia v, Noticia n, int pos) {
        if(n.getImagenes()==null || n.getImagenes().isEmpty()){
            v.contenAllImg.setVisibility(View.GONE);
        }
        else {
            final LinearLayout.LayoutParams params=
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(200));
            v.contenAllImg.setVisibility(View.VISIBLE);
            switch (n.getImagenes().size()){
                case 1: v.conten1.setVisibility(View.GONE);
                    break;
                case 2: v.conten1.setVisibility(View.VISIBLE);
                        v.conten2.setVisibility(View.GONE);
                        LinearLayout.LayoutParams ll=(LinearLayout.LayoutParams) v.conten1.getLayoutParams();
                        ll.weight=1;
                        v.conten1.setLayoutParams(ll);
                        LinearLayout.LayoutParams imL=(LinearLayout.LayoutParams)v.img2.getLayoutParams();
                        imL.weight=1;
                        imL.height=dpToPx(200);
                        v.img2.setLayoutParams(imL);
                    break;
                case 3: v.conten1.setVisibility(View.VISIBLE);
                        v.conten2.setVisibility(View.VISIBLE);
                        v.contenMas.setVisibility(View.GONE);

                    LinearLayout.LayoutParams ll1=(LinearLayout.LayoutParams) v.conten1.getLayoutParams();
                    ll1.weight=0.3f;
                    v.conten1.setLayoutParams(ll1);
                    LinearLayout.LayoutParams params1=
                            (LinearLayout.LayoutParams) v.img2.getLayoutParams();
                    params1.height=dpToPx(100);
                    v.img2.setLayoutParams(params1);
                    break;
                default: v.conten1.setVisibility(View.VISIBLE);
                    v.conten2.setVisibility(View.VISIBLE);
                    v.contenMas.setVisibility(View.GONE);

                    LinearLayout.LayoutParams ll2=(LinearLayout.LayoutParams) v.conten1.getLayoutParams();
                    ll2.weight=0.3f;
                    v.conten1.setLayoutParams(ll2);
                    LinearLayout.LayoutParams params2=
                            (LinearLayout.LayoutParams) v.img2.getLayoutParams();
                    params2.height=dpToPx(100);
                    v.img2.setLayoutParams(params2);
                         String cant="+"+(n.getImagenes().size()-3);
                         v.textMasImg.setText(cant);
            }
        }
        noticiaView.populateViewHolder(v,n,pos);
    }



    private int dpToPx(int dp) {
        return dp * displayMetrics.densityDpi / 160;
    }

    public FirebaseRecyclerAdapterMatt<Noticia,ViewHolderNoticia> getAdapter() {
        return noticiaInteractor.getAdapter();
    }
}

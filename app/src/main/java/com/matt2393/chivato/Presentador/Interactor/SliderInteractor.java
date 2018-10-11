package com.matt2393.ind.Presentador.Interactor;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.ind.Modelo.Imagen;

import java.util.ArrayList;

public class SliderInteractor {
    private Query query;
    private ValueEventListener valueEventListener;
    public interface OnLoadSlider{
        void loadSlider(ArrayList<Imagen> imgs);
    }
    public void load(final OnLoadSlider onLoadSlider){
        query= FirebaseDatabase.getInstance().getReference()
                .child("Slider");
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0){
                    ArrayList<Imagen> imm=new ArrayList<>();
                    for (DataSnapshot d: dataSnapshot.getChildren()) {
                        Imagen im=d.getValue(Imagen.class);
                        imm.add(im);

                    }
                    onLoadSlider.loadSlider(imm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public void starListeners(){
        if(query!=null && valueEventListener!=null)
            query.addValueEventListener(valueEventListener);
    }
    public void stopListeners(){
        if(query!=null && valueEventListener!=null)
            query.removeEventListener(valueEventListener);
    }
}

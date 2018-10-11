package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.Usuario;

import java.util.ArrayList;

public class UserInitInteractor {

    private Query queryRoles,queryUser;

    private ValueEventListener valRol,valUser;

    public interface OnFinishLoadUser{
        void onLoadRoles(ArrayList<RolUsuario> roles);
        void onLoadUser(Usuario us);
        void errorRoles(String mess);
        void errorUser(String mess);
    }

    public void loadRoles(final OnFinishLoadUser finishLoadUser){
        queryRoles= FirebaseDatabase.getInstance().getReference()
                .child("RolUsuario");

        valRol=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RolUsuario> roles=new ArrayList<>();
                for (DataSnapshot dd: dataSnapshot.getChildren()) {
                    RolUsuario rr=dd.getValue(RolUsuario.class);
                    rr.setKey(dd.getKey());
                    roles.add(rr);
                }
                finishLoadUser.onLoadRoles(roles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finishLoadUser.errorRoles(databaseError.getMessage());
            }
        };
    }
    public void loadUser(String keyUser,final OnFinishLoadUser finishLoadUser){
        queryUser=FirebaseDatabase.getInstance().getReference()
                .child("Usuario").child(keyUser);
        valUser=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario us=dataSnapshot.getValue(Usuario.class);
                if(us!=null) {
                    us.setKey(dataSnapshot.getKey());
                    finishLoadUser.onLoadUser(us);
                }
                else
                    finishLoadUser.errorUser("Error interno");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finishLoadUser.errorUser(databaseError.getMessage());
            }
        };
    }

    public void addListenerRoles(){
        if(queryRoles!=null && valRol!=null)
            queryRoles.addValueEventListener(valRol);
    }
    public void removeListenerRoles(){
        if(queryRoles!=null && valRol!=null)
            queryRoles.removeEventListener(valRol);
    }

    public void addListenerUser(){
        if(queryUser!=null && valUser!=null)
            queryUser.addValueEventListener(valUser);
    }
    public void removeListenerUser(){
        if(queryUser!=null && valUser!=null)
            queryUser.addValueEventListener(valUser);
    }
}

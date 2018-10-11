package com.matt2393.chivato.Presentador.Interactor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Tools.FirebaseInit;

public class LoginInteractor {
    private FirebaseAuth auth;
    private Query query;
    private DatabaseReference ref;
    private ValueEventListener valueEventListener;
    private Usuario usuario;

    public interface OnLoginFinishedListener{
        void usernameError();
        void passwordError();
        void error(String mess);
        void success();
    }
    public void login(final String username,final String pass,final OnLoginFinishedListener listener){
        if (TextUtils.isEmpty(username)){
            listener.usernameError();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            listener.passwordError();
            return;
        }

        ref = FirebaseDatabase.getInstance()
                .getReference().child("Usuario");

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario=dataSnapshot.getValue(Usuario.class);
                if(usuario!=null && usuario.isActivo()){
                    FirebaseInit.setUser();
                    listener.success();
                    query.removeEventListener(valueEventListener);
                }
                else{
                    query.removeEventListener(valueEventListener);
                    listener.error("Lo sentimos, su cuenta no esta activa");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(query!=null)
                    query.removeEventListener(valueEventListener);
                listener.error(databaseError.getMessage());
            }
        };
        auth=FirebaseInit.getAuth();
        auth.signInWithEmailAndPassword(username,pass)
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        query=ref.child(task.getResult().getUser().getUid());
                        query.addListenerForSingleValueEvent(valueEventListener);
                    }
                    else
                        listener.error("Error...");
                })
                .addOnFailureListener(error->{
                    listener.error(error.getMessage());
                });
    }
}

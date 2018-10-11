package com.matt2393.chivato.Tools;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseInit {
    private static FirebaseAuth auth;
    private static FirebaseUser user;
    private static FirebaseStorage firebaseStorage;
    private static StorageReference storageRef;

    public static FirebaseUser userA;

    public static void init(){
        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setUser();
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static void setAuth(FirebaseAuth auth) {
        FirebaseInit.auth = auth;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser() {
        FirebaseInit.user=auth.getCurrentUser();
    }

    public static StorageReference getStorageRef(String url) {
        return firebaseStorage.getReferenceFromUrl(url);
    }

    public static StorageReference getReference(){
        return firebaseStorage.getReference();
    }

}

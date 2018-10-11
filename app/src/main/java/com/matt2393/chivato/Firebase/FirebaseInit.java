package com.matt2393.chivato.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by matt on 28-09-17.
 * inicio de Firebase Database, Storage, etc
 */

public class FirebaseInit {
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;
    private static FirebaseAuth auth;

    public static void initDatabase(){
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        storage=FirebaseStorage.getInstance();
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static void setAuth(FirebaseAuth auth) {
        FirebaseInit.auth = auth;
    }

    public static DatabaseReference refRootDatabase(){
        return database.getReference();
    }

    public static StorageReference getStorage(String link){
        //return storage.getReference().child(link);
        return storage.getReferenceFromUrl(link);
    }

}

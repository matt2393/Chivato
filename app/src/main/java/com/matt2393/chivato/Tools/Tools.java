package com.matt2393.chivato.Tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Modelo.Usuario;

import java.util.ArrayList;

public class Tools {

    public static RolUsuario rolUsuario;
    public static String email,pass;
    public static ArrayList<RolUsuario> roles;
    public static Usuario usuario;
    public static ArrayList<TipoNoticia> tiposNoticias;
    public static boolean tienePermiso(@NonNull Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || !(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    public static void pedirPermisos(FragmentActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                , 2222);
    }

    public static int dpToPx(Context ctx, int dp) {
        DisplayMetrics displayMetrics=ctx.getResources().getDisplayMetrics();
        return dp * displayMetrics.densityDpi / 160;
    }


}

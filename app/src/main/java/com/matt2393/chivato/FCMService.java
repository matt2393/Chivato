package com.matt2393.chivato;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;
import java.util.HashMap;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e("MATT","mensaje llego");
        mostrarNotificacion(remoteMessage);
    }

    private void mostrarNotificacion(RemoteMessage message) {

        Intent in = new Intent(this, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        HashMap<String, String> datos = new HashMap<>(message.getData());

        final String key = datos.get("key");
        final String keyTipo=datos.get("keyTipo");

        in.putExtra("KEY", key);
        in.putExtra("KEY_TIPO",keyTipo);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                in, PendingIntent.FLAG_ONE_SHOT);


        final NotificationCompat.Builder notif = new NotificationCompat.Builder(this,
                "IND_FCM")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(datos.get("titulo"))
                .setContentText(datos.get("descripcion"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500, 500})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.parseColor("#426783"), 1500, 1000);


        if (datos.get("imagen")!=null) {
            GlideApp.with(this)
                    .asBitmap()
                    .load(FirebaseStorage.getInstance().getReferenceFromUrl(datos.get("imagen")))
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            NotificationManagerCompat nmc = NotificationManagerCompat.from(FCMService.this);
                            nmc.notify((int)new Date().getTime(), notif.build());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            notif.setLargeIcon(resource);
                            notif.setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(resource)
                                    .bigLargeIcon(null));
                            NotificationManagerCompat nmc = NotificationManagerCompat.from(FCMService.this);
                            nmc.notify((int)new Date().getTime(), notif.build());
                            return false;
                        }
                    }).submit();
        } else {
            NotificationManagerCompat nmc = NotificationManagerCompat.from(FCMService.this);
            nmc.notify((int)new Date().getTime(), notif.build());
        }

    }
}

package com.matt2393.ind.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class RespuestaFCM implements Parcelable {
    private long message_id;

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.message_id);
    }

    public RespuestaFCM() {
    }

    protected RespuestaFCM(Parcel in) {
        this.message_id = in.readLong();
    }

    public static final Creator<RespuestaFCM> CREATOR = new Creator<RespuestaFCM>() {
        @Override
        public RespuestaFCM createFromParcel(Parcel source) {
            return new RespuestaFCM(source);
        }

        @Override
        public RespuestaFCM[] newArray(int size) {
            return new RespuestaFCM[size];
        }
    };
}

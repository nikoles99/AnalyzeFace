package by.balinasoft.faceanalyzer;

import android.graphics.Bitmap;

public class Photo {

    private Bitmap photo;

    public Photo(Bitmap photo) {
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public byte[] convert(Bitmap photo){

        return new byte[1];
    }

    /*public Bitmap convetr(byte[] photo){

        return new Bitmap();
    }*/

}

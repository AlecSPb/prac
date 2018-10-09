
package com.example.user.dprac;


import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;


class BarcodeGraphicTracker extends Tracker<Barcode> {
    private BarcodeGraphicTrackerListener listener;

    BarcodeGraphicTracker(BarcodeGraphicTrackerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        if (listener != null) {
            listener.onScanned(item);
        }
    }

    public interface BarcodeGraphicTrackerListener {
        void onScanned(Barcode barcode);

    }
}

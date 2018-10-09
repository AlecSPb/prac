package com.example.user.dprac;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener;

    BarcodeTrackerFactory(BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener) {
        this.listener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeGraphicTracker(listener);
    }

}


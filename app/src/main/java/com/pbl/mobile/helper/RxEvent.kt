package com.pbl.mobile.helper

import android.graphics.Bitmap

class RxEvent {
    class EventMaybeRefreshAfterPurchase
    class EventSetIdentityFrontSide(val bitmap: Bitmap)
    class EventSetIdentityBackSide(val bitmap: Bitmap)
}

package com.pbl.mobile.video

import com.google.android.exoplayer2.Player

interface PlayStateCallback {
    /**
     * Callback to when the [PlayerView] has fetched the duration of video
     **/
    fun onVideoDurationRetrieved(duration: Long, player: Player)

    fun onVideoBuffering(player: Player)

    fun onStartedPlaying(player: Player)

    fun onFinishedPlaying(player: Player)
}
package com.example.multivideoplayer;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;

import com.google.android.exoplayer.DefaultLoadControl;
import com.google.android.exoplayer.LoadControl;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.hls.DefaultHlsTrackSelector;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylistParser;
import com.google.android.exoplayer.hls.HlsSampleSource;
import com.google.android.exoplayer.hls.PtsTimestampAdjusterProvider;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;

public class HlsRendererBuilder implements RendererBuilder {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int MAIN_BUFFER_SEGMENTS = 254;

    private Context mContext;
    private Uri mUri;
    private String mUserAgent;
    private final ManifestFetcher<HlsPlaylist> mPlaylistFetcher;

    public HlsRendererBuilder(Context context, Uri url) {
        mContext = context;
        mUri = url;
        mUserAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        HlsPlaylistParser parser = new HlsPlaylistParser();
        mPlaylistFetcher = new ManifestFetcher<>(url.toString(),
                new DefaultUriDataSource(context, mUserAgent), parser);
    }


    @Override
    public void buildRender(RendererBuilderCallback callback) {
        /*LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        PtsTimestampAdjusterProvider timestampAdjusterProvider = new PtsTimestampAdjusterProvider();
        DataSource dataSource = new DefaultUriDataSource(mContext, bandwidthMeter, mUserAgent);
        HlsChunkSource chunkSource = new HlsChunkSource(true , dataSource, mUri, mPlaylistFetcher,
                DefaultHlsTrackSelector.newDefaultInstance(mContext), bandwidthMeter, timestampAdjusterProvider,
                HlsChunkSource.ADAPTIVE_MODE_SPLICE);
        HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl,
                MAIN_BUFFER_SEGMENTS * BUFFER_SEGMENT_SIZE);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(mContext, sampleSource,
                MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
                MediaCodecSelector.DEFAULT);
        callback.onRender(videoRenderer, audioRenderer);*/
    }
}

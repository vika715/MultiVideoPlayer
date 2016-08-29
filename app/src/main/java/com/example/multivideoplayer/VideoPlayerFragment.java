package com.example.multivideoplayer;

import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.util.PlayerControl;
import com.google.android.exoplayer.util.Util;


public class VideoPlayerFragment extends Fragment implements SurfaceHolder.Callback,
        ExoPlayer.Listener, MediaCodecVideoTrackRenderer.EventListener {

    private static final String ARGS_URI = "uri";
    private static final String ARGS_CONTENT_TYPE = "content_type";
    private static final int RENDERER_COUNT = 2;
    private MediaController mMediaController;
    private SurfaceView mVideoSurfaceView;
    private Uri mUri;
    private int mContentType;
    private ExoPlayer mPlayer;
    private boolean mIsAutoPlay = true;
    private int mPlayerPosition;
    private MediaCodecVideoTrackRenderer mVideoRenderer;
    private RendererBuilder mBuilder;
    private final Handler mHandler = new Handler();

    public VideoPlayerFragment() {
    }

    public static VideoPlayerFragment newInstance(Uri uri, int contentType) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_URI, uri.toString());
        bundle.putInt(ARGS_CONTENT_TYPE, contentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUri = Uri.parse(bundle.getString(ARGS_URI));
        }
        if (bundle != null) {
            mContentType = bundle.getInt(ARGS_CONTENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.root_view);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                }
                return true;
            }
        });
        mBuilder = getRendererBuilder();
        mVideoSurfaceView = (SurfaceView) view.findViewById(R.id.video_view);
        mMediaController = new MediaController(getActivity());
        mMediaController.setAnchorView(mVideoSurfaceView);
        mVideoSurfaceView.getHolder().addCallback(this);
        return view;
    }

    private void toggleControlsVisibility() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT, 1000, 5000);
        mPlayer.addListener(this);
        mPlayer.seekTo(mPlayerPosition);
        mMediaController.setMediaPlayer(new PlayerControl(mPlayer));
        mMediaController.setEnabled(true);
        mBuilder.buildRender(new RendererBuilderCallback() {
            @Override
            public void onRender(MediaCodecVideoTrackRenderer videoRenderer,
                                 MediaCodecAudioTrackRenderer audioRenderer) {
                mVideoRenderer = videoRenderer;
                mPlayer.prepare(videoRenderer, audioRenderer);
                maybeStartPlayback();
            }

            @Override
            public void onRenderFailure(Exception e) {
                onError(e);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayerPosition = (int) mPlayer.getCurrentPosition();
            mPlayer.release();
            mPlayer = null;
        }
        mVideoRenderer = null;
    }

    private RendererBuilder getRendererBuilder() {
        switch (mContentType) {
            case Util.TYPE_OTHER:
                return new ExtractorRendererBuilder(getContext(), mUri);
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(getContext(), mUri, mHandler);
            default:
                throw new IllegalStateException();
        }
    }

    private void maybeStartPlayback() {
        Surface surface = mVideoSurfaceView.getHolder().getSurface();
        if (mVideoRenderer == null || surface == null || !surface.isValid()) {
            return;
        }
        mPlayer.sendMessage(mVideoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        if (mIsAutoPlay) {
            mPlayer.setPlayWhenReady(true);
            mIsAutoPlay = false;
        }
    }

    private void onError(Exception e) {
        getActivity().finish();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onPlayWhenReadyCommitted() {
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        onError(e);
    }

    @Override
    public void onDrawnToSurface(Surface surface) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio) {
        /*mVideoSurfaceView.setVideoWidthHeightRatio(
                height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);*/
    }

    @Override
    public void onDecoderInitializationError(
            MediaCodecTrackRenderer.DecoderInitializationException e) {
    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e) {
    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs,
                                     long initializationDurationMs) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        maybeStartPlayback();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mVideoRenderer != null) {
            mPlayer.blockingSendMessage(mVideoRenderer,
                    MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, null);
        }
    }

}

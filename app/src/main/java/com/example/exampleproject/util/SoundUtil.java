package com.example.exampleproject.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

public class SoundUtil {

	public interface OnMediaListener {
		public abstract void onCreate();

		public abstract void onPlay();

		public abstract void onError();

		public abstract void onPause();

		public abstract void onComplete();

		public abstract void onRelease();

		public abstract void onProgress(int progress);
	}

	private static SoundUtil mInstance;
	private Context mContext;
	private MediaPlayer m_MediaPlayer;
	private boolean m_IsPlaying = false;
	private boolean m_IsLoaded = false;// 是否已加载
	private OnMediaListener mMediaListener;

	protected SoundUtil(Context context) {
		mContext = context;
	}

	public static SoundUtil getIntance(Context context) {
		if (null == mInstance) {
			mInstance = new SoundUtil(context);
		}
		return mInstance;
	}
	
	public void setOnMediaListener(OnMediaListener listener) {
		mMediaListener = listener;
	}

	public void seekTo(int progress) {
		if (m_MediaPlayer != null)
			m_MediaPlayer.seekTo(progress);
	}

	public int getCurrentProgress() {
		if (m_MediaPlayer != null)
			return m_MediaPlayer.getCurrentPosition();
		return 0;
	}

	public void playFromUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		play(0, url, 0);
	}

	public void playFromAsset(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		play(1, url, 0);
	}

	public void playFromRaw(int id) {
		play(2, null, id);
	}

	public void pause() {
		if (m_MediaPlayer != null && m_IsPlaying) {
			m_MediaPlayer.pause();// 暂停
			m_IsPlaying = false;
			mMediaListener.onPause();
		}
	}

	public void release() {
		if (m_MediaPlayer != null) {
			m_MediaPlayer.release();
			m_MediaPlayer = null;
			m_IsPlaying = false;
			mMediaListener.onRelease();
		}
	}

	private MediaPlayer createUrlMediaPlayer(String path) {
		MediaPlayer mp = new MediaPlayer();
		try {
			mp.setDataSource(path);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return mp;
	}

	private MediaPlayer createAssetMediaPlayer(String fileName) {
		AssetManager am = mContext.getAssets();
		MediaPlayer mp = null;
		try {
			AssetFileDescriptor fd = am.openFd(fileName);
			mp = new MediaPlayer();
			mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
					fd.getLength());
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return mp;
	}

	private MediaPlayer createRawMediaPlayer(int id) {
		AssetFileDescriptor fd = mContext.getResources().openRawResourceFd(id);
		MediaPlayer mp = null;
		try {
			mp = new MediaPlayer();
			mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
					fd.getLength());
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalStateException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return mp;
	}

	private void play(int type, final String url, int id) {
		if (!m_IsPlaying) {
			if (m_MediaPlayer == null) {
				switch (type) {
				case 0:
					m_MediaPlayer = createUrlMediaPlayer(url);
					break;
				case 1:
					m_MediaPlayer = createAssetMediaPlayer(url);
					break;
				case 2:
					m_MediaPlayer = createRawMediaPlayer(id);
					break;
				}
				m_IsLoaded = false;
				mMediaListener.onCreate();
			}
			if (m_MediaPlayer != null) {
				// 当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
				// 以便其他应用程序可以使用该资源:
				m_MediaPlayer
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								m_IsPlaying = false;
								mMediaListener.onComplete();
							}
						});
				try {
					// 在播放音频资源之前，必须调用Prepare方法完成些准备工作
					if (!m_IsLoaded) {
						m_MediaPlayer.prepare();
						m_IsLoaded = true;
					}
					// 开始播放音频
					m_MediaPlayer.start();
					m_IsPlaying = true;
					mMediaListener.onPlay();
				} catch (IllegalStateException e) {
					e.printStackTrace();
					release();
					mMediaListener.onError();
				} catch (IOException e) {
					e.printStackTrace();
					release();
					mMediaListener.onError();
				}
			}
		}
	}

}

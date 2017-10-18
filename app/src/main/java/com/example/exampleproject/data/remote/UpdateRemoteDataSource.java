package com.example.exampleproject.data.remote;

import android.content.Context;

import com.example.exampleproject.data.util.net.okhttp.callback.FileCallback;
import com.example.exampleproject.util.AppUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 版本升级信息远程数据源
 * Created by chang on 2017/3/10.
 */

public class UpdateRemoteDataSource extends BaseRemoteDataSource {

    private Context mContext;
    private static UpdateRemoteDataSource INSTANCE;

    public UpdateRemoteDataSource(Context context) {
        super();
        this.mContext = context;
    }

    public static UpdateRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UpdateRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * 下载应用APK文件
     *
     * @param fileDir  目标目录
     * @param fileName 目标文件名
     * @param url      接口地址
     * @return
     */
    public Observable<String> getAppApk(final String fileDir, final String fileName, final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    mHttpUtil.downloadFileByGet(url, null, null, new FileCallback(fileDir, fileName) {
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            if (file != null && response != null) {
                                emitter.onComplete();
                                return;
                            }
                            emitter.onNext("");
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            if (!emitter.isDisposed())
                                emitter.onError(new OnErrorNotImplementedException(new Throwable("error")));
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                            emitter.onNext("" + Math.round(progress * 100));
                        }

                    });

                }
            }
        });
    }

    /**
     * 检查版本更新
     *
     * @param institutionCode 检测站ID
     * @param url             检查版本接口地址
     * @return
     */
    public Observable<String> checkUpdate(String institutionCode, String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("versionCode", "" + AppUtil.getVersionCode(mContext, AppUtil.getPackageName(mContext)));
        params.put("institutionCode", institutionCode);
        return post(url, null, params);
    }

}

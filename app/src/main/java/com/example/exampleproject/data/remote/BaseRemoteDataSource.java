package com.example.exampleproject.data.remote;

import android.text.TextUtils;

import com.example.exampleproject.data.util.net.HttpUtil;
import com.example.exampleproject.data.util.net.okhttp.callback.StringCallback;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 远程数据源基类
 * Created by chang on 2017/3/3.
 */

public class BaseRemoteDataSource {

    protected HttpUtil mHttpUtil;

    public BaseRemoteDataSource() {
        mHttpUtil = HttpUtil.getInstance();
    }

    public void cancelTag(Object tag) {
        mHttpUtil.cancelTag(tag);
    }

    public void cancelAll() {
        mHttpUtil.cancelAll();
    }

    /**
     * http post请求
     *
     * @param url    接口地址
     * @param params 参数
     * @return
     */
    protected Observable<String> post(final String url, final Map<String, String> headers, final Map<String, Object> params) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    mHttpUtil.post(url, headers, params, new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (TextUtils.isEmpty(s)) {
                                emitter.onNext("");
                                return;
                            }
                            emitter.onNext(s);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            if (!emitter.isDisposed())
                                emitter.onError(new OnErrorNotImplementedException(new Throwable("error")));
                        }

                    });
                }
            }
        });
    }
}

package com.example.exampleproject.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * m3u8文件解析类
 * <p>
 * Created by chang on 2017/3/8.
 */

public class M3U8ParserUtil {

    /**
     * 解析m3u8的ts下载地址
     *
     * @param fis
     * @return
     */
    public static List<String> parseString(FileInputStream fis) {
        List<String> resultList = null;
        try {
            if (fis != null) {
                resultList = new ArrayList<String>();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(fis));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) {
                        //解析出播放视频的iv的key
                        if (line.startsWith("#EXT-X-KEY")) {
                            resultList.add(line);
                        }

                    } else if (line.length() > 0 && line.startsWith("http://")) {
                        resultList.add(line);
                    }
                }
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "解析m3u8错误" + e.getMessage());
        }
        return resultList;
    }


    /**
     * 获取m3u8文件中的uri对应的key
     *
     * @param fis
     * @return
     */
    public static String getM3u8Key(FileInputStream fis) {
        try {
            List<String> strings = parseString(fis);
            String[] split = strings.get(0).split(",");
            for (String s : split) {
                if (s.contains("URI")) {
                    Log.e("解析出的内容getM3u8Key", s);
                    return s.split("=")[1];
                }

            }
        } catch (Exception e) {
            Log.e("解析内容错误", e.getMessage());
        }
        return "";
    }
}

package com.example.exampleproject.util.screenshot.helper;

import java.io.File;

public interface ShotCallback {
    void success(File file);
    void fail();
}

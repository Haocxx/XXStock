package com.haocxx.xxstock;

import com.haocxx.xxstock.base.core.Looper;
import com.haocxx.xxstock.sina.SinaStockHelper;

/**
 * Created by Haocxx
 * on 2021-1-24
 */
public class Main {

    /**
     * 阿里巴巴 hk09988
     * 小米 hk01810
     */
    public static void main(String[] args) {
        Looper.initMain();
        SinaStockHelper.start("hk01810");
        Looper.getMainLooper().loop();
        Looper.exit();
    }
}

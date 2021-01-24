package com.haocxx.xxstock;

import com.haocxx.xxstock.base.network.HttpUtil;
import com.haocxx.xxstock.sina.SinaStockHelper;

/**
 * Created by Haocxx
 * on 2021-1-24
 */
public class Main {

    public static void main(String[] args) {
        String result = HttpUtil.doGetRequest("http://hq.sinajs.cn/list=sh601006");
        StockDataModel model = SinaStockHelper.parseFromSina(result);
	    System.out.println(result);
    }
}

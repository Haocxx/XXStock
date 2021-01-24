package com.haocxx.xxstock.sina;

import com.haocxx.xxstock.StockDataModel;
import com.haocxx.xxstock.base.log.Log;
import com.haocxx.xxstock.base.network.HttpUtil;

/**
 * 针对新浪股票实时接口的工具类
 *
 * Created by Haocxx
 * on 2021-1-24
 */
public class SinaStockHelper {
    private final static String TAG = "SinaStockHelper";

    /**
     * 将新浪股票实时接口数据转为{@link StockDataModel
     */
    public static StockDataModel parseFromSina(String sinaData) {
        try {
            int startIndex = sinaData.indexOf("\"");
            int lastIndex = sinaData.lastIndexOf("\"");
            sinaData = sinaData.substring(startIndex + 1, lastIndex);
            String[] properties = sinaData.split(",");
            StockDataModel model = new StockDataModel();
            model.stockName = properties[0];
            model.todayOpenPrice = Double.parseDouble(properties[1]);
            model.yesterdayClosePrice = Double.parseDouble(properties[2]);
            model.currentPrice = Double.parseDouble(properties[3]);
            model.todayHighestPrice = Double.parseDouble(properties[4]);
            model.todayLowestPrice = Double.parseDouble(properties[5]);
            return model;
        } catch (Throwable e) {
            Log.debug(TAG, "bad format sinaData, no split sign: " + sinaData);
            return null;
        }
    }

    /**
     * 获取某一个股票的实时信息
     */
    public static StockDataModel requestSinaSingleStockInfo(String stockCode) {
        String result = HttpUtil.doGetRequest("http://hq.sinajs.cn/list=" + stockCode);
        return SinaStockHelper.parseFromSina(result);
    }
}

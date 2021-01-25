package com.haocxx.xxstock.sina;

import com.haocxx.xxstock.StockDataModel;
import com.haocxx.xxstock.base.core.Looper;
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

    public static void start(String stockCode) {
        new Thread(() -> {
            while (true) {
                StockDataModel result = requestSinaSingleStockInfo(stockCode);
                if (result != null) {
                    Looper.getMainLooper().enqueue(() -> {
                        Log.debug("股票名字", result.stockName);
                        Log.debug("今日开盘价", String.valueOf(result.todayOpenPrice));
                        Log.debug("昨日收盘价", String.valueOf(result.yesterdayClosePrice));
                        Log.debug("当前价格", String.valueOf(result.currentPrice));
                        Log.debug("今日最高价", String.valueOf(result.todayHighestPrice));
                        Log.debug("今日最低价", String.valueOf(result.todayLowestPrice));
                        Log.debug("==============================");
                    });
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 将新浪股票实时接口数据转为{@link StockDataModel
     */
    public static StockDataModel parseFromSina(String stockCode, String sinaData) {
        if (stockCode.startsWith("hk")) {
            return parseHKFromSina(sinaData);
        } else if (stockCode.startsWith("sz")) {
            return parseSZFromSina(sinaData);
        } else if (stockCode.startsWith("sh")) {
            return parseSHFromSina(sinaData);
        } else if (stockCode.startsWith("gb_")) {
            return parseUSFromSina(sinaData);
        } else {
            return null;
        }
    }

    /**
     * 解析深股数据
     */
    public static StockDataModel parseSZFromSina(String sinaData) {
        return parseAFromSina(StockDataModel.StockType.SZ, sinaData);
    }

    /**
     * 解析沪股数据
     */
    public static StockDataModel parseSHFromSina(String sinaData) {
        return parseAFromSina(StockDataModel.StockType.SH, sinaData);
    }

    /**
     * 解析A股数据
     */
    private static StockDataModel parseAFromSina(StockDataModel.StockType stockType, String sinaData) {
        try {
            int startIndex = sinaData.indexOf("\"");
            int lastIndex = sinaData.lastIndexOf("\"");
            sinaData = sinaData.substring(startIndex + 1, lastIndex);
            String[] properties = sinaData.split(",");
            StockDataModel model = new StockDataModel(stockType);
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
     * 解析港股数据
     */
    public static StockDataModel parseHKFromSina(String sinaData) {
        try {
            int startIndex = sinaData.indexOf("\"");
            int lastIndex = sinaData.lastIndexOf("\"");
            sinaData = sinaData.substring(startIndex + 1, lastIndex);
            String[] properties = sinaData.split(",");
            StockDataModel model = new StockDataModel(StockDataModel.StockType.HK);
            model.stockName = properties[1];
            model.todayOpenPrice = Double.parseDouble(properties[2]);
            model.yesterdayClosePrice = Double.parseDouble(properties[3]);
            model.currentPrice = Double.parseDouble(properties[6]);
            model.todayHighestPrice = Double.parseDouble(properties[4]);
            model.todayLowestPrice = Double.parseDouble(properties[5]);
            return model;
        } catch (Throwable e) {
            Log.debug(TAG, "bad format sinaData, no split sign: " + sinaData);
            return null;
        }
    }

    /**
     * 解析A股数据
     */
    private static StockDataModel parseUSFromSina(String sinaData) {
        try {
            int startIndex = sinaData.indexOf("\"");
            int lastIndex = sinaData.lastIndexOf("\"");
            sinaData = sinaData.substring(startIndex + 1, lastIndex);
            String[] properties = sinaData.split(",");
            StockDataModel model = new StockDataModel(StockDataModel.StockType.US);
            model.stockName = properties[0];
            model.todayOpenPrice = Double.parseDouble(properties[5]);
            model.yesterdayClosePrice = Double.parseDouble(properties[26]);
            model.currentPrice = Double.parseDouble(properties[1]);
            model.todayHighestPrice = Double.parseDouble(properties[6]);
            model.todayLowestPrice = Double.parseDouble(properties[7]);
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
        return SinaStockHelper.parseFromSina(stockCode, result);
    }
}

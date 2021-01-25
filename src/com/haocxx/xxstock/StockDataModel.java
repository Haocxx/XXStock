package com.haocxx.xxstock;

import java.io.Serializable;

/**
 * 股票实时信息数据结构
 *
 * Created by Haocxx
 * on 2021-1-24
 */
public class StockDataModel implements Serializable {
    public enum StockType {
        HK, //港股
        SZ, //深股
        SH, //沪股
        US, //美股
    }

    public StockDataModel(StockType stockType) {
        this.stockType = stockType;
    }

    public final StockType stockType;
    public String stockName; //股票名字
    public double todayOpenPrice; //今日开盘价
    public double yesterdayClosePrice; //昨日收盘价
    public double currentPrice; //当前价格
    public double todayHighestPrice; //今日最高价
    public double todayLowestPrice; //今日最低价
}

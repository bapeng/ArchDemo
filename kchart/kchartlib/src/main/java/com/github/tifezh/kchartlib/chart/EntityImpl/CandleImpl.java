package com.github.tifezh.kchartlib.chart.EntityImpl;

/**
 * 蜡烛图实体
 * Created by tifezh on 2016/6/9.
 */

public interface CandleImpl {

    /**
     * 开盘价
     */
    float getOpenPrice();

    /**
     * 最高价
     */
    float getHighPrice();

    /**
     * 最低价
     */
    float getLowPrice();

    /**
     * 收盘价
     */
    float getClosePrice();


    float getMA5Price();

    float getMA10Price();

    float getMA20Price();
}

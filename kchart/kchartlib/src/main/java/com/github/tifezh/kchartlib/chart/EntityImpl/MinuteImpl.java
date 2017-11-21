package com.github.tifezh.kchartlib.chart.EntityImpl;

import java.util.Date;

public interface MinuteImpl {

    /**
     * @return 获取均价
     */
    float getAvgPrice();

    /**
     * @return 获取成交价
     */
    float getPrice();

    Date getDate();
}

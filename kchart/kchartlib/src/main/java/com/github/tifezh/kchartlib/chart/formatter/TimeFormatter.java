package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.impl.ITimeFormatter;
import com.github.tifezh.kchartlib.utils.DateUtil;

import java.util.Date;

/**
 * 时间格式化器
 * Created by tifezh on 2016/6/21.
 */

public class TimeFormatter implements ITimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.shortTimeFormat.format(date);
    }
}

package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.impl.IValueFormatter;

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        return String.format("%.2f", value);
    }
}

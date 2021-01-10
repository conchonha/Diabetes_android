package com.example.diabetes.model;

import org.eazegraph.lib.models.BarModel;

public class ChartModel extends BarModel {

    public ChartModel(String _legendLabel, float _value) {
        super(_legendLabel, _value, 0xFF1FF4AC);
    }

    public ChartModel(float _value, int _color) {
        super(_value, _color);
    }

    public ChartModel(float _value) {
        super(_value);
    }
}

package org.nurgisa.api.graphics;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.theme.MatlabTheme;
import org.nurgisa.weather.models.Measurement;

import java.util.List;

public class XChart {
    public static void plotChart(List<Measurement> measurements) {
        List<Double> temperatures = measurements
                .stream()
                .map(Measurement::getTemperature)
                .toList();

        List<Integer> indices = measurements
                .stream()
                .map(measurements::indexOf)
                .toList();

        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title("Temperature Readings")
                .xAxisTitle("Measurement Index")
                .yAxisTitle("Temperature (°C)")
                .build();

        chart.getStyler().setTheme(new MatlabTheme());

        chart.addSeries("Temperature", indices, temperatures);

        new SwingWrapper<>(chart).displayChart();
    }
}

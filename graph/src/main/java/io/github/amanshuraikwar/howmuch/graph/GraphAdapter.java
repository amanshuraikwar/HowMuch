package io.github.amanshuraikwar.howmuch.graph;

import androidx.annotation.NonNull;

public class GraphAdapter extends SparkAdapter {
    private float[] yData;
    private String[] xAxisLabels;

    public GraphAdapter(float[] yData, String[] xAxisLabels) {
        this.yData = yData;
        this.xAxisLabels = xAxisLabels;
        notifyDataSetChanged();
    }


    public void updateGraph (float[] yData, String[] xAxisLabels){
        this.yData = yData;
        this.xAxisLabels = xAxisLabels;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return yData.length;
    }

    @NonNull
    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    @Override
    public String getXAxis(int index) {
        return xAxisLabels[index];
    }
}

package code_clone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;



public class BoxAndWhiskerChart {
     private static final int CHART_WIDTH = 800;
     private static final int CHART_HEIGHT = 600;

    // Refactored method to create Box-and-Whisker chart
    public void displayChart(BoxAndWhiskerChar boxData) {
        JFrame frame = new JFrame("Clone_Check");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create renderer
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(true);
        renderer.setUseOutlinePaintForWhiskers(true);
        renderer.setMedianVisible(true);
        renderer.setMeanVisible(false);

        // Create axes
        CategoryAxis xAxis = new CategoryAxis("First_Project_Files");
        NumberAxis yAxis = new NumberAxis("Second_Project_Values");
        
        // Create plot
        CategoryPlot plot = new CategoryPlot(boxData, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart("Box-and-Whisker Plot", new Font("SansSerif", Font.BOLD, 20), plot, true);

        // Create chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.LIGHT_GRAY);

        // Configure frame
        frame.add(chartPanel);
        frame.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Refactored method to create dataset from input data
    private DefaultBoxAndWhiskerCategoryDataset createDataset(List<List<Double>> data, List<String> fileNames) {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        for (int i = 0; i < data.size(); i++) {
            dataset.add(data.get(i), "First_Project vs Second_Project", fileNames.get(i));
        }
        return dataset;
    }

    // Refactored method to be called from main class
    public static void createAndDisplayChart(List<List<Double>> similarData, List<String> fileNames) {
        BoxAndWhiskerChart chart = new BoxAndWhiskerChart();
        DefaultBoxAndWhiskerCategoryDataset dataset = chart.createDataset(similarData, fileNames);
        EventQueue.invokeLater(() -> chart.displayChart(dataset));
    }
}

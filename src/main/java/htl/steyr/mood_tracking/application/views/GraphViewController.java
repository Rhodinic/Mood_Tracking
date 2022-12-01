package htl.steyr.mood_tracking.application.views;

import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.Entry;
import htl.steyr.mood_tracking.application.model.EntryRepository;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class GraphViewController {

    public LineChart<String, Integer> lineChart;
    public BarChart<String, Integer> barChart;

    public ComboBox<String> graphTypeComboBox;
    public DatePicker fromDatePicker;
    public DatePicker toDatePicker;

    public CheckBox showMoodCheckBox;
    public CheckBox showSocialCheckBox;
    public CheckBox showExhaustionCheckBox;
    public CheckBox showHumidityCheckBox;
    public CheckBox showTemperatureCheckBox;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    UserHandler userHandler;

    public void initialize(){
        /*
         * Initialize GraphType ComboBox with available Graphs
         */
        graphTypeComboBox.getItems().add("Linien-Graph");
        graphTypeComboBox.getItems().add("Balken-Graph");

        updateShownData();
    }

    public void graphTypeComboBoxClicked() {
        switch (graphTypeComboBox.getSelectionModel().getSelectedItem()) {
            case "Linien-Graph":
                hideAllGraphs();
                lineChart.setVisible(true);
                updateShownData();
                break;
            case "Balken-Graph":
                hideAllGraphs();
                barChart.setVisible(true);
                updateShownData();
                break;
        }
    }

    public void updateShownData(){
        List<Entry> currentData = getCurrentData();

        lineChart.getData().clear();
        barChart.getData().clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        /*
         * Fill all selected Series' and add to all charts
         */
        if(showMoodCheckBox.isSelected()){
            XYChart.Series<String, Integer> moodSeries = new XYChart.Series<>();
            moodSeries.setName("Stimmung von 1 - 100");

            for(Entry e : currentData){
                moodSeries.getData().add(new XYChart.Data<>(sdf.format(e.getDate()), e.getMood()));
            }

            addSeriesToAllCharts(moodSeries);
        }

        if(showSocialCheckBox.isSelected()){
            XYChart.Series<String, Integer> socialSeries = new XYChart.Series<>();
            socialSeries.setName("Menge an sozialer Interaktion von 1 - 100");

            for(Entry e : currentData){
                socialSeries.getData().add(new XYChart.Data<>(sdf.format(e.getDate()), e.getAmountOfSocialInteraction()));
            }

            addSeriesToAllCharts(socialSeries);
        }

        if(showExhaustionCheckBox.isSelected()){
            XYChart.Series<String, Integer> exhaustionSeries = new XYChart.Series<>();
            exhaustionSeries.setName("Anstrengung in Schule / Arbeit von 1 - 100");

            for(Entry e : currentData){
                exhaustionSeries.getData().add(new XYChart.Data<>(sdf.format(e.getDate()), e.getSchoolIntensity()));
            }

            addSeriesToAllCharts(exhaustionSeries);
        }

        if(showHumidityCheckBox.isSelected()){
            XYChart.Series<String, Integer> humiditySeries = new XYChart.Series<>();
            humiditySeries.setName("Feuchtigkeit in %");

            for(Entry e : currentData){
                humiditySeries.getData().add(new XYChart.Data<>(sdf.format(e.getDate()), (int) e.getWeather().getHumidity()));
            }

            addSeriesToAllCharts(humiditySeries);
        }

        if(showTemperatureCheckBox.isSelected()){
            XYChart.Series<String, Integer> temperatureSeries = new XYChart.Series<>();
            temperatureSeries.setName("Temperatur in °C");

            for(Entry e : currentData){
                temperatureSeries.getData().add(new XYChart.Data<>(sdf.format(e.getDate()), (int) e.getWeather().getTemperature()));
            }

            addSeriesToAllCharts(temperatureSeries);
        }
    }

    private List<Entry> getCurrentData(){
        /*
         * When no Date is selected select everything
         */
        Date fromDate = new GregorianCalendar(1970, Calendar.JANUARY, 1).getTime();
        Date toDate = new GregorianCalendar(3000, Calendar.JANUARY, 1).getTime();

        if(fromDatePicker.getValue() != null){
            fromDate = Date.from(Instant.from(fromDatePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        }

        if(toDatePicker.getValue() != null){
            toDate = Date.from(Instant.from(toDatePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
        }

        return entryRepository.findAllBetweenDates(userHandler.getUser(), fromDate, toDate);
    }

    private void hideAllGraphs(){
        lineChart.setVisible(false);
        barChart.setVisible(false);
    }

    private void addSeriesToAllCharts(XYChart.Series<String, Integer> series){
        lineChart.getData().add(series);
        barChart.getData().add(series);
    }
}

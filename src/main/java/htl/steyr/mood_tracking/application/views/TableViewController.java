package htl.steyr.mood_tracking.application.views;

import htl.steyr.mood_tracking.handlers.UserHandler;
import htl.steyr.mood_tracking.application.model.Entry;
import htl.steyr.mood_tracking.application.model.EntryRepository;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class TableViewController {

    public DatePicker fromDatePicker;
    public DatePicker toDatePicker;

    public TableView<Entry> dataTableView;
    public TableColumn<Entry, Date> dateColumn;
    public TableColumn<Entry, Integer> moodColumn;
    public TableColumn<Entry, Integer> socialColumn;
    public TableColumn<Entry, Integer> exhaustionColumn;
    public TableColumn<Entry, Double> humidityColumn;
    public TableColumn<Entry, Double> temperatureColumn;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    UserHandler userHandler;

    public void initialize(){
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));
        socialColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfSocialInteraction"));
        exhaustionColumn.setCellValueFactory(new PropertyValueFactory<>("schoolIntensity"));
        humidityColumn.setCellValueFactory(tf -> new SimpleDoubleProperty(tf.getValue().getWeather().getHumidity()).asObject());
        temperatureColumn.setCellValueFactory(tf -> new SimpleDoubleProperty(tf.getValue().getWeather().getTemperature()).asObject());

        updateShownData();
    }

    public void updateShownData() {
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

        /*
         * Add all entries in the date range to the table
         */
        dataTableView.getItems().clear();
        dataTableView.getItems().addAll(entryRepository.findAllBetweenDates(userHandler.getUser(), fromDate, toDate));
    }
}

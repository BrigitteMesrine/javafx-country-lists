package fr.afpa.combobox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    // left side elements of the visual interface : list of countries
    Label countriesLabel = new Label("Pays disponibles");
    ObservableList<Country> comboBoxList = FXCollections.observableArrayList();
    ComboBox<Country> countryComboBox = new ComboBox<>(comboBoxList);
    List<Country> countries = getCountriesList();

    // middle elements of the visual interface : interactions between left and right
    // sides
    Button addButton = new Button(">");
    Button addAllButton = new Button(">>");
    Button removeButton = new Button("<");
    Button removeAllButton = new Button("<<");
    VBox buttonVBox = new VBox(addButton, addAllButton, removeButton, removeAllButton);

    // right side elements of the visual interface : UpDowListView object and its
    // content ObservableList<Country> upDownCountries
    ObservableList<Country> upDownCountries = FXCollections.observableArrayList();
    UpDownListView<Country> upDownListView = new UpDownListView<>();
    Button exitButton = new Button("Quitter");

    // visual interface boxes
    GridPane gridPane = new GridPane();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        // adding visual elements to gridPane
        gridPane.add(countriesLabel, 0, 0, 1, 1);
        gridPane.add(countryComboBox, 0, 1, 1, 1);
        gridPane.add(buttonVBox, 1, 1, 1, 1);
        gridPane.add(upDownListView, 2, 1, 1, 1);
        gridPane.add(exitButton, 2, 2, 1, 1);

        // adding ISO 3166 countries and codes into comboBox
        for (Country country : countries) {
            comboBoxList.add(country);
        }

        // disabling Buttons by default
        removeButton.setDisable(true);
        removeAllButton.setDisable(true);
        addButton.setDisable(true);

        // listening changes in listView to disable add buttons if no elements remain in
        // comboBox
        comboBoxList.addListener(new ListChangeListener<Country>() {

            @Override
            public void onChanged(Change<? extends Country> c) {
                if (comboBoxList.isEmpty()) {
                    addButton.setDisable(true);
                    addAllButton.setDisable(true);
                } else {
                    addButton.setDisable(false);
                    addAllButton.setDisable(false);
                }
            }
        });

        // listening changes in listView to enable remove buttons if there are elements
        // in it
        upDownCountries.addListener(new ListChangeListener<Country>() {

            @Override
            public void onChanged(Change<? extends Country> c) {
                if (upDownCountries.isEmpty()) {
                    removeButton.setDisable(true);
                    removeAllButton.setDisable(true);
                } else {
                    removeButton.setDisable(false);
                    removeAllButton.setDisable(false);
                }
            }

        });

        countryComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            addButton.setDisable(newValue == null);
        });

        // event handlers for Buttons
        addButton.setOnAction(event -> {
            // checks if ComboBox selected item is of Country class (avoids null being
            // considered for the event handling)
            // java exception provoked with ```if
            // (countryComboBox.getSelectionModel().getSelectedItem().getClass() ==
            // Country.class)```
            // is this heresy ?
            if (countryComboBox.getSelectionModel().getSelectedItem() != null) {
                upDownCountries.add(countryComboBox.getValue());
                upDownListView.setObjectsList(upDownCountries);
                countryComboBox.getItems().remove(countryComboBox.getValue());
                countryComboBox.getSelectionModel().selectFirst();
            }
        });

        removeButton.setOnAction(event -> {
            if (upDownListView.getListView().getSelectionModel().getSelectedItem() != null) {
                countryComboBox.getItems().add(upDownListView.getListView().getSelectionModel().getSelectedItem());
                upDownCountries.remove(upDownListView.getListView().getSelectionModel().getSelectedItem());
                upDownListView.setObjectsList(upDownCountries);
            }
        });

        addAllButton.setOnAction(event -> {
            upDownCountries.addAll(comboBoxList);
            upDownListView.setObjectsList(upDownCountries);
            comboBoxList.clear();
        });

        removeAllButton.setOnAction(event -> {
            upDownCountries.clear();
            comboBoxList.setAll(countries);
        });

        // why is these functions not working while selectedCountryIndex is outside of it ?
        upDownListView.getUpBtn().setOnAction(event -> {
            int selectedCountryIndex = upDownListView.getListView().getSelectionModel().getSelectedIndex();
            if (selectedCountryIndex > 0) {
                Collections.swap(upDownListView.getListView().getItems(), selectedCountryIndex, (selectedCountryIndex - 1));
                upDownListView.getListView().getSelectionModel().selectPrevious();
            }
        });
        upDownListView.getDownBtn().setOnAction(event -> {
            int selectedCountryIndex = upDownListView.getListView().getSelectionModel().getSelectedIndex();
            if (selectedCountryIndex < upDownListView.getListView().getItems().size()) {
                Collections.swap(upDownListView.getListView().getItems(), selectedCountryIndex, (selectedCountryIndex + 1));
                upDownListView.getListView().getSelectionModel().selectNext();
            }
        });

        exitButton.setOnAction(event -> {
            Platform.exit();
        });

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }

    public static List<Country> getCountriesList() {

        List<Country> countries = new ArrayList<>();

        String[] countryCodes = Locale.getISOCountries();

        for (String countryCode : countryCodes) {
            Locale obj = Locale.of("", countryCode);
            countries.add(new Country(obj.getDisplayCountry(), obj.getISO3Country()));
        }

        return countries;
    }

}

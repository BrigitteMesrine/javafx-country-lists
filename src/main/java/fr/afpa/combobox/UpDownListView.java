package fr.afpa.combobox;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UpDownListView<T> extends VBox {

    private Button upBtn;
    private Button downBtn;
    private ListView<T> listView;
    /**
     * Cette liste sera instanciée par un code extérieur à la classe
     * Dans App.java, par exemple :)
     */
    private ObservableList<T> objectsList;

    public UpDownListView() {
        // TODO Charger les images des boutons
        // -> code fourni dans l'énoncé du TP
        // Création des composants graphiques utilisés
        this.upBtn = new Button("Up");
        this.downBtn = new Button("Down");
        this.listView = new ListView<>();
        // positionnerment des boutons côte-à-côte, utilisation d'un HBox
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.getChildren().add(this.upBtn);
        buttonBox.getChildren().add(this.downBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().add(buttonBox);
        this.getChildren().add(this.listView);
    }

    /**
     * Fonction à appeler pour associer une "ObservableList" à ce composant
     * graphique (sinon ça marche pas).
     * 
     * @param list La liste observable concernée.
     */
    public void setObjectsList(ObservableList<T> list) {
        this.objectsList = list;
        listView.setItems(list);
    }

    public ObservableList<T> getObjectsList() {
        return objectsList;
    }

    public ListView<T> getListView() {
        return listView;
    }

}

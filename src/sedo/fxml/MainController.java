/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sedo.fxml;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sedo.Document;
import sedo.Document.State;
import sedo.db.testDB;
import sedo.ExtendedState;

/**
 *
 * @author tsowa
 */
public class MainController implements Initializable {
    @FXML
    private AnchorPane apParent;
    @FXML
    TableView tvTable;
    @FXML
    private ToggleButton btnToRegister;
    @FXML
    private ToggleButton btnInAction;
    @FXML
    private ToggleButton btnInControl;
    @FXML
    private ToggleButton btnAtManager;
    @FXML
    private ToggleButton btnInReview;
    @FXML
    private ToggleButton btnWriteOff;
    @FXML
    private MenuItem mnuNewDocument;
    
    private ProgressIndicator piWait;  
    private Label lblEmptyList;
    private Label lblEmpty;
    private ToggleGroup tgToolBar;
    
    private ObservableList<Document> docList;
    
    @FXML
    TableColumn<Document, ImageView> colType;
    @FXML
    TableColumn<Document, ImageView> colControl;
    @FXML
    TableColumn<Document, String> colRegNum;
    @FXML
    TableColumn<Document, String> colRegDate;
    @FXML
    TableColumn<Document, String> colContent;
    @FXML
    TableColumn<Document, String> colAddr;
    @FXML
    TableColumn<Document, String> colOutNum;
    @FXML
    TableColumn<Document, String> colOutDate;
    @FXML
    TableColumn<Document, Integer> colFiles;
    
    private void setCellFactories() {
        colType.setCellValueFactory((CellDataFeatures<Document, ImageView> d) -> {
            ImageView iv = new ImageView();
            Image img;
            switch(d.getValue().getType()){
                case OUT:
                    img = new Image(getClass().getResourceAsStream("/sedo/res/out.png"));
                    break;
                case IN:
                default:
                    img = new Image(getClass().getResourceAsStream("/sedo/res/in.png"));
                    break;
            }
            iv.setImage(img);
            return new SimpleObjectProperty(iv);
        });
        colControl.setCellValueFactory((CellDataFeatures<Document, ImageView> d) -> {
            ImageView iv = new ImageView();
            if(d.getValue().isControl()){
              Image img = new Image(getClass().getResourceAsStream("/sedo/res/control.png"));
              iv.setImage(img);
            }
            return new SimpleObjectProperty(iv);
        });
        colRegNum.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        colRegDate.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          Date regDate = d.getValue().getRegDate();
          String ret = null != regDate ? (new SimpleDateFormat("dd MMM yyyy")).format(regDate) : "";
          return new SimpleStringProperty(ret);
        });
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colAddr.setCellValueFactory(new PropertyValueFactory<>("addr"));
        colOutNum.setCellValueFactory(new PropertyValueFactory<>("outNumber"));
        colOutDate.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          Date outDate = d.getValue().getOutDate();
          String ret = null != outDate ? (new SimpleDateFormat("dd MMM yyyy")).format(outDate) : "";
          return new SimpleStringProperty(ret);
        });
        colFiles.setCellValueFactory((CellDataFeatures<Document, Integer> d) -> {
            return new SimpleIntegerProperty(d.getValue().getFiles().length).asObject();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        docList = FXCollections.observableArrayList();
        tgToolBar = new ToggleGroup();
        piWait = new ProgressIndicator();
        lblEmptyList = new Label("Список пуст");
        lblEmpty = new Label("");
        piWait.setMaxSize(52, 53);
        piWait.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        
        setCellFactories();

        btnToRegister.setToggleGroup(tgToolBar);
        btnInAction.setToggleGroup(tgToolBar);
        btnInControl.setToggleGroup(tgToolBar);
        btnAtManager.setToggleGroup(tgToolBar);
        btnInReview.setToggleGroup(tgToolBar);
        btnWriteOff.setToggleGroup(tgToolBar);
        
        btnToRegister.setSelected(true);
        
        btnToRegister.setOnAction(event -> {
            reloadTableDocs(ExtendedState.NOT_REGISTERED);
        });
        btnInAction.setOnAction(event -> {
            reloadTableDocs(ExtendedState.IN_ACTION);
        });
        btnInControl.setOnAction(event -> {
            reloadTableDocs(ExtendedState.IS_CONTROL);
        });
        btnAtManager.setOnAction(event -> {
            reloadTableDocs(ExtendedState.AT_MANAGER);
        });
        btnInReview.setOnAction(event -> {
            reloadTableDocs(ExtendedState.IN_REVIEW);
        });
        btnWriteOff.setOnAction(event -> {
            reloadTableDocs(ExtendedState.WORK_OFF);
        });
        
        reloadTableDocs(ExtendedState.NOT_REGISTERED);
    }
    
    private void reloadTableDocs(ExtendedState docExtState) {
        tvTable.setItems(null);
        docList.clear();
        tvTable.setPlaceholder(piWait);
        Collection<Document> docs = new ArrayList<>();
        switch(docExtState) {
            case REGISTERED:
                docs = testDB.getRegistered();
                break;
            case IS_CONTROL:
                docs = testDB.getControl();
                break;
            case IN_ACTION:
                break;
            case AT_MANAGER:
                break;
            case IN_REVIEW:
                break;
            case WORK_OFF:
                break;
            case NOT_REGISTERED:
            default:
                docs = testDB.getNotRegistered();
                break;
        }
        docList.addAll(docs);
        if(docs.isEmpty() || docList.isEmpty()) {
            tvTable.setPlaceholder(lblEmptyList);
        } else {
            tvTable.setPlaceholder(lblEmpty);
            tvTable.setItems(docList);
        }
    }
    
    @FXML
    private void openDocument(MouseEvent event) throws Exception {
      if (2 == event.getClickCount()) {
        Document doc = (Document)tvTable.getSelectionModel().getSelectedItem();
        if(null != doc) {
          Parent root = FXMLLoader.load(getClass().getResource("/sedo/fxml/Document.fxml"));
          Scene scene = new Scene(root);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.initOwner(tvTable.getScene().getWindow());
          stage.setResizable(false);
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.setUserData(doc);
          stage.showAndWait();
          tvTable.getSelectionModel().clearSelection();
        }
      }
    }
}
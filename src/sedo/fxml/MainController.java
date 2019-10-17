package sedo.fxml;

import java.io.IOException;
import java.time.Instant;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sedo.db.entity.Document;
import sedo.db.testDB;
import sedo.ExtendedState;
import sedo.db.entity.lists.Nomenclature;

/**
 *
 * @author tsowa
 */
public class MainController implements Initializable {
    private ResourceBundle _rb;
    
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
    private Button btnPrint;
    @FXML
    private MenuItem mnuNewOutcoming;
    @FXML
    private MenuItem mnuNewIncoming;
    @FXML
    private MenuItem mnuNewDocument;
    @FXML
    private MenuItem mnuFindDocument;
    
    private ProgressIndicator piWait;  
    private Label lblEmptyList;
    private Label lblEmpty;
    private ToggleGroup tgToolBar;
    
    private ObservableList<Document> docList;
    private ExtendedState extState;
    
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
            if(null != d.getValue().getControl().getKey()){
              Image img = new Image(getClass().getResourceAsStream("/sedo/res/control.png"));
              iv.setImage(img);
            }
            return new SimpleObjectProperty(iv);
        });
        colRegNum.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          return new SimpleStringProperty(d.getValue().getRegNumber());
        });
        colRegDate.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          Instant regDate = d.getValue().getRegDate();
          String ret = null != regDate ? DateTimeFormatter.ofPattern(_rb.getString("date_format")).withZone(ZoneId.systemDefault()).format(regDate) : "";
          return new SimpleStringProperty(ret);
        });
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        colAddr.setCellValueFactory(new PropertyValueFactory<>("correspondent"));
        colOutNum.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          return new SimpleStringProperty(d.getValue().getOutNumber());
        });
        colOutDate.setCellValueFactory((CellDataFeatures<Document, String> d) -> {
          Instant outDate = d.getValue().getOutDate();
          String ret = null != outDate ? DateTimeFormatter.ofPattern(_rb.getString("date_format")).withZone(ZoneId.systemDefault()).format(outDate) : "";
          return new SimpleStringProperty(ret);
        });
        colFiles.setCellValueFactory((CellDataFeatures<Document, Integer> d) -> {
            return new SimpleIntegerProperty(d.getValue().getFile().size()).asObject();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      _rb = ResourceBundle.getBundle("Settings");
        docList = FXCollections.observableArrayList();
        tgToolBar = new ToggleGroup();
        piWait = new ProgressIndicator();
        lblEmptyList = new Label(_rb.getString("list_is_empty"));
        lblEmpty = new Label("");
        piWait.setMaxSize(52, 53);
        piWait.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        extState = ExtendedState.NOT_REGISTERED;
        
        setCellFactories();

        btnToRegister.setToggleGroup(tgToolBar);
        btnInAction.setToggleGroup(tgToolBar);
        btnInControl.setToggleGroup(tgToolBar);
        btnAtManager.setToggleGroup(tgToolBar);
        btnInReview.setToggleGroup(tgToolBar);
        btnWriteOff.setToggleGroup(tgToolBar);
        
        btnToRegister.setSelected(true);
        
        btnToRegister.setOnAction(event -> {
          extState = ExtendedState.NOT_REGISTERED;
          reloadTableDocs(extState);
        });
        btnInAction.setOnAction(event -> {
            extState = ExtendedState.IN_ACTION;
            reloadTableDocs(extState);
        });
        btnInControl.setOnAction(event -> {
            extState = ExtendedState.IS_CONTROL;
            reloadTableDocs(extState);
        });
        btnAtManager.setOnAction(event -> {
            extState = ExtendedState.AT_MANAGER;
            reloadTableDocs(extState);
        });
        btnInReview.setOnAction(event -> {
            extState = ExtendedState.IN_REVIEW;
            reloadTableDocs(extState);
        });
        btnWriteOff.setOnAction(event -> {
            extState = ExtendedState.WORK_OFF;
            reloadTableDocs(extState);
        });
        
        mnuNewDocument.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/new.png"))));
        mnuNewOutcoming.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/out.png"))));
        mnuNewIncoming.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/in.png"))));
        mnuFindDocument.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/find2.png"))));
        btnPrint.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/print.png"))));
        
        mnuNewOutcoming.setOnAction(event -> {
          registerNewDoc(Document.Type.OUT);
        });
        mnuNewIncoming.setOnAction(event -> {
          registerNewDoc(Document.Type.IN);
        });
        
        reloadTableDocs(extState);//TODO: make async load
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
                docs = testDB.getInAction();
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
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/sedo/fxml/Document.fxml"));
          loader.setController(new DocumentController());
          DocumentController dc = loader.getController();
          dc.doc = doc;
          AnchorPane root = loader.load();
          Scene scene = new Scene(root);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.initOwner(tvTable.getScene().getWindow());
          stage.setResizable(false);
          stage.initModality(Modality.APPLICATION_MODAL);
          //stage.setUserData(doc);
          stage.getIcons().add(new Image(getClass().getResourceAsStream("/sedo/res/" + (Document.Type.IN == doc.getType() ? "in" : "out") + ".png")));
          if(Document.Status.REGISTERED == doc.getStatus() || null != doc.getRegNumber())
            stage.setTitle("№" + doc.getRegNumber() + " от " + DateTimeFormatter.ofPattern(_rb.getString("date_format")).withZone(ZoneId.systemDefault()).format(doc.getRegDate()));
          else
            stage.setTitle(_rb.getString("unregistered_document"));
          stage.showAndWait();
          tvTable.getSelectionModel().clearSelection();
          reloadTableDocs(extState);
        }
      }
    }
    
    private void registerNewDoc(Document.Type type) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sedo/fxml/Document.fxml"));
        loader.setController(new DocumentController());
        DocumentController dc = loader.getController();
        dc.doc = new Document();
        dc.doc.setType(type);
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(tvTable.getScene().getWindow());
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/sedo/res/man.png")));
        stage.setTitle(_rb.getString("new_document"));
        stage.showAndWait();
        reloadTableDocs(extState);
      } catch(IOException iex) {
        
      }
    }
}

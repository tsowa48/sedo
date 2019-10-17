package sedo.fxml;

import java.io.File;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sedo.db.entity.Document;
import sedo.db.entity.lists.ICorrespondent;
import sedo.db.entity.lists.Nomenclature;
import sedo.db.testDB;

/**
 * FXML Controller class
 *
 * @author tsowa
 */
public class DocumentController implements Initializable {
  private ResourceBundle _rb;
  public Document doc;
  
  private Boolean canEdit;
  
  @FXML
  private ComboBox<Nomenclature> cbRegNumber;
  @FXML
  private DatePicker dpRegDate;
  @FXML
  private ChoiceBox<Document.Access> cbAccess;
  @FXML
  private TextArea taContent;
  @FXML
  private TextArea taNote;
  @FXML
  private ListView<File> lvFiles;
  @FXML
  private TextField tfOutNum;
  @FXML
  private TextField tfOutDate;
  @FXML
  private ComboBox<ICorrespondent> cbTo;
  @FXML
  private ChoiceBox<Document.Delivery> cbDeliveredBy;
  
  @FXML
  private SplitPane sp1;
  @FXML
  private SplitPane sp2;
  @FXML
  private AnchorPane apRight2;
  @FXML
  private Button btnSave;
  @FXML
  private Button btnPrint;
  @FXML
  private Button btnSend;
  @FXML
  private GridPane gpFrom;
  @FXML
  private GridPane gpTo;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    _rb = ResourceBundle.getBundle("Settings");
    btnSave.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/save.png"))));
    btnPrint.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/print.png"))));
    btnSend.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/sedo/res/send.png"))));
    
    cbRegNumber.setConverter(new StringConverter<Nomenclature>() {
      @Override public String toString(Nomenclature nom) { return nom.toString(); }
      @Override public Nomenclature fromString(String string) { return null; }
    });
    cbRegNumber.setCellFactory((ListView<Nomenclature> param) -> {
      final ListCell<Nomenclature> cell = new ListCell<Nomenclature>() {
        @Override protected void updateItem(Nomenclature n, boolean bln) {
          super.updateItem(n, bln);
          if(n != null)
            setText(n.toString() + " " + n.getName());
          else
            setText(null);
        }
      };
      return cell;
    });
    tfOutNum.setText(doc.getOutNumber());
    tfOutDate.setText(DateTimeFormatter.ofPattern(_rb.getString("date_format")).withZone(ZoneId.systemDefault()).format(doc.getOutDate()));
    Node nodeLeft = sp1.getItems().get(0);
    Node nodeRight = sp1.getItems().get(1);
    
    cbRegNumber.setOnAction(this::onRegNumberChanged);
    cbRegNumber.setItems(testDB.getNomenclatures(doc.getType()));
    cbAccess.setItems(FXCollections.observableArrayList(Document.Access.values()));
    cbDeliveredBy.setItems(FXCollections.observableArrayList(Document.Delivery.values()));
    apRight2.minWidthProperty().bind(sp2.widthProperty().multiply(0.3));
    apRight2.maxWidthProperty().bind(sp2.widthProperty().multiply(0.3));
    gpFrom.minWidthProperty().bind(sp1.widthProperty().multiply(0.5));
    gpFrom.maxWidthProperty().bind(sp1.widthProperty().multiply(0.5));
    lvFiles.setCellFactory((ListView<File> list) -> {
      ListCell<File> lc = new ListCell<File>() {
        @Override
        protected void updateItem(File file, boolean b) {
          super.updateItem(file, b);
          if(file != null) {
            HBox container = new HBox();
            ImageView iv = new ImageView();
            //TODO: set icon of file
            Image img = new Image(getClass().getResourceAsStream("/sedo/res/new.png"));
            iv.setImage(img);
            Label lblText = new Label();
            lblText.setText(file.getName());
            container.getChildren().add(iv);
            container.getChildren().add(lblText);
            setGraphic(container);
          }
        }
      };
      return lc;
    });
    
    if(Document.Type.IN == doc.getType()) {
      sp1.getItems().set(0, nodeRight);
      sp1.getItems().set(1, nodeLeft);
      if(null != cbTo.getValue())
        cbTo.setDisable(true);
      if(!tfOutNum.getText().isEmpty())
        tfOutNum.setDisable(true);
      if(!tfOutDate.getText().isEmpty())
        tfOutDate.setDisable(true);
      if(null != cbDeliveredBy.getValue())
        cbDeliveredBy.setDisable(true);
    }
    
    if(Document.Status.REGISTERED.equals(doc.getStatus()) || null != doc.getRegNumber()) {
      canEdit = false;
      cbRegNumber.setValue(new Nomenclature("", doc.getRegNumber(), doc.getType()));
      dpRegDate.setValue(doc.getRegDate().atZone(ZoneId.systemDefault()).toLocalDate());
    } else {
      canEdit = true;
      dpRegDate.setValue((new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
      cbAccess.setValue(Document.Access.ALL);
    }
    taContent.setText(doc.getContent());
    lvFiles.setItems(FXCollections.observableArrayList(doc.getFile()));
    
    taContent.setEditable(canEdit);
    taNote.setEditable(canEdit);
    dpRegDate.setDisable(!canEdit);
    cbAccess.setDisable(!canEdit);
    lvFiles.setDisable(!canEdit);
    cbRegNumber.setDisable(!canEdit);
    
    System.out.println(doc.toString());
  }
  
  @FXML
  private void onRegNumberChanged(ActionEvent event) {
    System.out.println("DEBUG: NOMENCLATURE CHANGE");
  }
  
  @FXML
  private void onSave(ActionEvent event) {
    //TODO: check required fields
    doc.setAccess(cbAccess.getValue());
    doc.setContent(taContent.getText());
    doc.setControl(null);
    doc.setCorrespondent(null);
    doc.setDelivered_by(null);
    //doc.setFile(null);
    doc.setNote(taNote.getText());
    doc.setRegDate(dpRegDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    Nomenclature nomenclature = cbRegNumber.getValue();
    doc.setRegNumber(nomenclature.get());
   if(Document.Type.IN.equals(doc.getType()))
      doc.setStatus(Document.Status.AT_MANAGER);
    else
      doc.setStatus(Document.Status.REGISTERED);
    //TODO: save doc into DB
    ((Stage)btnSave.getScene().getWindow()).close();
  }
  
  @FXML
  private void onPrint(ActionEvent event) {
    System.out.println("DEBUG: DOCUMENT PRINTED");
  }
  
  @FXML
  private void onSend(ActionEvent event) {
    System.out.println("DEBUG: DOCUMENT SENDED");
  }
}

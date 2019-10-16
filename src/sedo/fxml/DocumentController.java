package sedo.fxml;

import java.io.File;
import java.net.URL;
import java.time.ZoneId;
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
import javafx.util.StringConverter;
import sedo.db.entity.Document;
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
    Node nodeLeft = sp1.getItems().get(0);
    Node nodeRight = sp1.getItems().get(1);
    if(Document.Type.OUT == doc.getType()) {
      sp1.getItems().set(0, nodeRight);
      sp1.getItems().set(1, nodeLeft);
    }
    cbRegNumber.setOnAction(this::onRegNumber);
    cbRegNumber.setItems(testDB.getNomenclatures());
    cbAccess.setItems(FXCollections.observableArrayList(Document.Access.values()));
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
    
    if(Document.Status.REGISTERED == doc.getStatus() || null != doc.getRegNumber()) {
      canEdit = false;
      cbRegNumber.setValue(doc.getRegNumber());
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
  }
  
  @FXML
  private void onRegNumber(ActionEvent event) {
    System.out.println("DEBUG: NOMENCLATURE CHANGE");
  }
}

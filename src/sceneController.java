
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class sceneController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    ObservableList<String> schranken = FXCollections.observableArrayList();
    ObservableList<String> schrankeneng = FXCollections.observableArrayList();
    ObservableList<String> slots = FXCollections.observableArrayList();
    ObservableList<Benutzer> benutzerList = FXCollections.observableArrayList();
    ObservableList<Gerate> gerateList = FXCollections.observableArrayList();
    ObservableList<Gerate> tempGerateList = FXCollections.observableArrayList();
    ObservableList<Gerate> fehlerhafteGerateList = FXCollections.observableArrayList();
    ObservableList<Gerate> korrektGerateList = FXCollections.observableArrayList();
    ObservableList<String> benutzer = FXCollections.observableArrayList(Arrays.asList("Admin", "Benutzer"));

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private final Semaphore sem = new Semaphore(20);
    public int FLAG, FLAG2, X = 0;
    public int PAUSEDTIME = 0;
    public double pausedProgress = 0;
    public boolean PAUSED = false;
    public boolean PROCESSING = false;
    private Label label;
    private ResourceBundle bundle;
    private Locale locale;
    Benutzer aktuelleBenutzer = new Benutzer(1, "1", "Hans", "Mueller", "Admin");

    @FXML
    private Button abbrechen_button, sorunvar, sorunyok, geciciweiter_button,
            pause_button, abbrechenTest_button,
            testNichtInOrdnung_button, init_weiter_Button;
    @FXML
    private AnchorPane loginPane, schrankPane, menuAdminPane, menuBenutzerPane, initialisierungStartPane, initialisierungPane, benutzerSettings, benutzerSettingsPane,
            settingsPane, schrankSettingsPane,
            weiterZumTestProduktCheckInPane, testProduktCheckInPane, produkt_pane1, vortestPane, haupttestPane, funktionCheckPane, funktionCheckPane2, endePane,
            erfolgreich_pane1, fertig_pane1, funktest_pane1, pruef_pane, fehler_pane, bevorNachtest_pane, nachtest_pane,
            bevorHaupttest_pane, alleProdukteNichtOK_pane, alleProdukteOK_pane, ende_pane;
    @FXML
    private Label lblErrors, lblStatus1, lblStatus, lblStatusSchrank, lblStatusSchrank1, hinzufuegen_label, entfernen_label,
            time_label, slot_pruef_label, pruef_time_label, fehler_label, entfernen_label1, hinzufuegen_label2, init_timer_label, slot_pruef_label2, check_timer_label, nachtestinit_label,
            nachtestcheck_label, achtung_label, verstrZeit_label, haupttest_label, testNichtInOrdnung_label, temp_label;
    @FXML
    private TextField txtId, schrankid_txtfield1, schrankid_txtfield, benutzerid_txtfield, benutzerPassword_txtfield, benutzerName_txtfield,
            benutzerNachame_txtfield, benutzerid_txtfield1, schrank_benutzerid, bauteil_textfield, auftrag_textfield,
            bauteil_textfield2, auftrag_textfield2;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> slot_comboBox = new ComboBox<>();
    @FXML
    private ComboBox<String> slot_comboBox2 = new ComboBox<>();
    @FXML
    private ComboBox<String> slot_comboBox3 = new ComboBox<>();
    @FXML
    private ComboBox<String> schrankauswahl_comboBox = new ComboBox<>();

    @FXML
    private ProgressBar progress_bar = new ProgressBar(0);
    @FXML
    private TableColumn<Gerate, Integer> bauteil_column, slot_column, bauteil_column1, slot_column1, slot_column2, bauteil_column2;
    @FXML
    private TableColumn<Gerate, String> auftrag_column, ergebnis_column, auftrag_column1, ergebnis_column1, ergebnis_column2, auftrag_column2;
    @FXML
    private TableView burnintest_table, burnintest_table1, burnintest_table2;

    public sceneController() {
        con = ConnectionUtil.conDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (con != null) {
            loadBenutzerList();
            loadGerateList();
            fill_schrankAuswahlComboBox();
            fill_slotsComboBox();
        }
        burnintest_table1.setItems(tempGerateList);
        burnintest_table2.setItems(tempGerateList);
        schrankauswahl_comboBox.setEditable(false);
        schrankauswahl_comboBox.setValue("Schranken");
        schrankauswahl_comboBox.getItems().addAll(schranken);
        slot_comboBox3.setValue("Rolle");
        slot_comboBox3.getItems().addAll(benutzer);
        slot_comboBox.setEditable(false);
        slot_comboBox.setValue("Slots");
        slot_comboBox.getItems().addAll(slots);
        slot_comboBox2.setEditable(false);
        slot_comboBox2.setValue("Slots");
        slot_comboBox2.getItems().addAll(slots);
    }

    public void switchScreen(ActionEvent event, AnchorPane oldPaneId, AnchorPane newPaneId, String title) {
        oldPaneId.setVisible(false);
        newPaneId.setVisible(true);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
    }

    public void setTimer(int interval, int time) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int seconds = interval - time;
            int actual_time = time;
            int index = 0;

            @Override
            public void run() {
                if (actual_time <= interval && stage.getTitle().equals("Haupttest")) {
                    PROCESSING = true;
                    if (actual_time % 10 == actual_time) {
                        Platform.runLater(() -> verstrZeit_label.setText("00:00:0" + actual_time));
                    } else {
                        Platform.runLater(() -> verstrZeit_label.setText("00:00:" + actual_time));
                    }
                    Platform.runLater(() -> actual_time++);
                } else if (seconds >= 0) {
                    PROCESSING = true;
                    if (seconds % 10 == seconds) {
                        if (stage.getTitle().equals("Nachtest")) {
                            Platform.runLater(() -> init_timer_label.setText("00:00:0" + seconds));
                        } else {
                            Platform.runLater(() -> time_label.setText("00:00:0" + seconds));
                        }
                    } else {
                        if (stage.getTitle().equals("Nachtest")) {
                            Platform.runLater(() -> init_timer_label.setText("00:00:" + seconds));
                        } else {
                            Platform.runLater(() -> time_label.setText("00:00:" + seconds));
                        }
                    }
                } else {
                    init_weiter_Button.setDisable(false);
                    if (stage.getTitle().equals("Nachtest")) {
                        nachtestinit_label.setDisable(true);
                        init_timer_label.setDisable(true);
                        PROCESSING = false;
                    }
                    if (stage.getTitle().equals("Haupttest")) {
                        pause_button.setDisable(true);
                        Platform.runLater(() -> haupttest_label.setText("Haupttest Ende..."));
                        geciciweiter_button.setDisable(false);
                        setHaupttestErgebnis();
                        PROCESSING = false;
                    }
                    if (stage.getTitle().equals("Initialisierung")) {
                        PROCESSING = false;
                    }
                    Platform.runLater(() -> timer.cancel());
                    Platform.runLater(() -> timer.purge());
                }
                Platform.runLater(() -> seconds--);
                if (FLAG == 1) {
                    FLAG = 0;
                    Platform.runLater(() -> PROCESSING = false);
                    PAUSEDTIME = actual_time;
                    Platform.runLater(() -> seconds = -1);
                    Platform.runLater(() -> actual_time = 11);
                    Platform.runLater(() -> timer.cancel());
                    Platform.runLater(() -> timer.purge());
                }
            }
        }, 0, 1000);
    }

    public void progress(double progressTime) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;
            double actual_progress = progressTime;
            float x = RequestHandler1.actualTemp;
            String f = Float.toString(x);

            @Override
            public void run() {
                if (actual_progress < 1) {
                    Platform.runLater(() -> progress_bar.setProgress(actual_progress));
                    Platform.runLater(() -> actual_progress += 0.017);
                    float y = RequestHandler1.actualTemp;
                    String z = Float.toString(y);
                    Platform.runLater(() -> temp_label.setText(z));
                } else {
                    pause_button.setDisable(true);
                    Platform.runLater(() -> timer.cancel());
                    Platform.runLater(() -> timer.purge());
                }
                if (FLAG2 == 1) {
                    FLAG2 = 0;
                    pausedProgress = actual_progress;
                    Platform.runLater(() -> actual_progress = 1);
                    Platform.runLater(() -> timer.cancel());
                    Platform.runLater(() -> timer.purge());
                }
            }
        }, 0, 1000);

    }

    public void setVortestTimer(int interval, int index) {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int seconds = interval;
            int tempListSize = tempGerateList.size();
            int slotIndex = index;

            @Override
            public void run() {
                while (PROCESSING && stage.getTitle().equals("Nachtest")) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex1) {
                        Logger.getLogger(sceneController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
                nachtestcheck_label.setDisable(false);
                check_timer_label.setDisable(false);
                if (slotIndex != -1) {
                    int k = tempGerateList.get(slotIndex).getSlot_id();
                    if (stage.getTitle().equals("Nachtest")) {
                        Platform.runLater(() -> slot_pruef_label2.setText("Slots werden geprueft."));
                    } else {
                        Platform.runLater(() -> slot_pruef_label.setText("Slots werden geprueft."));
                    }
                    if (tempGerateList.get(slotIndex).getVortest_ergebnis().equals("Kein Ergebnis")) {
                        if (seconds > 0) {
                            if (seconds % 10 == seconds) {
                                if (stage.getTitle().equals("Nachtest") && init_timer_label.isDisabled()) {
                                    Platform.runLater(() -> check_timer_label.setText("00:00:0" + seconds));
                                } else {
                                    Platform.runLater(() -> pruef_time_label.setText("00:00:0" + seconds));
                                }
                            } else {
                                if (stage.getTitle().equals("Nachtest") && init_timer_label.isDisabled()) {
                                    Platform.runLater(() -> check_timer_label.setText("00:00:" + seconds));
                                } else {
                                    Platform.runLater(() -> pruef_time_label.setText("00:00:" + seconds));
                                }
                            }
                            Platform.runLater(() -> seconds--);
                        } else if (seconds == 0) {
                            Platform.runLater(() -> check_timer_label.setText("00:00:00"));
                            Random rand = new Random();
                            for (int i = 0; i < tempGerateList.size(); i++) {
                                int f = rand.nextInt(5);
                                if (f < 0) {
                                    tempGerateList.get(i).setVortest_ergebnis("Vortest nicht bestanden.");
                                } else {
                                    tempGerateList.get(i).setVortest_ergebnis("Vortest bestanden.");
                                }
                            }
                            Platform.runLater(() -> timer.cancel());
                            Platform.runLater(() -> timer.purge());
                            sem.release();
                            burnintest_table1.refresh();
                            slotIndex++;
                            PROCESSING = false;
                            Platform.runLater(() -> slot_pruef_label.setText("Vortest ist beendet."));
                            controlFehlerhafteProdukte();

                        }
                    } else {
                        Platform.runLater(() -> timer.cancel());
                        Platform.runLater(() -> timer.purge());
                        sem.release();
                        slotIndex++;
                        PROCESSING = false;
                        if (slotIndex != tempListSize) {
                            setVortestTimer(interval, slotIndex);
                        } else {
                            Platform.runLater(() -> slot_pruef_label.setText("Vortest ist beendet."));
                            controlFehlerhafteProdukte();
                        }
                    }
                } else {
                    Platform.runLater(() -> timer.cancel());
                    Platform.runLater(() -> timer.purge());
                    sem.release();
                    slotIndex++;
                    PROCESSING = false;
                    setVortestTimer(interval, slotIndex);
                }
            }
        }, 0, 1000);

    }

    public void controlFehlerhafteProdukte() {
        boolean check = true;
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getVortest_ergebnis().equals("Vortest nicht bestanden.")) {
                sorunvar_isClicked();
                check = false;
                break;
            }
        }
        if (check == true) {
            sorunyok_isClicked();
        }
    }

    public void setHaupttestErgebnis() {
        for (int i = 0; i < tempGerateList.size(); i++) {
            Random rand = new Random();
            int f = rand.nextInt(10);
            if (f <= 2) {
                tempGerateList.get(i).setHaupttest_ergebnis("Haupttest nicht bestanden.");
            } else {
                tempGerateList.get(i).setHaupttest_ergebnis("Haupttest bestanden.");
            }
        }
        burnintest_table2.setItems(tempGerateList);
        burnintest_table2.refresh();
    }

    @FXML
    public void entfernenFehlerhafteProdukte() throws InterruptedException {
        int selectedIndex = burnintest_table1.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Gerate a = (Gerate) burnintest_table1.getSelectionModel().getSelectedItem();
            int slot_id = a.getSlot_bauteil_id()[0];
            int bauteil_id = a.getSlot_bauteil_id()[1];
            if (a.getVortest_ergebnis().equals("Vortest nicht bestanden.")) {
                tempGerateList.remove(a);
                gerateList.remove(a);
                entfernen_label1.setText("Slot " + slot_id + " ist aus der Tabelle entfernt.");
            } else {
                fehler_label.setText("Das Produkt ist nicht fehlerhaft.");
            }
        } else {
            entfernen_label1.setText("Kein Slot ausgewaehlt.");
        }
        burnintest_table1.setItems(tempGerateList);

        burnintest_table1.getSelectionModel().clearSelection();
    }

    public void loadBenutzerList() {
        try {
            String st = "SELECT * FROM `benutzer`";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                benutzerList.add(new Benutzer(
                        resultSet.getInt("benutzer_id"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("nachname"),
                        resultSet.getString("rolle")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void loadGerateList() {
        try {
            String st = "SELECT * FROM gerate";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gerateList.add(new Gerate(resultSet.getInt("gerate_id"), resultSet.getString("gerate_name"),
                        resultSet.getString("auftrag"), resultSet.getInt("slot_id"), resultSet.getInt("benutzer_id"),
                        resultSet.getString("vortest_ergebnis"), resultSet.getString("haupttest_ergebnis")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean checkGerateSlot(String slot_id) {
        for (Gerate g : tempGerateList) {
            if (String.valueOf(g.getSlot_id()).equals(slot_id)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkGerateId(int gerate_id) {
        for (Gerate g : gerateList) {
            if (g.getBauteil() == gerate_id) {
                return false;
            }
        }
        return true;
    }

    public void fill_schrankAuswahlComboBox() {

        try {

            String st = "SELECT klimaschrank_id FROM klimaschrank";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                schranken.add("Schrank" + resultSet.getString("klimaschrank_id"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fill_slotsComboBox() {
        try {
            String st = "SELECT slot_id FROM slot";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                slots.add(resultSet.getString("slot_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String logIn() {
        String status = "Success";
        String benutzer_id = txtId.getText();
        String password = txtPassword.getText();
        if (benutzer_id.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Leere Zugangsdaten");
            status = "Error";
        } else {
            String sql = "SELECT * FROM benutzer Where benutzer_id = ? and password = ?";
            try {
                if (con != null) {
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, benutzer_id);
                    preparedStatement.setString(2, password);
                    resultSet = preparedStatement.executeQuery();
                }
                if (resultSet == null || !resultSet.next() || benutzer_id.length() > 11 || password.length() > 20) {
                    if (resultSet != null) {
                        setLblError(Color.TOMATO, "Geben Sie die richtige ID/das richtige Passwort ein");
                        status = "Error";
                    } else {
                        System.out.println("Database not connected. ID and Password not controlled.");
                    }
                } else {
                    setLblError(Color.GREEN, "Anmeldung erfolgreich..Weiterleitung..");
                    for (int i = 0; i < benutzerList.size(); i++) {
                        if (benutzerList.get(i).getId() == Integer.parseInt(benutzer_id)) {
                            aktuelleBenutzer = benutzerList.get(i);
                        }
                    }
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                CabinetMock.serverStart();
                            } catch (Exception ex) {
                                Logger.getLogger(sceneController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }, 0, 600000000);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        return status;
    }
    @FXML
    public void switch_Screen(ActionEvent event, String sceneName, String title) {
        try {
            root = FXMLLoader.load(getClass().getResource(sceneName));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void btnDE(ActionEvent event) {
        loadLang("deu");
        switch_Screen(event, "/Scenes/Login.fxml", "Login");
    }

    @FXML
    void btnEN(ActionEvent event) {
        loadLang("en");
        switch_Screen(event, "/Scenes/Eng.fxml", "Login");
    }

    private void loadLang(String lang) {
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("resources.lang", locale);
        label.setText(bundle.getString("label"));

    }

    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }

    private void clear_Fields() {
        benutzerid_txtfield1.clear();
    }

    private String entfernenBenutzerData() {
        try {
            String st = "DELETE FROM benutzer where benutzer_id=?";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            preparedStatement.setInt(1, Integer.parseInt(benutzerid_txtfield1.getText()));
            preparedStatement.executeUpdate();
            lblStatus1.setTextFill(Color.GREEN);
            lblStatus1.setText("Benutzer erfolgreich entfernt.");
            resetBenutzerList();
            clear_Fields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus1.setTextFill(Color.TOMATO);
            lblStatus1.setText(ex.getMessage());
            return "Exception";
        }

    }

    private void clearFields() {
        benutzerid_txtfield.clear();
        benutzerPassword_txtfield.clear();
        benutzerName_txtfield.clear();
        benutzerNachame_txtfield.clear();
        slot_comboBox3.setValue("Rolle");
    }

    private String hinzufuegenBenutzerData() {

        try {
            String st = "INSERT INTO benutzer (benutzer_id, password, name, nachname, rolle) VALUES (?,?,?,?,?)";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            preparedStatement.setString(1, benutzerid_txtfield.getText());
            preparedStatement.setString(2, benutzerPassword_txtfield.getText());
            preparedStatement.setString(3, benutzerName_txtfield.getText());
            preparedStatement.setString(4, benutzerNachame_txtfield.getText());
            preparedStatement.setString(5, slot_comboBox3.getSelectionModel().getSelectedItem());
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Benutzer erfolgreich hinzugefugt");
            loadBenutzerList();
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    private void clearSchrank_Fields() {
        schrankid_txtfield.clear();
    }

    private String entfernenSchrankData() {
        try {
            String st = "DELETE FROM klimaschrank where klimaschrank_id=?";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            preparedStatement.setString(1, schrankid_txtfield.getText());
            preparedStatement.executeUpdate();
            lblStatusSchrank.setTextFill(Color.GREEN);
            lblStatusSchrank.setText("Schrank erfolgreich entfernen");
            clearSchrank_Fields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatusSchrank.setTextFill(Color.TOMATO);
            lblStatusSchrank.setText(ex.getMessage());
            return "Exception";
        }

    }

    private String hinzufuegenSchrankData() {

        try {
            String st = "INSERT INTO klimaschrank (klimaschrank_id,benutzer_id) VALUES (?,?)";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            preparedStatement.setString(1, schrankid_txtfield1.getText());
            preparedStatement.setString(2, schrank_benutzerid.getText());
            preparedStatement.executeUpdate();
            lblStatusSchrank1.setTextFill(Color.GREEN);
            lblStatusSchrank1.setText("Schrank erfolgreich hinzugefugt");
            clearSchrank1_Fields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            lblStatusSchrank1.setTextFill(Color.TOMATO);
            lblStatusSchrank1.setText(ex.getMessage());
            return "Exception";
        }
    }

    private void clearSchrank1_Fields() {
        schrankid_txtfield1.clear();
        schrank_benutzerid.clear();
    }

    private void hinzufuegenGerateData(Gerate g) {

        try {
            String st = "INSERT INTO gerate (gerate_id, gerate_name,auftrag, slot_id, benutzer_id, vortest_ergebnis, haupttest_ergebnis) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) con.prepareStatement(st);
            preparedStatement.setInt(1, g.getBauteil());
            preparedStatement.setString(2, g.getName());
            preparedStatement.setString(3, g.getAuftrag());
            preparedStatement.setInt(4, g.getSlot_id());
            preparedStatement.setInt(5, aktuelleBenutzer.getId());
            preparedStatement.setString(6, g.getVortest_ergebnis());
            preparedStatement.setString(7, g.getHaupttest_ergebnis());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void connectColumnToGerate() {
        slot_column.setCellValueFactory(new PropertyValueFactory<>("slot_id"));
        bauteil_column.setCellValueFactory(new PropertyValueFactory<>("bauteil"));
        auftrag_column.setCellValueFactory(new PropertyValueFactory<>("auftrag"));
        ergebnis_column.setCellValueFactory(new PropertyValueFactory<>("vortest_ergebnis"));
        slot_column1.setCellValueFactory(new PropertyValueFactory<>("slot_id"));
        bauteil_column1.setCellValueFactory(new PropertyValueFactory<>("bauteil"));
        auftrag_column1.setCellValueFactory(new PropertyValueFactory<>("auftrag"));
        ergebnis_column1.setCellValueFactory(new PropertyValueFactory<>("vortest_ergebnis"));
        slot_column2.setCellValueFactory(new PropertyValueFactory<>("slot_id"));
        bauteil_column2.setCellValueFactory(new PropertyValueFactory<>("bauteil"));
        auftrag_column2.setCellValueFactory(new PropertyValueFactory<>("auftrag"));
        ergebnis_column2.setCellValueFactory(new PropertyValueFactory<>("haupttest_ergebnis"));
        slot_column1.setSortable(false);
        bauteil_column1.setSortable(false);
        auftrag_column1.setSortable(false);
        ergebnis_column1.setSortable(false);
    }

    @FXML
    void hinzufuegen_buttonClicked(ActionEvent event) {
        connectColumnToGerate();
        ComboBox<String> x = new ComboBox<>();
        TextField bauteil = new TextField();
        TextField auftrag = new TextField();
        Label textLabel = new Label();
        if (stage.getTitle().equals("Nachtest")) {
            x = slot_comboBox2;
            bauteil = bauteil_textfield2;
            auftrag = auftrag_textfield2;
            textLabel = hinzufuegen_label2;
        } else {
            x = slot_comboBox;
            bauteil = bauteil_textfield;
            auftrag = auftrag_textfield;
            textLabel = hinzufuegen_label;
        }
        String bauteilField = bauteil.getText();
        String auftragField = auftrag.getText().replaceAll("\\s", "");
        if (x.getSelectionModel().getSelectedItem().equals("Slots") || bauteilField.isEmpty() || auftragField.isEmpty()) {
            textLabel.setTextFill(Color.TOMATO);
            textLabel.setText("Geben Sie alle Details ein");
        } else if (bauteilField.length() > 10 || auftragField.length() > 20) {
            textLabel.setTextFill(Color.TOMATO);
            textLabel.setText("Eingabeparameter zu lang! Bitte korrigieren.");
        } else if (!bauteilField.matches("[0-9]+")) {
            textLabel.setTextFill(Color.TOMATO);
            textLabel.setText("Bauteil-ID kann nur aus Zahlen bestehen!");
        } else if (!checkGerateId(Integer.parseInt(bauteilField))) {
            textLabel.setTextFill(Color.TOMATO);
            textLabel.setText("Geraet-ID existiert. Kontrollieren Sie bitte das Geraet-Id.");
        } else if (!checkGerateSlot(x.getSelectionModel().getSelectedItem())) {
            textLabel.setTextFill(Color.TOMATO);
            textLabel.setText("Dieses Slot ist besetzt waehlen Sie bitte ein anderes Slot.");
        } else {
            textLabel.setText("");
            Gerate gerate = new Gerate();
            gerate.setSlot_id(Integer.parseInt(x.getSelectionModel().getSelectedItem()));
            gerate.setBauteil(Integer.parseInt(bauteilField));
            gerate.setAuftrag(auftragField);
            textLabel.setTextFill(Color.GREEN);
            textLabel.setText("Geraet erfolgreich hinzugefügt");
            bauteil.clear();
            auftrag.clear();
            gerateList.add(gerate);
            tempGerateList.add(gerate);
            CabinKontrol.addMsg(x.getSelectionModel().getSelectedItem(), bauteilField);
            if (stage.getTitle().equals("Testprodukt Check-In")) {
                burnintest_table.getItems().add(gerate);
                burnintest_table.getSortOrder().add(slot_column);
                burnintest_table.sort();
            }
            if (stage.getTitle().equals("Nachtest")) {
                burnintest_table1.getSortOrder().add(slot_column);
                burnintest_table1.sort();
                burnintest_table1.setItems(tempGerateList);
                burnintest_table1.refresh();
                Collections.sort(tempGerateList, new sortGerate());
            }
        }
    }

    @FXML
    void entfernen_gerateData(ActionEvent event) throws InterruptedException {
        int selectedIndex = burnintest_table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Gerate a = (Gerate) burnintest_table.getSelectionModel().getSelectedItem();
            int slot_id = a.getSlot_bauteil_id()[0];
            int bauteil_id = a.getSlot_bauteil_id()[1];
            tempGerateList.remove(a);
            gerateList.remove(a);
            entfernen_label.setText("Slot " + slot_id + " ist aus der Tabelle entfernt.");
            burnintest_table.getItems().remove(selectedIndex);
            CabinKontrol.deleteMsg(Integer.toString(slot_id));
        } else {
            entfernen_label.setText("Kein Slot ausgewaehlt.");
        }
        burnintest_table.getSelectionModel().clearSelection();
    }

    @FXML
    void benutzerhinzufuegen_buttonClicked(ActionEvent event) {
        loadBenutzerList();
        String id = benutzerid_txtfield.getText();
        String password = benutzerPassword_txtfield.getText().replaceAll("\\s", "");
        String benutzerName = benutzerName_txtfield.getText().replaceAll("\\s", "");
        String benutzerNName = benutzerNachame_txtfield.getText().replaceAll("\\s", "");
        if (id.isEmpty() || password.isEmpty() || benutzerName.isEmpty() || benutzerNName.isEmpty() || slot_comboBox3.getSelectionModel().getSelectedItem().equals("Rolle")) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Geben Sie alle Details ein");
        } else if (!id.matches("[0-9]+")) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Benutzer-ID kann nur aus Zahlen bestehen!");
        } else if (id.length() > 10 || password.length() > 15 || benutzerName.length() > 20 || benutzerNName.length() > 20) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Eingabeparameter zu lang! Bitte korrigieren.");
        } else if (checkBenutzerId()) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Benutzer-ID existiert!");
        } else {
            hinzufuegenBenutzerData();
        }
    }

    public boolean checkBenutzerId() {
        for (int i = 0; i < benutzerList.size(); i++) {
            if (Integer.parseInt(benutzerid_txtfield.getText()) == benutzerList.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean resetBenutzerList() {
        for (int i = 0; i < benutzerList.size(); i++) {
            if (Integer.parseInt(benutzerid_txtfield1.getText()) == benutzerList.get(i).getId()) {
                benutzerList.remove(benutzerList.get(i));
                return true;
            }
        }
        return false;
    }

    @FXML
    void benutzerentfernen_buttonClicked(ActionEvent event) {
        String input = benutzerid_txtfield1.getText();
        if (input.isEmpty()) {
            lblStatus1.setTextFill(Color.TOMATO);
            lblStatus1.setText("Geben Sie alle Details ein");
        } else if (!input.matches("[0-9]+") || input.length() > 11) {
            lblStatus1.setTextFill(Color.TOMATO);
            lblStatus1.setText("Keine gultige Nummer!");
            benutzerid_txtfield1.setText("");
        } else {
            if (resetBenutzerList()) {
                entfernenBenutzerData();
            } else {
                lblStatus1.setTextFill(Color.TOMATO);
                benutzerid_txtfield1.setText("");
                lblStatus1.setText("Kein Benutzer gefunden.");

            }
        }
    }

    @FXML
    void benutzerstellung_is_Clicked(ActionEvent event) {
        switchScreen(event, settingsPane, benutzerSettingsPane, "Benutzer-Einstellungen");
    }

    @FXML
    void schrankstellung_is_Clicked(ActionEvent event) {
        switchScreen(event, settingsPane, schrankSettingsPane, "Schrank-Einstellungen");
    }

    @FXML
    void schrankentfernen_buttonClicked(ActionEvent event) {
        if (schrankid_txtfield.getText().isEmpty()) {
            lblStatusSchrank.setTextFill(Color.TOMATO);
            lblStatusSchrank.setText("Geben Sie alle Details ein");
        } else {
            entfernenSchrankData();
        }
    }

    @FXML
    void schrankhinzufuegen_buttonClicked(ActionEvent event) {
        if (schrankid_txtfield1.getText().isEmpty() || schrank_benutzerid.getText().isEmpty()) {
            lblStatusSchrank1.setTextFill(Color.TOMATO);
            lblStatusSchrank1.setText("Geben Sie alle Details ein");
        } else {
            hinzufuegenSchrankData();
        }
    }

    @FXML
    void menuzuruckgehen_isClicked(ActionEvent event) {
        switchScreen(event, benutzerSettingsPane, menuAdminPane, "Einstellungen");
        switchScreen(event, schrankSettingsPane, menuAdminPane, "Einstellungen");
        lblStatus.setText("");
        lblStatus1.setText("");
        lblStatusSchrank.setText("");
        lblStatusSchrank1.setText("");
        benutzerid_txtfield.setText("");
        benutzerPassword_txtfield.setText("");
        benutzerName_txtfield.setText("");
        benutzerNachame_txtfield.setText("");
        slot_comboBox3.setValue("Rolle");
        benutzerid_txtfield1.setText("");
    }

    @FXML
    void settings_button_isClicked(ActionEvent event) {
        switchScreen(event, menuAdminPane, settingsPane, "Einstellungen");
    }

    @FXML
    void login_button_isClicked(ActionEvent event) {
        if (logIn().equals("Success")) {
            switchScreen(event, loginPane, schrankPane, "Schrankauswahl");
        }
    }

    @FXML
    void confirm_button_isClicked(ActionEvent event) {
        if (aktuelleBenutzer.getRolle().equals("Admin")) {
            switchScreen(event, schrankPane, menuAdminPane, "Menu");
        } else {
            switchScreen(event, schrankPane, menuBenutzerPane, "Menu");
        }
    }

    @FXML
    void testbetrieb_button_isClicked(ActionEvent event) {
        switchScreen(event, menuAdminPane, initialisierungStartPane, "Initialisierung Starten");
        CabinKontrol.addUser();
        init_weiter_Button.setDisable(true);
    }

    @FXML
    void handbetrieb_button_isClicked(ActionEvent event) {

    }

    @FXML
    void exit_button_isClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void abbrechen_button_isClicked(ActionEvent event) {
        if (aktuelleBenutzer.getRolle().equals("Admin")) {
            CabinKontrol.messages.clear();
            if (stage.getTitle().equals("Initialisierung Starten")) {
                switchScreen(event, initialisierungStartPane, menuAdminPane, "Menu");
            } else if (stage.getTitle().equals("Initialisierung")) {
                switchScreen(event, initialisierungPane, menuAdminPane, "Menu");
                if (PROCESSING) {
                    Platform.runLater(() -> FLAG = 1);
                } else {
                    Platform.runLater(() -> FLAG = 0);
                }
            } else if (stage.getTitle().equals("Testprodukt Check-In Starten")) {
                switchScreen(event, weiterZumTestProduktCheckInPane, menuAdminPane, "Menu");
            } else if (stage.getTitle().equals("Testprodukt Check-In")) {
                switchScreen(event, testProduktCheckInPane, menuAdminPane, "Menu");
                clearTestProduktCheckIn();
            } else if (fehler_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuAdminPane, "Menu");
                clearFehlerPane();
                clearTestProduktCheckIn();
            } else if (bevorHaupttest_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuAdminPane, "Menu");
                bevorHaupttest_pane.setVisible(false);
                clearTestProduktCheckIn();
            } else if (bevorNachtest_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuAdminPane, "Menu");
                clearNachtestCheckIn();
            } else if (haupttestPane.isVisible() == true) {
                switchScreen(event, haupttestPane, menuAdminPane, "Menu");
                clearNachtestCheckIn();
                resetHaupttestPane();
            }
        } else {
            CabinKontrol.messages.clear();
            if (stage.getTitle().equals("Initialisierung Starten")) {
                switchScreen(event, initialisierungStartPane, menuBenutzerPane, "Menu");
            } else if (stage.getTitle().equals("Initialisierung")) {
                switchScreen(event, initialisierungPane, menuBenutzerPane, "Menu");
                if (PROCESSING) {
                    Platform.runLater(() -> FLAG = 1);
                } else {
                    Platform.runLater(() -> FLAG = 0);
                }
            } else if (stage.getTitle().equals("Testprodukt Check-In Starten")) {
                switchScreen(event, weiterZumTestProduktCheckInPane, menuBenutzerPane, "Menu");
            } else if (stage.getTitle().equals("Testprodukt Check-In")) {
                switchScreen(event, testProduktCheckInPane, menuBenutzerPane, "Menu");
                clearTestProduktCheckIn();
            } else if (fehler_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuBenutzerPane, "Menu");
                clearFehlerPane();
                clearTestProduktCheckIn();
            } else if (bevorHaupttest_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuBenutzerPane, "Menu");
                bevorHaupttest_pane.setVisible(false);
                clearTestProduktCheckIn();
            } else if (bevorNachtest_pane.isVisible() == true) {
                switchScreen(event, vortestPane, menuBenutzerPane, "Menu");
                clearNachtestCheckIn();
            } else if (haupttestPane.isVisible() == true) {
                switchScreen(event, haupttestPane, menuBenutzerPane, "Menu");
                clearNachtestCheckIn();
                resetHaupttestPane();
            }
        }
    }

    public void pause_isClicked() {
        if (X == 1 && !PROCESSING) {
            setTimer(10, PAUSEDTIME + 1);
            progress(pausedProgress + 0.1);
            pause_button.setDisable(true);
            pause_button.setText("Pausieren");
            pause_button.setDisable(false);
            X = 0;
        } else if (PROCESSING) {
            Platform.runLater(() -> FLAG = 1);
            Platform.runLater(() -> FLAG2 = 1);
            X = 1;
            pause_button.setDisable(true);
            pause_button.setText("Test weiterfuhren");
            PAUSED = true;
            pause_button.setDisable(false);
        }
    }

    public void resetHaupttestPane() {
        Platform.runLater(() -> FLAG = 1);
        Platform.runLater(() -> FLAG2 = 1);
        PAUSEDTIME = 0;
        pausedProgress = 0;
        haupttest_label.setText("Haupttest Laeuft...");
        pause_button.setText("Pausieren");
        PROCESSING = false;
    }

    public void clearTestProduktCheckIn() {
        slot_comboBox.setValue("Slots");
        bauteil_textfield.setText("");
        auftrag_textfield.setText("");
        hinzufuegen_label.setText("");
        entfernen_label.setText("");
        achtung_label.setText("");
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (gerateList.contains(tempGerateList.get(i))) {
                gerateList.remove(tempGerateList.get(i));
            }
        }
        burnintest_table.getItems().clear();
        burnintest_table1.getItems().clear();
        burnintest_table2.getItems().clear();
        tempGerateList.clear();
    }

    public void clearNachtestCheckIn() {
        slot_comboBox2.setValue("Slots");
        bauteil_textfield2.setText("");
        auftrag_textfield2.setText("");
        hinzufuegen_label2.setText("");
        bevorNachtest_pane.setVisible(false);
        clearTestProduktCheckIn();
    }

    public void clearFehlerPane() {
        fehler_label.setText("Fehlerhafte Produkte erkannt!");
        entfernen_label1.setText("");
        fehler_pane.setVisible(false);
    }

    @FXML
    void initialisierungStart_button_isClicked(ActionEvent event) throws Exception {
        switchScreen(event, initialisierungStartPane, initialisierungPane, "Initialisierung");
        Platform.runLater(() -> FLAG = 0);
        Platform.runLater(() -> FLAG2 = 0);
        setTimer(5, 0);
    }

    @FXML
    void weiterZumTestProduktCheckIn_button_isClicked(ActionEvent event) {
        switchScreen(event, initialisierungPane, weiterZumTestProduktCheckInPane, "Testprodukt Check-In Starten");
    }

    @FXML
    void testProduktCheckStart_button_isClicked(ActionEvent event) {
        switchScreen(event, weiterZumTestProduktCheckInPane, testProduktCheckInPane, "Testprodukt Check-In");
    }

    @FXML
    public void fertig_button_isClicked(ActionEvent event) {
        if (tempGerateList.isEmpty()) {
            achtung_label.setTextFill(Color.TOMATO);
            achtung_label.setText("Kein Produkt vorhanden!!!");
        } else {
            produkt_pane1.setVisible(false);
            erfolgreich_pane1.setVisible(true);
            fertig_pane1.setVisible(false);
            funktest_pane1.setVisible(true);
            hinzufuegen_label.setText("");
            achtung_label.setText("");
            entfernen_label.setText("");
            Collections.sort(tempGerateList, new sortGerate());
            CabinKontrol.endInit();
        }
    }

    @FXML
    public void funktest_button_isClicked(ActionEvent event) {
        switchScreen(event, testProduktCheckInPane, vortestPane, "Vortest");
        produkt_pane1.setVisible(true);
        erfolgreich_pane1.setVisible(false);
        fertig_pane1.setVisible(true);
        funktest_pane1.setVisible(false);
        setVortestTimer(10, -1);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int x = 0;

            @Override
            public void run() {
                try {
                    CabinKontrol.clientStart();
                    x++;
                } catch (Exception ex) {
                    Logger.getLogger(sceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 600000000);
    }

    @FXML
    public void sorunvar_isClicked() {
        fehler_pane.setVisible(true);
        abbrechen_button.setVisible(true);
        sorunvar.setVisible(false);
        sorunyok.setVisible(false);
        nachtest_pane.setVisible(false);
    }

    @FXML
    public void sorunyok_isClicked() {
        bevorHaupttest_pane.setVisible(true);
        abbrechen_button.setVisible(true);
        sorunvar.setVisible(false);
        sorunyok.setVisible(false);

        nachtest_pane.setVisible(false);
    }

    @FXML
    public void kontroll_button_isClicked(ActionEvent event) {
        boolean check = true;
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getVortest_ergebnis().equals("Vortest nicht bestanden.")) {
                fehler_label.setText("Es existieren noch fehlerhafte Produkte!!!");
                check = false;
                break;
            }
        }
        if (check == true) {
            fehler_pane.setVisible(false);
            bevorNachtest_pane.setVisible(true);
            stage.setTitle("Nachtest");
            fehler_label.setText("Fehlerhafte Produkte erkannt!");
            entfernen_label1.setText("");
            hinzufuegen_label2.setText("");
        }
        nachtestinit_label.setDisable(false);
        init_timer_label.setDisable(false);
        nachtestcheck_label.setDisable(true);
        check_timer_label.setDisable(true);
        slot_pruef_label.setText("Slot x wird geprueft.");
        slot_pruef_label2.setText("");
    }

    @FXML
    public void nachtest_button_isClicked(ActionEvent event) {
        boolean check = true;
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getVortest_ergebnis().equals("Kein Ergebnis")) {
                check = false;
                break;
            }
        }
        if (tempGerateList.isEmpty()) {
            hinzufuegen_label2.setTextFill(Color.TOMATO);
            hinzufuegen_label2.setText("Kein Produkt vorhanden!!!");
        } else {
            nachtestcheck_label.setDisable(true);
            check_timer_label.setDisable(true);
            bevorNachtest_pane.setVisible(false);
            if (!check) {
                nachtest_pane.setVisible(true);

                abbrechen_button.setVisible(false);
                setTimer(7, 0);
                setVortestTimer(3, -1);
            } else {

                bevorHaupttest_pane.setVisible(true);
            }
            hinzufuegen_label2.setText("");
            slot_comboBox2.setValue("Slots");
        }

    }

    @FXML
    public void haupttestStartButton_isClicked(ActionEvent event) {
        switchScreen(event, vortestPane, haupttestPane, "Haupttest");
        bevorHaupttest_pane.setVisible(false);
        bevorNachtest_pane.setVisible(false);
        fehler_pane.setVisible(false);
        pruef_pane.setVisible(true);
        geciciweiter_button.setDisable(true);
        if (aktuelleBenutzer.getRolle().equals("standard")) {
            pause_button.setDisable(true);
            abbrechenTest_button.setDisable(true);
        }
        CabinKontrol.setControl();
        setTimer(60, 0);
        progress(0);
    }

    @FXML
    public void geciciWeiterButton_isClicked(ActionEvent event) {
        switchScreen(event, haupttestPane, funktionCheckPane, "Funktions Check Scene");
        geciciweiter_button.setDisable(true);
        boolean check = true;
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getHaupttest_ergebnis().equals("Haupttest nicht bestanden.")) {
                check = false;
                alleProdukteNichtOK_pane.setVisible(true);
                break;
            }
        }
        if (check == true) {
            alleProdukteOK_pane.setVisible(true);
        }

        for (int i = 0; i < tempGerateList.size(); i++) {
            hinzufuegenGerateData(tempGerateList.get(i));
        }
        pause_button.setText("Pausieren");
        PAUSEDTIME = 0;
        pausedProgress = 0;
    }

    @FXML
    public void nichtinOrdnung_button_isClicked(ActionEvent event) {
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getHaupttest_ergebnis().equals("Haupttest nicht bestanden.")) {
                fehlerhafteGerateList.add(tempGerateList.get(i));
            }
        }
        testNichtInOrdnung_button.setVisible(false);
        testNichtInOrdnung_label.setText("Nicht in Ordnung Produkte gespeichert!");
    }

    @FXML
    public void inOrdnung_button_isClicked(ActionEvent event) {
        for (int i = 0; i < tempGerateList.size(); i++) {
            if (tempGerateList.get(i).getHaupttest_ergebnis().equals("Haupttest bestanden.")) {
                korrektGerateList.add(tempGerateList.get(i));
            }
        }
        testNichtInOrdnung_label.setText("Manche Produkte haben den Haupttest nicht bestanden!");
        alleProdukteNichtOK_pane.setVisible(false);
        testNichtInOrdnung_button.setVisible(true);
        ende_pane.setVisible(true);
    }

    @FXML
    void testInOrdnung_isClicked(ActionEvent event) {
        for (int i = 0; i < tempGerateList.size(); i++) {
            korrektGerateList.add(tempGerateList.get(i));
        }
        ende_pane.setVisible(true);
    }

    @FXML
    void testNichtInOrdnung_isClicked(ActionEvent event) {
        switchScreen(event, funktionCheckPane, funktionCheckPane2, "Funktions Ordnung Scene");
    }

    @FXML
    void testInOrdnung2_isClicked(ActionEvent event) {
        switchScreen(event, funktionCheckPane2, endePane, "Funktions Ordnung Scene");
    }

    @FXML
    void endeClicked(ActionEvent event) {
        CabinKontrol.messages.clear();
        switchScreen(event, funktionCheckPane, menuAdminPane, "Menu");
        slot_comboBox.setValue("Slots");
        slot_comboBox2.setValue("Slots");
        ende_pane.setVisible(false);
        burnintest_table.getItems().clear();
        burnintest_table1.getItems().clear();
        burnintest_table2.getItems().clear();
        tempGerateList.clear();
        fehlerhafteGerateList.clear();
        korrektGerateList.clear();
        resetHaupttestPane();
    }
}

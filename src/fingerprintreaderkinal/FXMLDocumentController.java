/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintreaderkinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusListener;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import DataBase.Conexion;
import javafx.application.Platform;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.InnerShadow;

/**
 *
 * @author Broadcast
 */
public class FXMLDocumentController implements Initializable {
    
    private final DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();

    private final DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();

    private DPFPTemplate template;

    /**
     * Get the value of template
     *
     * @return the value of template
     */
    public DPFPTemplate getTemplate() {
        return template;
    }

    /**
     * Set the value of template
     *
     * @param template new value of template
     */
    public void setTemplate(DPFPTemplate template) {
        this.template = template;
    }
 
    
    public DPFPFeatureSet featuresverificacion;
    
    public DPFPFeatureSet featuresinscripcion;
    
    @FXML
    private Label labelHora;
        
    @FXML
    private Label labelNombre;
        
    @FXML
    private Label labelAcceso;
     
    final DateFormat format = DateFormat.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline.setCycleCount(Animation.INDEFINITE);  
        timeline.play();  
        iniciar();
        start();
    }    
  
    final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        labelHora.setText(simpleDateFormat.format(cal.getTime()));
        labelHora.setFont(Font.font("Segoe UI", 96));
        labelHora.setEffect( new Glow(1.0));
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(254, 235, 66, 0.3));
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);
        blend.setBottomInput(ds);
        DropShadow ds1 = new DropShadow();
        ds1.setColor(Color.web("#f13a00"));
        ds1.setRadius(20);
        ds1.setSpread(0.2);
        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);
        InnerShadow is = new InnerShadow();
        is.setColor(Color.web("#feeb42"));
        is.setRadius(9);
        is.setChoke(0.8);
        blend2.setBottomInput(is);
        InnerShadow is1 = new InnerShadow();
        is1.setColor(Color.web("#f13a00"));
        is1.setRadius(5);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);
        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.HARD_LIGHT);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);
        labelHora.setEffect(blend);
        labelAcceso.setText("Acceso Concedido");
        labelNombre.setText("Pablo Franco");
       // labelNombre.setEffect(new Glow (1));
        //labelNombre.setEffect(new InnerShadow (10,21,21,Color.web("#da1212")));
        
    }));
    
  protected void iniciar() {

        Lector.addDataListener(new DPFPDataAdapter() {
            @Override
            public void dataAcquired(final DPFPDataEvent e) {
                System.out.println("La huella Digital ha sido Capturada ");
                try {
                    ProcesarCaptura(e.getSample());
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Lector.addReaderStatusListener(new DPFPReaderStatusListener() {
            @Override
            public void readerConnected(DPFPReaderStatusEvent arg0) {
                System.out.println("El Sensor esta Conectado");
            }

            @Override
            public void readerDisconnected(DPFPReaderStatusEvent arg0) {
                System.out.println("El Sensor no esta Conectado");
            }
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                System.out.println("El dedo ha sido colocado sobre el Lector de Huellas");
            }

            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                System.out.println("El dedo se ha quitado del lector!");
            }
        });

        Lector.addErrorListener(new DPFPErrorAdapter() {
            public void errorReader(final DPFPErrorEvent e) {
                System.out.println("Error: " + e.getError());
            }
        });
    }
  
   public void ProcesarCaptura(DPFPSample sample) throws IOException {
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        if (featuresverificacion != null) {
                        identificarHuella();
                        }
    }
          
      public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }
    public void start() {
        Lector.startCapture();
        System.out.println("Lector en uso");
    }

    public void stop() {
        Lector.stopCapture();
        System.out.println("Lector detenido");
    }

    public void identificarHuella() throws IOException {
        try {
            Connection con = Conexion.getInstancia().Conectarse();
            PreparedStatement identificarStmt = con.prepareStatement("SELECT huenombre,huehuella FROM huellas");
            ResultSet rs = identificarStmt.executeQuery();
            Verificador.setFARRequested(DPFPVerification.PROBABILITY_ONE);
            while (rs.next()) {
                byte templateBuffer[] = rs.getBytes("huehuella");
                String Nombre = rs.getString("huenombre");
                DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                setTemplate(referenceTemplate);
                DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
                if (result.isVerified()) {
                 //    System.out.println("La huella capturada es de :" + Nombre);
                   Platform.runLater(() -> actualizarNombre(Nombre));            
                     return;
                  //  break;
                }
            }
            System.out.println("No existen registros de esa huella!");
            setTemplate(null);
        } catch (SQLException ex) {
            System.err.println("Error identificando la huella dactilar" + ex.getMessage());
        } finally {
            Conexion.getInstancia().desconectar();
        }
//throw new IllegalStateException("Error de estado ilegal...");
    }

    private void actualizarNombre(String no) {
     labelNombre.setText(no);
    }
}

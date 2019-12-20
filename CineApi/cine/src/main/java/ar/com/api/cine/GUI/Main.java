package ar.com.api.cine.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import javax.swing.UIManager;

/**
 *
 * @author Luis Marcos
 */
public class Main {

   
    public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException{
        try {
//            Desktop.getDesktop().browse(new URI("https://maps.google.es/maps?q=Espa%C3%B1a&output=embed"));
        } catch (Exception ex) {
        }
        for(UIManager.LookAndFeelInfo laf:UIManager.getInstalledLookAndFeels()){
            if("Nimbus".equals(laf.getName()))
                try {
                UIManager.setLookAndFeel(laf.getClassName());
            } catch (Exception ex) {
            }
        }
        MainFrame mainF=new MainFrame();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        mainF.setLocation(d.width-(mainF.getWidth()+50), 50);
        mainF.setSize(550, 630);
        mainF.setVisible(true);
       
    }
}

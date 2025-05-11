/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public JLabel loadingText;

    public SplashScreen() {
        JLabel logo = new JLabel(new ImageIcon("/home/fenohery/NetBeansProjects/newAppVenteVoiture/src/main/java/utils/mercedes.jpg"));
        logo.setHorizontalAlignment(JLabel.CENTER);

        getContentPane().add(logo, BorderLayout.CENTER);
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    public void showSplashAndLaunch(Runnable onFinish) {
        setVisible(true);
        
         // Animation du texte "Chargement..."
        Timer animationTimer = new Timer(500, null);
        animationTimer.addActionListener(e -> {
            String text = loadingText.getText();
            if (text.endsWith("...")) {
                loadingText.setText("Chargement");
            } else {
                loadingText.setText(text + ".");
            }
        });
        animationTimer.start();
        
        Timer timer = new Timer(3000, e -> {
            animationTimer.stop();
            dispose();
            onFinish.run(); // Lancer l'interface principale
        });
        timer.setRepeats(false); // important
        timer.start();
    }
}

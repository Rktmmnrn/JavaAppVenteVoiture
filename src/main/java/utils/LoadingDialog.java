/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.*;
import java.awt.*;

public class LoadingDialog extends JDialog {
    private JLabel label;

    public LoadingDialog(Frame parent, String message) {
        super(parent, "Chargement", true);
        label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        getContentPane().add(label);
        setSize(200, 100);
        setLocationRelativeTo(parent);
    }

    public void showWithDelay(Runnable backgroundTask) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                backgroundTask.run();
                return null;
            }

            @Override
            protected void done() {
                dispose();
            }
        };
        worker.execute();
        setVisible(true);
    }

    // Pour afficher le loading dialog de façon bloquante
    public void showLoading() {
        setVisible(true);
    }

    // Pour le fermer après le traitement
    public void hideLoading() {
        setVisible(false);
        dispose();
    }
}

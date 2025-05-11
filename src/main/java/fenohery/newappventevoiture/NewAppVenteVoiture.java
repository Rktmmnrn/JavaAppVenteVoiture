/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package fenohery.newappventevoiture;

import ui.DashboardUI;
import utils.SplashScreen;

public class NewAppVenteVoiture {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(()-> {
            SplashScreen splash = new SplashScreen();
            splash.showSplashAndLaunch(() -> {
                new DashboardUI();
            });
        });
    }
}
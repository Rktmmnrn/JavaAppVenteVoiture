/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package fenohery.newappventevoiture;

import ui.ClientUI;
import ui.VoitureUI;

public class NewAppVenteVoiture {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(()-> {
        new ClientUI().setVisible(true);
        // new VoitureUI().setVisible(true);
    });
}
}
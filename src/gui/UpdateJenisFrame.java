package gui;

import dao.KendaraanDAO;

import javax.swing.*;
import java.awt.*;

public class UpdateJenisFrame extends JFrame {

    public UpdateJenisFrame() {

        setTitle("Update Jenis Kendaraan");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextField txtPlat = new JTextField();
        JRadioButton rbMotor = new JRadioButton("Motor");
        JRadioButton rbMobil = new JRadioButton("Mobil");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMotor);
        bg.add(rbMobil);

        JButton btnUpdate = new JButton("Update");

        setLayout(new GridLayout(5, 1, 10, 10));
        add(new JLabel("No Plat"));
        add(txtPlat);
        add(rbMotor);
        add(rbMobil);
        add(btnUpdate);

        btnUpdate.addActionListener(e -> {
            try {
                String jenis = rbMotor.isSelected() ? "Motor" : "Mobil";
                new KendaraanDAO()
                        .updateJenis(txtPlat.getText().trim().toUpperCase(), jenis);

                JOptionPane.showMessageDialog(this, "Berhasil diupdate");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // ENTER = UPDATE
        getRootPane().setDefaultButton(btnUpdate);

        // ESC = CLOSE
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
}

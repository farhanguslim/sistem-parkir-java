package gui;

import dao.RekapPendapatanDAO;

import javax.swing.*;
import java.awt.*;

public class ChartPendapatanFrame extends JFrame {

    private JLabel lblTotal;

    public ChartPendapatanFrame() {
        setTitle("Chart Pendapatan Hari Ini");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Pendapatan Hari Ini", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        lblTotal = new JLabel("Rp 0", SwingConstants.CENTER);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(lblTotal, BorderLayout.CENTER);
    }

    private void loadData() {
        RekapPendapatanDAO dao = new RekapPendapatanDAO();
        int total = dao.getPendapatanHariIni();

        lblTotal.setText("Rp " + total);
    }
}

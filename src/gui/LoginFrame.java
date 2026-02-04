package gui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {

        setTitle("Login Sistem Parkir");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Username"));
        txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("Password"));
        txtPass = new JPasswordField();
        add(txtPass);

        JButton btnLogin = new JButton("Login");
        add(new JLabel());
        add(btnLogin);

        // ===== ACTION =====
        btnLogin.addActionListener(e -> doLogin());

        // 🔥 ENTER LANGSUNG LOGIN
        getRootPane().setDefaultButton(btnLogin);
    }

    private void doLogin() {

        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username dan Password wajib diisi",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User loggedIn = userDAO.login(username, password);

        if (loggedIn != null) {
            JOptionPane.showMessageDialog(this,
                    "Login berhasil sebagai " + loggedIn.getRole());

            dispose();
            new MenuFrame(loggedIn).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Username atau Password salah",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}

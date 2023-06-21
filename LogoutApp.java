import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogoutApp {
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogoutApp window = new LogoutApp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LogoutApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(100, 80, 100, 30);
        frame.getContentPane().add(btnLogout);

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Buat URL untuk endpoint logout
                    URL url = new URL("http://localhost:3500/logout");

                    // Buka koneksi ke URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set metode request ke GET
                    connection.setRequestMethod("GET");

                    // Dapatkan kode respons
                    int responseCode = connection.getResponseCode();

                    // Baca respons dari server
                    BufferedReader reader;
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }

                    // Baca respons sebagai string
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Tampilkan respons dari server
                    JOptionPane.showMessageDialog(frame, response.toString(), "Logout",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Tutup koneksi
                    connection.disconnect();

                    // Tutup frame setelah logout berhasil
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Terjadi kesalahan. Silakan coba lagi.", "Logout",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

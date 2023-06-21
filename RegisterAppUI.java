import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.*;

public class RegisterAppUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterAppUI::createAndShowUI);
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("User Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Username input
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        // Name input
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(10);
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // Password input
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(10);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        panel.add(formPanel, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Sign Up");
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String name = nameField.getText();
            String password = new String(passwordField.getPassword());

            // Call the registration method with the entered values
            boolean registrationSuccess = registerCustomer(username, name, password);

            if (registrationSuccess) {
                JOptionPane.showMessageDialog(panel, "Registrasi Berhasil!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(panel, "Registrasi Gagal - Data Sudah Ada", "Gagal;",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the login method to access LoginAppUI.java
                login();
            }
        });
        buttonPanel.add(loginButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean registerCustomer(String username, String name, String password) {
        try {
            // Set the URL of the endpoint
            URL url = new URL("http://localhost:3500/registercostumer");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set the request body data
            String requestBody = "{\"username\": \"" + username + "\", \"nama\": \"" + name + "\", \"password\": \""
                    + password + "\"}";
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();

            // Get the response from the server
            int responseCode = connection.getResponseCode();
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // Read the response
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response
            System.out.println("Response: " + response.toString());

            // Close the connection
            connection.disconnect();

            // Return true if registration is successful
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void login() {
        // Call the login method to access LoginAppUI.java
        LoginAppUI.main(null);
    }
}

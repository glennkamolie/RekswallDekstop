import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class LoginAppUI {
    private static String authToken;
    private static boolean loggedIn = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginAppUI::createAndShowUI);
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("User Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("Login Page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Username input
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        // Password input
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(10);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        panel.add(formPanel, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Login");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Call the login method with the entered values
                String response = login(username, password);

                // Show the response in a popup dialog
                JOptionPane.showMessageDialog(frame, response, "Login Result", JOptionPane.INFORMATION_MESSAGE);

                // If login is successful, show the main menu
                if (loggedIn) {
                    System.out.println(loggedIn);
                    showMainMenu(username);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Login button
        JButton registerappuiButton = new JButton("Register");
        registerappuiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterAppUI();
            }
        });

        buttonPanel.add(registerappuiButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(panel);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String login(String username, String password) {
        try {
            // Set the URL of the endpoint
            URL url = new URL("http://localhost:3500/loginrekswall");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set the request body data
            String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
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

            // Print the response to terminal
            System.out.println("Response: " + response.toString());

            // If login is successful, store the authorization token
            String ambilrespon = response.toString();
            System.out.println(ambilrespon);

            if (ambilrespon.equals("Login Berhasil")) {
                // Login berhasil
                loggedIn = true;
            } else if (ambilrespon.equals("Login Gagal")) {
                // Login gagal
                loggedIn = false;
            }

            // Close the connection
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "An error occurred.";
    }

    private static void showMainMenu(String username) {
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Main Menu", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JButton topUpButton = new JButton("Top-Up");
        topUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String topupInput = JOptionPane.showInputDialog(mainMenuFrame, "Enter the top-up amount:");
                float topupNominal = Float.parseFloat(topupInput);
                if (topupNominal != 0) {
                    topUp(username, topupNominal);
                }
            }
        });

        JButton buyHistoryButton = new JButton("Buy History");
        buyHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getBuyHistory(username);
            }
        });

        JButton sellHistoryButton = new JButton("Sell History");
        sellHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getSellHistory(username);
            }

        });

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        buttonPanel.add(topUpButton);
        buttonPanel.add(buyHistoryButton);
        buttonPanel.add(sellHistoryButton);
        buttonPanel.add(logoutButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        mainMenuFrame.getContentPane().add(panel);
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setVisible(true);
    }

    private static void topUp(String username, float topup_nominal) {
        try {
            // Set the URL of the endpoint
            URL url = new URL("http://localhost:3500/costumers");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set the request headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);

            // Set the request body data
            String requestBody = "{\"username\": \"" + username + "\", \"topup_nominal\": \"" + topup_nominal + "\"}";

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
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response to the terminal
            System.out.println("Response: " + response.toString());

            // Close the connection
            connection.disconnect();

            // Show the response in a dialog box
            JOptionPane.showMessageDialog(null, response.toString(), "Top-Up Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.", "Top-Up Result",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void getBuyHistory(String username) {
        try {
            System.out.println(username);
            // Set the URL of the endpoint
            URL url = new URL("http://localhost:3500/buyhistory");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set the request headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);

            // Set the request body data
            String requestBody = "{\"username\": \"" + username + "\"}";

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
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response to the terminal
            System.out.println("Response: " + response.toString());

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response.toString());

            // Create a List to store the data for the table
            List<Object[]> data = new ArrayList<>();

            // Iterate over the JSON array and extract the values
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                // Extract the values from the JSON object
                int idBeli = obj.getInt("id_beli");
                int userId = obj.getInt("user_id");
                double totalReksadanaLama = obj.getDouble("total_reksadana_lama");
                double beliReksadana = obj.getDouble("beli_reksadana");
                double totalReksadanaBaru = obj.getDouble("total_reksadana_baru");
                String tanggalTransaksi = obj.getString("tanggal_transaksi");

                // Create an array of objects to represent a row in the table
                Object[] row = { idBeli, userId, totalReksadanaLama, beliReksadana, totalReksadanaBaru,
                        tanggalTransaksi };

                // Add the row to the data list
                data.add(row);
            }

            // Column names
            String[] columnNames = { "id_beli", "user_id", "total_reksadana_lama", "beli_reksadana",
                    "total_reksadana_baru", "tanggal_transaksi" };

            // Convert List<Object[]> to Object[][]
            Object[][] rowData = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                rowData[i] = data.get(i);
            }

            // Create a JTable with the data and column names
            JTable table = new JTable(rowData, columnNames);

            // Set preferred size for the table
            table.setPreferredScrollableViewportSize(new Dimension(500, 200));

            // Create a scroll pane and add the table to it
            JScrollPane scrollPane = new JScrollPane(table);

            // Create a panel and add the scroll pane to it
            JPanel panel = new JPanel();
            panel.add(scrollPane);


            // Create a frame and add the panel to it
            JFrame frame = new JFrame("Buy History");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel);

            // Pack and display the frame
            frame.pack();
            frame.setVisible(true);

            // Close the connection
            connection.disconnect();

            // Show the response in a dialog box as JSON Response (untuk melihat popup
            // bentuk JSON)
            // JOptionPane.showMessageDialog(null, response.toString(), "Buy History",
            // JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.", "Buy History",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    private static void getSellHistory(String username) {
        try {
            // Set the URL of the endpoint
            URL url = new URL("http://localhost:3500/sellhistory");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input and output streams
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set the request headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);

            // Set the request body data
            String requestBody = "{\"username\": \"" + username + "\"}";

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
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print the response to the terminal
            System.out.println("Response: " + response.toString());

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response.toString());

            // Create a List to store the data for the table
            List<Object[]> data = new ArrayList<>();

            // Iterate over the JSON array and extract the values
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                // Extract the values from the JSON object
                int idJual = obj.getInt("id_jual");
                int userId = obj.getInt("user_id");
                double totalReksadanaLama = obj.getDouble("total_reksadana_lama");
                double jualReksadana = obj.getDouble("jual_reksadana");
                double totalReksadanaBaru = obj.getDouble("total_reksadana_baru");
                String tanggalTransaksi = obj.getString("tanggal_transaksi");

                // Create an array of objects to represent a row in the table
                Object[] row = { idJual, userId, totalReksadanaLama, jualReksadana, totalReksadanaBaru,
                        tanggalTransaksi };

                // Add the row to the data list
                data.add(row);
            }

            // Column names
            String[] columnNames = { "id_jual", "user_id", "total_reksadana_lama", "jual_reksadana",
                    "total_reksadana_baru", "tanggal_transaksi" };

            // Convert List<Object[]> to Object[][]
            Object[][] rowData = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                rowData[i] = data.get(i);
            }

            // Create a JTable with the data and column names
            JTable table = new JTable(rowData, columnNames);

            // Set preferred size for the table
            table.setPreferredScrollableViewportSize(new Dimension(500, 200));

            // Create a scroll pane and add the table to it
            JScrollPane scrollPane = new JScrollPane(table);

            // Create a panel and add the scroll pane to it
            JPanel panel = new JPanel();
            panel.add(scrollPane);

            // Create a frame and add the panel to it
            JFrame frame = new JFrame("Sell History");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel);

            // Pack and display the frame
            frame.pack();
            frame.setVisible(true);

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.", "Sell History",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
    }

    private static void logout() {
        // Call the Logout method to access LogoutApp.java
        LogoutApp.main(null);
    }

    private static void RegisterAppUI() {
        // Call the method to access RegisterAppUI.java
        RegisterAppUI.main(null);
    }
}

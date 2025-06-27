/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

/**
 *
 * @author sarda
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// ===== Client Class =====
class Client {
    private String clientId;
    private String name;
    private String contact;
    private String course;

    public Client(String name, String contact, String course) {
        this.name = name;
        this.contact = contact;
        this.course = course;
        this.clientId = generateClientId();
    }

    private String generateClientId() {
        return "CLT" + System.currentTimeMillis();  // Simple unique ID
    }

    public String getClientId() { return clientId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getCourse() { return course; }

    public String toString() {
        return "ID: " + clientId + "\nName: " + name + "\nContact: " + contact + "\nCourse: " + course + "\n";
    }
}

// ===== Client Repository =====
class ClientRepository {
    private List<Client> clientList;

    public ClientRepository() {
        clientList = new ArrayList<>();
    }

    public void saveClient(Client client) {
        clientList.add(client);
        System.out.println("Client saved successfully.");
    }

    public List<Client> getAllClients() {
        return clientList;
    }
}

// ===== Registration Service =====
class RegistrationService {
    private ClientRepository clientRepository;

    public RegistrationService() {
        clientRepository = new ClientRepository();
    }

    public boolean registerClient(String name, String contact, String course) {
        if (validateClient(name, contact, course)) {
            Client newClient = new Client(name, contact, course);
            clientRepository.saveClient(newClient);
            JOptionPane.showMessageDialog(null, "Client registered. ID: " + newClient.getClientId());
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return false;
        }
    }

    private boolean validateClient(String name, String contact, String course) {
        return name != null && !name.trim().isEmpty()
                && contact != null && !contact.trim().isEmpty()
                && course != null && !course.trim().isEmpty();
    }

    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }
}


public class Main {
    private static RegistrationService registrationService = new RegistrationService();

  
    private static String adminUsername = null;
    private static String adminPassword = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (adminUsername == null || adminPassword == null) {
                // No admin yet - show signup
                if (!showSignupDialog()) {
                    JOptionPane.showMessageDialog(null, "Signup cancelled. Exiting application.");
                    System.exit(0);
                }
            }

            // Show login, verify credentials based on signup
            if (showLoginDialog()) {
                showRegistrationForm();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Exiting application.");
                System.exit(0);
            }
        });
    }

    private static boolean showSignupDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("Choose Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Choose Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Admin Signup", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and Password cannot be empty.");
                return showSignupDialog();  // Retry signup
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return showSignupDialog();  // Retry signup
            }

            // Save credentials in memory
            adminUsername = username;
            adminPassword = password;
            JOptionPane.showMessageDialog(null, "Signup successful! Please login now.");
            return true;
        }
        return false; // User cancelled
    }

    private static boolean showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            return username.equals(adminUsername) && password.equals(adminPassword);
        }

        return false; // Cancel or close dialog
    }

    private static void showRegistrationForm() {
        JFrame frame = new JFrame("Fee Accounting System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        frame.add(panel);

        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField courseField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact:"));
        panel.add(contactField);
        panel.add(new JLabel("Course:"));
        panel.add(courseField);

        JButton registerBtn = new JButton("Register");
        JButton viewBtn = new JButton("View Registered Clients");

        panel.add(registerBtn);
        panel.add(viewBtn);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String contact = contactField.getText();
            String course = courseField.getText();

            registrationService.registerClient(name, contact, course);

            nameField.setText("");
            contactField.setText("");
            courseField.setText("");
        });

        viewBtn.addActionListener(e -> {
            List<Client> clients = registrationService.getAllClients();
            JTextArea area = new JTextArea(15, 30);
            area.setEditable(false);

            if (clients.isEmpty()) {
                area.setText("No clients registered.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Client c : clients) {
                    sb.append(c.toString()).append("-------------------------\n");
                }
                area.setText(sb.toString());
            }

            JScrollPane scrollPane = new JScrollPane(area);
            JOptionPane.showMessageDialog(frame, scrollPane, "Registered Clients", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }
}


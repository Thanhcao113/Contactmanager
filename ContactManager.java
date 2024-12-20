
package contactmanager;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ContactManager {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, emailField;

    public ContactManager() {
        frame = new JFrame("Contact Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Table setup
        String[] columnNames = {"Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();

                if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                    tableModel.addRow(new Object[]{name, phone, email});
                    nameField.setText("");
                    phoneField.setText("");
                    emailField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.csv"))) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            writer.write(tableModel.getValueAt(i, j).toString() + ",");
                        }
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(frame, "Contacts saved successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving contacts.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (BufferedReader reader = new BufferedReader(new FileReader("contacts.csv"))) {
                    tableModel.setRowCount(0);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        tableModel.addRow(data);
                    }
                    JOptionPane.showMessageDialog(frame, "Contacts loaded successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading contacts.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManager::new);
    }
}



   

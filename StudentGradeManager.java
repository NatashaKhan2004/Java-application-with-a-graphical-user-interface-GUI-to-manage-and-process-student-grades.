import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class StudentGradeManager extends JFrame {
    private JTextField nameField;
    private JTextField gradeField;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentGradeManager() {
        setTitle("Student Grade Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        nameField = new JTextField(15);
        gradeField = new JTextField(5);
        JButton addButton = new JButton("Add Grade");
        JButton saveButton = new JButton("Save to CSV");
        JButton loadButton = new JButton("Load from CSV");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Grade:"));
        inputPanel.add(gradeField);
        inputPanel.add(addButton);
        inputPanel.add(saveButton);
        inputPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table to display grades
        tableModel = new DefaultTableModel(new String[]{"Name", "Grade"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(new AddGradeAction());
        saveButton.addActionListener(new SaveToCSVAction());
        loadButton.addActionListener(new LoadFromCSVAction());
    }

    private class AddGradeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String grade = gradeField.getText();
            if (!name.isEmpty() && !grade.isEmpty()) {
                tableModel.addRow(new Object[]{name, grade});
                nameField.setText("");
                gradeField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter both name and grade.");
            }
        }
    }

    private class SaveToCSVAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("grades.csv"))) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String name = (String) tableModel.getValueAt(i, 0);
                    String grade = (String) tableModel.getValueAt(i, 1);
                    writer.write(name + "," + grade);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Data saved to grades.csv");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving to file: " + ex.getMessage());
            }
        }
    }

    private class LoadFromCSVAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedReader reader = new BufferedReader(new FileReader("grades.csv"))) {
                String line;
                tableModel.setRowCount(0); // Clear existing data
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    tableModel.addRow(data);
                }
                JOptionPane.showMessageDialog(null, "Data loaded from grades.csv");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading from file: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeManager manager = new StudentGradeManager();
            manager.setVisible(true);
        });
    }
}
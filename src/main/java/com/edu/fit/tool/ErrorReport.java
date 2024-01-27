package com.edu.fit.tool;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ErrorReport extends JFrame {
    private final List<ErrorEntry> errors = new ArrayList<>();
    private JTable tblReport;
    private DefaultTableModel modelTblReport;

    public void addError(String type, String name, String errorDescription) {
        errors.add(new ErrorEntry(type, name, errorDescription));
    }

    public void printErrors(File projectDir) {
        setTitle("Report");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel lblTitle = new JLabel("REPORT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 25));
        lblTitle.setBounds(630,10,300,50);
        add(lblTitle);

        JLabel lblFile = new JLabel("File: ");
        lblFile.setFont(new Font("Arial", Font.PLAIN, 15));
        lblFile.setBounds(50,60,100,50);
        add(lblFile);

        JLabel lblFileValue = new JLabel(projectDir.getAbsolutePath());
        lblFileValue.setFont(new Font("Arial", Font.ITALIC, 15));
        lblFileValue.setBounds(90,60,700,50);
        add(lblFileValue);

        String[] columns = {"Type", "Name", "Error Description"};
        modelTblReport = new DefaultTableModel(columns, 0);
        tblReport = new JTable(modelTblReport);

        for (ErrorEntry error : errors) {
            modelTblReport.addRow(new Object[]{error.getType(), error.getName(), error.getErrorDescription()});
        }

        tblReport.setFont(new Font("Arial", Font.PLAIN, 14));
        tblReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        tblReport.getTableHeader().setForeground(Color.BLUE);
        tblReport.setRowHeight(30);
        tblReport.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setBorder(new TitledBorder(
                new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
                "", TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(0, 0, 0)));
        scrollPane.setBounds(30,110,1330,500);

        TableColumnModel tcm = tblReport.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(10);
        tcm.getColumn(1).setPreferredWidth(280);
        tcm.getColumn(2).setPreferredWidth(580);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tcm.getColumn(0).setCellRenderer(centerRenderer);
        tcm.getColumn(1).setCellRenderer(centerRenderer);

        add(scrollPane);
        setVisible(true);
    }

    private static class ErrorEntry {
        private final String type;
        private final String name;
        private final String errorDescription;

        public ErrorEntry(String type, String name, String errorDescription) {
            this.type = type;
            this.name = name;
            this.errorDescription = errorDescription;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getErrorDescription() {
            return errorDescription;
        }
    }
}

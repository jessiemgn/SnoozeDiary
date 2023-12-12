package ui;

import model.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import persistence.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a Sleep Tracker with GUI
public class SleepTrackerGUI {
    private SleepTracker sleepTracker;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    private JPanel cardPanel;
    private JPanel chartPanel;
    private CardLayout cardLayout;
    private JTable sleepTable;
    private Sleep selectedSleep;

    private JTextField sleepTimeTf;
    private JTextField awakeTimeTf;
    private JTextField sleepQualityTf;
    private JTextArea journalTf;

    private JLabel avgSleepDuration;

    private int week = 0;

    private Color primary;
    private Color secondary;
    private Color accent1;
    private Color accent2;

    private static final String JSON_STORE = "./data/sleep-tracker.json";

    // EFFECTS: constructs a Sleep Tracker with GUI, jsonWriter, and jsonReader
    public SleepTrackerGUI() throws FileNotFoundException {
        sleepTracker = new SleepTracker();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        showGUI();
    }

    // EFFECTS: creates frame
    @SuppressWarnings("methodlength")
    public void showGUI() {
        primary = new Color(35, 41, 70);
        secondary = new Color(184, 193, 236);
        accent1 = new Color(255, 255, 254);
        accent2 = new Color(227, 246, 245);

        JFrame mainFrame = new JFrame("SnoozeDiary");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(700, 400);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(primary);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(primary);

        JPanel splashPanel = splashScreenPanel();
        splashPanel.setBackground(primary);
        cardPanel.add(splashPanel, "splash");

        JPanel menuPanel = mainPanel();
        cardPanel.add(menuPanel, "main");
        menuPanel.setBackground(primary);

        JPanel formPanel = formPanel();
        cardPanel.add(formPanel, "form");
        formPanel.setBackground(primary);

        cardLayout.show(cardPanel, "splash");
        mainFrame.add(cardPanel);
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Would you like to save changes?",
                        "Save Changes to File", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    saveSleepTracker();
                }

                System.exit(0);
            }
        });

        splashScreen();
    }

    // EFFECTS: simulates splash screen for 3000 ms
    private void splashScreen() {
        try {
            Thread.sleep(3000);
            int response = JOptionPane.showConfirmDialog(null, "Would you like to load saved data?",
                    "Load Data From File", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                loadSleepTracker();
            }

            refreshData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cardLayout.show(cardPanel, "main");
    }

    // EFFECTS: restrict inputs to number
    @SuppressWarnings("methodlength")
    private void setNumericFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                StringBuilder sb = new StringBuilder(string);
                for (int i = sb.length() - 1; i >= 0; i--) {
                    char c = sb.charAt(i);
                    if (!Character.isDigit(c)) {
                        sb.deleteCharAt(i);
                    }
                }
                super.insertString(fb, offset, sb.toString(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                StringBuilder sb = new StringBuilder(text);
                for (int i = sb.length() - 1; i >= 0; i--) {
                    char c = sb.charAt(i);
                    if (!Character.isDigit(c)) {
                        sb.deleteCharAt(i);
                    }
                }
                super.replace(fb, offset, length, sb.toString(), attrs);
            }
        });
    }

    // EFFECTS: resets sleep time, awake time, sleep quality, and journal
    private void resetFieldSleep() {
        sleepTimeTf.setText(null);
        awakeTimeTf.setText(null);
        sleepQualityTf.setText(null);
        journalTf.setText(null);
    }

    // EFFECTS: action for modify sleep
    private void selectSleep(Sleep sleep) {
        sleepTimeTf.setText(String.valueOf(sleep.getSleepTime()));
        awakeTimeTf.setText(String.valueOf(sleep.getAwakeTime()));
        sleepQualityTf.setText(String.valueOf(sleep.getSleepQuality()));
        journalTf.setText(sleep.getJournal());
    }

    // EFFECTS: checks if input is valid
    @SuppressWarnings("methodlength")
    private boolean validateSleepInput() {
        boolean valid = true;
        if (sleepTimeTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sleep Time cannot be empty");
            valid = false;
        } else {
            int sleepTime = Integer.parseInt(sleepTimeTf.getText());
            if (!(0 <= sleepTime && sleepTime <= 23)) {
                JOptionPane.showMessageDialog(null, "Sleep Time must be in range 0-23");
                valid = false;
            }
        }
        if (awakeTimeTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Awake Time cannot be empty");
            valid = false;
        } else {
            int awakeTime = Integer.parseInt(awakeTimeTf.getText());
            if (!(0 <= awakeTime && awakeTime <= 23)) {
                JOptionPane.showMessageDialog(null, "Awake Time must be in range 0-23");
                valid = false;
            }
        }
        if (sleepQualityTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sleep Quality cannot be empty");
            valid = false;
        } else {
            int sleepQuality = Integer.parseInt(sleepQualityTf.getText());
            if (!(1 <= sleepQuality && sleepQuality <= 5)) {
                JOptionPane.showMessageDialog(null, "Sleep Quality must be in range 1-5");
                valid = false;
            }
        }
        if (journalTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sleep Journal cannot be empty");
            valid = false;
        }
        return valid;
    }

    // EFFECTS: creates dataset for Sleep Duration Chart
    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int daysWeek = week * 7;
        int count = 1;
        for (int i = daysWeek; (i < daysWeek + 7) && (i < sleepTracker.getSleepCycles().size()); i++) {
            dataset.addValue(sleepTracker.getSleepCycles().get(i).getDuration(), "Time", String.valueOf(count++));
        }
        return dataset;
    }

    // EFFECTS: creates Sleep Duration Chart
    private JFreeChart createChart(DefaultCategoryDataset dataset) {

        return ChartFactory.createLineChart(
                "Sleep Duration Chart",
                "Day",
                "Duration",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
    }

    // EFFECTS: creates panel for Sleep Duration Chart
    private JPanel createChartPanel() {
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);

        return new ChartPanel(chart);
    }

    // EFFECTS: creates splash screen
    private JPanel splashScreenPanel() {
        ImageIcon imageIcon = new ImageIcon("./data/icon.png");
        Image adjustImage = imageIcon.getImage().getScaledInstance(220,132,100);
        ImageIcon adjustedImage = new ImageIcon(adjustImage);
        JLabel appIconLabel = new JLabel(adjustedImage);

        JPanel splashPanel = new JPanel();
        splashPanel.setLayout(new BorderLayout());
        splashPanel.setBackground(primary);
        splashPanel.add(appIconLabel, BorderLayout.CENTER);

        return splashPanel;
    }

    // EFFECTS: creates panel for main menu
    @SuppressWarnings("methodlength")
    private JPanel mainPanel() {
        GridLayout mainGrid = new GridLayout(1, 2);
        mainGrid.setHgap(5);
        JPanel mainPanel = new JPanel(mainGrid);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(primary);

        sleepTable = new JTable();
        sleepTable.setBackground(primary);
        sleepTable.setForeground(accent2);

        JScrollPane scrollPane = new JScrollPane(sleepTable);
        scrollPane.setBackground(primary);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                trackColor = primary;
            }

            @Override
            protected void paintThumb(Graphics graphics, JComponent component, Rectangle thumb) {
                graphics.setColor(accent2);
                graphics.fillRoundRect(thumb.x + 4, thumb.y + 3,
                        thumb.width - 8, thumb.height - 6,
                        10, 10);
            }
        });

        JButton addSleepButton = new JButton("Track a New Sleep Cycle");
        JButton viewSleepButton = new JButton("View/Modify Sleep Details");
        JButton removeSleepButton = new JButton("Remove Last Sleep Cycle");

        removeSleepButton.setBackground(accent1);
        addSleepButton.setBackground(accent1);
        viewSleepButton.setBackground(accent1);

        removeSleepButton.setForeground(primary);
        addSleepButton.setForeground(primary);
        viewSleepButton.setForeground(primary);

        JPanel centerButtonPanel = new JPanel(new GridLayout(1, 2));
        centerButtonPanel.add(removeSleepButton);
        centerButtonPanel.setBackground(primary);
        centerButtonPanel.setForeground(accent1);

        JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 2));
        bottomButtonPanel.add(viewSleepButton);
        bottomButtonPanel.setBackground(primary);
        bottomButtonPanel.setForeground(accent1);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(addSleepButton, BorderLayout.NORTH);
        buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
        buttonPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
        buttonPanel.setBackground(primary);
        buttonPanel.setForeground(accent1);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(primary);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        avgSleepDuration = new JLabel("Avg. Sleep Duration : ");
        avgSleepDuration.setHorizontalAlignment(JLabel.CENTER);

        JPanel dataPanel = new JPanel(new GridLayout(3, 1));
        dataPanel.add(avgSleepDuration);

        JPanel buttonWeekPanel = new JPanel(new GridLayout(1, 3));

        JButton prevWeekButton = new JButton("<");
        JButton nextWeekButton = new JButton(">");

        JLabel weekLabel = new JLabel("Week " + (week + 1));
        weekLabel.setHorizontalAlignment(JLabel.CENTER);

        buttonWeekPanel.add(prevWeekButton);
        buttonWeekPanel.add(weekLabel);
        buttonWeekPanel.add(nextWeekButton);
        buttonWeekPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        dataPanel.add(buttonWeekPanel);

        dataPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        leftPanel.add(chartPanel, BorderLayout.CENTER);
        leftPanel.add(dataPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        prevWeekButton.addActionListener(actionEvent -> {
            if (week > 0) {
                week -= 1;
                weekLabel.setText("Week " + (week + 1));
            }
            refreshData();
        });

        nextWeekButton.addActionListener(actionEvent -> {
            if (week < sleepTracker.getWeekSize() - 1) {
                week += 1;
                weekLabel.setText("Week " + (week + 1));
            }
            refreshData();
        });

        removeSleepButton.addActionListener(actionEvent -> {
            sleepTracker.removeLastSleep();
            refreshData();
        });

        addSleepButton.addActionListener(actionEvent -> {
            resetFieldSleep();
            selectedSleep = null;
            cardLayout.show(cardPanel, "form");
        });

        viewSleepButton.addActionListener(actionEvent -> {
            int idx = sleepTable.getSelectedRow();
            if (idx != -1) {
                selectedSleep = sleepTracker.getSleepCycles().get(idx);
                selectSleep(selectedSleep);
                cardLayout.show(cardPanel, "form");
            } else {
                JOptionPane.showMessageDialog(null, "Please select sleep on table");
            }
        });

        refreshData();
        return mainPanel;
    }

    // EFFECTS: refreshes chart's data
    private void refreshData() {
        loadSleepTable();
        chartPanel.removeAll();
        chartPanel.add(createChartPanel(), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
        setAvg();
    }

    // EFFECTS: sets the average sleep duration
    private void setAvg() {
        int current = week + 1;
        double avgDuration = 0;
        int count = 0;

        for (Sleep sleep : sleepTracker.getSleepCycles()) {
            if (sleep.getWeek() == current) {
                avgDuration += sleep.getDuration();
                count++;
            }
        }

        avgDuration /= count;
        avgSleepDuration.setText(String.format("Average Sleep Duration: %.2f hours", avgDuration));

    }

    // EFFECTS: creates panel for add sleep and view/modify sleep
    @SuppressWarnings("methodlength")
    private JPanel formPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(primary);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        JButton okButton = new JButton("Ok");

        sleepTimeTf = new JTextField();
        setNumericFilter(sleepTimeTf);
        awakeTimeTf = new JTextField();
        setNumericFilter(awakeTimeTf);
        sleepQualityTf = new JTextField();
        setNumericFilter(sleepQualityTf);
        journalTf = new JTextArea();
        journalTf.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        journalTf.setLineWrap(true);
        journalTf.setWrapStyleWord(true);

        JLabel sleepTimeLbl = new JLabel("Sleep Time");
        sleepTimeLbl.setForeground(accent1);
        JLabel awakeTimeLbl = new JLabel("Awake Time");
        awakeTimeLbl.setForeground(accent1);
        JLabel sleepQualityLbl = new JLabel("Sleep Quality");
        sleepQualityLbl.setForeground(accent1);
        JLabel journalLbl = new JLabel("Sleep Journal");
        journalLbl.setForeground(accent1);

        GridLayout topFormGrid = new GridLayout(2, 3);
        topFormGrid.setHgap(10);
        JPanel topFormLayout = new JPanel(topFormGrid);
        topFormLayout.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topFormLayout.add(sleepTimeLbl);
        topFormLayout.add(awakeTimeLbl);
        topFormLayout.add(sleepQualityLbl);
        topFormLayout.add(sleepTimeTf);
        topFormLayout.add(awakeTimeTf);
        topFormLayout.add(sleepQualityTf);
        topFormLayout.setBackground(primary);

        JPanel journalLayout = new JPanel(new BorderLayout());
        journalLayout.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        journalLayout.add(journalLbl, BorderLayout.NORTH);
        journalLayout.add(journalTf, BorderLayout.CENTER);
        journalLayout.setBackground(primary);

        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.add(topFormLayout, BorderLayout.NORTH);
        formPanel.add(journalLayout, BorderLayout.CENTER);
        topFormLayout.setBackground(primary);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(okButton);
        buttonPanel.add(backButton);
        buttonPanel.setBackground(primary);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        sleepTimeTf.setBackground(accent1);
        awakeTimeTf.setBackground(accent1);
        sleepQualityTf.setBackground(accent1);
        journalTf.setBackground(accent1);

        sleepTimeTf.setForeground(primary);
        awakeTimeTf.setForeground(primary);
        sleepQualityTf.setForeground(primary);
        journalTf.setForeground(primary);

        sleepTimeTf.setHorizontalAlignment(JTextField.CENTER);
        awakeTimeTf.setHorizontalAlignment(JTextField.CENTER);
        sleepQualityTf.setHorizontalAlignment(JTextField.CENTER);

        okButton.setForeground(primary);
        backButton.setForeground(primary);

        okButton.addActionListener(actionEvent -> {
            if (validateSleepInput()) {
                int sleepTime = Integer.parseInt(sleepTimeTf.getText());
                int awakeTime = Integer.parseInt(awakeTimeTf.getText());
                int sleepQuality = Integer.parseInt(sleepQualityTf.getText());
                if (selectedSleep == null) {
                    sleepTracker.addSleep(new Sleep(sleepTime, awakeTime, sleepQuality, journalTf.getText()), true);
                    resetFieldSleep();
                    JOptionPane.showMessageDialog(null, "Add sleep data successful");
                } else {
                    selectedSleep.modifySleepDetails(sleepTime, awakeTime, sleepQuality, journalTf.getText());
                    JOptionPane.showMessageDialog(null, "Modify sleep data successful");
                }
            }
        });

        backButton.addActionListener(actionEvent -> {
            cardLayout.show(cardPanel, "main");
            refreshData();
        });

        return mainPanel;
    }

    // EFFECTS: loads sleep table
    public void loadSleepTable() {
        DefaultTableModel sleepTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        sleepTable.setModel(sleepTableModel);
        sleepTable.setBackground(accent1);
        sleepTable.setForeground(primary);

        sleepTableModel.addColumn("Week");
        sleepTableModel.addColumn("Day");
        sleepTableModel.addColumn("Duration");

        for (Sleep sleep : sleepTracker.getSleepCycles()) {
            sleepTableModel.addRow(new Object[]{
                    sleep.getWeek(),
                    sleep.getDay(),
                    sleep.getDuration()
            });
        }

        tableDesign();
        tableCellDesign();
    }

    // EFFECTS: sets table's design
    private void tableDesign() {
        sleepTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(JLabel.CENTER);
                setBackground(primary);
                setForeground(accent1);
            }
        });
    }

    // EFFECTS: sets table cell's design
    private void tableCellDesign() {
        sleepTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component tableCell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                tableCell.setForeground(primary);
                setHorizontalAlignment(JLabel.CENTER);

                if (row % 2 == 0) {
                    tableCell.setBackground(accent1);
                } else {
                    tableCell.setBackground(accent2);
                }

                if (isSelected) {
                    tableCell.setBackground(secondary);
                    tableCell.setForeground(primary);
                }

                return tableCell;
            }
        });
    }

    // EFFECTS: saves workout plan to file
    private void saveSleepTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(sleepTracker);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved sleep tracker data to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to write data to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads sleep tracker from file
    private void loadSleepTracker() {
        try {
            sleepTracker = jsonReader.read();
            JOptionPane.showMessageDialog(null, "Loaded data from: " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read data from file: " + JSON_STORE);
        }
    }
}

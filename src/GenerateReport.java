import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class GenerateReport extends JFrame{
    String bgColor = "#f7f1e3";
    @SuppressWarnings("unused")
    private Dashboard dashboard;
    JPanel rightPanel, leftPanel;
    private JTable salesReportTable;
    private JTextField searchBar;
    JButton button1, button2, button3, button4, button5, button6;

    /**
     * This method is responsible for loading the image for the Icons in the Buttons Panel.
     * @param path - source file path of the image
     * @param width - width of the image (px)
     * @param height - height of the image (px)
     * @return
     * @comments Note:
     */
    public static ImageIcon resizeIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(newImg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This class contains the ImageButton method. THis is responsible for 
     * allowing the addition of padding to the image.
     * @comments Note:
     */
    public static class ImageButton extends JButton {
        private ImageIcon icon;
        private int paddingTop; 
        /**
         * This method is responsible for adding padding to the image and
         * lowers the text relative to the button.
         * @param icon
         * @param paddingTop
         */
        public ImageButton(ImageIcon icon, int paddingTop) {
            this.icon = icon;
            this.paddingTop = paddingTop;
            setVerticalTextPosition(SwingConstants.BOTTOM);
        }
        /**
         * This method allows irreversible changes. Making sure the components
         * are located exactly where they are.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (icon != null) {
                int iconX = (getWidth() - icon.getIconWidth()) / 2;
                int iconY = paddingTop; 
                icon.paintIcon(this, g, iconX, iconY);
            }
        }
    }

    /**
     * This method combined with ImageButton allows for the exact
     * location of the TEXT inside the button. Such as changing the 
     * margin / padding of it.
     * @param button
     * @param paddingTop
     * @comments Note:
     */
    public static void adjustButtonTextHeight(AbstractButton button, int paddingTop) {
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setMargin(new Insets(paddingTop, 0, 0, 0)); // Only Top since it is already centered.
    }

    /**
     * This constructor is responsible for showing the Customer Details GUI
     * @param access_level
     * @param dashboard
     * @comments Note:
     */
    public GenerateReport(String access_level, Dashboard dashboard) {
        System.out.println("Opening Generate Report with access level " + access_level);

        this.dashboard = dashboard;
        JFrame generateReportFrame = new JFrame("Manage Customer Details");
        generateReportFrame.setSize(1680, 720);
        generateReportFrame.setLayout(null);
        generateReportFrame.setLocationRelativeTo(null);
        generateReportFrame.getContentPane().setBackground(Color.decode(bgColor));

        try {
            // Load Rubik-Bold font
            Font rubikFont = loadFont("fonts/Inter.ttc", 24f);

            // Set the Rubik-Bold font for the whole UI
            UIManager.put("Button.font", rubikFont);
            UIManager.put("Label.font", rubikFont);
            UIManager.put("TextField.font", rubikFont);
            UIManager.put("PasswordField.font", rubikFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        generateReportFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        generateReportFrame.setVisible(true);

        System.out.println("Generate Report Opened!");

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- Generate Report Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(generateReportFrame,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    generateReportFrame.dispose(); // Close the window if user confirms
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };

        generateReportFrame.addWindowListener(windowAdapter);
        generateReportFrame.addWindowFocusListener(windowAdapter);

        /**This is the left portion of the GUI. 
         * This should contain 6 Buttons
         * button1 - Add Customer
         * button2 - Rename Customer
         * button3 - Change Contact Number
         * button4 - Remove Customer / But Not Really
         * button5 - Cancel / Undo Current Changes
         * button6 - EXIT
         * @Comment Note:
         */

        leftPanel = new JPanel();
        leftPanel.setBounds(5, 5, 830, 670);
        leftPanel.setLayout(null);
        //leftPanel.setBorder(BorderFactory.createTitledBorder("11"));
        leftPanel.setBackground(Color.decode(bgColor));

        generateReportFrame.add(leftPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/gr_daily.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/gr_monthly.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/gr_complete.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/gr_all.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/gr_excel.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/back_exit.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("Daily Report");
        button2.setText("Monthly Report");
        button3.setText("Complete Only");
        button4.setText("Show All");
        button5.setText("Excel");
        button6.setText("Exit");

        // Adjust text height for buttons
        adjustButtonTextHeight(button1, 50);
        adjustButtonTextHeight(button2, 50);
        adjustButtonTextHeight(button3, 50);
        adjustButtonTextHeight(button4, 50);
        adjustButtonTextHeight(button5, 50);
        adjustButtonTextHeight(button6, 50);

        button1.setBackground(Color.decode("#ff793f"));
        button2.setBackground(Color.decode("#ff793f"));
        button3.setBackground(Color.decode("#ff793f"));
        button4.setBackground(Color.decode("#ff793f"));
        button5.setBackground(Color.decode("#ff793f"));
        button6.setBackground(Color.decode("#ff793f"));

        button1.setFocusPainted(false);
        button2.setFocusPainted(false);
        button3.setFocusPainted(false);
        button4.setFocusPainted(false);
        button5.setFocusPainted(false);
        button6.setFocusPainted(false);

        // Location of all the BUTTONS
        button1.setBounds(20, 24, 250, 300);
        button2.setBounds(290, 25, 250, 300);
        button3.setBounds(560, 25, 250, 300);
        button4.setBounds(20, 345, 250, 300);
        button5.setBounds(290, 345, 250, 300);
        button6.setBounds(560, 345, 250, 300);

        // BUTTON 1 MODEL with LISTENER
        button1.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button1.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 1 - Is Pressed");
                } else {
                    button1.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });
        
        // BUTTON 2 MODEL with LISTENER
        button2.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button2.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 2 - Is Pressed");
                } else {
                    button2.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 3 MODEL with LISTENER
        button3.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button3.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 3 - Is Pressed");
                } else {
                    button3.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 4 MODEL with LISTENER
        button4.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button4.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 4 - Is Pressed");
                } else {
                    button4.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 5 MODEL with LISTENER
        button5.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button5.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 5 - Is Pressed");
                } else {
                    button5.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 6 MODEL with CHANGE LISTENER (EXIT)
        button6.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button6.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 6 - Is Pressed");
                } else {
                    button6.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 6 MODEL with ACTION LISTENER (EXIT)
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(generateReportFrame,
                        "Are you sure you want to close? Any unsaved changes will be lost.",
                        "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window.");
                    generateReportFrame.dispose();
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled.");
                }
            }
        });

        // ADDING THE BUTTONS
        leftPanel.add(button1);
        leftPanel.add(button2);
        leftPanel.add(button3);
        leftPanel.add(button4);
        leftPanel.add(button5);
        leftPanel.add(button6);

        /**This is the Right portion of the GUI. 
         * It should contain a Search Panel and Button and Below
         * should be a JTable containing the Customer Database
         * @Comment Note:
         */

        rightPanel = new JPanel();
        rightPanel.setBounds(830, 5, 830, 670);
        rightPanel.setLayout(null);
        //rightPanel.setBorder(BorderFactory.createTitledBorder("11"));
        rightPanel.setBackground(Color.decode(bgColor));

        generateReportFrame.add(rightPanel);

        // SEARCH BAR
        searchBar = new JTextField();
        searchBar.setBounds(20, 30, 200, 30);
        rightPanel.add(searchBar);

        // SEARCH BUTTON
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(230, 30, 200, 30);
        searchButton.setBackground(Color.decode("#ff793f"));
        rightPanel.add(searchButton);

        // SEARCH MODEL  MODEL with LISTENER
        searchButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    searchButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Search Button - Is Pressed");
                } else {
                    searchButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // Filter

        JButton filterButton = new JButton("Filter");
        filterButton.setBounds(450, 30, 200, 30);
        filterButton.setBackground(Color.decode("#ff793f"));
        rightPanel.add(filterButton);

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();

        // Create a table and set the model
        salesReportTable = new JTable(tableModel);
        JScrollPane salesReportScrollPane = new JScrollPane(salesReportTable);
        salesReportScrollPane.setBounds(20, 70, 790, 570);
        rightPanel.add(salesReportScrollPane);

        tableModel.addColumn("Transaction ID");
        tableModel.addColumn("Total Amount");
        tableModel.addColumn("Cashier");
        tableModel.addColumn("Date");
        tableModel.addColumn("Payment Status");

        

        resetSalesReportFilter();
        updateSalesReportTable();
    } // end of customerDetailsFrame

    public void generateDailySalesReport() {
        // input date / day of what day's sales report to make
        // set the values in the 'srfilter' to set the from and to date to current date
        // set orderStatus to 'paid'
        // save the table in excel 

        // add some few spacing or make a second booklet inside or page
        // to display the product revenue wherein it gets all the 
        // save it to excel file
    }

    public void generateMonthlySalesReport() {
        // input month of sales report to make

        // save it to excel file
    }

    public void generateCustomSalesReport() {
        // get the srfilter query and make a table based on it
    }

    private static final String FILTER_FILE = "srfilter.txt";

    public void updateSalesReportTable() {
        Map<String, String> filter = readSalesReportFilter();
        String query = constructQuery(filter);

        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/prj_tan", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel tableModel = (DefaultTableModel) salesReportTable.getModel();
            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("transaction_ID"),
                        rs.getDouble("transaction_TotalPrice"),
                        rs.getString("cashier"),
                        rs.getTimestamp("transaction_Date"),
                        rs.getString("transaction_OrderStatus")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetSalesReportFilter() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILTER_FILE))) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            writer.write("dateSelectRange.fromDate = " + currentDate + "\n");
            writer.write("dateSelectRange.toDate = " + currentDate + "\n");
            writer.write("orderStatus.showStatus = paid\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> readSalesReportFilter() {
        Map<String, String> filterValues = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILTER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    filterValues.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filterValues;
    }

    public String constructQuery(Map<String, String> filter) {
        String fromDate = filter.getOrDefault("dateSelectRange.fromDate", "1970-01-01");
        String toDate = filter.getOrDefault("dateSelectRange.toDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        String showStatus = filter.getOrDefault("orderStatus.showStatus", "paid");

        return "SELECT t.transaction_ID, t.transaction_TotalPrice, " +
            "CONCAT(e.employee_FirstName, ' ', e.employee_LastName) AS cashier, " +
            "t.transaction_Date, t.transaction_OrderStatus " +
            "FROM tbl_transaction t " +
            "JOIN tbl_employee e ON t.employee_ID = e.employee_ID " +
            "WHERE t.transaction_Date BETWEEN '" + fromDate + "' AND '" + toDate + "' " +
            "AND t.transaction_OrderStatus = '" + showStatus + "' " +
            "AND t.transaction_ActiveStatus = 'active'";
    }

    /**
     * This method loads custom fonts (ttf files etc.) from the Fonts Folder
     * so that they can be used within this application.
     * @param path
     * @param size
     * @return
     * @throws IOException
     * @throws FontFormatException
     * @comments Note:
     */
    public static Font loadFont(String path, float size) throws IOException, FontFormatException {
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
        return customFont;
    } // end of show loadFont

    /**
    * This method enables all the Button Components.
    * @comments Note:
    */
    public void enableAllCustomerButtons() { 
        Component[] components = rightPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ImageButton) {
                ImageButton button = (ImageButton) component;
                button.setEnabled(true);
                button.setBackground(Color.decode("#ff793f"));
            }
        }
    } // end of enableAllCustomerButtons

    /**
     * This method disables all the Button Components.
     * @comments Note:
     */
    public void disableAllCustomerButtons() {
        Component[] components = rightPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ImageButton) {
                ImageButton button = (ImageButton) component;
                button.setEnabled(false);
                button.setBackground(Color.decode("#84817a"));
            }
        }
    } // end of disableAllCustomerButtons
}

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class OrderList extends JFrame {
    String bgColor = "#f7f1e3";
    @SuppressWarnings("unused")
    private Dashboard dashboard;
    JPanel rightPanel, leftPanel;
    JButton button1, button2, button3, button4, button5, button6;
    JTable transaction_Table;
    DefaultTableModel transaction_TableModel, items_TableModel;
    JButton transactionNextButton, transactionPrevButton;
    JFrame orderListFrame;
    ModifyOrder modifyOrder;
    int PAGE_SIZE = 16;

    Operations ops = new Operations();
    private int currentPage = 0;
    int employee_ID;

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
         * @comments Note:
         */
        public ImageButton(ImageIcon icon, int paddingTop) {
            this.icon = icon;
            this.paddingTop = paddingTop;
            setVerticalTextPosition(SwingConstants.BOTTOM);
        }
        /**
         * This method allows irreversible changes. Making sure the components
         * are located exactly where they are.
         * @comments Note:
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

    public OrderList(String access_level, Dashboard dashboard, int employeeID) {
        this.dashboard = dashboard;
        employee_ID = employeeID;

        orderListFrame = new JFrame("Order List Panel");
        orderListFrame.setSize(1680, 720);
        orderListFrame.setLayout(null);
        orderListFrame.setLocationRelativeTo(null);
        orderListFrame.getContentPane().setBackground(Color.decode(bgColor));

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

        orderListFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        orderListFrame.setVisible(true);

        System.out.println("Order List Opened!");

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- Order List Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(orderListFrame,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    orderListFrame.dispose(); // Close the window if user confirms
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };

        orderListFrame.addWindowListener(windowAdapter);
        orderListFrame.addWindowFocusListener(windowAdapter);

        /**This is the left portion of the GUI. 
         * @Comment Note:
         */

        leftPanel = new JPanel();
        leftPanel.setBounds(5, 5, 830, 670);
        leftPanel.setLayout(null);
        //leftPanel.setBorder(BorderFactory.createTitledBorder("11"));
        leftPanel.setBackground(Color.decode(bgColor));

        orderListFrame.add(leftPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/ol_payment.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/ol_change.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/ol_delete.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/ol_details.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/ol_filter.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/back_exit.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("Add Payment");
        button2.setText("Modify Order");
        button3.setText("Remove Order");
        button4.setText("Transaction Details");
        button5.setText("More Filters");
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
                    System.out.println("Add Payment - Is Pressed");
                } else {
                    button1.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.addPaymentToTransaction();
                updateTransactionsTable();

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
                updateTransactionsTable();
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

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modifyTransaction()) { // Check if modification was successful
                    updateTransactionsTable();  // Update table only if modification succeeded
                    hideOrderList();            // Hide order list (assuming this hides unnecessary elements)
        
                    // Clear selection regardless of modification success
                    System.out.println("Clearing selected row.");
                    transaction_Table.clearSelection();
                    items_TableModel.setRowCount(0); // Clear existing data in the table model
                } else {
                    // Handle modification error (optional)
                    System.err.println("Error modifying transaction. Please check details.");
                    // You can display an error message to the user here
                }

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
                updateTransactionsTable();
            }
        });

        // BUTTON 3 MODEL with LISTENER
        button3.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button3.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Remove Order - Is Pressed");
                } else {
                    button3.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.removeTransaction();
                updateTransactionsTable();

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
                updateTransactionsTable();
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

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.seeFullDetailsTransaction();

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
                updateTransactionsTable();
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

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFilter();

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
                updateTransactionsTable();
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
                int choice = JOptionPane.showConfirmDialog(orderListFrame,
                        "Are you sure you want to close? Any unsaved changes will be lost.",
                        "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window.");
                    orderListFrame.dispose();
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
         * @Comment Note:
         */

        rightPanel = new JPanel();
        rightPanel.setBounds(830, 5, 830, 670);
        rightPanel.setLayout(null);
        //rightPanel.setBorder(BorderFactory.createTitledBorder("11"));
        rightPanel.setBackground(Color.decode(bgColor));

        orderListFrame.add(rightPanel);

        JLabel transaction_label = new JLabel("Transactions");
        transaction_label.setBounds(5, 5, 250, 35);
        rightPanel.add(transaction_label);

        // Create a table model for the first table
        transaction_TableModel = new DefaultTableModel();
        transaction_Table = new JTable(transaction_TableModel);
        transaction_Table.setDefaultEditor(Object.class, null);
        JScrollPane transaction_TableScroll = new JScrollPane(transaction_Table);
        transaction_TableScroll.setBounds(5, 40, 800, 290);
        rightPanel.add(transaction_TableScroll);

        transaction_TableModel.addColumn("TRANSACTION ID");
        transaction_TableModel.addColumn("TOTAL AMOUNT");
        transaction_TableModel.addColumn("AMOUNT PAID");
        transaction_TableModel.addColumn("STATUS");
        transaction_TableModel.addColumn("CUSTOMER");
        transaction_TableModel.addColumn("EMPLOYEE");

        // Label for 
        JLabel itemInventory_label = new JLabel("Transaction Items");
        itemInventory_label.setBounds(5, 335, 250, 35);
        rightPanel.add(itemInventory_label);

        // Create a table model for the second table
        items_TableModel = new DefaultTableModel();
        JTable items_Table = new JTable(items_TableModel);
        items_Table.setDefaultEditor(Object.class, null);
        JScrollPane items_TableScroll = new JScrollPane(items_Table);
        items_TableScroll.setBounds(5, 370, 800, 290);
        rightPanel.add(items_TableScroll);

        items_TableModel.addColumn("ID");
        items_TableModel.addColumn("NAME");
        items_TableModel.addColumn("PRICE");
        items_TableModel.addColumn("QUANTITY");
        items_TableModel.addColumn("SUB-TOTAL");

        // Add row selection listener to transaction table
        transaction_Table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && transaction_Table.getSelectedRow() != -1) {
                int selectedRow = transaction_Table.getSelectedRow();
                int transactionId = (int) transaction_TableModel.getValueAt(selectedRow, 0);
                ops.updateItemsTable(items_TableModel, transactionId);
            }
        });

        transactionNextButton = new JButton("Next");
        transactionNextButton.setBounds(680, 5, 120, 30);
        rightPanel.add(transactionNextButton);

        transactionNextButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    transactionNextButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Prev - Is Pressed");
                } else {
                    transactionNextButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        transactionPrevButton = new JButton("Prev");
        transactionPrevButton.setBounds(550, 5, 120, 30);
        rightPanel.add(transactionPrevButton);

        transactionPrevButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    transactionPrevButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Next - Is Pressed");
                } else {
                    transactionPrevButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        JButton transactionClearButton = new JButton("Clear");
        transactionClearButton.setBounds(680, 335, 120, 30);
        rightPanel.add(transactionClearButton);

        transactionClearButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    transactionClearButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Clear - Is Pressed");
                } else {
                    transactionClearButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        transactionNextButton.setBackground(Color.decode("#ff793f"));
        transactionPrevButton.setBackground(Color.decode("#ff793f"));
        transactionClearButton.setBackground(Color.decode("#ff793f"));

        // Add action listeners for pagination and clearing selection
        transactionNextButton.addActionListener(e -> nextPage());
        transactionPrevButton.addActionListener(e -> previousPage());
        transactionClearButton.addActionListener(e -> clearSelectedRow());
        resetFilterFile();
        updateTransactionsTable();
        
    } // end of New Order

    /**
     * Establishes Connection to the database
     */
    private Connection connect() throws SQLException {
        String url1 = "jdbc:mysql://localhost:3306/prj_tan";
        String url2 = "jdbc:mysql://localhost:3306/prj_yanez"; // <-- Put Your Database Name Here
        String username = "root";
        String password = ""; 

        try {
            return DriverManager.getConnection(url1, username, password);
        } catch (SQLException e1) {
            System.out.println("Failed to connect to prj_tan, attempting to connect to prj_yanez...");
            try {
                return DriverManager.getConnection(url2, username, password);
            } catch (SQLException e2) {
                System.err.println("Failed to connect to both prj_tan and prj_yanez.");
                e2.printStackTrace();
                throw e2;  // rethrow the last exception
            }
        }
    }

    /**
     * Checks if there are more rows available beyond the current page
     */
    public boolean hasMoreRows(int offset) {
        Map<String, String> filters = readFilterFile();
        String query = constructCountQuery(filters); // Using a count query for row count

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int rowCount = rs.getInt("rowcount");
                return rowCount > offset;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void nextPage() {
        int offset = (currentPage + 1) * PAGE_SIZE;
        System.out.println("Next page offset: " + offset);
        if (hasMoreRows(offset)) {
            System.out.println("Next page: More rows available, proceeding to next page.");
            currentPage++;
            updateTransactionsTable();
        } else {
            System.out.println("Next page: No more rows available.");
            JOptionPane.showMessageDialog(null, "No more pages to display.");
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            System.out.println("Previous page: Going back to previous page.");
            currentPage--;
        } else {
            System.out.println("Previous page: Already on the first page.");
        }
        updateTransactionsTable();
    }
    
    public void clearSelectedRow() {
        System.out.println("Clearing selected row.");
        transaction_Table.clearSelection();
        items_TableModel.setRowCount(0);
    }

    public void updateTransactionsTable() {
        int offset = currentPage * PAGE_SIZE;
        System.out.println("Current page: " + currentPage);
        System.out.println("Update table offset: " + offset);
        updateTransactionsTable(transaction_TableModel, offset);

        boolean hasNextPage = hasMoreRows(offset + PAGE_SIZE);
        System.out.println("Has next page: " + hasNextPage);
        transactionNextButton.setEnabled(hasNextPage);
        transactionPrevButton.setEnabled(currentPage > 0);
    }

    

    public void resetFilterFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("filter.txt"))) {
            writer.write("dateSelectRange.fromDate = " + LocalDate.now().minusDays(8) + "\n");
            writer.write("dateSelectRange.toDate = " + LocalDate.now().plusDays(1) + "\n");
            writer.write("orderStatus.showStatus = pending\n");
            writer.write("totalPriceRange.minPrice = 0.00\n");
            writer.write("totalPriceRange.maxPrice = 1000000.00\n");
            writer.write("customerName.searchQuery = \n");
            writer.write("cashierName.searchQuery = \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads filter values from filter.txt file
     */
    public Map<String, String> readFilterFile() {
        Map<String, String> filters = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("filter.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    filters.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filters;
    }

    /**
 * Updates the transactions table with recent data
 */
public void updateTransactionsTable(DefaultTableModel transaction_TableModel, int offset) {
    // Read filters from the filter file
    Map<String, String> filters = readFilterFile();
    
    // Construct the base query using filters
    String query = constructQuery(filters);
    
    // Add pagination to the query
    query += " ORDER BY transaction_Date DESC LIMIT " + PAGE_SIZE + " OFFSET " + offset + ";";
    
    // Log the query for debugging
    System.out.println("Executing query: " + query.toString());
    
    try (Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

        transaction_TableModel.setRowCount(0);  // Clear the table

        while (rs.next()) {
            int transactionId = rs.getInt("transaction_ID");
            double totalPrice = rs.getDouble("transaction_TotalPrice");
            double totalPaid = rs.getDouble("transaction_TotalPaid");
            String status = rs.getString("transaction_OrderStatus");
            String customer = rs.getString("customerName");
            String employee = rs.getString("employeeName");

            // Log the fetched data
            System.out.println("Fetched transaction: " + transactionId + ", " + totalPrice + ", " + totalPaid + ", " + status);

            transaction_TableModel.addRow(new Object[]{transactionId, totalPrice, totalPaid, status, customer, employee});
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    /**
     * Constructs SQL query based on filter values
     */
    private String constructQuery(Map<String, String> filters) {
        StringBuilder query = new StringBuilder("SELECT t.transaction_ID, t.transaction_TotalPrice, t.transaction_TotalPaid, t.transaction_OrderStatus, CONCAT(c.customer_FirstName, ' ', c.customer_LastName) AS customerName, CONCAT(e.employee_FirstName, ' ', e.employee_LastName) AS employeeName FROM tbl_transaction t LEFT JOIN tbl_customer c ON t.customer_ID = c.customer_ID LEFT JOIN tbl_employee e ON t.employee_ID = e.employee_ID WHERE t.transaction_ActiveStatus = 'active'");
    
        // Filtering based on date range
        String fromDate = filters.getOrDefault("dateSelectRange.fromDate", "2024-05-28");
        String toDate = filters.getOrDefault("dateSelectRange.toDate", "2024-06-04");
        query.append(" AND t.transaction_Date BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");
    
        // Filtering based on order status
        String orderStatus = filters.getOrDefault("orderStatus.showStatus", "pending");
        if (!orderStatus.isEmpty()) {
            query.append(" AND t.transaction_OrderStatus IN ('").append(orderStatus).append("')");
        }
    
        // Filtering based on total price range
        String minPrice = filters.getOrDefault("totalPriceRange.minPrice", "0.00");
        String maxPrice = filters.getOrDefault("totalPriceRange.maxPrice", "10000000.00");
        query.append(" AND t.transaction_TotalPrice BETWEEN ").append(minPrice).append(" AND ").append(maxPrice);
    
        // Filtering based on customer name
        String customerName = filters.getOrDefault("customerName.searchQuery", "");
        if (!customerName.isEmpty()) {
            query.append(" AND (c.customer_FirstName LIKE '%").append(customerName).append("%'");
            query.append(" OR c.customer_LastName LIKE '%").append(customerName).append("%')");
        }
    
        // Filtering based on cashier name
        String cashierName = filters.getOrDefault("cashierName.searchQuery", "");
        if (!cashierName.isEmpty()) {
            query.append(" AND (e.employee_FirstName LIKE '%").append(cashierName).append("%'");
            query.append(" OR e.employee_LastName LIKE '%").append(cashierName).append("%')");
        }
    
        return query.toString();
    }
    
    
    

    /**
     * Constructs a SQL count query based on filter values
     */
    private String constructCountQuery(Map<String, String> filters) {
        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT t.transaction_ID) AS rowcount FROM tbl_transaction t");

        query.append(" WHERE t.transaction_ActiveStatus = 'active'");

        String fromDate = filters.getOrDefault("dateSelectRange.fromDate", "2024-05-28");
        String toDate = filters.getOrDefault("dateSelectRange.toDate", "2024-06-04");
        query.append(" AND t.transaction_Date BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("'");

        String orderStatus = filters.getOrDefault("orderStatus.showStatus", "pending");
        if (!orderStatus.isEmpty()) {
            query.append(" AND t.transaction_OrderStatus = '").append(orderStatus).append("'");
        }

        String minPrice = filters.getOrDefault("totalPriceRange.minPrice", "0.00");
        String maxPrice = filters.getOrDefault("totalPriceRange.maxPrice", "10000000.00");
        query.append(" AND t.transaction_TotalPrice BETWEEN ").append(minPrice).append(" AND ").append(maxPrice);

        String customerName = filters.getOrDefault("customerName.searchQuery", "");
        if (!customerName.isEmpty()) {
            // Join with tbl_customer for customer name search
            query.append(" LEFT JOIN tbl_customer c ON t.customer_ID = c.customer_ID");
            query.append(" AND (c.customer_FirstName LIKE '%").append(customerName).append("%'");
            query.append(" OR c.customer_LastName LIKE '%").append(customerName).append("%')");
        }
    
        String cashierName = filters.getOrDefault("cashierName.searchQuery", "");
        if (!cashierName.isEmpty()) {
            // Join with tbl_employee for cashier name search
            query.append(" LEFT JOIN tbl_employee e ON t.employee_ID = e.employee_ID");
            query.append(" AND (e.employee_FirstName LIKE '%").append(cashierName).append("%'");
            query.append(" OR e.employee_LastName LIKE '%").append(cashierName).append("%')");
        }

        return query.toString();
    }


    public void selectFilter() {
        // Provide options for different filters
        String[] filterOptions = {"Date Select", "Order Status", "Total Price Range", "Customer Name", "Cashier Name", "Reset"};
        
        // Show dialog for selecting filter
        String selectedFilter = (String) JOptionPane.showInputDialog(null, 
                                "Select a filter:", "Filter Selection", 
                                JOptionPane.PLAIN_MESSAGE, null, 
                                filterOptions, filterOptions[0]);
        
        // Act according to the selected filter
        if (selectedFilter != null) {
            switch (selectedFilter) {
                case "Date Select":
                    selectDateFilter();
                    break;
                case "Order Status":
                    selectOrderStatusFilter();
                    break;
                case "Total Price Range":
                    selectTotalPriceRangeFilter();
                    break;
                case "Customer Name":
                    selectCustomerNameFilter();
                    break;
                case "Cashier Name":
                    selectCashierNameFilter();
                    break;
                case "reset":
                    resetFilterFile();
                    break;
                default:
                    break;
            }
        }
    }
    
    private void selectDateFilter() {
        // Display date picker dialogs to select the range
        LocalDate fromDate = null;
        LocalDate toDate = null;
    
        while (fromDate == null) {
            // Prompt the user to enter the "From Date"
            String fromDateString = JOptionPane.showInputDialog(null,
                    "Enter From Date (YYYY-MM-DD):", "Date Range Selection", JOptionPane.PLAIN_MESSAGE);
            if (fromDateString == null) {
                // User canceled, exit the method
                return;
            }
            try {
                fromDate = LocalDate.parse(fromDateString);
            } catch (DateTimeParseException e) {
                // Invalid date format, prompt the user again
                JOptionPane.showMessageDialog(null, "Invalid date format. Please enter the date in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        // Now prompt the user to enter the "To Date" only if the "From Date" is provided
        while (toDate == null) {
            String toDateString = JOptionPane.showInputDialog(null,
                    "Enter To Date (YYYY-MM-DD):", "Date Range Selection", JOptionPane.PLAIN_MESSAGE);
            if (toDateString == null) {
                // User canceled, exit the method
                return;
            }
            try {
                toDate = LocalDate.parse(toDateString);
            } catch (DateTimeParseException e) {
                // Invalid date format, prompt the user again
                JOptionPane.showMessageDialog(null, "Invalid date format. Please enter the date in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        // Check if the date range is valid
        if (fromDate != null && toDate != null && !fromDate.isBefore(toDate)) {
            JOptionPane.showMessageDialog(null, "To Date must be after From Date.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit if the date range is invalid
        }
    
        // Update the filter file with the selected date range
        Map<String, String> filters = readFilterFile();
        filters.put("dateSelectRange.fromDate", fromDate.toString());
        filters.put("dateSelectRange.toDate", toDate.toString());
        writeFilterFile(filters);
    }
    

    
    private void selectOrderStatusFilter() {
        // Display options for different order statuses
        String[] options = {"pending", "paid", "both"};
        String selectedStatus = (String) JOptionPane.showInputDialog(null,
                "Select Order Status:", "Order Status Selection",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    
        // Update the filter file with the selected order status
        Map<String, String> filters = readFilterFile();
        if (selectedStatus.equals("both")) {
            filters.put("orderStatus.showStatus", "pending', 'paid");
        } else {
            filters.put("orderStatus.showStatus", selectedStatus);
        }
        writeFilterFile(filters);
    }
    

    private void selectTotalPriceRangeFilter() {
        // Allow the user to input min and max values for the price range
        String minPriceStr = JOptionPane.showInputDialog(null,
                "Enter Minimum Price:", "Price Range Selection", JOptionPane.PLAIN_MESSAGE);
        String maxPriceStr = JOptionPane.showInputDialog(null,
                "Enter Maximum Price:", "Price Range Selection", JOptionPane.PLAIN_MESSAGE);

        // Convert input to double values
        double minPrice = Double.parseDouble(minPriceStr);
        double maxPrice = Double.parseDouble(maxPriceStr);

        // Update the filter file with the selected price range
        Map<String, String> filters = readFilterFile();
        filters.put("totalPriceRange.minPrice", String.valueOf(minPrice));
        filters.put("totalPriceRange.maxPrice", String.valueOf(maxPrice));
        writeFilterFile(filters);
    }

    private void selectCustomerNameFilter() {
        // Provide a text field for the user to input the customer name
        String customerName = JOptionPane.showInputDialog(null,
                "Enter Customer Name:", "Customer Name Selection", JOptionPane.PLAIN_MESSAGE);

        // Update the filter file with the selected customer name
        Map<String, String> filters = readFilterFile();
        filters.put("customerName.searchQuery", customerName != null ? customerName : "");
        writeFilterFile(filters);
    }

    private void selectCashierNameFilter() {
        // Provide a text field for the user to input the cashier name
        String cashierName = JOptionPane.showInputDialog(null,
                "Enter Cashier Name:", "Cashier Name Selection", JOptionPane.PLAIN_MESSAGE);

        // Update the filter file with the selected cashier name
        Map<String, String> filters = readFilterFile();
        filters.put("cashierName.searchQuery", cashierName != null ? cashierName : "");
        writeFilterFile(filters);
    }

    /**
     * Writes filter values to the filter file.
     */
    public void writeFilterFile(Map<String, String> filters) {
        // Read existing content from the file
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("filter.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                boolean updated = false;
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    if (line.startsWith(entry.getKey())) {
                        // Replace the line with updated value
                        content.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
                        updated = true;
                        break;
                    }
                }
                if (!updated) {
                    // If the line is not updated, keep it unchanged
                    content.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the modified content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("filter.txt"))) {
            writer.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes a specific filter value in the filter file.
     */
    public void changeFilter(String filterKey, String newValue) {
        Map<String, String> filters = readFilterFile();
        filters.put(filterKey, newValue);
        writeFilterFile(filters);
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

    public void hideOrderList() {
        orderListFrame.setVisible(false);
        System.out.println("Hiding OrderList!");
    }
    
    public void showOrderList() {
        orderListFrame.setVisible(true);
        toFront();
        System.out.println("Showing OrderList!");
        updateTransactionsTable();
    }

    public boolean modifyTransaction() {
        try {
            // Ask for transaction ID
            String transactionIdStr = JOptionPane.showInputDialog("Enter Transaction ID:");
            if (transactionIdStr == null) {
                return false; // User canceled the input dialog
            }
    
            int transactionId;
            try {
                transactionId = Integer.parseInt(transactionIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Transaction ID.");
                return false; // Invalid input
            }
    
            // Check if transaction can be modified (assuming ops.isTransactionActive and ops.isTransactionPending are boolean methods)
            if (ops.isTransactionActive(transactionId) && ops.isTransactionPending(transactionId)) {
                modifyOrder = new ModifyOrder(OrderList.this, transactionId, employee_ID);
                return true; // Modification initiated successfully
            } else {
                // Inform the user that the transaction cannot be modified
                JOptionPane.showMessageDialog(null, "Transaction cannot be modified.");
                return false; // Transaction not modifiable
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to modify transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // General error
        }
    }
}

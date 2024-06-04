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
import java.io.File;
import java.io.IOException;
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

    Operations ops = new Operations();
    private int currentPage = 0;

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

    public OrderList(String access_level, Dashboard dashboard) {
        this.dashboard = dashboard;

        JFrame orderListFrame = new JFrame("Order List Panel");
        orderListFrame.setSize(1680, 720);
        orderListFrame.setLayout(null);
        orderListFrame.setLocationRelativeTo(null);
        orderListFrame.getContentPane().setBackground(Color.decode(bgColor));

        try {
            // Load Rubik-Bold font
            Font rubikFont = loadFont("fonts/Rubik-Medium.ttf", 24f);

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
                ops.modifyTransaction();
                updateTransactionsTable();

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
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


                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
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
                ops.selectFilter();
                ops.updateTableFromFilterFile(transaction_TableModel);

                // CLEAR SELECTION
                System.out.println("Clearing selected row.");
                transaction_Table.clearSelection();
                items_TableModel.setRowCount(0);
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

        transactionPrevButton = new JButton("Prev");
        transactionPrevButton.setBounds(550, 5, 120, 30);
        rightPanel.add(transactionPrevButton);

        JButton transactionClearButton = new JButton("Clear");
        transactionClearButton.setBounds(680, 335, 120, 30);
        rightPanel.add(transactionClearButton);

        // Add action listeners for pagination and clearing selection
        transactionNextButton.addActionListener(e -> nextPage());
        transactionPrevButton.addActionListener(e -> previousPage());
        transactionClearButton.addActionListener(e -> clearSelectedRow());

        updateTransactionsTable();
        ops.resetFilterFile();
    } // end of New Order

    public void nextPage() {
        int offset = (currentPage + 1) * ops.PAGE_SIZE;
        System.out.println("Next page offset: " + offset); // Verify the calculated offset
        if (ops.hasMoreRows(offset)) {
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
        int offset = currentPage * ops.PAGE_SIZE;
        System.out.println("Current page: " + currentPage);
        System.out.println("Update table offset: " + offset); // Verify the offset for updating transactions table
        ops.updateTransactionsTable(transaction_TableModel, offset);
    
        // Enable or disable pagination buttons
        boolean hasNextPage = ops.hasMoreRows(offset + ops.PAGE_SIZE);
        System.out.println("Has next page: " + hasNextPage);
        transactionNextButton.setEnabled(hasNextPage);
        transactionPrevButton.setEnabled(currentPage > 0);
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
}

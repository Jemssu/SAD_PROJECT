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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ModifyOrder extends JFrame {
    String bgColor = "#f7f1e3";
    @SuppressWarnings("unused")
    private OrderList orderlist;
    JPanel rightPanel, leftPanel;
    JButton button1, button2, button3, button4, button5, button6;
    int transactionID;
    int currentTotalAmount = 0;
    JButton checkButton, searchProductButton, addButton;
    JLabel transaction_label;
    JLabel itemPriceLabel, itemLengthLabel, itemNameLabel, itemLeftLabel, orderTotalLabel;
    JTextField enterIdTextField;
    String checkedStockLeft = "", checkedType = "", checkedLength = "", checkedPrice = "";
    JTable transaction_Table;
    DefaultTableModel transaction_TableModel;
    int current_TransactionID;

    Operations ops = new Operations();

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

    public ModifyOrder(OrderList orderlist, int transactionID, int employeeID) {
        this.orderlist = orderlist;
        System.out.println(transactionID + " " + employeeID);
        current_TransactionID = transactionID;

        JFrame modifyOrderFrame = new JFrame("Modify Order Panel");
        modifyOrderFrame.setSize(1680, 720);
        modifyOrderFrame.setLayout(null);
        modifyOrderFrame.setLocationRelativeTo(null);
        modifyOrderFrame.getContentPane().setBackground(Color.decode(bgColor));

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

        modifyOrderFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        modifyOrderFrame.setVisible(true);

        System.out.println("New Order Opened!");

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- New Order Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(modifyOrderFrame,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    modifyOrderFrame.dispose(); // Close the window if user confirms
                    orderlist.showOrderList();
                    ops.updateNewTransaction(transactionID);
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };

        modifyOrderFrame.addWindowListener(windowAdapter);
        modifyOrderFrame.addWindowFocusListener(windowAdapter);

        /**This is the left portion of the GUI. 
         * @Comment Note:
         */

        leftPanel = new JPanel();
        leftPanel.setBounds(5, 5, 830, 670);
        leftPanel.setLayout(null);
        //leftPanel.setBorder(BorderFactory.createTitledBorder("11"));
        leftPanel.setBackground(Color.decode(bgColor));

        modifyOrderFrame.add(leftPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/no_new.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/no_cancel.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/no_confirm.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/no_modify.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/dash_list.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/back_exit.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("New Order");
        button2.setText("Cancel Order");
        button3.setText("Confirm Order");
        button4.setText("Edit Item");
        button5.setText("Order List");
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
                    System.out.println("New Order - Is Pressed");
                } else {
                    //button1.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        
        
        // BUTTON 2 MODEL with LISTENER
        button2.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button2.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Cancel Order - Is Pressed");
                } else {
                    //button2.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        // BUTTON 3 MODEL with LISTENER
        button3.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button3.setBackground(Color.decode("#ff793f")); // Change color when pressed
                    System.out.println("Button 3 - Is Pressed");
                } else {
                    //button3.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.updateNewTransaction(transactionID);

                ops.updateTransactionTotalBySQL(transactionID, orderTotalLabel);
                ops.updateTransactionTableBySQL(transactionID, transaction_TableModel);

                System.out.println("Closing the window.");
                    modifyOrderFrame.dispose();
                    orderlist.showOrderList();
            }
        });
        

        // BUTTON 4 MODEL with LISTENER
        button4.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    //button4.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 4 - Is Pressed");
                } else {
                    //button4.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.modifyTransaction(ops.getTransactionID(transaction_label), orderTotalLabel);
                ops.updateTransactionTotalBySQL(ops.getTransactionID(transaction_label), orderTotalLabel);

                ops.updateTransactionTotalBySQL(transactionID, orderTotalLabel);
                ops.updateTransactionTableBySQL(transactionID, transaction_TableModel);
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
                    //button5.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
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
                    //button6.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 6 MODEL with ACTION LISTENER (EXIT)
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(modifyOrderFrame,
                        "Are you sure you want to close? Any unsaved changes will be lost.",
                        "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window.");
                    modifyOrderFrame.dispose();
                    orderlist.showOrderList();
                    ops.updateNewTransaction(transactionID);
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

        modifyOrderFrame.add(rightPanel);

        transaction_label = new JLabel("Current Transaction: " + transactionID);
        transaction_label.setBounds(5, 5, 500, 35);
        rightPanel.add(transaction_label);

        // Create a table model for the first table
        transaction_TableModel = new DefaultTableModel();
        JTable transaction_Table = new JTable(transaction_TableModel);
        transaction_Table.setDefaultEditor(Object.class, null);

        JScrollPane transaction_TableScroll = new JScrollPane(transaction_Table);
        transaction_TableScroll.setBounds(5, 40, 800, 290);
        rightPanel.add(transaction_TableScroll);

        transaction_TableModel.addColumn("ID");
        transaction_TableModel.addColumn("NAME");
        transaction_TableModel.addColumn("QUANTITY");
        transaction_TableModel.addColumn("PRICE");
        transaction_TableModel.addColumn("SUBTOTAL");

        JLabel enterIdLabel = new JLabel("Product ID:");
        enterIdLabel.setBounds(5, 340, 245, 50);
        rightPanel.add(enterIdLabel);

        enterIdTextField = new JTextField();
        enterIdTextField.setBounds(5, 400, 245, 50);
        rightPanel.add(enterIdTextField);

        searchProductButton = new JButton("Product List");
        searchProductButton.setBounds(5, 460, 245, 50);
        searchProductButton.setBackground(Color.decode("#ff793f"));
        rightPanel.add(searchProductButton);

        searchProductButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    searchProductButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Product List - Is Pressed");
                } else {
                    searchProductButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        searchProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.seeProductList();
            }
        });   

        addButton = new JButton("Add");
        addButton.setBounds(130, 520, 120, 50);
        addButton.setBackground(Color.decode("#ff793f"));
        rightPanel.add(addButton);
        
        addButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    addButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Add Button - Is Pressed");
                } else {
                    addButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = enterIdTextField.getText().trim(); // Trim to remove leading and trailing whitespaces

                if (inputText.isEmpty()) {
                    // Handle empty input
                    JOptionPane.showMessageDialog(null, "Error: Product ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int productID = Integer.parseInt(inputText);
                    // Call the checkItemFromTransaction method from the Operations class
                    ops.checkItemFromTransaction(productID, itemNameLabel, itemLengthLabel, itemPriceLabel, itemLeftLabel);
                    ops.addItemToTransaction(productID, transaction_TableModel, ops.getTransactionID(transaction_label));

                    // Update the order total label
                    System.out.println("Before updating transaction total: Current Total Amount = " + currentTotalAmount);
                    ops.updateTransactionTotal(transaction_TableModel, currentTotalAmount, orderTotalLabel);
                    ops.updateTransactionTotalBySQL(ops.getTransactionID(transaction_label), orderTotalLabel);
                    ops.checkItemFromTransaction(productID, itemNameLabel, itemLengthLabel, itemPriceLabel, itemLeftLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Invalid product ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                ops.updateTransactionTotalBySQL(transactionID, orderTotalLabel);
                ops.updateTransactionTableBySQL(transactionID, transaction_TableModel);
            }
        });
        
        checkButton = new JButton("Check");
        checkButton.setBounds(5, 520, 120, 50);
        checkButton.setBackground(Color.decode("#ff793f"));
        rightPanel.add(checkButton);

        checkButton.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    checkButton.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Check Button - Is Pressed");
                } else {
                    checkButton.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int productID;
                try {
                    productID = Integer.parseInt(enterIdTextField.getText());
                } catch (NumberFormatException ex) {
                    // Handle the case where the text is not a valid integer
                    JOptionPane.showMessageDialog(null, "Error: Invalid product ID. checkButton", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Received product ID 1: " + productID);
                // Call the checkItemFromTransaction method from the Operations class
                ops.checkItemFromTransaction(productID, itemNameLabel, itemLengthLabel, itemPriceLabel, itemLeftLabel);
            }
        });
        
        orderTotalLabel = new JLabel("Total Amount: ₱0.00");
        orderTotalLabel.setBounds(5, 600, 500, 35);
        rightPanel.add(orderTotalLabel);

        itemNameLabel = new JLabel("" + checkedType);
        itemNameLabel.setBounds(270, 340, 250, 50);
        rightPanel.add(itemNameLabel);

        itemLengthLabel = new JLabel("" + checkedLength);
        itemLengthLabel.setBounds(270, 400, 250, 50);
        rightPanel.add(itemLengthLabel);

        itemPriceLabel = new JLabel("" + checkedPrice);
        itemPriceLabel.setBounds(270, 460, 250, 50);
        rightPanel.add(itemPriceLabel);

        itemLeftLabel = new JLabel("" + checkedStockLeft);
        itemLeftLabel.setBounds(270, 520, 250, 50);
        rightPanel.add(itemLeftLabel);

        yesTransaction();

        
        updateTransactionLabel(transactionID, transaction_label);

        ops.updateTransactionTotalBySQL(transactionID, orderTotalLabel);
        ops.updateTransactionTableBySQL(transactionID, transaction_TableModel);

    } // end of New Order

    public void updateTransactionLabel(int current_TransactionID, JLabel transactionLabel) {
        transactionLabel.setText("Current Transaction: " + current_TransactionID);
        System.out.println(current_TransactionID + "RECEIVED!");
    }

    public void noTransaction() {
        transaction_label.setText("Current Transaction: ");

        button1.setEnabled(false);
        button1.setBackground(Color.decode("#84817a"));

        button2.setEnabled(false);
        button2.setBackground(Color.decode("#84817a"));

        button3.setEnabled(false);
        button3.setBackground(Color.decode("#84817a"));

        button4.setEnabled(false);
        button4.setBackground(Color.decode("#84817a"));

        button5.setEnabled(false);
        button5.setBackground(Color.decode("#84817a"));

        addButton.setEnabled(false);
        addButton.setBackground(Color.decode("#84817a"));

        checkButton.setEnabled(false);
        checkButton.setBackground(Color.decode("#84817a"));

        button6.setEnabled(false);
        button6.setBackground(Color.decode("#84817a"));


        searchProductButton.setEnabled(false);
        searchProductButton.setBackground(Color.decode("#84817a"));

        enterIdTextField.setEnabled(false);

        clearOtherComponents();
    }

    public void yesTransaction() {
        button1.setEnabled(false);
        button1.setBackground(Color.decode("#84817a"));

        button2.setEnabled(false);
        button2.setBackground(Color.decode("#84817a"));

        button3.setEnabled(true);
        button3.setBackground(Color.decode("#ff793f"));

        button4.setEnabled(true);
        button4.setBackground(Color.decode("#ff793f"));

        button5.setEnabled(false);
        button5.setBackground(Color.decode("#84817a"));

        button6.setEnabled(false);
        button6.setBackground(Color.decode("#84817a"));

        addButton.setEnabled(true);
        addButton.setBackground(Color.decode("#ff793f"));

        checkButton.setEnabled(true);
        checkButton.setBackground(Color.decode("#ff793f"));

        searchProductButton.setEnabled(true);
        searchProductButton.setBackground(Color.decode("#ff793f"));

        enterIdTextField.setEnabled(true);

        clearOtherComponents();
    }

    public void clearOtherComponents() {
        enterIdTextField.setText("");
        itemPriceLabel.setText("");
        itemLeftLabel.setText("");
        itemLengthLabel.setText("");
        itemNameLabel.setText("");
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

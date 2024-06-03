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

public class NewOrder extends JFrame {
    String bgColor = "#f7f1e3";
    @SuppressWarnings("unused")
    private Dashboard dashboard;
    JPanel rightPanel, leftPanel;
    JButton button1, button2, button3, button4, button5, button6;
    String transactionID = "--- ---";
    String currentTotalAmount = "--- ---";
    JButton checkButton, searchProductButton, addButton;
    JLabel itemPriceLabel, itemLengthLabel, itemNameLabel, itemLeftLabel, orderTotalLabel;
    JTextField enterIdTextField;
    String checkedStockLeft, checkedType, checkedLength, checkedPrice;

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

    public NewOrder(String access_level, Dashboard dashboard) {
        this.dashboard = dashboard;

        JFrame newOrderFrame = new JFrame("New Order Panel");
        newOrderFrame.setSize(1680, 720);
        newOrderFrame.setLayout(null);
        newOrderFrame.setLocationRelativeTo(null);
        newOrderFrame.getContentPane().setBackground(Color.decode(bgColor));

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

        newOrderFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        newOrderFrame.setVisible(true);

        System.out.println("New Order Opened!");

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- New Order Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(newOrderFrame,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    newOrderFrame.dispose(); // Close the window if user confirms
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };

        newOrderFrame.addWindowListener(windowAdapter);
        newOrderFrame.addWindowFocusListener(windowAdapter);

        /**This is the left portion of the GUI. 
         * @Comment Note:
         */

        leftPanel = new JPanel();
        leftPanel.setBounds(5, 5, 830, 670);
        leftPanel.setLayout(null);
        //leftPanel.setBorder(BorderFactory.createTitledBorder("11"));
        leftPanel.setBackground(Color.decode(bgColor));

        newOrderFrame.add(leftPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/no_.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/no_.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/no_.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/no_.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/no_.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/back_exit.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("New Order");
        button2.setText("Cancel Order");
        button3.setText("Confirm Order");
        button4.setText("Edit Item");
        button5.setText("W.I.P.");
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
                    button6.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 6 MODEL with ACTION LISTENER (EXIT)
        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(newOrderFrame,
                        "Are you sure you want to close? Any unsaved changes will be lost.",
                        "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window.");
                    newOrderFrame.dispose();
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

        newOrderFrame.add(rightPanel);

        JLabel transaction_label = new JLabel("Current Transaction: " + transactionID );
        transaction_label.setBounds(5, 5, 500, 35);
        rightPanel.add(transaction_label);

        // Create a table model for the first table
        DefaultTableModel transaction_TableModel = new DefaultTableModel();
        JTable transaction_Table = new JTable(transaction_TableModel);

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
                ops.addItemToTransaction(Integer.parseInt(enterIdTextField.toString()));
                ops.updateCurrentTransactionTable();
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
                ops.checkItemFromTransaction(Integer.parseInt(enterIdTextField.getText()));
            }
        });

        orderTotalLabel = new JLabel("ORDER TOTAL : " + currentTotalAmount);
        orderTotalLabel.setBounds(5, 600, 500, 35);
        rightPanel.add(orderTotalLabel);

        itemNameLabel = new JLabel("TYPE: " + checkedType);
        itemNameLabel.setBounds(270, 340, 250, 50);
        rightPanel.add(itemNameLabel);

        itemLengthLabel = new JLabel("LENGTH: " + checkedLength);
        itemLengthLabel.setBounds(270, 400, 250, 50);
        rightPanel.add(itemLengthLabel);

        itemPriceLabel = new JLabel("PRICE: " + checkedPrice);
        itemPriceLabel.setBounds(270, 460, 250, 50);
        rightPanel.add(itemPriceLabel);

        itemLeftLabel = new JLabel("STOCK: " + checkedStockLeft);
        itemLeftLabel.setBounds(270, 520, 250, 50);
        rightPanel.add(itemLeftLabel);
    } // end of New Order

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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdminStuff extends JFrame{
    String bgColor = "#f7f1e3";
    @SuppressWarnings("unused")
    private Dashboard dashboard;
    JPanel rightPanel, leftPanel;
    JButton button1, button2, button3, button4, button5, button6;
    JButton button7, button8, button9, button12, button10, button11;

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
    public AdminStuff(String access_level, Dashboard dashboard, int adminID) {
        System.out.println("Opening Admin Panel with access level " + access_level);

        this.dashboard = dashboard;
        JFrame adminStuffFrame = new JFrame("Admin Panel");
        adminStuffFrame.setSize(1680, 720);
        adminStuffFrame.setLayout(null);
        adminStuffFrame.setLocationRelativeTo(null);
        adminStuffFrame.getContentPane().setBackground(Color.decode(bgColor));

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

        adminStuffFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        adminStuffFrame.setVisible(true);

        System.out.println("Admin Panel Opened!");

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- Admin Panel Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(adminStuffFrame,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    adminStuffFrame.dispose(); // Close the window if user confirms
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };

        adminStuffFrame.addWindowListener(windowAdapter);
        adminStuffFrame.addWindowFocusListener(windowAdapter);

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

        adminStuffFrame.add(leftPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/as_addtype.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/as_addsize.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/as_addsupplier.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/as_modtype.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/as_modsize.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/as_modsupplier.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("Add Type");
        button2.setText("Add Size");
        button3.setText("Add Supplier");
        button4.setText("Modify Type");
        button5.setText("Modify Size");
        button6.setText("Modify Supplier");

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
                    System.out.println("Add Type - Is Pressed");
                } else {
                    button1.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_addType();
            }
        });
        
        // BUTTON 2 MODEL with LISTENER
        button2.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button2.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Add Size - Is Pressed");

                    
                } else {
                    button2.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_addSize();
            }
        });

        // BUTTON 3 MODEL with LISTENER
        button3.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button3.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Add Supplier - Is Pressed");

                    
                } else {
                    button3.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_addSupplier();
            }
        });

        // BUTTON 4 MODEL with LISTENER
        button4.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button4.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Modify Type - Is Pressed");

                    
                } else {
                    button4.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_modifyType();
            }
        });

        // BUTTON 5 MODEL with LISTENER
        button5.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button5.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Modify Size - Is Pressed");

                    
                } else {
                    button5.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_modifySize();
            }
        });

        // BUTTON 6 MODEL with CHANGE LISTENER (EXIT)
        button6.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button6.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Modify Supplier - Is Pressed");

                    
                } else {
                    button6.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_modifySupplier();
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

        adminStuffFrame.add(rightPanel);
        
        // Create buttons with icons aligned on top and padding
        button7 = new ImageButton(resizeIcon("icons/as_inactorder.png", 100, 100), 50);
        button8 = new ImageButton(resizeIcon("icons/as_inactcust.png", 100, 100), 50);
        button9 = new ImageButton(resizeIcon("icons/as_inactproduct.png", 100, 100), 50);
        button10 = new ImageButton(resizeIcon("icons/as_addemployee.png", 100, 100), 50);
        button11 = new ImageButton(resizeIcon("icons/as_modemployee.png", 100, 100), 50);
        button12 = new ImageButton(resizeIcon("icons/back_exit.png", 100, 100), 50);

        // Set Text of Buttons
        button7.setText("Inactive Orders");
        button8.setText("Inactive Customers");
        button9.setText("Inactive Products");
        button10.setText("Add New Employee");
        button11.setText("Employee Details");
        button12.setText("Exit");

        // Adjust text height for buttons
        adjustButtonTextHeight(button7, 50);
        adjustButtonTextHeight(button8, 50);
        adjustButtonTextHeight(button9, 50);
        adjustButtonTextHeight(button10, 50);
        adjustButtonTextHeight(button11, 50);
        adjustButtonTextHeight(button12, 50);

        button7.setBackground(Color.decode("#ff793f"));
        button8.setBackground(Color.decode("#ff793f"));
        button9.setBackground(Color.decode("#ff793f"));
        button10.setBackground(Color.decode("#ff793f"));
        button11.setBackground(Color.decode("#ff793f"));
        button12.setBackground(Color.decode("#ff793f"));

        button7.setFocusPainted(false);
        button8.setFocusPainted(false);
        button9.setFocusPainted(false);
        button10.setFocusPainted(false);
        button11.setFocusPainted(false);
        button12.setFocusPainted(false);

        // Location of all the BUTTONS
        button7.setBounds(20, 24, 250, 300);
        button8.setBounds(290, 25, 250, 300);
        button9.setBounds(560, 25, 250, 300);
        button10.setBounds(20, 345, 250, 300);
        button11.setBounds(290, 345, 250, 300);
        button12.setBounds(560, 345, 250, 300);

        // BUTTON 7 MODEL with LISTENER
        button7.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button7.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Inactive Orders - Is Pressed");

                    
                } else {
                    button7.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_seeInactiveOrders();
            }
        });
        
        // BUTTON 8 MODEL with LISTENER
        button8.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button8.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Inactive Customers - Is Pressed");

                    
                } else {
                    button8.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_seeInactiveCustomers();
            }
        });

        // BUTTON 9 MODEL with LISTENER
        button9.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button9.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Inactive Products - Is Pressed");

                    
                } else {
                    button9.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_seeInactiveProducts();
            }
        });

        // BUTTON 10 MODEL with LISTENER
        button10.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button10.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Add New Employee - Is Pressed");

                    
                } else {
                    button10.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_addNewEmployee(adminID);
            }
        });

        // BUTTON 11 MODEL with LISTENER
        button11.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button11.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Modify Employee - Is Pressed");

                    
                } else {
                    button11.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        button11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ops.admin_modifyEmployee();
            }
        });

        // BUTTON 12 MODEL with CHANGE LISTENER (EXIT)
        button12.getModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                if (model.isPressed()) {
                    button12.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 12 - Is Pressed");
                } else {
                    button12.setBackground(Color.decode("#ff793f")); // Change color back when released
                }
            }
        });

        // BUTTON 12 MODEL with ACTION LISTENER (EXIT)
        button12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(adminStuffFrame,
                        "Are you sure you want to close? Any unsaved changes will be lost.",
                        "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice);
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window.");
                    adminStuffFrame.dispose();
                    dashboard.showMainDashboard();
                } else {
                    System.out.println("Window close canceled.");
                }
            }
        });

        // ADDING THE BUTTONS
        rightPanel.add(button7);
        rightPanel.add(button8);
        rightPanel.add(button9);
        rightPanel.add(button10);
        rightPanel.add(button11);
        rightPanel.add(button12);
        
    } // end of customerDetailsFrame

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

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Dashboard extends JFrame {
    private JFrame dashboardFrame;
    private JPanel topLeftPanel, bottomLeftPanel, rightPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel loginPanel, welcomePanel;
    private ImageButton button1, button2, button3, button4, button5, button6;    
    String bgColor = "#f7f1e3";
    String access_level = "";
    @SuppressWarnings("unused")
    private NewOrder newOrder;
    @SuppressWarnings("unused")
    private OrderList orderList;
    @SuppressWarnings("unused")
    private CustomerDetails customerDetails;
    @SuppressWarnings("unused")
    private GenerateReport generateReport;
    @SuppressWarnings("unused")
    private ManageInventory manageInventory;
    @SuppressWarnings("unused")
    private UpdateProducts updateProducts;
    

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

    /**
     * This class represents a clock panel that displays the current time. <br> <br>
     * It also updates the panel in real-time.
     * @comments Note:
     */
    private static class ClockPanel extends JPanel {
        private JLabel clockLabel;

        // Displays and Adds the ClockPanel
        public ClockPanel() {
            clockLabel = new JLabel();
            updateClock();
            add(clockLabel);

            Timer timer = new Timer(1000, e -> updateClock());
            timer.start();
        }
        // Updates the ClockPanel
        private void updateClock() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss a");
            String currentTime = dateFormat.format(new Date());
            clockLabel.setText(getGreeting() + ", Today is " + currentTime);
        }
        // Updates the Greeting Message
        private String getGreeting() {
            @SuppressWarnings("deprecation")
            int hour = new Date().getHours();
            if (hour >= 0 && hour < 12)
                return "Good Morning";
            else if (hour >= 12 && hour < 18)
                return "Good Afternoon";
            else
                return "Good Evening";
        }
    }

    /**
     * This constructor is responsible for displaying the top left portion of the software.  <br><br> It is also
     * responsible for displaying all the necessary buttons on the right hand side of the software.  <br><br>
     * By default all of these are disabled. Other methods such as Dashboard_Manager() will be
     * the one enabling the buttons and other functions.
     * @comments Note:
     */
    public Dashboard () {
        dashboardFrame = new JFrame("Dashboard Panel");
        dashboardFrame.setSize(1680, 720);
        dashboardFrame.setLayout(null);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setResizable(false);
        //dashboardFrame.setUndecorated(true);
        dashboardFrame.getContentPane().setBackground(Color.decode(bgColor));

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

        // Set font for JOptionPane message
        try {
            Font rubikFont = loadFont("fonts/Rubik-Medium.ttf", 16f);
            UIManager.put("OptionPane.messageFont", rubikFont);
            UIManager.put("OptionPane.buttonFont", rubikFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // Create two sub-panels within the leftPanel
        topLeftPanel = new JPanel();
        topLeftPanel.setLayout(null);
        topLeftPanel.setBounds(5, 5, 820, 285); // Half width of leftPanel
        //topLeftPanel.setBorder(BorderFactory.createTitledBorder(""));
        topLeftPanel.setBackground(Color.decode(bgColor));
        dashboardFrame.add(topLeftPanel);

        // Add image to the top left panel
        ImageIcon logoIcon = new ImageIcon("icons//team_header.png"); 
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(10, 10, 800, 240); 
        topLeftPanel.add(logoLabel);
        
        // Add clock to topLeftPanel
        ClockPanel clockPanel = new ClockPanel();
        clockPanel.setBounds(10, 230, 800, 40);
        clockPanel.setBackground(Color.decode(bgColor));
        topLeftPanel.add(clockPanel);

        bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(null);
        bottomLeftPanel.setBounds(5, 290, 820, 335); // Half width of leftPanel
        //bottomLeftPanel.setBorder(BorderFactory.createTitledBorder(""));
        bottomLeftPanel.setBackground(Color.decode(bgColor));
        dashboardFrame.add(bottomLeftPanel);

        /**
         *  THIS IS THE RIGHT PANEL / 6 BUTTON BOARD
         */

        rightPanel = new JPanel();
        rightPanel.setBounds(830, 5, 830, 670);
        rightPanel.setLayout(null);
        //rightPanel.setBorder(BorderFactory.createTitledBorder(""));
        rightPanel.setBackground(Color.decode(bgColor));

        dashboardFrame.add(rightPanel);

        // Create buttons with icons aligned on top and padding
        button1 = new ImageButton(resizeIcon("icons/dash_order.png", 100, 100), 50);
        button2 = new ImageButton(resizeIcon("icons/dash_list.png", 100, 100), 50);
        button3 = new ImageButton(resizeIcon("icons/dash_customer.png", 100, 100), 50);
        button4 = new ImageButton(resizeIcon("icons/dash_report.png", 100, 100), 50);
        button5 = new ImageButton(resizeIcon("icons/dash_inventory.png", 100, 100), 50);
        button6 = new ImageButton(resizeIcon("icons/dash_pricing.png", 100, 100), 50);

        // Set Text of Buttons
        button1.setText("New Order");
        button2.setText("Order List");
        button3.setText("Customer Details");
        button4.setText("Generate Report");
        button5.setText("Manage Inventory");
        button6.setText("Update Products");

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

        // BUTTON 1 MODEL with LISTENER (New Order)
        button1.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState = model.isPressed();

                if (currentPressedState && !previousPressedState) {
                    button1.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 1 - Is Pressed");

                    newOrder = new NewOrder(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState = currentPressedState;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button1.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState = currentPressedState;
                }
            }
        });
        
        // BUTTON 2 MODEL with LISTENER (Order List)
        button2.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState2 = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState2 = model.isPressed();

                if (currentPressedState2 && !previousPressedState2) {
                    button2.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 2 - Is Pressed");

                    orderList = new OrderList(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState2 = currentPressedState2;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button2.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState2 = currentPressedState2;
                }
            }
        });

        // BUTTON 3 MODEL with LISTENER (Customer Details)
        button3.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState3 = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState3 = model.isPressed();

                if (currentPressedState3 && !previousPressedState3) {
                    button3.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 3 - Is Pressed");

                    customerDetails = new CustomerDetails(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState3 = currentPressedState3;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button3.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState3 = currentPressedState3;
                }
            }
        });

        // BUTTON 4 MODEL with LISTENER (Generate Report)
        button4.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState4 = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState4 = model.isPressed();

                if (currentPressedState4 && !previousPressedState4) {
                    button4.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 4 - Is Pressed");

                    generateReport = new GenerateReport(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState4 = currentPressedState4;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button4.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState4 = currentPressedState4;
                }
            }
        });

        // BUTTON 5 MODEL with LISTENER (Manage Inventory)
        button5.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState5 = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState5 = model.isPressed();

                if (currentPressedState5 && !previousPressedState5) {
                    button5.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 5 - Is Pressed");

                    manageInventory = new ManageInventory(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState5 = currentPressedState5;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button5.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState5 = currentPressedState5;
                }
            }
        });

        // BUTTON 6 MODEL with LISTENER (Update Pricing)
        button6.getModel().addChangeListener(new ChangeListener() {
            private boolean previousPressedState6 = false;

            @Override
            public void stateChanged(ChangeEvent e) {
                ButtonModel model = (ButtonModel) e.getSource();
                boolean currentPressedState6 = model.isPressed();

                if (currentPressedState6 && !previousPressedState6) {
                    button6.setBackground(Color.decode("#33d9b2")); // Change color when pressed
                    System.out.println("Button 5 - Is Pressed");

                    updateProducts = new UpdateProducts(access_level, Dashboard.this);
                    // Update the previousPressedState and Disables the Button
                    previousPressedState6 = currentPressedState6;
                    // Freeze the dashboardFrame until newOrder is closed
                    hideMainDashboard();
                } else {
                    button6.setBackground(Color.decode("#ff793f")); // Change color back when released
                    previousPressedState6 = currentPressedState6;
                }
            }
        });

        // ADDING THE BUTTONS
        rightPanel.add(button1);
        rightPanel.add(button2);
        rightPanel.add(button3);
        rightPanel.add(button4);
        rightPanel.add(button5);
        rightPanel.add(button6);

        // Set up nobody
        showDashboard_Nobody();
        // Set up login form
        showDashboard_Login_Form(bottomLeftPanel);

        dashboardFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dashboardFrame.setVisible(true);

        WindowAdapter windowAdapter = new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println("Focus Lost -- Main Dashboard Frame");
            }
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(Dashboard.this,
                    "Are you sure you want to close? Any unsaved changes will be lost.",
                    "Confirm Close", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                System.out.println("Choice: " + choice); // Log the choice made by the user
                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Closing the window."); // Log that the window is being closed
                    dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    dispose(); // Close the window if user confirms
                } else {
                    System.out.println("Window close canceled."); // Log that the window close was canceled
                }
            }
        };
        dashboardFrame.addWindowListener(windowAdapter);
        dashboardFrame.addWindowFocusListener(windowAdapter);

    } // end of Dashboard

    /**
     * This method is responsible for handling the logging in process of the system.
     * This checks if the userID entered matches with the Database. It also checks
     * if whether the entered password matches with the one inside the Database.
     * @comments Note:
     */
    public void showDashboard_Login_Form(JPanel parentPanel) {
        // Adding username and password fields
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(15, 15, 820, 335); // Half width of leftPanel
        //loginPanel.setBorder(BorderFactory.createTitledBorder(""));
        loginPanel.setBackground(Color.decode(bgColor));

        JLabel loginPanelLabel = new JLabel("System Login");
        loginPanelLabel.setBounds(325, 10, 170, 40);
        loginPanel.add(loginPanelLabel);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        usernameLabel.setBounds(250, 50, 150, 40);
        passwordLabel.setBounds(250, 100, 150, 40);
        usernameField.setBounds(400, 50, 200, 40);
        passwordField.setBounds(400, 100, 200, 40);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        // Adding login button
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);

        loginButton.setBounds(325, 150, 170, 40);
        bottomLeftPanel.add(loginPanel);


        // ACTION LISTENERS
        ButtonModel buttonModel = loginButton.getModel();
        buttonModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (buttonModel.isPressed()) {
                    int username = Integer.parseInt(usernameField.getText());
                    String password = new String(passwordField.getPassword());
                    // Check username and password validity
                    if (isValidUsername(username) && isValidPassword(password)) {
                        System.out.println("Login Button: Valid, Checking Power Level...");
                        if (checkUserPower(username) == "admin") { 
                            showDashboard_Admin();
                            showDashboard_Login_Welcome(bottomLeftPanel);
                            hideDashboard_Login_Form();
                        } else if (checkUserPower(username) == "manager") { 
                            showDashboard_Manager();
                            showDashboard_Login_Welcome(bottomLeftPanel);
                            hideDashboard_Login_Form();
                        } else if (checkUserPower(username) == "employee") { 
                            showDashboard_Employee();
                            showDashboard_Login_Welcome(bottomLeftPanel);
                            hideDashboard_Login_Form();
                        } else if (checkUserPower(username) == "ERROR") {
                            showDashboard_Nobody();
                        } else {
                            showDashboard_Nobody();
                        }
                    } else {
                        System.out.println("Login Button: Error, Incorrect username or password.");
                        showDashboard_Nobody();
                    }
                }
            }
        });
    } // end of Dashboard_Login_Form

    // Method to check validity of username
    private boolean isValidUsername(int username) {
        return username >= 100000 && username <= 999999;
        // check database if ID exists
    }
    
    // Method to check validity of password
    private boolean isValidPassword(String password) {
        return password.length() <= 16;
        // check database if password exists
    }
    
    /**
     * Codes divisible by 3: 102, 303, 405, 606, 909
     * Codes with digits summing up to 7: 106, 205, 304, 403, 502
     * Codes not meeting the above conditions: 104, 212, 302, 410, 500
     */

    public String checkUserPower(int username) {
        @SuppressWarnings("unused")
        String result = "";
        int firstThreeDigits = username / 1000;
        if (firstThreeDigits % 3 == 0) {
            // Admin
            System.out.println("Power Level Checked: ADMIN");
            access_level = "admin";
            return result = "admin";
        } else if (sumOfDigits(firstThreeDigits) == 7) {
            // Manager
            System.out.println("Power Level Checked: MANAGER");
            access_level = "manager";
            return result = "manager";
        } else if (sumOfDigits(firstThreeDigits) == 5){
            // Employee
            System.out.println("Power Level Checked: EMPLOYEE");
            access_level = "employee";
            return result = "employee";
        } else {
            System.out.println("Power Level Checked: ERROR");
            access_level = "";
            return result = "ERROR";
        }
    }
    
    // Method to calculate the sum of digits
    private int sumOfDigits(int number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }
    
    public void hideDashboard_Login_Form() {
        // Remove the login panel from the bottomLeftPanel
        bottomLeftPanel.remove(loginPanel);
        bottomLeftPanel.revalidate(); // Revalidate the panel to reflect changes
        bottomLeftPanel.repaint(); // Repaint the panel to update the UI
        bottomLeftPanel.validate(); // Validate the panel after removing components
    } // end of hideDashboard_Login_Form

    public void showDashboard_Login_Welcome(JPanel parentPanel) {
        // Adding username and password fields
        welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBounds(15, 15, 820, 335); // Half width of leftPanel
        //loginPanel.setBorder(BorderFactory.createTitledBorder(""));
        welcomePanel.setBackground(Color.decode(bgColor));

        JLabel welcomePanelLabel = new JLabel("Welcome *User*");
        welcomePanelLabel.setBounds(325, 10, 200, 40);
        welcomePanel.add(welcomePanelLabel);

        JLabel roleLabel = new JLabel("Temporary Text Here: " + access_level);
        roleLabel.setBounds(200, 50, 500, 40);
        welcomePanel.add(roleLabel);

        // Adding logout button
        JButton logoutButton = new JButton("Log Out");
        welcomePanel.add(logoutButton);

        logoutButton.setBounds(325, 150, 170, 40);
        bottomLeftPanel.add(welcomePanel);

        // ACTION LISTENERS
        ButtonModel buttonModel = logoutButton.getModel();
        buttonModel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (buttonModel.isPressed()) {
                    hideDashboard_Welcome_Form();
                    showDashboard_Login_Form(bottomLeftPanel);
                    showDashboard_Nobody();
                    access_level = "";
                }
            }
        });
    } // end of Dashboard_Login_Welcome

    public void hideDashboard_Welcome_Form() {
        bottomLeftPanel.remove(welcomePanel);
        bottomLeftPanel.revalidate(); // Revalidate the panel to reflect changes
        bottomLeftPanel.repaint(); // Repaint the panel to update the UI
        bottomLeftPanel.validate(); // Validate the panel after removing components
    } // end of hideDashboard_Welcome_Form

    /**
     * This method enables all the Button Components.
     * @comments Note:
     */
    public void enableAllDashButtons() { 
        Component[] components = rightPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ImageButton) {
                ImageButton button = (ImageButton) component;
                button.setEnabled(true);
                button.setBackground(Color.decode("#ff793f"));
            }
        }
    } // end of enableAllDashButtons

    /**
     * This method disables all the Button Components.
     * @comments Note:
     */
    public void showDashboard_Nobody() {
        Component[] components = rightPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ImageButton) {
                ImageButton button = (ImageButton) component;
                button.setEnabled(false);
                button.setBackground(Color.decode("#84817a"));
            }
        }
    } // end of showDashboard_Nobody
    
    /**
     * This method is responsible for showing ADMIN
     * access.
     * @comments Note:
     */
    public void showDashboard_Admin() {
        enableAllDashButtons();
    } // end of showDashboard_Admin

    /**
     * This method is responsible for showing MANAGER 
     * access.
     * @comments Note:
     */
    public void showDashboard_Manager() {
        enableAllDashButtons();
        button6.setEnabled(false);
        button6.setBackground(Color.decode("#84817a"));
    } // end of show Dashboard_Manager
    
    /**
     * This method is responsible for showing EMPLOYEE 
     * access.
     * @comments Note:
     */
    public void showDashboard_Employee() {
        enableAllDashButtons();
        button3.setEnabled(false);
        button3.setBackground(Color.decode("#84817a"));
        button5.setEnabled(false);
        button5.setBackground(Color.decode("#84817a"));
        button6.setEnabled(false);
        button6.setBackground(Color.decode("#84817a"));
    } // end of show showDashboard_Employee

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

    public void hideMainDashboard() {
        dashboardFrame.setVisible(false);
        System.out.println("Hiding Main Dashboard!");
    }
    
    public void showMainDashboard() {
        dashboardFrame.setVisible(true);
        toFront(); // Bring DashboardUI frame to front
        System.out.println("Showing Main DasDashboard!");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("unused")
                Dashboard dashboard = new Dashboard();
            }
        });
    }

} // end of class Dashboard
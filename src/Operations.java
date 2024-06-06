
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Operations {

    int PAGE_SIZE = 16;

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

    //----------------------------------------------------------------
    /** 
     * Dashboard Methods
    */

    /**
     * Checks if Username Exists in tbl_login
     */
    public boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) FROM tbl_login WHERE login_Username = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if Password matches the username
     */
    public boolean doesPasswordMatchWithUsername(String username, String password) {
        String query = "SELECT COUNT(*) FROM tbl_login WHERE login_Username = ? AND login_Password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Get the Login ID
     */
    public int getLoginID(String username, String password) {
        String query = "SELECT login_ID FROM tbl_login WHERE login_Username = ? AND login_Password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("login_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found or an error occurs
    }

    /**
     * Gets the Access level based on ID
     */
    public String getAccessLevel(int login_ID) {
        String query = "SELECT login_AccessLevel FROM tbl_login WHERE login_ID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, login_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("login_AccessLevel");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found or an error occurs
    }
    
    /**
     * Gets the name of the User
     */
    public String getFullName(int login_ID) {
        String queryEmployee = "SELECT employee_FirstName, employee_LastName FROM tbl_employee WHERE employee_ID = ?";
        String queryAdmin = "SELECT admin_FirstName, admin_LastName FROM tbl_admin WHERE admin_ID = ?";
        try (Connection conn = connect()) {
            // Check if the ID matches an employee
            try (PreparedStatement pstmtEmployee = conn.prepareStatement(queryEmployee)) {
                pstmtEmployee.setInt(1, login_ID);
                try (ResultSet rsEmployee = pstmtEmployee.executeQuery()) {
                    if (rsEmployee.next()) {
                        String firstName = rsEmployee.getString("employee_FirstName");
                        String lastName = rsEmployee.getString("employee_LastName");
                        return firstName + " " + lastName;
                    }
                }
            }
            // Check if the ID matches an admin
            try (PreparedStatement pstmtAdmin = conn.prepareStatement(queryAdmin)) {
                pstmtAdmin.setInt(1, login_ID);
                try (ResultSet rsAdmin = pstmtAdmin.executeQuery()) {
                    if (rsAdmin.next()) {
                        String firstName = rsAdmin.getString("admin_FirstName");
                        String lastName = rsAdmin.getString("admin_LastName");
                        return firstName + " " + lastName;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Couldn't Be Found ERROR"; // Return this if nothing matches or an error occurs
    }

    //----------------------------------------------------------------
    /** 
     * CUSTOMER MANAGEMENT METHODS
    */

    /**DESCRIPTION
     * 
     */
    public void addCustomer() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void removeCustomer() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void updateCustomerFirstName() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void updateCustomerLastName() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void updateCustomerContactNumber() { 
        // 
    }

    //----------------------------------------------------------------
    /** 
     * UPDATE PRODUCT DETAILS METHODS
    */

    /**
     * Checks if a product with the given product_ID exists in the tbl_product table
     * 
     * @param product_ID The ID of the product to check
     * @return true if the product exists, false otherwise
     */
    public boolean doesProductExist(int product_ID) {
        String query = "SELECT COUNT(*) FROM tbl_product WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, product_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean isProductActive(int product_ID) {
        String query = "SELECT COUNT(*) FROM tbl_product WHERE product_ID = ? AND product_ActiveStatus = 'active'";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, product_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }


    /**
     * Checks if a product with the given type_ID and size_ID already exists in the tbl_product table.
     * 
     * @param type_ID The ID of the product type
     * @param size_ID The ID of the product size
     * @return true if the product already exists, false otherwise
     */
    public boolean doesProductTypeSizeExist(int type_ID, int size_ID) { 
        String query = "SELECT COUNT(*) FROM tbl_product WHERE type_ID = ? AND size_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, type_ID);
            pstmt.setInt(2, size_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Checks if a supplier with the given supplier_ID exists in the tbl_supplier table.
     *
     * @param supplier_ID The ID of the supplier to check.
     * @return true if the supplier exists, false otherwise.
     */
    public boolean doesSupplierExist(int supplier_ID) {
        String query = "SELECT COUNT(*) FROM tbl_supplier WHERE supplier_ID = ?";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, supplier_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets the full product name by concatenating type_Name from tbl_type and size_length from tbl_size.
     *
     * @param type_ID The ID of the product type.
     * @param size_ID The ID of the product size.
     * @return The full product name in the format "TYPE_NAME SIZE_LENGTH".
     */
    public String getProductFullName(int type_ID, int size_ID) {
        String typeName = null;
        String sizeLength = null;

        String typeQuery = "SELECT type_Name FROM tbl_type WHERE type_ID = ?";
        String sizeQuery = "SELECT size_length FROM tbl_size WHERE size_ID = ?";

        try (Connection conn = connect();
            PreparedStatement typeStmt = conn.prepareStatement(typeQuery);
            PreparedStatement sizeStmt = conn.prepareStatement(sizeQuery)) {

            typeStmt.setInt(1, type_ID);
            sizeStmt.setInt(1, size_ID);

            try (ResultSet typeRs = typeStmt.executeQuery();
                ResultSet sizeRs = sizeStmt.executeQuery()) {

                if (typeRs.next()) {
                    typeName = typeRs.getString("type_Name");
                }

                if (sizeRs.next()) {
                    sizeLength = sizeRs.getString("size_length");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (typeName != null && sizeLength != null) {
            return typeName + " " + sizeLength;
        } else {
            return null; // or handle this case as needed
        }
    }

    /**
     * Adds a new product to the tbl_product table.
     */
    public void addProduct() {
        try (Connection conn = connect()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Select Type_ID and Type_Name
            int type_ID = selectTypeID(conn);
            if (type_ID == -1) return; // User cancelled

            // Select Size_ID and Size_Length
            int size_ID = selectSizeID(conn);
            if (size_ID == -1) return; // User cancelled

            if (doesProductTypeSizeExist(type_ID, size_ID)) {
                JOptionPane.showMessageDialog(null, "Product with Type and Size already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }; 

            // Get the full product name
            String defaultProductName = getProductFullName(type_ID, size_ID);
            if (defaultProductName == null) {
                JOptionPane.showMessageDialog(null, "Failed to generate product full name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Input Product Name with default value
            String productName = (String) JOptionPane.showInputDialog(
                null,
                "Enter the product name:",
                "Add Product",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                defaultProductName
            );

            if (productName == null || productName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Input Product Price
            double productPrice = getValidDouble("Enter the product price:", "Add Product");

            if (productPrice <= 0) return; // User cancelled or invalid input

            // Input Supplier ID
            int supplier_ID = getValidInt("Enter the supplier ID:", "Add Product");
            if (!doesSupplierExist(supplier_ID)) {
                JOptionPane.showMessageDialog(null, "This Supplier Doesn't Exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }; 

            // Input Stock Amount
            int stockAmount = getValidInt("Enter the stock amount:", "Add Product");
            if (stockAmount < 0) return; // User cancelled or invalid input

            // Insert the new product into the database
            String insertQuery = "INSERT INTO tbl_product (type_ID, size_ID, product_Name, product_Price, last_Supplied_BY, product_StockLeft, product_ActiveStatus) VALUES (?, ?, ?, ?, ?, ?, 'active')";

            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setInt(1, type_ID);
                pstmt.setInt(2, size_ID);
                pstmt.setString(3, productName.trim());
                pstmt.setDouble(4, productPrice);
                pstmt.setInt(5, supplier_ID);
                pstmt.setInt(6, stockAmount);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Product added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add the product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while adding the product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int selectTypeID(Connection conn) throws SQLException {
        String query = "SELECT type_ID, type_Name FROM tbl_type";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            DefaultTableModel model = new DefaultTableModel(new String[]{"Type ID", "Type Name"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("type_ID"), rs.getString("type_Name")});
            }
            return showTableDialog(model, "Select Type");
        }
    }

    private int selectSizeID(Connection conn) throws SQLException {
        String query = "SELECT size_ID, size_length FROM tbl_size";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            DefaultTableModel model = new DefaultTableModel(new String[]{"Size ID", "Size Length"}, 0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("size_ID"), rs.getString("size_length")});
            }
            return showTableDialog(model, "Select Size");
        }
    }

    private int showTableDialog(DefaultTableModel model, String title) {
        JTable table = new JTable(model);
        table.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(table);
        int result = JOptionPane.showConfirmDialog(null, scrollPane, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION && table.getSelectedRow() != -1) {
            return (int) table.getValueAt(table.getSelectedRow(), 0);
        }
        return -1; // User cancelled or no selection
    }

    private int getValidInt(String message, String title) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) return -1; // User cancelled
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double getValidDouble(String message, String title) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) return -1; // User cancelled
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Removes a product by setting its product_ActiveStatus to 'inactive'
     */
    public void removeProduct() {
        // Ask for ID
        String input = JOptionPane.showInputDialog(null, "Enter the Product ID to remove:", "Remove Product", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) {
            return; // User cancelled or didn't enter an ID
        }

        // Checks if product ID is valid / Integer
        int product_ID;
        try {
            product_ID = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Product ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } 
        

        // Check If Product ID does exist
        if (!doesProductExist(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if product is inactive
        if (!isProductActive(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show Confirmation
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove the product with ID " + product_ID + "?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return; // User chose not to proceed
        }

        // Update product_ActiveStatus to 'inactive'
        String updateQuery = "UPDATE tbl_product SET product_ActiveStatus = 'inactive' WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            
            pstmt.setInt(1, product_ID);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " has been set to inactive.", "Product Removed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update the product status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating the product status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Renames the product
     */
    public void renameProduct() { 
        // Ask for ID
        String input = JOptionPane.showInputDialog(null, "Enter the Product ID to rename:", "Rename Product", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) {
            return; // User cancelled or didn't enter an ID
        }

        // Checks if product ID is valid / Integer
        int product_ID;
        try {
            product_ID = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Product ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } 

        // Check If Product ID does exist
        if (!doesProductExist(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if product is inactive
        if (!isProductActive(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ask for new product name
        String newName = JOptionPane.showInputDialog(null, "Enter the new name for " + getCurrentProductName(product_ID) + ":", "Rename Product", JOptionPane.QUESTION_MESSAGE);
        if (newName == null || newName.trim().isEmpty()) {
            return; // User cancelled or didn't enter a name
        }

        // Update product name
        String updateQuery = "UPDATE tbl_product SET product_Name = ? WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            
            pstmt.setString(1, newName.trim());
            pstmt.setInt(2, product_ID);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Product name has been updated successfully.", "Product Renamed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update the product name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating the product name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves the current name of the product with the given ID.
     * 
     * @param product_ID The ID of the product
     * @return The current name of the product
     */
    private String getCurrentProductName(int product_ID) {
        String currentName = null;
        String query = "SELECT product_Name FROM tbl_product WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, product_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentName = rs.getString("product_Name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return currentName;
    }

    /**
     * Changes the price of a product
     */
    public void changePriceOfProduct() {
        // Ask for ID
        String input = JOptionPane.showInputDialog(null, "Enter the Product ID to change the price:", "Change Product Price", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) {
            return; // User cancelled or didn't enter an ID
        }

        int product_ID;
        try {
            product_ID = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Product ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if product exists
        if (!doesProductExist(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if product is inactive
        if (!isProductActive(product_ID)) {
            JOptionPane.showMessageDialog(null, "Product with ID " + product_ID + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get current price
        double currentPrice = 0;
        String selectQuery = "SELECT product_Price FROM tbl_product WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            
            pstmt.setInt(1, product_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentPrice = rs.getDouble("product_Price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving the product price.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show current price and ask for new price
        String newPriceStr = JOptionPane.showInputDialog(null, getCurrentProductName(product_ID) + " Current price: " + currentPrice + "\nEnter the new price:", "Change Product Price", JOptionPane.QUESTION_MESSAGE);
        if (newPriceStr == null || newPriceStr.trim().isEmpty()) {
            return; // User cancelled or didn't enter a new price
        }

        double newPrice;
        try {
            newPrice = Double.parseDouble(newPriceStr.trim());
            if (newPrice <= 0) {
                JOptionPane.showMessageDialog(null, "Price must be a positive value.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Price", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update product price
        String updateQuery = "UPDATE tbl_product SET product_Price = ? WHERE product_ID = ?";
        
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, product_ID);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Price of product with ID " + product_ID + " has been updated to " + newPrice + ".", "Price Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update the product price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating the product price.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update the table with the initial data
    public void updateProductTable(DefaultTableModel tableModel) {
        String query = "SELECT product_ID, product_Name, product_Price FROM tbl_product WHERE product_ActiveStatus = 'active'";
        updateTableWithQuery(tableModel, query);
    }

    // Method to perform search and update the table
    public void searchAndUpdateTable(DefaultTableModel tableModel, String searchQuery) {
        if (searchQuery.isEmpty()) {
            updateProductTable(tableModel);
        } else {
            String query = "SELECT product_ID, product_Name, product_Price FROM tbl_product " +
                        "WHERE product_ActiveStatus = 'active' AND " +
                        "(product_ID LIKE ? OR product_Name LIKE ? OR product_Price LIKE ?)";
            String searchPattern = "%" + searchQuery + "%";
            updateTableWithQuery(tableModel, query, searchPattern, searchPattern, searchPattern);
        }
    }

    // Method to update the table model with data from the database
    private void updateTableWithQuery(DefaultTableModel tableModel, String query, String... params) {
        tableModel.setRowCount(0); // Clear existing rows

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setString(i + 1, params[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int productID = rs.getInt("product_ID");
                    String productName = rs.getString("product_Name");
                    double productPrice = rs.getDouble("product_Price");
                    tableModel.addRow(new Object[]{productID, productName, productPrice});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void saveProductTableModelToExcel() {
        String query = "SELECT * FROM tbl_product";
    
        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
    
            // Create a new workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Products");
    
            // Get metadata to create headers
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Create the header row
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnLabel(i));
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }
    
            // Populate data rows
            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(rs.getString(i));
                }
            }
    
            // Resize all columns to fit the content size
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }
    
            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream("Products.xlsx")) {
                workbook.write(fileOut);
            }
    
            // Closing the workbook
            workbook.close();
    
            System.out.println("Data exported to Excel successfully.");
    
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    
    
    //----------------------------------------------------------------
    /** 
     * MANAGE INVENTORY METHODS
    */
    public void updateLargeInvTable(DefaultTableModel largeInventoryTableModel) {
        try (Connection connection = connect()) {
            String query = "SELECT product_ID, product_Name, product_StockLeft, product_LastStocked FROM tbl_product WHERE product_ActiveStatus = 'active'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing rows
            largeInventoryTableModel.setRowCount(0);

            // Add rows from the fetched data
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("product_ID"),
                    resultSet.getString("product_Name"),
                    resultSet.getInt("product_StockLeft"),
                    resultSet.getTimestamp("product_LastStocked")
                };
                largeInventoryTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSmallInvTable(DefaultTableModel smallInventoryTableModel) {
        try (Connection connection = connect()) {
            String query = "SELECT product_ID, product_Name, product_StockLeft, product_SupplierStatus " +
                        "FROM tbl_product " +
                        "WHERE product_ActiveStatus = 'active' AND " +
                        "(product_SupplierStatus = 'pending' OR product_StockLeft < 100)";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
    
            // Clear existing rows
            smallInventoryTableModel.setRowCount(0);
    
            // Add rows from the fetched data
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("product_ID"),
                    resultSet.getString("product_Name"),
                    resultSet.getInt("product_StockLeft"),
                    resultSet.getString("product_SupplierStatus")
                };
                smallInventoryTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void filterLargeInvTable(DefaultTableModel largeInventoryTableModel, int sortOption) {
        // Get the data vector from the table model
        Vector<Vector<Object>> dataVector = new Vector<>(largeInventoryTableModel.getDataVector());
        
        // Sort based on the selected option
        switch (sortOption) {

            case 1: // Sort by product ID
                Collections.sort(dataVector, Comparator.comparing(row -> (Integer) row.get(0))); // Assuming product ID is in the first column
                break;
            case 2: // Sort by name (A - Z)
                Collections.sort(dataVector, Comparator.comparing(row -> (String) row.get(1))); // Assuming product name is in the second column
                break;
            case 3: // Sort by stock left
                Collections.sort(dataVector, Comparator.comparing(row -> (Integer) row.get(2))); // Assuming stock left is in the third column
                break;
            default:
                // Invalid option, do nothing
                return;
        }
        
        // Clear existing rows
        largeInventoryTableModel.setRowCount(0);
    
        // Add sorted rows to the table model
        for (Vector<Object> row : dataVector) {
            largeInventoryTableModel.addRow(row);
        }
    
        // Fire table data changed to notify the table model of the changes
        largeInventoryTableModel.fireTableDataChanged();
    }

    public void filterSmallInvTable(DefaultTableModel smallInventoryTableModel, int sortOption) {
        // Get the data vector from the table model
        Vector<Vector<Object>> dataVector = new Vector<>(smallInventoryTableModel.getDataVector());
        
        // Sort based on the selected option
        switch (sortOption) {

            case 1: // Sort by product ID
                Collections.sort(dataVector, Comparator.comparing(row -> (Integer) row.get(0))); // Assuming product ID is in the first column
                break;
            case 2: // Sort by name (A - Z)
                Collections.sort(dataVector, Comparator.comparing(row -> (String) row.get(1))); // Assuming product name is in the second column
                break;
            case 3: // Sort by stock left
                Collections.sort(dataVector, Comparator.comparing(row -> (Integer) row.get(2))); // Assuming stock left is in the third column
                break;
            default:
                // Invalid option, do nothing
                return;
        }
        
        // Clear existing rows
        smallInventoryTableModel.setRowCount(0);
    
        // Add sorted rows to the table model
        for (Vector<Object> row : dataVector) {
            smallInventoryTableModel.addRow(row);
        }
    
        // Fire table data changed to notify the table model of the changes
        smallInventoryTableModel.fireTableDataChanged();
    }

    public void addStock() {
        try {
            // Ask for product ID using JOptionPane
            String input = JOptionPane.showInputDialog(null, "Enter Product ID to add stock:", "Add Stock", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.isEmpty()) {
                return; // User cancelled or provided empty input
            }
            int productID = Integer.parseInt(input);
    
            // Check if Product ID exists
            if (!doesProductExist(productID)) {
                JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Check if product is inactive
            if (!isProductActive(productID)) {
                JOptionPane.showMessageDialog(null, "Product with ID " + productID + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Ask if there is a new supplier
            int confirmSupplier = JOptionPane.showConfirmDialog(null, "Is there a new supplier for this product?", "New Supplier", JOptionPane.YES_NO_OPTION);
            if (confirmSupplier == JOptionPane.YES_OPTION) {
                // Ask for the supplier ID
                String supplierInput = JOptionPane.showInputDialog(null, "Enter Supplier ID:", "Supplier ID", JOptionPane.QUESTION_MESSAGE);
                if (supplierInput == null || supplierInput.isEmpty()) {
                    return; // User cancelled or provided empty input
                }
                int supplierID = Integer.parseInt(supplierInput);
    
                // Check if Supplier ID exists
                if (!doesSupplierExist(supplierID)) {
                    JOptionPane.showMessageDialog(null, "Supplier ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Ask how much to add
                String stockInput = JOptionPane.showInputDialog(null, "Enter the quantity to add:", "Add Quantity", JOptionPane.QUESTION_MESSAGE);
                if (stockInput == null || stockInput.isEmpty()) {
                    return; // User cancelled or provided empty input
                }
                int quantityToAdd = Integer.parseInt(stockInput);
    
                // Update product stock left and supplier ID with the new values, and set supplier status to 'none'
                try (Connection connection = connect()) {
                    String updateQuery = "UPDATE tbl_product SET product_StockLeft = product_StockLeft + ?, last_Supplied_BY = ?, product_LastStocked = current_timestamp(), product_SupplierStatus = 'none' WHERE product_ID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, quantityToAdd);
                    updateStatement.setInt(2, supplierID);
                    updateStatement.setInt(3, productID);
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Stock added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Ask how much to add
                String stockInput = JOptionPane.showInputDialog(null, "Enter the quantity to add:", "Add Quantity", JOptionPane.QUESTION_MESSAGE);
                if (stockInput == null || stockInput.isEmpty()) {
                    return; // User cancelled or provided empty input
                }
                int quantityToAdd = Integer.parseInt(stockInput);
    
                // Update product stock left with the new value and set supplier status to 'none'
                try (Connection connection = connect()) {
                    String updateQuery = "UPDATE tbl_product SET product_StockLeft = product_StockLeft + ?, product_LastStocked = current_timestamp(), product_SupplierStatus = 'none' WHERE product_ID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, quantityToAdd);
                    updateStatement.setInt(2, productID);
                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Stock added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void removeStock() {
        // Ask for product ID using JOptionPane
        String input = JOptionPane.showInputDialog(null, "Enter Product ID to remove stock:", "Remove Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) {
            return; // User cancelled or provided empty input
        }
    
        try {
            int productIDInput = Integer.parseInt(input);
    
            // Check if product is inactive
            if (!isProductActive(productIDInput)) {
                JOptionPane.showMessageDialog(null, "Product with ID " + productIDInput + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Check if Product ID exists
            if (doesProductExist(productIDInput)) {
                // Ask how much to remove using JOptionPane
                String removeAmountString = JOptionPane.showInputDialog(null, "Enter quantity to remove:", "Remove Stock", JOptionPane.QUESTION_MESSAGE);
                if (removeAmountString == null || removeAmountString.isEmpty()) {
                    return; // User cancelled or provided empty input
                }
                int removeAmount = Integer.parseInt(removeAmountString);
    
                // Check if the amount to remove is available
                try (Connection connection = connect()) {
                    String selectQuery = "SELECT product_StockLeft FROM tbl_product WHERE product_ID = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, productIDInput);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        int currentStock = resultSet.getInt("product_StockLeft");
                        if (currentStock < removeAmount) {
                            JOptionPane.showMessageDialog(null, "Insufficient stock. Current stock: " + currentStock, "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
    
                        // Update database
                        String updateQuery = "UPDATE tbl_product SET product_StockLeft = product_StockLeft - ? WHERE product_ID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, removeAmount);
                        updateStatement.setInt(2, productIDInput);
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Stock removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to remove stock.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void emptyStock() {
        // Ask for product ID using JOptionPane
        String input = JOptionPane.showInputDialog(null, "Enter Product ID to empty stock:", "Empty Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) {
            return; // User cancelled or provided empty input
        }
    
        try {
            int productIDInput = Integer.parseInt(input);
    
            // Check if product is inactive
            if (!isProductActive(productIDInput)) {
                JOptionPane.showMessageDialog(null, "Product with ID " + productIDInput + " is already inactive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Check if Product ID exists
            if (doesProductExist(productIDInput)) {
                // Check if the stock is already empty
                try (Connection connection = connect()) {
                    String selectQuery = "SELECT product_StockLeft FROM tbl_product WHERE product_ID = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, productIDInput);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        int currentStock = resultSet.getInt("product_StockLeft");
                        if (currentStock == 0) {
                            JOptionPane.showMessageDialog(null, "Stock is already empty.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
    
                        // Confirm if user wants to empty stock
                        int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to empty the stock for this product?", "Confirm Empty Stock", JOptionPane.YES_NO_OPTION);
                        if (confirmResult == JOptionPane.YES_OPTION) {
                            // Update database to set stock left to 0
                            String updateQuery = "UPDATE tbl_product SET product_StockLeft = 0 WHERE product_ID = ?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setInt(1, productIDInput);
                            int rowsUpdated = updateStatement.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(null, "Stock emptied successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to empty stock.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public void changeStockStatus() {
        // Ask for product ID using JOptionPane
        String input = JOptionPane.showInputDialog(null, "Enter Product ID to change stock status:", "Change Stock Status", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) {
            return; // User cancelled or provided empty input
        }
    
        try {
            int productIDInput = Integer.parseInt(input);
    
            // Check if Product ID exists
            if (doesProductExist(productIDInput)) {
                // Check if product is inactive
                if (isProductActive(productIDInput)) {
                    JOptionPane.showMessageDialog(null, "Product with ID " + productIDInput + " is inactive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                // Get current status
                String currentStatus = getProductSupplierStatus(productIDInput);
    
                // Ask if user wants to change status
                String[] options = { "none", "pending" };
                int choice = JOptionPane.showOptionDialog(null, "Current status: " + currentStatus + "\nSelect new status:", "Change Stock Status", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
                // Update status in database
                if (choice == 0 || choice == 1) { // User selected an option
                    String newStatus = options[choice];
                    try (Connection connection = connect()) {
                        String updateQuery = "UPDATE tbl_product SET product_SupplierStatus = ? WHERE product_ID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, newStatus);
                        updateStatement.setInt(2, productIDInput);
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Stock status changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to change stock status.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while updating stock status.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchStock() {
        // Ask for product ID using JOptionPane
        String input = JOptionPane.showInputDialog(null, "Enter Product ID to search stock:", "Search Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) {
            return; // User cancelled or provided empty input
        }
    
        try {
            int productIDInput = Integer.parseInt(input);
    
            // Check if Product ID exists
            if (doesProductExist(productIDInput)) {
                // Display all details of the stock
                String details = getStockDetails(productIDInput);
                JOptionPane.showMessageDialog(null, "Stock Details:\n" + details, "Stock Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Product ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer for Product ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getProductSupplierStatus(int product_id) {
        try (Connection connection = connect()) {
            // Prepare the SQL query to fetch the product supplier status
            String query = "SELECT product_SupplierStatus FROM tbl_product WHERE product_ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, product_id);
            
            ResultSet resultSet = statement.executeQuery();
    
            // Check if any result is returned
            if (resultSet.next()) {
                // Extract and return the product supplier status
                return resultSet.getString("product_SupplierStatus");
            } else {
                return "Product with ID " + product_id + " not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while fetching product supplier status.";
        }
    }
    

    public String getStockDetails(int product_id) {
        try (Connection connection = connect()) {
            // Prepare the SQL query to fetch product details with supplier information
            String query = "SELECT p.product_ID, p.type_ID, p.size_ID, p.product_Name, p.product_Price, " +
                        "p.product_StockLeft, p.product_LastStocked, p.last_Supplied_BY, " +
                        "p.product_SupplierStatus, p.product_ActiveStatus " +
                        "FROM tbl_product p " +
                        "WHERE p.product_ID = ?";
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, product_id);
            
            ResultSet resultSet = statement.executeQuery();
    
            // Check if any result is returned
            if (resultSet.next()) {
                // Extract values from the result set
                int productId = resultSet.getInt("product_ID");
                int typeId = resultSet.getInt("type_ID");
                int sizeId = resultSet.getInt("size_ID");
                String productName = resultSet.getString("product_Name");
                double productPrice = resultSet.getDouble("product_Price");
                int stockLeft = resultSet.getInt("product_StockLeft");
                String lastStocked = resultSet.getString("product_LastStocked"); // Assuming lastStocked is a string
                int supplierId = resultSet.getInt("last_Supplied_BY");
                String supplierStatus = resultSet.getString("product_SupplierStatus");
                String activeStatus = resultSet.getString("product_ActiveStatus");
    
                // Prepare the details string
                StringBuilder details = new StringBuilder();
                details.append("Product ID: ").append(productId).append("\n");
                details.append("Type ID: ").append(typeId).append("\n");
                details.append("Size ID: ").append(sizeId).append("\n");
                details.append("Name: ").append(productName).append("\n");
                details.append("Price: ").append(productPrice).append("\n");
                details.append("Stock Left: ").append(stockLeft).append("\n");
                details.append("Last Stock: ").append(lastStocked).append("\n");
                details.append("Supplier ID: ").append(supplierId).append("\n");
                details.append("Supplier Status: ").append(supplierStatus).append("\n");
                details.append("Active Status: ").append(activeStatus);
    
                return details.toString();
            } else {
                return "Product with ID " + product_id + " not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while fetching product details.";
        }
    }

    //----------------------------------------------------------------
    /**
     * ADMIN PANEL
     */

    public void admin_addType() {
        String typeName = JOptionPane.showInputDialog("Enter the name of the type:");
        if (typeName == null || typeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Type name cannot be empty.");
            return;
        }

        try (Connection conn = connect()) {
            String query = "SELECT type_ID FROM tbl_type WHERE type_Name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, typeName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Type already exists in the database.");
                    } else {
                        String insert = "INSERT INTO tbl_type (type_Name) VALUES (?)";
                        try (PreparedStatement pstmtInsert = conn.prepareStatement(insert)) {
                            pstmtInsert.setString(1, typeName);
                            pstmtInsert.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Type added successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the type.");
        }
    }

    public void admin_addSize() {
        String sizeLength = JOptionPane.showInputDialog("Enter the size length:");
        if (sizeLength == null || sizeLength.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Size length cannot be empty.");
            return;
        }

        try (Connection conn = connect()) {
            String query = "SELECT size_ID FROM tbl_size WHERE size_length = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, sizeLength);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Size length already exists in the database.");
                    } else {
                        String insert = "INSERT INTO tbl_size (size_length) VALUES (?)";
                        try (PreparedStatement pstmtInsert = conn.prepareStatement(insert)) {
                            pstmtInsert.setString(1, sizeLength);
                            pstmtInsert.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Size added successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the size.");
        }
    }

    public void admin_addSupplier() {
        String supplierName = JOptionPane.showInputDialog("Enter the name of the supplier:");
        if (supplierName == null || supplierName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Supplier name cannot be empty.");
            return;
        }

        try (Connection conn = connect()) {
            String query = "SELECT supplier_ID FROM tbl_supplier WHERE supplier_Name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, supplierName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Supplier already exists in the database.");
                    } else {
                        String supplierAddress = JOptionPane.showInputDialog("Enter the address of the supplier:");
                        if (supplierAddress == null || supplierAddress.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Supplier address cannot be empty.");
                            return;
                        }

                        String insert = "INSERT INTO tbl_supplier (supplier_Name, supplier_Address) VALUES (?, ?)";
                        try (PreparedStatement pstmtInsert = conn.prepareStatement(insert)) {
                            pstmtInsert.setString(1, supplierName);
                            pstmtInsert.setString(2, supplierAddress);
                            pstmtInsert.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Supplier added successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the supplier.");
        }
    }    

    public void admin_modifyType() {
        try (Connection conn = connect()) {
            String query = "SELECT type_ID, type_Name FROM tbl_type";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Select a Type to Modify", JOptionPane.PLAIN_MESSAGE);

                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No type selected.");
                        return;
                    }

                    int typeId = (int) table.getValueAt(selectedRow, 0);
                    String currentTypeName = (String) table.getValueAt(selectedRow, 1);
                    String newTypeName = JOptionPane.showInputDialog("Enter new name for type (current name: " + currentTypeName + "):");

                    if (newTypeName != null && !newTypeName.trim().isEmpty()) {
                        String updateQuery = "UPDATE tbl_type SET type_Name = ? WHERE type_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, newTypeName);
                            updateStmt.setInt(2, typeId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Type modified successfully.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid type name.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while modifying the type.");
        }
    }

    public void admin_modifySize() {
        try (Connection conn = connect()) {
            String query = "SELECT size_ID, size_length FROM tbl_size";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Select a Size to Modify", JOptionPane.PLAIN_MESSAGE);
    
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No size selected.");
                        return;
                    }
    
                    int sizeId = (int) table.getValueAt(selectedRow, 0);
                    String currentSizeLength = (String) table.getValueAt(selectedRow, 1);
                    String newSizeLength = JOptionPane.showInputDialog("Enter new length for size (current length: " + currentSizeLength + "):");
    
                    if (newSizeLength != null && !newSizeLength.trim().isEmpty()) {
                        String updateQuery = "UPDATE tbl_size SET size_length = ? WHERE size_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, newSizeLength);
                            updateStmt.setInt(2, sizeId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Size modified successfully.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid size length.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while modifying the size.");
        }
    }
    

    public void admin_modifySupplier() {
        try (Connection conn = connect()) {
            String query = "SELECT supplier_ID, supplier_Name, supplier_Address FROM tbl_supplier";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Select a Supplier to Modify", JOptionPane.PLAIN_MESSAGE);
    
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No supplier selected.");
                        return;
                    }
    
                    int supplierId = (int) table.getValueAt(selectedRow, 0);
                    String currentSupplierName = (String) table.getValueAt(selectedRow, 1);
                    String currentSupplierAddress = (String) table.getValueAt(selectedRow, 2);
    
                    String newSupplierName = JOptionPane.showInputDialog("Enter new name for supplier (current name: " + currentSupplierName + "):");
                    String newSupplierAddress = JOptionPane.showInputDialog("Enter new address for supplier (current address: " + currentSupplierAddress + "):");
    
                    if ((newSupplierName != null && !newSupplierName.trim().isEmpty()) || (newSupplierAddress != null && !newSupplierAddress.trim().isEmpty())) {
                        String updateQuery = "UPDATE tbl_supplier SET supplier_Name = ?, supplier_Address = ? WHERE supplier_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, newSupplierName);
                            updateStmt.setString(2, newSupplierAddress);
                            updateStmt.setInt(3, supplierId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Supplier modified successfully.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid supplier name or address.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while modifying the supplier.");
        }
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data of the table
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public void admin_seeInactiveOrders() {
        try (Connection conn = connect()) {
            String query = "SELECT * FROM tbl_transaction WHERE transaction_ActiveStatus = 'inactive'";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Inactive Transaction", JOptionPane.PLAIN_MESSAGE);

                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No transaction selected.");
                        return;
                    }

                    int productId = (int) table.getValueAt(selectedRow, 0);
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to make this transaction active again?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        String updateQuery = "UPDATE tbl_transaction SET transaction_ActiveStatus = 'active' WHERE transaction_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, productId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Transaction made active again successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving transaction customer.");
        }
    }

    public void admin_seeInactiveCustomers() {
        try (Connection conn = connect()) {
            String query = "SELECT * FROM tbl_customer WHERE customer_ActiveStatus = 'inactive'";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Inactive Customers", JOptionPane.PLAIN_MESSAGE);

                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No customer selected.");
                        return;
                    }

                    int productId = (int) table.getValueAt(selectedRow, 0);
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to make this customer active again?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        String updateQuery = "UPDATE tbl_customer SET customer_ActiveStatus = 'active' WHERE customer_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, productId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Customer made active again successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving inactive customer.");
        }
    }

    public void admin_seeInactiveProducts() {
        try (Connection conn = connect()) {
            String query = "SELECT * FROM tbl_product WHERE product_ActiveStatus = 'inactive'";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    JTable table = new JTable(buildTableModel(rs));
                    table.setDefaultEditor(Object.class, null); // Disable cell editing
                    JOptionPane.showMessageDialog(null, new JScrollPane(table), "Inactive Products", JOptionPane.PLAIN_MESSAGE);

                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "No product selected.");
                        return;
                    }

                    int productId = (int) table.getValueAt(selectedRow, 0);
                    int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to make this product active again?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        String updateQuery = "UPDATE tbl_product SET product_ActiveStatus = 'active' WHERE product_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, productId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Product made active again successfully.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving inactive products.");
        }
    }

    public boolean doesEmployeeExist(String firstName, String lastName) {
        String query = "SELECT * FROM tbl_employee WHERE employee_FirstName = ? AND employee_LastName = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean doesLoginExist(String username, String password) {
        return true;
    }

    public void admin_addNewEmployee(int currentAdminId) {
        String firstName, lastName, contactNum;
        while (true) {
            firstName = JOptionPane.showInputDialog("Enter Employee First Name:");
            if (firstName == null || firstName.trim().isEmpty()) {
                // If canceled or left empty, exit the method
                return;
            }
            
            lastName = JOptionPane.showInputDialog("Enter Employee Last Name:");
            if (lastName == null || lastName.trim().isEmpty()) {
                // If canceled or left empty, exit the method
                return;
            }

            if (doesEmployeeExist(firstName, lastName)) {
                JOptionPane.showMessageDialog(null, "Employee already exists!");
                return;
            }

            contactNum = JOptionPane.showInputDialog("Enter Employee Contact Number:");
            if (contactNum != null && contactNum.startsWith("639") && contactNum.length() == 12) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Contact number must start with '639' and be 12 characters long.");
            }
        }
    
        String[] accessLevels = getAccessLevels();
        if (accessLevels.length == 0) {
            JOptionPane.showMessageDialog(null, "No access levels available.");
            return;
        }
        String role = (String) JOptionPane.showInputDialog(null, "Select Employee Role:", "Role Selection",
                JOptionPane.QUESTION_MESSAGE, null, accessLevels, accessLevels[0]);
    
        String username = (firstName.charAt(0) + lastName).toLowerCase();
        String password = "changeme";
    
        int response = JOptionPane.showConfirmDialog(null, "Do you wish to add the employee?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            Integer loginId = addLogin(username, password, role);
            if (loginId != null && addEmployee(firstName, lastName, contactNum, loginId, currentAdminId)) {
                JOptionPane.showMessageDialog(null, "Employee added successfully!");
                
                // Add the user to admin table if the role is "admin", "manager", or "owner"
                if (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("manager") || role.equalsIgnoreCase("owner")) {
                    addAdmin(firstName, lastName);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error adding employee.");
            }
        }
    }
    
    private String[] getAccessLevels() {
        String query = "SELECT DISTINCT login_AccessLevel FROM tbl_login";
        List<String> accessLevels = new ArrayList<>();
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                accessLevels.add(rs.getString("login_AccessLevel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessLevels.toArray(new String[0]);
    }

    private Integer addLogin(String username, String password, String accessLevel) {
        String query = "INSERT INTO tbl_login (login_Username, login_Password, login_AccessLevel) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, accessLevel);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean addEmployee(String firstName, String lastName, String contactNum, int loginId, int adminId) {
        String query = "INSERT INTO tbl_employee (employee_ID, employee_FirstName, employee_LastName, employee_ContactNum, admin_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, loginId); // Set the loginId as employee_ID
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, contactNum);
            stmt.setInt(5, adminId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addAdmin(String firstName, String lastName) {
        String query = "INSERT INTO tbl_admin (admin_FirstName, admin_LastName) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void admin_modifyEmployee() {
        boolean justviewing = false;
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
    
            // Prompt to select employee
            ResultSet rs = stmt.executeQuery("SELECT * FROM tbl_employee");
            JComboBox<String> employeeComboBox = new JComboBox<>();
            while (rs.next()) {
                String employeeFullName = rs.getString("employee_FirstName") + " " + rs.getString("employee_LastName");
                employeeComboBox.addItem(employeeFullName);
            }
            rs.close();
    
            String[] options = {"Employee Details", "Login Details", "Full Details"}; // Adding the third option
            int choice = JOptionPane.showOptionDialog(null, "Select action:", "Modify Employee",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    
                    if (choice == JOptionPane.CLOSED_OPTION) {
                        // If the dialog is closed without making a selection, exit the method
                        return;
                    }
                    
            String selectedEmployee = (String) JOptionPane.showInputDialog(null, "Select an employee:",
                    "Modify Employee", JOptionPane.QUESTION_MESSAGE, null, employeeComboBox.getItemCount() > 0 ? IntStream.range(0, employeeComboBox.getItemCount()).mapToObj(employeeComboBox::getItemAt).toArray() : null, null);
    
            if (selectedEmployee != null) {
                int employeeID = 0;
                rs = stmt.executeQuery("SELECT * FROM tbl_employee WHERE CONCAT(employee_FirstName, ' ', employee_LastName) = '" + selectedEmployee + "'");
                if (rs.next()) {
                    employeeID = rs.getInt("employee_ID");
                }
                rs.close();
    
                boolean modificationMade = false; // Flag to track if any modification is made
    
                if (choice == 0) { // Employee Details
                    String[] employeeOptions = {"First Name", "Last Name", "Contact Number"};
                    int empChoice = JOptionPane.showOptionDialog(null, "Select detail to modify:", "Modify Employee Details",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, employeeOptions, employeeOptions[0]);
    
                    if (empChoice == 0) { // First Name
                        String newFirstName = JOptionPane.showInputDialog(null, "Enter new First Name:", "Modify First Name", JOptionPane.QUESTION_MESSAGE);
                        if (newFirstName != null) { // Check if user cancels or closes the input dialog
                            if (!newFirstName.trim().isEmpty()) {
                                stmt.executeUpdate("UPDATE tbl_employee SET employee_FirstName = '" + newFirstName + "' WHERE employee_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Name.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (empChoice == 1) { // Last Name
                        String newLastName = JOptionPane.showInputDialog(null, "Enter new Last Name:", "Modify Last Name", JOptionPane.QUESTION_MESSAGE);
                        if (newLastName != null) { // Check if user cancels or closes the input dialog
                            if (!newLastName.trim().isEmpty()) {
                                stmt.executeUpdate("UPDATE tbl_employee SET employee_LastName = '" + newLastName + "' WHERE employee_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Name.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (empChoice == 2) { // Contact Number
                        String newContactNum = JOptionPane.showInputDialog(null, "Enter new Contact Number:", "Modify Contact Number", JOptionPane.QUESTION_MESSAGE);
                        if (newContactNum != null) { // Check if user cancels or closes the input dialog
                            if (newContactNum.matches("^639\\d{9}$")) {
                                stmt.executeUpdate("UPDATE tbl_employee SET employee_ContactNum = '" + newContactNum + "' WHERE employee_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                            } else {
                                JOptionPane.showMessageDialog(null, "Must Start with 639 and must be 12 Digits.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else if (choice == 1) { // Login Details
                    String[] loginOptions = {"Username", "Password", "Access Level"};
                    int loginChoice = JOptionPane.showOptionDialog(null, "Select detail to modify:", "Modify Login Details",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, loginOptions, loginOptions[0]);
                
                    if (loginChoice == 0) { // Username
                        String newUsername = JOptionPane.showInputDialog(null, "Enter new Username:", "Modify Username", JOptionPane.QUESTION_MESSAGE);
                        if (newUsername != null) { // Check if user cancels or closes the input dialog
                            if (!newUsername.trim().isEmpty()) {
                                stmt.executeUpdate("UPDATE tbl_login SET login_Username = '" + newUsername + "' WHERE login_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Name.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (loginChoice == 1) { // Password
                        String newPassword = JOptionPane.showInputDialog(null, "Enter new Password:", "Modify Password", JOptionPane.QUESTION_MESSAGE);
                        if (newPassword != null) { // Check if user cancels or closes the input dialog
                            if (!newPassword.trim().isEmpty()) {
                                stmt.executeUpdate("UPDATE tbl_login SET login_Password = '" + newPassword + "' WHERE login_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Name.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (loginChoice == 2) { // Access Level
                        String newAccessLevel = JOptionPane.showInputDialog(null, "Enter new Access Level:", "Modify Access Level", JOptionPane.QUESTION_MESSAGE);
                        if (newAccessLevel != null) { // Check if user cancels or closes the input dialog
                            if (!newAccessLevel.trim().isEmpty()) {
                                stmt.executeUpdate("UPDATE tbl_login SET login_AccessLevel = '" + newAccessLevel + "' WHERE login_ID = " + employeeID);
                                modificationMade = true; // Set flag indicating modification made
                
                                // Check if the new access level is admin, manager, or employee
                                if (newAccessLevel.equalsIgnoreCase("admin") || newAccessLevel.equalsIgnoreCase("manager") || newAccessLevel.equalsIgnoreCase("employee")) {
                                    // Get the first name and last name of the employee
                                    ResultSet employeeDetails = stmt.executeQuery("SELECT employee_FirstName, employee_LastName FROM tbl_employee WHERE employee_ID = " + employeeID);
                                    if (employeeDetails.next()) {
                                        String firstName = employeeDetails.getString("employee_FirstName");
                                        String lastName = employeeDetails.getString("employee_LastName");
                                        
                                        // Insert the employee into tbl_admin if they are not already there
                                        ResultSet adminCheck = stmt.executeQuery("SELECT * FROM tbl_admin WHERE admin_ID = " + employeeID);
                                        if (!adminCheck.next()) {
                                            stmt.executeUpdate("INSERT INTO tbl_admin (admin_ID, admin_FirstName, admin_LastName) VALUES (" + employeeID + ", '" + firstName + "', '" + lastName + "')");
                                        }
                                    }
                                } else {
                                    // Remove the employee from tbl_admin if they are there
                                    stmt.executeUpdate("DELETE FROM tbl_admin WHERE admin_ID = " + employeeID);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid Name.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } else if (choice == 2) { // Full Details
                    justviewing = true;
                    // Display full details including both employee and login details
                    rs = stmt.executeQuery("SELECT * FROM tbl_employee e JOIN tbl_login l ON e.employee_ID = l.login_ID WHERE e.employee_ID = " + employeeID);
                    if (rs.next()) {
                        StringBuilder fullDetails = new StringBuilder();
                        fullDetails.append("Employee ID: ").append(rs.getInt("employee_ID")).append("\n");
                        fullDetails.append("First Name: ").append(rs.getString("employee_FirstName")).append("\n");
                        fullDetails.append("Last Name: ").append(rs.getString("employee_LastName")).append("\n");
                        fullDetails.append("Contact Number: ").append(rs.getString("employee_ContactNum")).append("\n");
                        fullDetails.append("Username: ").append(rs.getString("login_Username")).append("\n");
                        fullDetails.append("Username: ").append(rs.getString("login_Password")).append("\n");
                        fullDetails.append("Access Level: ").append(rs.getString("login_AccessLevel")).append("\n");
    
                        JOptionPane.showMessageDialog(null, fullDetails.toString(), "Employee Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                if (justviewing) {
                    return;
                } else {
                    if (modificationMade) {
                        JOptionPane.showMessageDialog(null, "Employee details modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No modifications made.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                }    
            }
    
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //----------------------------------------------------------------
    /**
     * ORDER LIST PANEL METHODS
     */

    public void newTransaction(JLabel transaction_label, int employee_ID) {
        try (Connection conn = connect()) {
            // Insert a new transaction into the database
            String insertQuery = "INSERT INTO tbl_transaction (employee_ID, transaction_Date, transaction_TotalPrice, transaction_TotalPaid, transaction_OrderStatus, transaction_ActiveStatus) VALUES (?, CURRENT_TIMESTAMP, 0.00, 0.00, 'pending', 'active')";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                // Set parameters
                pstmt.setInt(1, employee_ID);
                
                // Execute the insert statement
                int rowsAffected = pstmt.executeUpdate();
                
                // Check if the insertion was successful
                if (rowsAffected > 0) {
                    // Get the generated transaction ID
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int transactionID = rs.getInt(1);
                            
                            // Print the transactionID to verify if it's being set correctly
                            System.out.println("New Transaction ID: " + transactionID);

                            // Update the transaction label with the new transaction ID
                            transaction_label.setText("Current Transaction: " + transactionID);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
        }
    }
    
    public int getTransactionID(JLabel transaction_label) {
        String labelText = transaction_label.getText();
        String prefix = "Current Transaction: ";
        
        if (labelText.startsWith(prefix)) {
            try {
                return Integer.parseInt(labelText.substring(prefix.length()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Error handling: Transaction ID is not a valid number
            }
        }
        // Error handling: Label text is not in the expected format
        return -1; // Return -1 or some other indication that the transaction ID could not be parsed
    }

    public void updateTransactionTable(DefaultTableModel tableModel, int transactionID) {
        try (Connection conn = connect()) {
            // Query to fetch items associated with the given transaction ID
            String query = "SELECT product_ID, product_Name, product_Quantity, item_Price, item_SubTotal " +
                        "FROM tbl_item INNER JOIN tbl_product ON tbl_item.product_ID = tbl_product.product_ID " +
                        "WHERE transaction_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, transactionID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Clear the existing table data
                    tableModel.setRowCount(0);
                    
                    // Populate the table model with fetched data
                    while (rs.next()) {
                        int productID = rs.getInt("product_ID");
                        String productName = rs.getString("product_Name");
                        int quantity = rs.getInt("product_Quantity");
                        double price = rs.getDouble("item_Price");
                        double subtotal = rs.getDouble("item_SubTotal");
                        
                        // Add a row to the table model
                        tableModel.addRow(new Object[]{productID, productName, quantity, price, subtotal});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
        }
    }
    
    public void addItemToTransaction(int productID, DefaultTableModel transactionTableModel, int transactionID) {
        System.out.println("Received product ID in addItemToTransaction: " + productID);
        System.out.println("Transaction ID in addItemToTransaction: " + transactionID);
        try (Connection conn = connect()) {
            // Check if the product exists
            if (!doesProductExist(productID)) {
                JOptionPane.showMessageDialog(null, "Error: Product ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Fetch product details including price
            String query = "SELECT product_Name, product_Price FROM tbl_product WHERE product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String productName = rs.getString("product_Name");
                        double productPrice = rs.getDouble("product_Price");
                        
                        // Prompt the user for the quantity to add
                        String quantityString = JOptionPane.showInputDialog(null, "Enter quantity to add:", "Add Item", JOptionPane.PLAIN_MESSAGE);
                        if (quantityString == null || quantityString.isEmpty()) {
                            // User canceled or entered an empty string
                            return;
                        }
                        
                        // Convert the input to integer
                        int quantity;
                        try {
                            quantity = Integer.parseInt(quantityString);
                            if (quantity <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Error: Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Check if there is enough product
                        if (!isThereEnoughProduct(productID, quantity)) {
                            JOptionPane.showMessageDialog(null, "Error: Not enough product in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Add the item to the transaction table
                        double subtotal = productPrice * quantity;

                        // Insert the item into tbl_item
                        String insertQuery = "INSERT INTO tbl_item (product_ID, transaction_ID, product_Quantity, item_Price, item_SubTotal) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertPstmt = conn.prepareStatement(insertQuery)) {
                            insertPstmt.setInt(1, productID);
                            insertPstmt.setInt(2, transactionID);
                            insertPstmt.setInt(3, quantity);
                            insertPstmt.setDouble(4, productPrice);
                            insertPstmt.setDouble(5, subtotal);
                            int rowsInserted = insertPstmt.executeUpdate();
                            System.out.println("Rows inserted into tbl_item: " + rowsInserted);
                        }
                        
                        transactionTableModel.addRow(new Object[]{productID, productName, quantity, productPrice, subtotal});
                        
                        // Update the stock
                        removeStock(productID, quantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public boolean isThereEnoughProduct(int productID, int quantity) {
        try (Connection conn = connect()) {
            // Check if there is enough stock for the given product
            String query = "SELECT product_StockLeft FROM tbl_product WHERE product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int stockLeft = rs.getInt("product_StockLeft");
                        return stockLeft >= quantity;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
        }
        return false; // Return false if there was an error or if the product was not found
    }

    public void removeStock(int productID, int quantity) {
        try (Connection conn = connect()) {
            // Fetch the current stock left
            String query = "SELECT product_StockLeft FROM tbl_product WHERE product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int currentStock = rs.getInt("product_StockLeft");
                        
                        // Calculate the new stock after removing the specified quantity
                        int newStock = currentStock - quantity;
                        if (newStock < 0) {
                            newStock = 0; // Ensure the stock doesn't go negative
                        }
                        
                        // Update the stock in the database
                        String updateQuery = "UPDATE tbl_product SET product_StockLeft = ? WHERE product_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, newStock);
                            updateStmt.setInt(2, productID);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelCurrentTransaction(int transactionID, JLabel transactionLabel) {
        System.out.println("Canceling transaction ID: " + transactionID);
        try (Connection conn = connect()) {
            String cancelQuery = "UPDATE tbl_transaction SET transaction_ActiveStatus = 'inactive' WHERE transaction_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(cancelQuery)) {
                pstmt.setInt(1, transactionID);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Transaction cancel rows affected: " + rowsAffected);
            }
            // Update the transaction label
            transactionLabel.setText("Current Transaction: ");
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
        }
    }
    
    
    public void putBackStock(int transactionID) {
        System.out.println("Putting back stock for transaction ID: " + transactionID);
        try (Connection conn = connect()) {
            // Retrieve items for the given transactionID
            String selectQuery = "SELECT product_ID, product_Quantity FROM tbl_item WHERE transaction_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setInt(1, transactionID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasItems = false;
                    while (rs.next()) {
                        hasItems = true;
                        int productID = rs.getInt("product_ID");
                        int quantity = rs.getInt("product_Quantity");
                        System.out.println("Retrieved product ID: " + productID + ", quantity: " + quantity);
    
                        // Update the product stock
                        String updateStockQuery = "UPDATE tbl_product SET product_StockLeft = product_StockLeft + ? WHERE product_ID = ?";
                        try (PreparedStatement updatePstmt = conn.prepareStatement(updateStockQuery)) {
                            updatePstmt.setInt(1, quantity);
                            updatePstmt.setInt(2, productID);
                            int rowsAffected = updatePstmt.executeUpdate();
                            System.out.println("Stock updated for product ID: " + productID + ", rows affected: " + rowsAffected);
                        }
                    }
                    if (!hasItems) {
                        System.out.println("No items found for transaction ID: " + transactionID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
        }
    }
    

    public void clearTransactionTable(DefaultTableModel transactionTableModel) {
        System.out.println("Clearing transaction table model");
        transactionTableModel.setRowCount(0);
    }
    

   // Method to fetch product information from the database based on product ID
    public void checkItemFromTransaction(int productID, JLabel itemNameLabel, JLabel itemLengthLabel, JLabel itemPriceLabel, JLabel itemLeftLabel) {
        System.out.println("Received product ID: " + productID);
        try (Connection conn = connect()) {
            // Check if the product exists
            if (!doesProductExist(productID)) {
                // Error: Product ID not found
                JOptionPane.showMessageDialog(null, "Error: Item ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                // Clear GUI labels
                itemNameLabel.setText("TYPE: ");
                itemLengthLabel.setText("LENGTH: ");
                itemPriceLabel.setText("PRICE: ");
                itemLeftLabel.setText("STOCK: ");
                return;
            }

            // Query to fetch product information
            String query = "SELECT p.product_Name, p.product_Price, p.product_StockLeft, t.type_Name, s.size_length " +
                        "FROM tbl_product p " +
                        "INNER JOIN tbl_type t ON p.type_ID = t.type_ID " +
                        "INNER JOIN tbl_size s ON p.size_ID = s.size_ID " +
                        "WHERE p.product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Product details
                        double productPrice = rs.getDouble("product_Price");
                        int stockLeft = rs.getInt("product_StockLeft");
                        String typeName = rs.getString("type_Name");
                        String sizeLength = rs.getString("size_length");

                        // Update GUI labels with product information
                        itemNameLabel.setText("TYPE: " + typeName);
                        itemLengthLabel.setText("LENGTH: " + sizeLength);
                        itemPriceLabel.setText("PRICE: " + productPrice);
                        itemLeftLabel.setText("STOCK: " + stockLeft);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
            JOptionPane.showMessageDialog(null, "Error fetching product information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // Method to see the product list
    public void seeProductList() {
        String query = "SELECT product_ID, product_Name, product_Price, product_StockLeft FROM tbl_product WHERE product_ActiveStatus = 'active'";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            // Prepare data for the JTable
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock Left"}, 0);

            while (rs.next()) {
                int id = rs.getInt("product_ID");
                String name = rs.getString("product_Name");
                double price = rs.getDouble("product_Price");
                int stockLeft = rs.getInt("product_StockLeft");

                model.addRow(new Object[]{id, name, price, stockLeft});
            }

            // Create a JTable with the model
            JTable table = new JTable(model);

            // Use ColumnsAutoSizer to adjust column widths
            ColumnsAutoSizer.sizeColumnsToFit(table);

            // Display the table in a JOptionPane
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "All Available Products", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching product list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateTransactionTotal(DefaultTableModel transactionTableModel, int currentTotalAmount, JLabel orderTotalLabel) {
        double total = currentTotalAmount; // Initialize total with the currentTotalAmount
        for (int row = 0; row < transactionTableModel.getRowCount(); row++) {
            double subtotal = (double) transactionTableModel.getValueAt(row, 4);
            total += subtotal;
        }
        System.out.println("New Total: " + total); // Print the new total
        orderTotalLabel.setText("Total Amount: ₱" + total);
    }

    public void updateTransactionTotalBySQL(int transactionID, JLabel orderTotalLabel) {
        try (Connection conn = connect()) {
            // Query to sum the subtotal of all items in the transaction
            String query = "SELECT SUM(item_SubTotal) AS total FROM tbl_item WHERE transaction_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, transactionID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Get the total amount from the result set
                        double totalAmount = rs.getDouble("total");
                        
                        // Update the JLabel with the total amount
                        orderTotalLabel.setText("Total Amount: ₱" + totalAmount);
                    } else {
                        // If no items found for the transaction, set total amount to 0
                        orderTotalLabel.setText("Total Amount: ₱0.00");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasItemsInTransaction(Connection conn, int transactionID) throws SQLException {
        // Check if there are items in the transaction
        String query = "SELECT COUNT(*) FROM tbl_item WHERE transaction_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int itemCount = rs.getInt(1);
                    return itemCount > 0;
                }
            }
        }
        return false;
    }

    public boolean confirmTransaction(int transactionID) {
        try (Connection conn = connect()) {

            if (!hasItemsInTransaction(conn, transactionID)) {
                JOptionPane.showMessageDialog(null, "Error: No items in this transaction. Cannot confirm.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Get the total amount and total paid for the transaction
            double totalAmount = getTotalAmountForTransaction(conn, transactionID);
            double totalPaid = getTotalPaidForTransaction(conn, transactionID);
    
            if (totalAmount == -1 || totalPaid == -1) {
                // Error occurred while fetching total amount or total paid
                JOptionPane.showMessageDialog(null, "Error: Failed to fetch total amount or total paid.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
    
            // Prompt for amount paid
            String amountPaidString = JOptionPane.showInputDialog(null, "Enter amount paid:", "Confirm Transaction", JOptionPane.PLAIN_MESSAGE);
            if (amountPaidString == null || amountPaidString.isEmpty()) {
                // User canceled or entered an empty string
                return false;
            }
    
            // Convert the input to double
            double amountPaid;
            try {
                amountPaid = Double.parseDouble(amountPaidString);
                if (amountPaid < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid amount paid.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
    
            // Determine order status based on total paid
        String orderStatus;
        if (amountPaid < totalAmount) {
            // Total paid is less than total amount
            int response = JOptionPane.showConfirmDialog(null, "Customer details must be added. Do you want to continue?", "Incomplete Payment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                return false;
            }
            while (true) {
                if (addCustomerWhoDidNotPayFull(conn, transactionID)) {
                    orderStatus = "pending";
                    break;
                } else {
                    // Ask again to continue or cancel
                    response = JOptionPane.showConfirmDialog(null, "Customer details must be added. Do you want to continue?", "Incomplete Payment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {
                        return false;
                    }
                }
            }
        } else {
            // Total paid is equal to or greater than total amount
            orderStatus = "paid";
        }
    
            // Calculate change amount if applicable
            double changeAmount = amountPaid - totalAmount;
            if (changeAmount > 0) {
                // Show change amount if there's change
                JOptionPane.showMessageDialog(null, "Change: $" + String.format("%.2f", changeAmount), "Change", JOptionPane.INFORMATION_MESSAGE);
            }
    
            // Update the transaction with the new order status, total amount, and total paid
            updateTransaction(conn, transactionID, totalAmount, amountPaid, orderStatus);
            return true; // Transaction successfully confirmed
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void updateTransaction(Connection conn, int transactionID, double totalAmount, double amountPaid, String orderStatus) throws SQLException {
        // Update the transaction with the new total amount, total paid, and order status
        String updateQuery = "UPDATE tbl_transaction SET transaction_TotalPrice = ?, transaction_TotalPaid = ?, transaction_OrderStatus = ? WHERE transaction_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, totalAmount);
            pstmt.setDouble(2, amountPaid);
            pstmt.setString(3, orderStatus);
            pstmt.setInt(4, transactionID);
            pstmt.executeUpdate();
        }
    }

    private double getTotalPaidForTransaction(Connection conn, int transactionID) throws SQLException {
        // Query to fetch the total amount paid for the transaction
        String query = "SELECT transaction_TotalPaid FROM tbl_transaction WHERE transaction_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("transaction_TotalPaid");
                }
            }
        }
        // If an error occurs while fetching total paid, return -1
        return -1;
    }
    
    private double getTotalAmountForTransaction(Connection conn, int transactionID) throws SQLException {
        // Query to sum the subtotal of all items in the transaction
        String query = "SELECT SUM(item_SubTotal) AS total FROM tbl_item WHERE transaction_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        // If an error occurs while fetching total amount, return -1
        return -1;
    }

    public boolean addCustomerWhoDidNotPayFull(Connection conn, int transactionID) {
        while (true) {
            try {
                // Check if the transaction already has a customer associated with it
                if (isCustomerAssociated(conn, transactionID)) {
                    return true; // Customer already associated
                }
    
                // Prompt for customer details
                String firstName = JOptionPane.showInputDialog(null, "Enter customer's first name:", "Customer Details", JOptionPane.PLAIN_MESSAGE);
                String lastName = JOptionPane.showInputDialog(null, "Enter customer's last name (optional):", "Customer Details", JOptionPane.PLAIN_MESSAGE);
                String contactNumber = JOptionPane.showInputDialog(null, "Enter customer's contact number (must start with 639 and be 12 digits long):", "Customer Details", JOptionPane.PLAIN_MESSAGE);
    
                if (firstName == null || firstName.isEmpty() || contactNumber == null || contactNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "First name and contact number are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
    
                if (!isValidContactNumber(contactNumber)) {
                    JOptionPane.showMessageDialog(null, "Contact number must start with 639 and be 12 digits long.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
    
                // Insert the customer into tbl_customer
                int customerID = insertCustomer(conn, firstName, lastName, contactNumber);
                if (customerID != -1) {
                    // Update the transaction with the customer ID
                    updateTransactionWithCustomer(conn, transactionID, customerID);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: Database error while adding customer details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isCustomerAssociated(Connection conn, int transactionID) throws SQLException {
        // Check if the transaction already has a customer associated with it
        String query = "SELECT customer_ID FROM tbl_transaction WHERE transaction_ID = ? AND customer_ID IS NOT NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private boolean isValidContactNumber(String contactNumber) {
        // Validate contact number: must start with 639 and be 12 digits long
        return contactNumber.startsWith("639") && contactNumber.length() == 12 && contactNumber.matches("\\d+");
    }
    
    private int insertCustomer(Connection conn, String firstName, String lastName, String contactNumber) throws SQLException {
        // Insert the customer's details into tbl_customer
        String insertQuery = "INSERT INTO tbl_customer (customer_FirstName, customer_LastName, customer_ContactNum) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, contactNumber);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                return -1; // Insert failed
            }
            // Retrieve the auto-generated customer ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1; // Failed to retrieve customer ID
                }
            }
        }
    }
    
    private void updateTransactionWithCustomer(Connection conn, int transactionID, int customerID) throws SQLException {
        // Update the transaction to associate it with the newly added customer
        String updateQuery = "UPDATE tbl_transaction SET customer_ID = ? WHERE transaction_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, customerID);
            pstmt.setInt(2, transactionID);
            pstmt.executeUpdate();
        }
    }

    public void modifyTransaction(int transactionID, JLabel orderTotalLabel) {
        try (Connection conn = connect()) {
            // Prompt for product ID
            String productIDString = JOptionPane.showInputDialog(null, "Enter product ID to modify:", "Modify Transaction", JOptionPane.PLAIN_MESSAGE);
            if (productIDString == null || productIDString.isEmpty()) {
                // User canceled or entered an empty string
                return;
            }
    
            int productID;
            try {
                productID = Integer.parseInt(productIDString);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid product ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Check if the product exists in the transaction
            String query = "SELECT product_Quantity, item_Price FROM tbl_item WHERE transaction_ID = ? AND product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, transactionID);
                pstmt.setInt(2, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int currentQuantity = rs.getInt("product_Quantity");
                        double itemPrice = rs.getDouble("item_Price");
    
                        // Ask if the user wants to remove or update the quantity
                        String[] options = {"Remove Item", "Update Quantity"};
                        int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Modify Transaction",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    
                        if (choice == 0) {
                            // Remove item
                            String deleteQuery = "DELETE FROM tbl_item WHERE transaction_ID = ? AND product_ID = ?";
                            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteQuery)) {
                                deletePstmt.setInt(1, transactionID);
                                deletePstmt.setInt(2, productID);
                                int rowsDeleted = deletePstmt.executeUpdate();
                                System.out.println("Rows deleted from tbl_item: " + rowsDeleted);
    
                                // Update the stock
                                addStock(productID, currentQuantity);
    
                                // Update the transaction total
                                updateTransactionTotalBySQL(transactionID, orderTotalLabel);
                            }
                        } else if (choice == 1) {
                            // Update quantity
                            String newQuantityString = JOptionPane.showInputDialog(null, "Enter new quantity:", "Update Quantity", JOptionPane.PLAIN_MESSAGE);
                            if (newQuantityString == null || newQuantityString.isEmpty()) {
                                // User canceled or entered an empty string
                                return;
                            }
    
                            int newQuantity;
                            try {
                                newQuantity = Integer.parseInt(newQuantityString);
                                if (newQuantity <= 0) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Error: Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
    
                            // Check if there is enough product
                            if (!isThereEnoughProduct(productID, newQuantity - currentQuantity)) {
                                JOptionPane.showMessageDialog(null, "Error: Not enough product in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
    
                            // Update the item in tbl_item
                            double newSubtotal = itemPrice * newQuantity;
                            String updateQuery = "UPDATE tbl_item SET product_Quantity = ?, item_SubTotal = ? WHERE transaction_ID = ? AND product_ID = ?";
                            try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                                updatePstmt.setInt(1, newQuantity);
                                updatePstmt.setDouble(2, newSubtotal);
                                updatePstmt.setInt(3, transactionID);
                                updatePstmt.setInt(4, productID);
                                int rowsUpdated = updatePstmt.executeUpdate();
                                System.out.println("Rows updated in tbl_item: " + rowsUpdated);
    
                                // Update the stock
                                int quantityDifference = newQuantity - currentQuantity;
                                if (quantityDifference != 0) {
                                    if (quantityDifference > 0) {
                                        removeStock(productID, quantityDifference);
                                    } else {
                                        addStock(productID, -quantityDifference);
                                    }
                                }
    
                                // Update the transaction total
                                updateTransactionTotalBySQL(transactionID, orderTotalLabel);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: Product ID not found in this transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock(int productID, int quantity) {
        try (Connection conn = connect()) {
            // Fetch the current stock left
            String query = "SELECT product_StockLeft FROM tbl_product WHERE product_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, productID);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int currentStock = rs.getInt("product_StockLeft");
    
                        // Calculate the new stock after adding the specified quantity
                        int newStock = currentStock + quantity;
    
                        // Update the stock in the database
                        String updateQuery = "UPDATE tbl_product SET product_StockLeft = ? WHERE product_ID = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, newStock);
                            updateStmt.setInt(2, productID);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //----------------------------------------------------------------
    /**
     * ORDER LIST PANEL METHODS
     */
    
    

    /**
     * Updates the items table with data from the selected transaction
     */
    public void updateItemsTable(DefaultTableModel items_TableModel, int transactionId) {
        String query = "SELECT i.product_ID, p.product_Name, i.item_Price, i.product_Quantity, i.item_SubTotal " +
                    "FROM tbl_item i JOIN tbl_product p ON i.product_ID = p.product_ID " +
                    "WHERE i.transaction_ID = ?;";

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {

                items_TableModel.setRowCount(0);  // Clear the table

                while (rs.next()) {
                    int productId = rs.getInt("product_ID");
                    String productName = rs.getString("product_Name");
                    double itemPrice = rs.getDouble("item_Price");
                    int quantity = rs.getInt("product_Quantity");
                    double subTotal = rs.getDouble("item_SubTotal");

                    items_TableModel.addRow(new Object[]{productId, productName, itemPrice, quantity, subTotal});
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPaymentToTransaction() {
        try (Connection conn = connect()) {
            // Ask for transaction ID using JOptionPane
            String transactionIdStr = JOptionPane.showInputDialog("Enter Transaction ID:");
            if (transactionIdStr == null) {
                return; // User canceled the input dialog
            }
            
            int transactionId;
            try {
                transactionId = Integer.parseInt(transactionIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Transaction ID.");
                return;
            }
            
            // Check if transaction ID exists and if transaction_ActiveStatus = 'active'
            String checkQuery = "SELECT transaction_TotalPrice, transaction_TotalPaid, transaction_OrderStatus FROM tbl_transaction " +
                                "WHERE transaction_ID = ? AND transaction_ActiveStatus = 'active'";
            
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, transactionId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    double totalPrice = rs.getDouble("transaction_TotalPrice");
                    double totalPaid = rs.getDouble("transaction_TotalPaid");
                    String orderStatus = rs.getString("transaction_OrderStatus");
                    
                    if ("paid".equals(orderStatus)) {
                        JOptionPane.showMessageDialog(null, "Transaction is already paid. No further payment can be added.");
                        return;
                    }
                    
                    // Show the current transaction_TotalPrice with transaction_TotalPaid
                    String message = String.format("Total Amount: %.2f\nTotal Paid: %.2f", totalPrice, totalPaid);
                    String amountPaidStr = JOptionPane.showInputDialog(message + "\nEnter amount to pay:");
                    
                    if (amountPaidStr == null) {
                        return; // User canceled the input dialog
                    }
                    
                    double amountPaid;
                    try {
                        amountPaid = Double.parseDouble(amountPaidStr);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid amount.");
                        return;
                    }
                    
                    double newTotalPaid = totalPaid + amountPaid;
                    
                    // If amountPaid + currentTotalPaid is equal or greater than totalAmount, set transaction_OrderStatus to 'paid'
                    String updateQuery;
                    if (newTotalPaid >= totalPrice) {
                        updateQuery = "UPDATE tbl_transaction SET transaction_TotalPaid = ?, transaction_OrderStatus = 'paid' " +
                                    "WHERE transaction_ID = ?";
                    } else {
                        updateQuery = "UPDATE tbl_transaction SET transaction_TotalPaid = ? WHERE transaction_ID = ?";
                    }
                    
                    // Update transaction_TotalPaid with the new amount
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setDouble(1, newTotalPaid);
                        updateStmt.setInt(2, transactionId);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Payment updated successfully.");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Transaction ID not found or is not active.");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    
    public void removeTransaction() {
        try (Connection conn = connect()) {
            // Ask for transaction ID using JOptionPane
            String transactionIdStr = JOptionPane.showInputDialog("Enter Transaction ID:");
            if (transactionIdStr == null) {
                return; // User canceled the input dialog
            }
            
            int transactionId;
            try {
                transactionId = Integer.parseInt(transactionIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Transaction ID.");
                return;
            }
            
            // Check if transaction ID exists and if transaction_ActiveStatus = 'active'
            String checkQuery = "SELECT transaction_ActiveStatus, transaction_OrderStatus FROM tbl_transaction WHERE transaction_ID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, transactionId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String activeStatus = rs.getString("transaction_ActiveStatus");
                    String orderStatus = rs.getString("transaction_OrderStatus");
                    
                    if ("active".equals(activeStatus)) {
                        if ("paid".equals(orderStatus)) {
                            JOptionPane.showMessageDialog(null, "Transaction is already paid and cannot be canceled.");
                        } else {
                            int confirm = JOptionPane.showConfirmDialog(null, 
                                    "Do you wish to cancel this transaction?", "Confirm Cancel", 
                                    JOptionPane.YES_NO_OPTION);
                            
                            if (confirm == JOptionPane.YES_OPTION) {
                                // Set transaction_ActiveStatus = 'inactive'
                                String updateQuery = "UPDATE tbl_transaction SET transaction_ActiveStatus = 'inactive' WHERE transaction_ID = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                    updateStmt.setInt(1, transactionId);
                                    updateStmt.executeUpdate();
                                }
                                
                                // Return the items using the addStock method
                                String itemsQuery = "SELECT product_ID, product_Quantity FROM tbl_item WHERE transaction_ID = ?";
                                try (PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery)) {
                                    itemsStmt.setInt(1, transactionId);
                                    ResultSet itemsRs = itemsStmt.executeQuery();
                                    
                                    while (itemsRs.next()) {
                                        int productId = itemsRs.getInt("product_ID");
                                        int quantity = itemsRs.getInt("product_Quantity");
                                        addStock(productId, quantity);
                                    }
                                }
                                
                                JOptionPane.showMessageDialog(null, "Transaction canceled and items restocked.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Transaction is not active or does not exist.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Transaction ID not found.");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void seeFullDetailsTransaction() {
        try (Connection conn = connect()) {
            // Prompt for Transaction ID using JOptionPane
            String transactionIdStr = JOptionPane.showInputDialog("Enter Transaction ID:");
            if (transactionIdStr == null || transactionIdStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Transaction ID cannot be empty.");
                return; // User canceled the input dialog
            }
    
            int transactionId;
            try {
                transactionId = Integer.parseInt(transactionIdStr.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid Transaction ID.");
                return;
            }
    
            // Check if transaction exists and is active
            String checkQuery = "SELECT t.transaction_TotalPrice, t.transaction_TotalPaid, t.customer_ID, t.employee_ID, e.employee_FirstName, e.employee_LastName " +
                                "FROM tbl_transaction t " +
                                "JOIN tbl_employee e ON t.employee_ID = e.employee_ID " +
                                "WHERE t.transaction_ID = ? AND t.transaction_ActiveStatus = 'active'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, transactionId);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    double totalPrice = rs.getDouble("transaction_TotalPrice");
                    double totalPaid = rs.getDouble("transaction_TotalPaid");
                    int customerId = rs.getInt("customer_ID");
                    String cashierFirstName = rs.getString("employee_FirstName");
                    String cashierLastName = rs.getString("employee_LastName");
                    double amountLeftToPay = totalPrice - totalPaid;
    
                    // Get customer details
                    String customerQuery = "SELECT customer_FirstName, customer_LastName, customer_ContactNum " +
                                        "FROM tbl_customer WHERE customer_ID = ?";
                    try (PreparedStatement customerStmt = conn.prepareStatement(customerQuery)) {
                        customerStmt.setInt(1, customerId);
                        ResultSet customerRs = customerStmt.executeQuery();
    
                        if (customerRs.next()) {
                            String firstName = customerRs.getString("customer_FirstName");
                            String lastName = customerRs.getString("customer_LastName");
                            String contactNum = customerRs.getString("customer_ContactNum");
    
                            // Get item details
                            String itemsQuery = "SELECT i.product_ID, p.product_Name, i.item_Price, i.product_Quantity, i.item_SubTotal " +
                                                "FROM tbl_item i JOIN tbl_product p ON i.product_ID = p.product_ID " +
                                                "WHERE i.transaction_ID = ?";
                            StringBuilder itemsDetails = new StringBuilder();
                            try (PreparedStatement itemsStmt = conn.prepareStatement(itemsQuery)) {
                                itemsStmt.setInt(1, transactionId);
                                ResultSet itemsRs = itemsStmt.executeQuery();
    
                                while (itemsRs.next()) {
                                    int productId = itemsRs.getInt("product_ID");
                                    String productName = itemsRs.getString("product_Name");
                                    double itemPrice = itemsRs.getDouble("item_Price");
                                    int quantity = itemsRs.getInt("product_Quantity");
                                    double subTotal = itemsRs.getDouble("item_SubTotal");
    
                                    itemsDetails.append(String.format("Product ID: %d, Name: %s, Price: %.2f, Quantity: %d, Subtotal: %.2f%n", 
                                        productId, productName, itemPrice, quantity, subTotal));
                                }
                            }
    
                            // Display full details
                            String message = String.format("Customer Name: %s %s%nContact Number: %s%nTotal Amount: %.2f%nTotal Paid: %.2f%nAmount Left to Pay: %.2f%nCashier: %s %s%nItems:%n%s", 
                                firstName, lastName, contactNum, totalPrice, totalPaid, amountLeftToPay, cashierFirstName, cashierLastName, itemsDetails.toString());
                            JOptionPane.showMessageDialog(null, message, "Transaction Details", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Customer not found.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Transaction ID not found or is not active.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    public boolean isTransactionActive(int transactionId) {
        try (Connection conn = connect()) {
            String query = "SELECT transaction_ActiveStatus FROM tbl_transaction WHERE transaction_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String activeStatus = rs.getString("transaction_ActiveStatus");
                    return "active".equals(activeStatus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurs or if transaction is not active
    }

    public boolean isTransactionPending(int transactionId) {
        try (Connection conn = connect()) {
            String query = "SELECT transaction_OrderStatus FROM tbl_transaction WHERE transaction_ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String orderStatus = rs.getString("transaction_OrderStatus");
                    return "pending".equals(orderStatus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurs or if order status is not pending
    }

    public void updateTransactionTableBySQL(int transactionID, DefaultTableModel transactionTableModel) {
        // Clear the table before updating
        transactionTableModel.setRowCount(0);
    
        // SQL query to select items from the transaction table
        String query = "SELECT * FROM tbl_item WHERE transaction_ID = ?";
    
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transactionID);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                // Iterate through the result set and add rows to the table model
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("product_id");
                    String itemName = getItemName(itemID); 
                    int quantity = resultSet.getInt("product_quantity");
                    double price = resultSet.getDouble("item_price");
                    double subtotal = price * quantity;

                    System.out.println(itemID + " " + itemName + " " + quantity + " " + price + " " + subtotal);
    
                    // Add row to the table model
                    transactionTableModel.addRow(new Object[]{itemID, itemName, quantity, price, subtotal});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getItemName(int itemID) {
        String itemName = null;
        String query = "SELECT product_name FROM tbl_product WHERE product_id = ?";
    
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    itemName = resultSet.getString("product_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return itemName;
    }
    
    public void updateNewTransaction(int transactionID) {
        String selectSQL = "SELECT SUM(item_subtotal) as total FROM tbl_item WHERE transaction_id = ?";
        String updateSQL = "UPDATE tbl_transaction SET transaction_totalPrice = ? WHERE transaction_id = ?";
        
        try (Connection conn = connect();
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
            
            selectStmt.setInt(1, transactionID);
            
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                double newTotal = rs.getDouble("total");
                
                updateStmt.setDouble(1, newTotal);
                updateStmt.setInt(2, transactionID);
                updateStmt.executeUpdate();
                System.out.println("Successfully updated transaction");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelCurrentTransaction(int transactionID) {
        System.out.println("Canceling transaction ID: " + transactionID);
        try (Connection conn = connect()) {
            String cancelQuery = "UPDATE tbl_transaction SET transaction_ActiveStatus = 'inactive' WHERE transaction_ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(cancelQuery)) {
                pstmt.setInt(1, transactionID);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Transaction cancel rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Error handling: Display appropriate error message in your GUI
        }
    }
}
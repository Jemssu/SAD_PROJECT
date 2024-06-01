import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Operations {

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
    
                // Update database
                try (Connection connection = connect()) {
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
                // Confirm if user wants to empty stock
                int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to empty the stock for this product?", "Confirm Empty Stock", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Update database to set stock left to 0
                    try (Connection connection = connect()) {
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
    

    // WORK IN PROGRESS / FUTURE ADDITIONS

    /**
     *  Might do some tweaks n stuffs sa ADDING A PRODUCT na button
     */

    /**
     * ABILITY TO DO THE FOLLOWING
     * 
     * -> ADD MORE TYPE / ID FOR ADMIN
     * -> ADD MORE SIZE / ID FOR ADMIN
     * -> ADD MORE SUPPLIER / ID FOR ADMIN
     * 
     *  -> ADD LOGGING / WHO AND WHEN DID A PERSON GO IN, maybe through text file...
     */
}

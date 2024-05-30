import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

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

    /**DESCRIPTION
     * 
     */
    public void addProduct() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void renameProduct() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void changePriceOfProduct() { 
        // 
    }

    /**DESCRIPTION
     * 
     */
    public void deleteProduct() { 
        // 
    }

    public void updateProductTable(DefaultTableModel tableModel) {
        // Clear the existing rows
        tableModel.setRowCount(0);

        // SQL query to select data from tbl_product
        String query = "SELECT product_ID, product_Name, product_Price FROM tbl_product";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set and add rows to the table model
            while (rs.next()) {
                int productID = rs.getInt("product_ID");
                String productName = rs.getString("product_Name");
                double productPrice = rs.getDouble("product_Price");
                tableModel.addRow(new Object[]{productID, productName, productPrice});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

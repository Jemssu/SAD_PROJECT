import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColumnsAutoSizer {

    public static void sizeColumnsToFit(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            int preferredWidth = 50; // Minimum width
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                Component component = table.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                preferredWidth = Math.max(component.getPreferredSize().width + table.getIntercellSpacing().width, preferredWidth);
            }
            table.getColumnModel().getColumn(columnIndex).setPreferredWidth(preferredWidth);
        }
    }
}
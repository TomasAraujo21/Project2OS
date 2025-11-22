/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import SYS.*;
import DS.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 *
 * @author tomasaraujo
 */
public class FilesTable extends JFrame {
    private FileSystem fileSystem;

    public FilesTable(FileSystem fs) {
        this.fileSystem = fs;

        setTitle("Tabla de Archivos");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTable table = new JTable();

        String[] columns = {
                "Nombre",
                "Bloques",
                "Primer Bloque",
                "Color",
                "Proceso Creador"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // La tabla es solo de lectura
            }
        };

        table.setModel(model);
        table.setRowHeight(30);

        // Render personalizado para la columna de color
        table.getColumn("Color").setCellRenderer(new ColorCellRenderer());

        // Llenar tabla con todos los archivos
        fillTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    private void fillTable(DefaultTableModel model) {
        addDirectoryFiles(model, fileSystem.getRoot());
    }

    // Recorre todos los directorios recursivamente y añade sus archivos
    private void addDirectoryFiles(DefaultTableModel model, Directory dir) {
        Node<MyFile> fileNode = dir.getFiles().getHead();

        while (fileNode != null) {
            MyFile f = fileNode.getData();

            model.addRow(new Object[]{
                    f.getName(),
                    f.getSize(),
                    f.getFirstBlock(),
                    f.getColor(),         // Se usa el renderer para mostrar el color
                    ""                    // Proceso creador (futuro)
            });

            fileNode = fileNode.getNext();
        }

        // Subdirectorios recursivos
        Node<Directory> subNode = dir.getSubdirectories().getHead();
        while (subNode != null) {
            addDirectoryFiles(model, subNode.getData());
            subNode = subNode.getNext();
        }
    }

    // ================================
    //  RENDERER PARA MOSTRAR EL COLOR
    // ================================
    private static class ColorCellRenderer extends JLabel implements TableCellRenderer {

        public ColorCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            String colorName = (value != null) ? value.toString().toUpperCase() : "";

            Color c = switch (colorName) {
                case "AZUL" -> new Color(66, 133, 244);
                case "ROJO" -> new Color(219, 68, 55);
                case "VERDE" -> new Color(15, 157, 88);
                case "AMARILLO" -> new Color(244, 180, 0);
                default -> Color.LIGHT_GRAY;
            };

            setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
            setText(colorName);

            return this;
        }
    }
}

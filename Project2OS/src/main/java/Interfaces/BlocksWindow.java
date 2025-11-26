/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import SYS.*;
import DS.*;
import Storage.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author tomasaraujo
 */
public class BlocksWindow extends JFrame {

    private final FileSystem fileSystem;

    public BlocksWindow(FileSystem fs) {
        this.fileSystem = fs;

        setTitle("Bloques del Disco");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        // Grid con 16 columnas
        JPanel grid = new JPanel(new GridLayout(0, 16, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(Color.WHITE);

        Disk disk = fileSystem.getDisk();
        int total = disk.getTotalBlocks();
        boolean[] busy = disk.getBusy();

        // Para saber qué archivo ocupa cada bloque
        MyFile[] owners = new MyFile[total];

        // Rellenamos owners siguiendo el encadenamiento de bloques
        fillBlockOwners(fileSystem.getRoot(), owners, disk);

        for (int i = 0; i < total; i++) {
            String txt;
            if (!busy[i]) {
                txt = "#" + i;
            } else if (owners[i] != null) {
                txt = owners[i].getName();
            } else {
                txt = "???";
            }

            BlockLabel lbl = new BlockLabel(txt);

            // Elegir color de fondo
            if (!busy[i]) {
                // Bloque libre: gris muy claro
                lbl.setBackground(new Color(235, 235, 235));
                lbl.setForeground(Color.DARK_GRAY);
            } else {
                MyFile f = owners[i];
                Color base = Color.LIGHT_GRAY;
                if (f != null && f.getColor() != null) {
                    base = mapColorName(f.getColor());
                }
                // “50% de opacidad”: mezclamos con blanco para suavizar
                Color soft = mixWithWhite(base, 0.5);
                lbl.setBackground(soft);
                lbl.setForeground(Color.BLACK);
            }

            lbl.setPreferredSize(new Dimension(45, 45));
            grid.add(lbl);
        }

        add(new JScrollPane(grid));
    }

    private void fillBlockOwners(Directory dir, MyFile[] owners, Disk disk) {

        // Archivos en este directorio
        Node<MyFile> fNode = dir.getFiles().getHead();
        while (fNode != null) {
            MyFile f = fNode.getData();

            int block = f.getFirstBlock();
            int remaining = f.getSize();

            while (block != -1 && remaining > 0) {
                if (block >= 0 && block < owners.length) {
                    owners[block] = f;
                }
                block = disk.getNextBlock(block);
                remaining--;
            }

            fNode = fNode.getNext();
        }

        // Subdirectorios (recursivo)
        Node<Directory> dNode = dir.getSubdirectories().getHead();
        while (dNode != null) {
            fillBlockOwners(dNode.getData(), owners, disk);
            dNode = dNode.getNext();
        }
    }

    private Color mapColorName(String name) {
        if (name == null) return Color.LIGHT_GRAY;
        String c = name.trim().toUpperCase();

        return switch (c) {
            case "AZUL" -> new Color(66, 133, 244);
            case "ROJO" -> new Color(219, 68, 55);
            case "VERDE" -> new Color(15, 157, 88);
            case "AMARILLO" -> new Color(244, 180, 0);
            default -> Color.LIGHT_GRAY;
        };
    }


    private Color mixWithWhite(Color base, double factor) {
        factor = Math.max(0.0, Math.min(1.0, factor));
        int r = (int) (base.getRed()   * factor + 255 * (1 - factor));
        int g = (int) (base.getGreen() * factor + 255 * (1 - factor));
        int b = (int) (base.getBlue()  * factor + 255 * (1 - factor));
        return new Color(r, g, b);
    }

    /**
     * Label con fondo redondeado (corner radius 15).
     */
    private static class BlockLabel extends JLabel {
        private final int arc = 10;

        public BlockLabel(String text) {
            super(text, SwingConstants.CENTER);
            setOpaque(false);          // No que pinte fondo por defecto
            setFont(new Font("SansSerif", Font.PLAIN, 11));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = getBackground();
            if (bg != null) {
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            }

            g2.setColor(new Color(180, 180, 180));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            g2.dispose();

            // Pinta el texto centrado
            super.paintComponent(g);
        }
    }
}

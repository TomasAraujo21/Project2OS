/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import DS.*;
import SYS.*;

/**
 *
 * @author tomasaraujo
 */

public class TreeManager {

    private final FileSystem fileSystem;     
    private JTree tree;                      
    private DefaultTreeModel model;          
    private Directory selectedDirectory = null;

    // NUEVO: para selección de archivos
    private MyFile selectedFile = null;                    
    private Directory selectedFileDirectory = null;        

    public TreeManager(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.tree = new JTree();
    }

    public void buildTree() {
        Directory rootDir = fileSystem.getRoot();

        // CAMBIO: seguimos usando el Directory como userObject
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDir);

        buildSubtree(rootNode, rootDir);

        if (model == null) {
            model = new DefaultTreeModel(rootNode);
            tree.setModel(model);

            addSelectionListener();   // aquí se maneja qué está seleccionado

        } else {
            model.setRoot(rootNode);
        }

        model.reload();
    }

    private void addSelectionListener() {
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                if (node == null) return;

                Object userObj = node.getUserObject();

                // CAMBIO: ahora distinguimos entre directorio y archivo
                if (userObj instanceof Directory) {
                    selectedDirectory = (Directory) userObj;
                    selectedFile = null;
                    selectedFileDirectory = null;

                } else if (userObj instanceof MyFile) {
                    selectedFile = (MyFile) userObj;

                    // buscamos el padre para saber en qué directorio está este archivo
                    DefaultMutableTreeNode parent =
                            (DefaultMutableTreeNode) node.getParent();

                    if (parent != null && parent.getUserObject() instanceof Directory) {
                        selectedFileDirectory = (Directory) parent.getUserObject();
                    } else {
                        selectedFileDirectory = null;
                    }

                    // al seleccionar un archivo no nos interesa el selectedDirectory "normal"
                    selectedDirectory = null;
                } else {
                    selectedDirectory = null;
                    selectedFile = null;
                    selectedFileDirectory = null;
                }
            }
        });
    }

    private void buildSubtree(DefaultMutableTreeNode parentNode, Directory dir) {

        Node<Directory> curDirNode = dir.getSubdirectories().getHead();
        while (curDirNode != null) {
            Directory sub = curDirNode.getData();
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(sub);
            parentNode.add(subNode);
            buildSubtree(subNode, sub);
            curDirNode = curDirNode.getNext();
        }

        Node<MyFile> curFileNode = dir.getFiles().getHead();
        while (curFileNode != null) {
            MyFile f = curFileNode.getData();
            DefaultMutableTreeNode fNode = new DefaultMutableTreeNode(f);
            parentNode.add(fNode);
            curFileNode = curFileNode.getNext();
        }
    }

    public void refresh() {
        buildTree();
    }

    public void showTree() {
        if (model == null) {
            buildTree();
        }

        JFrame frame = new JFrame("Explorador de Directorios");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);

        frame.add(new JScrollPane(tree));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public Directory getSelectedDirectory() {
        return selectedDirectory;
    }

    public MyFile getSelectedFile() {
        return selectedFile;
    }

    public Directory getSelectedFileDirectory() {
        return selectedFileDirectory;
    }

    public void openDirectoryChooser(java.util.function.Consumer<Directory> onSelected) {
        if (model == null) {
            buildTree();
        } else {
            refresh();
        }

        JFrame frame = new JFrame("Seleccionar directorio");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);

        JScrollPane scrollPane = new JScrollPane(tree);
        frame.add(scrollPane, java.awt.BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnSelect = new JButton("Seleccionar");
        JButton btnCancel = new JButton("Cancelar");
        bottom.add(btnSelect);
        bottom.add(btnCancel);

        frame.add(bottom, java.awt.BorderLayout.SOUTH);

        btnSelect.addActionListener(ev -> {
            Directory dir = selectedDirectory;

            if (dir == null) {
                dir = fileSystem.getRoot();
            }

            if (dir != null && onSelected != null) {
                onSelected.accept(dir);
            }
            frame.dispose();
        });

        btnCancel.addActionListener(ev -> frame.dispose());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void openFileChooser(java.util.function.BiConsumer<Directory, MyFile> onSelected) {
        if (model == null) {
            buildTree();
        } else {
            refresh();
        }

        JFrame frame = new JFrame("Seleccionar archivo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);

        JScrollPane scrollPane = new JScrollPane(tree);
        frame.add(scrollPane, java.awt.BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton btnSelect = new JButton("Seleccionar");
        JButton btnCancel = new JButton("Cancelar");
        bottom.add(btnSelect);
        bottom.add(btnCancel);
        frame.add(bottom, java.awt.BorderLayout.SOUTH);

        btnSelect.addActionListener(ev -> {
            MyFile f = selectedFile;
            Directory dir = selectedFileDirectory;

            if (f == null || dir == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        frame,
                        "Por favor, selecciona un archivo.",
                        "Ningún archivo seleccionado",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (onSelected != null) {
                onSelected.accept(dir, f);
            }
            frame.dispose();
        });

        btnCancel.addActionListener(ev -> frame.dispose());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
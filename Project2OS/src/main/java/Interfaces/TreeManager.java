/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;
// TreeManager.java
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

    public TreeManager(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.tree = new JTree();   // CAMBIO
    }

    public void buildTree() {
        Directory rootDir = fileSystem.getRoot();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDir);

        buildSubtree(rootNode, rootDir);

        if (model == null) {
            model = new DefaultTreeModel(rootNode);
            tree.setModel(model);

            addSelectionListener();

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

                if (userObj instanceof Directory) {
                    selectedDirectory = (Directory) userObj;
                } else {
                    selectedDirectory = null;
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
}
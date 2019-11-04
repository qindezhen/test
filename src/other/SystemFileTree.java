package other;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.tree.TreePath;
public class SystemFileTree {
    private DefaultTreeModel model;
    private JTree tree;
    public SystemFileTree () {
        JFrame f=new JFrame();
        f.getContentPane().setLayout(new BorderLayout());
        tree=new JTree();
       tree.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                node_mouseAction(e);
            }
        });
        JScrollPane scroll=new JScrollPane(tree);
        f.getContentPane().add(scroll,BorderLayout.CENTER);
        f.setLocation(250,250);
        f.setSize(new Dimension(300,500));
        f.setVisible(true);
    }
    private void node_mouseAction(MouseEvent e){
        int row = tree.getRowForLocation(e.getX(), e.getY());
        PathNode pathNode =null;
        if(row != -1){
        TreePath path = tree.getPathForRow(row);
        pathNode = (PathNode)path.getLastPathComponent();
        if(pathNode.isFolder()&&pathNode.getChildCount()==0){
            builderNode(pathNode);
            tree.expandPath(path);
        }
        }
    }
    private PathNode builderNode(PathNode pathNode){
        String filePath= pathNode.getValue().toString();
        File file=new File(filePath);
        File[] files=file.listFiles();
       for(int i=0;i<files.length;i++){
           PathNode node=new PathNode(files[i].getName(), files[i].getAbsolutePath(),files[i].isDirectory());
           pathNode.add(node);
       }
        return pathNode;
    }
    private void initData(String rootPath){
        File f=new File(rootPath);
        PathNode root=new PathNode(f.getName(), rootPath,f.isDirectory());
        File[] files=f.listFiles();
        for(int i=0;i<files.length;i++){
         PathNode node=
             new PathNode(files[i].getName(), files[i].getAbsolutePath(),files[i].isDirectory());
            root.add(node);
        }
   model=new DefaultTreeModel(root);
        tree.setModel(model);
        FileTreeRenderer renderer=new FileTreeRenderer();
        tree.setCellRenderer(renderer);
        tree.repaint();
}
    class FileTreeRenderer implements TreeCellRenderer{
        private Icon folder_open=new ImageIcon("icons/folder_open.jpg");
        private Icon folder_close=new ImageIcon("icons/folder_close.jpg");
        private Icon file=new ImageIcon("icons/file.gif");
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            JLabel label = null;
           if (value != null) {
                System.out.println(value.getClass().toString());
                if(value instanceof PathNode){
              PathNode pathNode = (PathNode) value;
                    if (pathNode.isFolder()) {
                        if (expanded) {
                            label = new JLabel(pathNode.getUserObject().
                                               toString(),
                                               folder_open, JLabel.RIGHT);
                        } else if(!expanded||leaf) {
                            label = new JLabel(pathNode.getUserObject().
                                               toString(),
                                               folder_close, JLabel.RIGHT);
                        }
                    } else {
                        label = new JLabel(pathNode.getUserObject().toString(),
                                           file, JLabel.RIGHT);
                    }
                    return label;
                }
            }
            return label;
        }
}
    @SuppressWarnings("serial")
	class PathNode extends DefaultMutableTreeNode{
        Object value;
        boolean isFolder;
       public PathNode(String name,Object value,boolean isFolder){
           super(name);
           this.value=value;
           this.isFolder=isFolder;
        }
        public Object getValue(){
          return value;
        }
        public boolean isFolder(){
            return isFolder;
        }
    }
    public static void main(String args[]){
    	SystemFileTree tree=new SystemFileTree();
         // 给个路径作演示
        tree.initData("E:/");
    }
}
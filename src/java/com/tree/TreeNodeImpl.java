package com.tree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import toolprod.Server;
import toolprod.TreeNode;



public class TreeNodeImpl {

    private final Log log = LogFactory.getLog(this.getClass());


    public TreeNode<Server> search(TreeNode<Server> searchNode, Server server) {
        TreeNode result = null;

        final String name = server.getName();
        final String port = server.getPortNumber().toString();

        Comparable<Server> searchCriteria = new Comparable<Server>() {
            @Override
            public int compareTo(Server serv) {
                if (serv == null)
                    return 1;
                boolean nodeOk = serv.getName().equals(name);
                if (nodeOk) {
                    nodeOk = serv.getPortNumber().equals(port);
                }
                return nodeOk ? 0 : 1;
            }
        };

        TreeNode treeRoot = getRoot(searchNode.parent);
        if ( treeRoot != null) {
            result = treeRoot.findTreeNode(searchCriteria);
        }
        System.out.println("Found: " + result);
        return result;
    }

    public TreeNode<Server> getRoot(TreeNode<Server> searchNode) {
        TreeNode parent = searchNode;

        if (searchNode == null) {
            log.warn("Can't getRoot of null object");
            return null;
        }

        if (searchNode.parent != null) {
            parent = getRoot(searchNode.parent);
        }

        return parent;

    }

}

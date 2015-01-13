package fr.edu.toolprod.gson

import fr.edu.toolprod.bean.ServerBean

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 12/12/14
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
class GSONBean {

    String name;

    ServerBean nodeData;

    GSONBean parent;

    List<GSONBean> children;

    List<GSONBean> elementsIndex;

    public GSONBean(ServerBean nodeData) {
        this.nodeData = nodeData;
        this.children = new LinkedList<GSONBean>();
        this.elementsIndex = new LinkedList<GSONBean>();
        this.elementsIndex.add(this);
    }

    public GSONBean addChild(ServerBean child) {
        GSONBean childNode = new GSONBean(child);
        childNode.parent = this;

        if (children == null) {
            this.children = new LinkedList<GSONBean>();
        }
        if (elementsIndex == null) {
            elementsIndex = new LinkedList<GSONBean>();
        }
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    private void registerChildForSearch(GSONBean node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }



    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

}

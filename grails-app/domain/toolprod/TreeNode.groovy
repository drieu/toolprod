package toolprod

import com.tree.TreeNodeIter


class TreeNode<T>  {

    public T data;

    public TreeNode<T> parent;

    static hasMany = [children : TreeNode, elementsIndex: TreeNode]


    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
        this.elementsIndex = new LinkedList<TreeNode<T>>();
        this.elementsIndex.add(this);
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        childNode.save()
        System.out.println("ChildNode:" + childNode)

        if (children == null) {
            this.children = new LinkedList<TreeNode<T>>();
        }
        if (elementsIndex == null) {
            elementsIndex = new LinkedList<TreeNode<T>>();
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

    private void registerChildForSearch(TreeNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }

    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
        for (TreeNode<T> element : this.elementsIndex) {
            T elData = element.data;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }

//    @Override
//    public Iterator<TreeNode<T>> iterator() {
////        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
////        return iter;
//        return null
//    }


    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    static constraints = {
    }
}

package toolprod

/**
 * Treenode used to store link between servers and applications into a tree.
 */
class TreeNode  {

    String name

    String port

    Server nodeData;

    TreeNode parent;

    static hasMany = [children : TreeNode, elementsIndex: TreeNode]

    static constraints = {
       name(nullable: true)
       port(nullable: true)
       nodeData(nullable: true)
       parent(nullable: true)
    }

//    static belongsTo = [ Server, TreeNode ]


    public TreeNode(Server nodeData) {
        this.nodeData = nodeData;
        this.children = new LinkedList<TreeNode>();
        this.elementsIndex = new LinkedList<TreeNode>();
        this.elementsIndex.add(this);
    }

    public TreeNode addChild(Server child) {
        TreeNode childNode = new TreeNode(child);
        childNode.parent = this;
        childNode.save(failOnError: true, flush:true)

        if (children == null) {
            this.children = new LinkedList<TreeNode>();
        }
        if (elementsIndex == null) {
            elementsIndex = new LinkedList<TreeNode>();
        }
        log.info("addChild() elementsIndex:" + elementsIndex)

        this.children.add(childNode);
        //this.registerChildForSearch(childNode);
        return childNode;
    }

    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    /**
     * @unused too heavy !
     * @param node
     */
    private void registerChildForSearch(TreeNode node) {
        log.info("registerChildForSearch() :" + elementsIndex)
        if (elementsIndex == null) {
            elementsIndex = new LinkedList<TreeNode>();
        }
        elementsIndex.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }

    public TreeNode findTreeNode(Server server) {
        for (TreeNode element : this.elementsIndex) {
            log.info("ICI element name:" + element.name)
            Server elData = element.nodeData;
            if (server.compareTo(elData) == 0)
                return element;
        }
        log.info("Nothing found !")
        return null;
    }

//    public TreeNode findTreeNode(Comparable cmp) {
//        for (TreeNode element : this.elementsIndex) {
//            Server elData = element.nodeData;
//            if (cmp.compareTo(elData) == 0)
//                return element;
//        }
//
//        return null;
//    }

    @Override
    public String toString() {
        return nodeData != null ? nodeData.toString() : "[nodeData null]";
    }

//    @Override
//    public Iterator<TreeNode> iterator() {
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


    public static String getNodesPath(Server server) {
        String result = ""
        List<TreeNode> nodes = TreeNode.findAllByNodeData(server)
        for(TreeNode node : nodes) {
            if (node != null) {
                result += node.nodeData?.name
                boolean bParent = true
                TreeNode parent = node.parent
                while(bParent == true) {
                    result += "->" + parent?.nodeData?.name
                    if (parent != null) {
                        parent = parent.parent
                    } else {
                        bParent = false
                    }
                }
            }
        }
        return result;
    }

}

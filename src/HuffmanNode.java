public class HuffmanNode {
    Pixel key = null;
    HuffmanNode left = null, right = null;
    boolean keyNull = true;
    int ctr = 0;
    String code = "";

    public HuffmanNode(Pixel key) {
        this.key = key;
        this.keyNull = false;
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.left = left;
        this.right = right;
    }

    public boolean isKeyNull() {
        return this.key == null;
    }
}
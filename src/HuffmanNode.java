public class HuffmanNode {
    Pixel key = null;
    HuffmanNode left = null, right = null;
    boolean keyNull = true;
    int ctr = 0;
    String code = "";
    static int counter = 0;

    public HuffmanNode(Pixel key) {
        this.key = key;
        this.keyNull = false;
        counter++;
    }

    public HuffmanNode(HuffmanNode left, HuffmanNode right) {
        this.left = left;
        this.right = right;
    }

    public boolean isKeyNull() {
        try {
            this.key.getRGB();
            return false;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
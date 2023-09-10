import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImageLoader extends JPanel {
    HuffmanNode[] counters = null;
    HuffmanNode tree = null;
    HuffmanNode node = null;
    int height, width;
    int width2, height2;
    BufferedImage image = null;
    GUI gui;

    public void setGUI(GUI gui) {

        this.gui = gui;
    }

    public BufferedImage getImage(String file) {

        try {
            String file1 = file.substring(0, file.length() - 3);
            ;
            file1 += "IJK";
            BufferedReader reader = new BufferedReader(new FileReader(file1));
            String s = reader.readLine();
            height = Integer.parseInt(s);
            s = reader.readLine();
            width = Integer.parseInt(s);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            if (width > height) {
                width2 = 1000;
                height2 = (int) (((float) this.height / (float) this.width) * 1000.0);
                if (height2 > 690) {
                    height2 = 690;
                    width2 = (int) (((float) this.width / (float) this.height) * 750.0);
                }
            } else if (height > width) {
                height2 = 690;
                width2 = (int) (((float) this.width / (float) this.height) * 750.0);
            } else {
                height2 = 690;
                width2 = 690;
            }
            System.out.println("Creating Pixels Done");
            gui.display("Creating Pixels Done");
            System.out.println("Loading Image");
            gui.display("Loading Image");
            counters = getCounters(file);
            tree = ImageReader.createHuffmanTree(counters);
            getBinaryCode(file);
        } catch (IOException ignored) {
        }

        return image;
    }

    public static HuffmanNode[] getCounters(String file) throws IOException {
        file = file.substring(0, file.length() - 3);
        file += "HUFF";

        int line, fileLength = 0;
        HuffmanNode[] counters = null;
        int ctr = 0;
        short[] dataByte = null;
        InputStreamReader reader = null;
        try {
            System.out.println("Initiating Huff File Reading");
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            while ((line = reader.read()) != -1) {
                fileLength++;
            }
            reader.close();
            dataByte = new short[fileLength];
            counters = new HuffmanNode[(int) ((float) fileLength / 5.0)];
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            int i = 0;
            while ((line = reader.read()) != -1) {
                dataByte[i++] = (short) line;
            }
        } finally {
            assert reader != null;
            reader.close();
        }
        System.out.println("Reading Huff File");
        for (int i = 0; i < dataByte.length - 2;) {
            counters[ctr] = new HuffmanNode(
                    new Pixel((int) dataByte[i++], (int) dataByte[i++], (int) dataByte[i++], 0, 0));
            String ctr1 = Integer.toBinaryString((int) dataByte[i++]),
                    ctr2 = Integer.toBinaryString((int) dataByte[i++]), zeroes = "00000000";
            ctr1 = zeroes.substring(0, (ctr1.length() < 8 ? 8 - ctr1.length() : 0)) + ctr1;
            ctr2 = zeroes.substring(0, (ctr2.length() < 8 ? 8 - ctr2.length() : 0)) + ctr2;
            counters[ctr].ctr = Integer.parseInt(ctr1 + ctr2, 2);
            ctr++;
        }
        HuffmanNode[] buffer = new HuffmanNode[ctr];
        System.arraycopy(counters, 0, buffer, 0, buffer.length);
        counters = null;
        counters = new HuffmanNode[buffer.length];
        counters = buffer;
        System.out.println("Counters = " + counters.length + ", Actual = " + ctr);
        System.out.println("Huff File Reading Done");
        return counters;
    }

    public void getBinaryCode(String file) throws IOException {
        file = file.substring(0, file.length() - 3);
        file += "IJK";
        int line;
        int x = 0, y = 0;
        Pixel rgb = null;
        System.out.println("Reading Image File");
        gui.display("Reading Image File");
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            int lineNo = 0;
            while (lineNo != 2) {
                int g = reader.read();
                if (g == '\n') {
                    lineNo++;
                }
            }
            node = tree;
            System.out.println("Printing Image File");
            gui.display("Printing Image File");
            while ((line = reader.read()) != -1) {
                StringBuilder character = new StringBuilder(Integer.toBinaryString(line));
                while (character.length() < 7) {
                    character.insert(0, "0");
                }
                for (int i = 0; i < character.length(); i++) {
                    if (character.charAt(i) == '1') {
                        node = node.right;
                    } else if (character.charAt(i) == '0') {
                        node = node.left;
                    }
                    if (!node.isKeyNull()) {
                        rgb = node.key;
                        try {
                            image.setRGB(x++, y, rgb.getRGB());
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                        if (x >= width) {
                            x = 0;
                            y++;
                        }
                        repaint();
                        node = tree;
                    }
                }
            }
            System.out.println("Printing Image File Done");
            gui.display("Printing Image File Done");
        }
        System.out.println("Reading Image File Done");
        gui.display("Reading Image File Done");
        file = file.substring(0, file.length() - 4);
        file += "(compressed).png";
        File outImage = new File(file);
        ImageIO.write(image, "png", outImage);
    }
}
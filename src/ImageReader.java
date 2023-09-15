import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

public class ImageReader{
    BufferedImage image = null;
    Raster raster = null;
    HuffmanNode[] counters = null;
    int distinctColorCtr = 0;
    HuffmanNode tree = null;
    GUI gui;

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    public void assignTree() {
        gui.display("Generating Tree");
        tree = createHuffmanTree(counters);
        codeAssignment(tree, "");
        gui.display("Tree Generated");
    }

    public void loadImage(String file){
        reset();
        try{
            image = ImageIO.read(new File(file));
            raster = image.getRaster();
        }catch(IOException e){return;}
        System.out.println("Image height = " + raster.getHeight() + ", width = " + raster.getWidth());
        gui.display("Image height = " + raster.getHeight() + ", width = " + raster.getWidth());
    }

    public void reset(){
        raster = null;
        counters = null;
        distinctColorCtr = 0;
        tree = null;
    }

    public void countFreq(){
        System.out.println("Counting Distinct Colors");
        gui.display("Counting Distinct Colors");
        counters = new HuffmanNode[raster.getHeight()*raster.getWidth()];
        Pixel pixel;
        for(int i = 0; i < raster.getHeight(); i++){
            for(int j = 0; j < raster.getWidth(); j++){
                System.out.println("Currently on i = " + i + " and j = " + j);
                int[] buffer = new int[4];
                buffer = raster.getPixel(j, i, buffer);
                pixel = new Pixel(buffer[0], buffer[1], buffer[2], j, i);
                int start = 0, end = distinctColorCtr-1, mid = (int)((float)(start+end)/2.0);
                if(distinctColorCtr == 0){
                    counters[distinctColorCtr] = new HuffmanNode(pixel);
                    counters[distinctColorCtr].ctr = 1;
                    distinctColorCtr++;
                    continue;
                }
                while(true){
                    if(counters[mid].key.getRGB() == pixel.getRGB()){
                        counters[mid].ctr++;
                        break;
                    }else if(counters[mid].key.getRGB() < pixel.getRGB()){
                        start = mid+1;
                        mid = (int)((float)(start+end)/2.0);
                    }else if(counters[mid].key.getRGB() > pixel.getRGB()){
                        end = mid-1;
                        mid = (int)((float)(start+end)/2.0);
                    }
                    if(start > end){
                        if(counters[mid].key.getRGB() < pixel.getRGB()){
                            for(int k = distinctColorCtr; k > mid+1; k--){
                                counters[k] = counters[k-1];
                            }
                            counters[mid+1] = new HuffmanNode(pixel);
                            counters[mid+1].ctr = 1;
                            distinctColorCtr++;
                        }else if(counters[mid].key.getRGB() > pixel.getRGB()){
                            for(int k = distinctColorCtr; k > mid; k--){
                                counters[k] = counters[k-1];
                            }
                            counters[mid] = new HuffmanNode(pixel);
                            counters[mid].ctr = 1;
                            distinctColorCtr++;
                        }
                        break;
                    }
                }
            }
        }
        System.out.println("Number of Pixels Created: " + Pixel.counter);
        gui.display("Number of Pixels Created: " + Pixel.counter);
        System.out.println("Number of HuffmanNode Created: " + HuffmanNode.counter);
        gui.display("Number of HuffmanNode Created: " + HuffmanNode.counter);
        System.out.println("Distinct Colors Counted");
        gui.display("Distinct Colors Counted");
        System.out.println("Counters length = " + counters.length);
        HuffmanNode[] buffer = new HuffmanNode[distinctColorCtr];
        System.arraycopy(counters, 0, buffer, 0, distinctColorCtr);
        counters = buffer;
        System.out.println("Counters length = " + counters.length);
    }

    public static HuffmanNode createHuffmanTree(HuffmanNode[] counters){
        System.out.println("Generating Tree");
        PriorityQueue prioQueue = new PriorityQueue();
        System.out.println("Generating Queue");
        for (HuffmanNode counter : counters) {
            prioQueue.enqueue(counter);
        }
        System.out.println("Queue Generated");
        while(prioQueue.list.length > 1){
            HuffmanNode emptyNode = new HuffmanNode(prioQueue.dequeue(), prioQueue.dequeue());
            emptyNode.ctr = emptyNode.left.ctr + emptyNode.right.ctr;
            prioQueue.enqueue(emptyNode);
        }
        System.out.println("Tree Generated");
        return prioQueue.dequeue();
    }

    public void codeAssignment(HuffmanNode node, String traversalHistory){
        if(node.key == null){
            codeAssignment(node.left, traversalHistory+"0");
            codeAssignment(node.right, traversalHistory+"1");
        }else{
            for(int i = 0; i < distinctColorCtr; i++){
                if(counters[i].key.getRed() == node.key.getRed() && counters[i].key.getGreen() == node.key.getGreen() && counters[i].key.getBlue() == node.key.getBlue()){
                    counters[i].code = traversalHistory;
                }
            }
        }
    }

    public void createHuffFile(String file){
        try{
            System.out.println("Generating Huff File");
            gui.display("Generating Huff File");
            String huffName = file.substring(0, file.length()-3) + "HUFF";
            PrintWriter writer = new PrintWriter(huffName, StandardCharsets.UTF_8);
            for (HuffmanNode counter : counters) {
                String rgb = "" + (char) counter.key.getRed() + (char) counter.key.getGreen() + (char) counter.key.getBlue();
                String ctrBinary = Integer.toBinaryString(counter.ctr), zeroes = "00000000000000000000000000000000";
                ctrBinary = zeroes.substring(0, (ctrBinary.length() < 16 ? 16 - ctrBinary.length() : 0)) + ctrBinary;
                ctrBinary = "" + (char) (Integer.parseInt(ctrBinary.substring(0, ctrBinary.length() - 8), 2)) + (char) (Integer.parseInt(ctrBinary.substring(ctrBinary.length() - 8), 2));
                writer.print(rgb + ctrBinary);
            }
            writer.close();
            System.out.println("Huff File Generated");
            gui.display("Huff File Generated");
        }catch(IOException ignored){}
    }

    public void createNewImageFile(String file){
        try{
            System.out.println("Generating Image File");
            gui.display("Generating Image File");
            String huffName = file.substring(0, file.length()-3) + "IJK";
            PrintWriter writer = new PrintWriter(huffName);
            writer.println(raster.getHeight()+"\n"+raster.getWidth());
            StringBuilder binaryImage = new StringBuilder();
            Pixel pixel;
            System.out.println("Converting Image to Binary String");
            gui.display("Converting Image to Binary String");
            System.out.println("Converting Binary String to ASCII and Save to File");
            gui.display("Converting Binary String to ASCII and Save to File");
            for(int i = 0; i < raster.getHeight(); i++){
                for(int j = 0; j < raster.getWidth(); j++){
                    int[] buffer = new int[4];
                    buffer = raster.getPixel(j, i, buffer);
                    pixel = new Pixel(buffer[0], buffer[1], buffer[2], j, i);
                    int start = 0, end = distinctColorCtr-1, mid = (int)((float)(start+end)/2.0);
                    while(true){
                        if(counters[mid].key.getRGB() == pixel.getRGB()){
                            binaryImage.append(counters[mid].code);
                            while(binaryImage.length()>=7){
                                writer.print((char)Integer.parseInt(binaryImage.substring(0, 7), 2));
                                binaryImage = new StringBuilder(binaryImage.substring(7));
                            }
                            break;
                        }else if(counters[mid].key.getRGB() < pixel.getRGB()){
                            start = mid+1;
                            mid = (int)((float)(start+end)/2.0);
                        }else if(counters[mid].key.getRGB() > pixel.getRGB()){
                            end = mid-1;
                            mid = (int)((float)(start+end)/2.0);
                        }
                    }
                }
            }
            while(binaryImage.length()%7!=0){
                binaryImage.append("0");
            }
            System.out.println("Converting Image to Binary String Done");
            gui.display("Converting Image to Binary String Done");
            for(int i = 0; i < binaryImage.length();){
                writer.print((char)Integer.parseInt(binaryImage.substring(i, i+7), 2));
                i+=7;
            }
            System.out.println("Converting Binary String to ASCII and Save to File Done");
            gui.display("Converting Binary String to ASCII and Save to File Done");
            writer.close();
            System.out.println("Image File Created");
            gui.display("Image File Created");
        }catch(IOException ignored){}
    }
}
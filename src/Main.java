import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.setContentPane(mainMenu.pnlMain);
        mainMenu.setTitle("Multi-Channel Image Compressor");
        mainMenu.setSize(400, 300);
        mainMenu.setVisible(true);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}



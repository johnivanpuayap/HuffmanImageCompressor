import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JFrame {
    private JButton openPNGButton;
    private JButton openIJKButton;
    private JButton createNewHuffFileButton;
    private JButton updateExistingHuffFileButton;
    private JButton compressImageButton;
    JPanel pnlMain;
    private JPanel pnlOImage;
    private JPanel pnlCmage;

    public MainMenu() {
        openPNGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPNGImage();
            }
        });
    }

    private void openPNGImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images (*.png)", "png"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Do something with the selected PNG image file
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }
}
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GUIImportTest {

    @Test
    public void fileChoose() {
        ArrayList<String> list = new ArrayList<String>();
        JFileChooser chooser = new JFileChooser(Global.DefaultSavePath);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnval=chooser.showDialog(new JLabel(), "选择");
        if(returnval==JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            String str = "";
            for (File file : files) {
                if (file.isDirectory())
                    str = file.getPath();
                else {
                    str = file.getAbsoluteFile().toString();
                    boolean bool=str.contains(".csv");
                    Assert.assertTrue(bool);
                }
            }
        }
    }
}
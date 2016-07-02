package Gui;

import MNISTReader.*;
import MNISTReader.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.LinkedList;

/**
 * Created by Dkopljar on 3/28/16.
 */
public class Panel extends JPanel {

    Image imgae;
    public Panel(Image image){
        this.imgae=image;
        this.setLayout(new GridLayout(2,1));


        BufferedImage bi = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = bi.getRaster();

        byte[][] data = imgae.getData();
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                raster.setSample(i, j, 0, data[i][j]);
            }
        }
        Icon icon = new ImageIcon(bi);
        this.add(new JLabel(Integer.toString(image.getPredicted()),icon,JLabel.CENTER));


    }

}

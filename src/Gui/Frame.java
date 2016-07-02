package Gui;

import MNISTReader.Image;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Dkopljar on 3/28/16.
 */
public class Frame extends JFrame{


    JPanel p1,p2;
    Dimension d;

    public Frame(LinkedList<Image> photos)
    {
        createAndShowGUI(photos);
    }

    private void createAndShowGUI(LinkedList<Image> photos)
    {
        setTitle("MNIST Examples");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2,2));
        setSize(400,400);
        setVisible(true);

        for (int i = 0; i < photos.size(); i++) {
            Panel panel = new Panel(photos.get(i));
            panel.setVisible(true);
            this.add(panel);
            if(i>20){
                break;
            }
        }

        this.revalidate();


    }

    public static void main(String args[])
    {

    }


}

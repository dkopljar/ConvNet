package MNISTReader;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ReadingDataSet {
	
	private final int WIDTH = 28;
	private final int HEIGHT = 28;

	public static void main(String[] args) {
		LinkedList<Image> photos = new LinkedList<>();
	    ReadingDataSet set = new ReadingDataSet();
	    photos = set.loadPhotos(50);
	    //set.savePhotosToDisk("test", photos);
	    System.out.println("Number of photos: "+ set.getNumberOfPhotos());
	    LinkedList<Integer> labels = set.loadLabels(50);

	    set.labelData(photos, labels);
	    //System.out.println(photos.get(0).getLabel());

		byte[][] image = photos.get(21).getData();
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				System.out.print(( ( (image[j][i])& 0xFF) > 0 )? "1" : "0");
			}
			System.out.println();
		}

		System.out.println("Label: " +photos.get(21).getLabel());


	}

	public LinkedList<Image> getExamples(int n){
		LinkedList<Image> photos = new LinkedList<>();
		ReadingDataSet set = new ReadingDataSet();
		photos = set.loadPhotos(n);
		LinkedList<Integer> labels = set.loadLabels(n);

		set.labelData(photos, labels);
		return photos;
	}
	
	/**
	 * Method is labeling data. Every photo from list will be labeled with correct label
	 * @param images list of images
	 * @param labels list of labels
	 */
	private void labelData(LinkedList<Image> images, LinkedList<Integer> labels){
		if(images.size()!=labels.size()){
			throw new IllegalArgumentException("Number of labels and images must be same!");
		}
		
		for (int i = 0; i < images.size(); i++) {
			images.get(i).setLabel(labels.get(i));
		}
		
	}
	
	/**
	 * Method counts number of photos in file
	 * @return number of photos in file
	 */
	public int getNumberOfPhotos(){
		int num = 0;
		try {
			FileInputStream  in = new FileInputStream("images");
			//Skipping magic number
			for (int i = 0; i < 4; i++) {
				in.read();
			}
			byte[] tmpVar = new byte[4];
			for (int i = 0; i < 4; i++) {
				tmpVar[i] = (byte) in.read();
			}
			num = ByteBuffer.wrap(tmpVar).getInt();
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return num;
		
	}
	
	/**
	 * Method is saving given photos to disk
	 * @param path path were photos will be saved
	 * @param listOfImages list of images
	 */
	public void savePhotosToDisk(String path, LinkedList<Image> listOfImages){
		try{
		    BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		    WritableRaster raster = bi.getRaster();

		    for (int k = 0; k < listOfImages.size(); k++) {
		    	
		    	byte[][] image = listOfImages.get(k).getData();
		    	for (int i = 0; i < 28; i++) {
					for (int j = 0; j < 28; j++) {
						raster.setSample(i, j, 0, image[i][j]);
					}
				}
		    	
			    ImageIO.write(bi, "jpg", new File(path+k+".jpg"));
			}		    
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	
	/**
	 * Method returns list of labels for first n images
	 * @param n number of labels in list
	 * @return list of labels
	 */
	private LinkedList<Integer> loadLabels(int n){
		LinkedList<Integer> labels = new LinkedList<>();
		try {
			FileInputStream  in = new FileInputStream("labels");
			//Skipping magic number and array size
			for (int i = 0; i < 8; i++) {
				in.read();	
			}
			for (int i = 0; i < n; i++) {
				labels.add(new Integer((int) in.read()));
			}
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return labels;
		
	}
	
	
	/**
	 * Method is loading first n photos from dataset
	 * @param n number of photos
	 * @return List of photos in binary format
	 */
	private LinkedList<Image> loadPhotos(int n){
		LinkedList<Image> listOfPhotos = new LinkedList<>();
		
		
		try {

			FileInputStream  in = new FileInputStream("images");
			//Skipping magic number and array size
			for (int i = 0; i < 16; i++) {
				in.read();
				
			}
			
			for (int k = 0; k < n; k++) {
				Image img = new Image(HEIGHT, WIDTH);
				
				byte[][] imageData = new byte[WIDTH][HEIGHT];
				for (int i = k*WIDTH; i < k*WIDTH + WIDTH; i++) {
					for (int j = k*HEIGHT; j < k*HEIGHT + HEIGHT; j++) {
						imageData[j%28][i%28]  = (byte) in.read();
					}
				}
				img.setData(imageData);
				listOfPhotos.add(img);
			}
			in.close();

		}catch(Exception e){
			e.printStackTrace();
		}
			
		
		return listOfPhotos;
		
	}

}

package MNISTReader;

public class Image {
	
	private byte[][] data;
	private int height;
	private int width;
	private int label;
	private int predicted;
	
	public Image(int height, int width){
		this.height=height;
		this.width=width;
		data = new byte[height][width];
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}

	public int getPredicted() {
		return predicted;
	}

	public void setPredicted(int predicted) {
		this.predicted = predicted;
	}

	public void setData(byte[][] data){
		this.data= data;
	}
	
	public void setLabel(int label){
		this.label=label;
	}
	
	public int getLabel(){
		return label;
	}
	
	public byte[][] getData(){
		return data;
	}
}

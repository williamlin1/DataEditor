package dataEditor;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DataEditor {
	
	String directory;
	
	ArrayList<BufferedImage> pictures = new ArrayList<>();	
	
	
	public static void main(String[] args) {
		DataEditor editor = new DataEditor("/Users/william/Desktop/testthing");
		try {
			editor.loadData();
			editor.generateCropped();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataEditor(String directory) {
		this.directory = directory;
	}
	
	private void loadData() throws IOException{
		
		int num = new File(directory).listFiles().length;
		File tmpDir = new File(directory+"/.DS_Store");
		boolean exists = tmpDir.exists();
		if(exists) num--;
		for(int i =0; i < num; i++) {
			String filePath = directory + "/" + String.valueOf(i) + ".png";
			System.out.println(num);
			System.out.println(filePath);
			BufferedImage pic = ImageIO.read(new File(filePath));
			pictures.add(pic);	
		}
	}
	
	public void generateCropped() {
		
		for(int i =0; i < pictures.size(); i++) {
			BufferedImage temp = pictures.get(i);
			ArrayList<Rectangle> rects = genRect(temp.getWidth(),temp.getHeight());
			String x = String.valueOf(i);
			for(int j =0; j < 5; j++) {
				BufferedImage crop = crop(temp,rects.get(j));
				String y = String.valueOf(j);
				save(directory,crop,x+"Crop"+y);
				System.out.println(j);
			}
			BufferedImage horiz = flipHoriz(temp);
			save(directory, horiz, x + "Flip1");
			BufferedImage rot = flip180(temp);
			save(directory, rot, x + "Flip2");
//			BufferedImage vert = flipVert(temp);
//			save(directory, vert, x + "Flip3");
			
		}
		
	}
	
	
	public void generateColored() {
		
	}
	
	
	private ArrayList<Rectangle> genRect(int width, int height){
		int h = height;
		int w = width;
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
		rects.add(new Rectangle(0,0,(int)(w*0.8),(int)(h*0.8)));
		rects.add(new Rectangle((int)(w*0.2),(int)(h*0.2),(int)(w*0.8-1),(int)(h*0.8-1)));
		rects.add(new Rectangle((int)(0),(int)(h*0.2),(int)(w*0.8-1),(int)(h*0.8-1)));
		rects.add(new Rectangle((int)(w*0.2),(int)(0),(int)(w*0.8-1),(int)(h*0.8-1)));
		rects.add(new Rectangle((int)(w*0.1),(int)(h*0.1),(int)(w*0.8-1),(int)(h*0.8-1)));
		return rects;	
	}
	
	private BufferedImage crop(BufferedImage src, Rectangle rect) {
		BufferedImage fin = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
		return fin;
	}
	
	private BufferedImage flipHoriz(BufferedImage src) {
		BufferedImage fin = new BufferedImage(src.getWidth(),src.getHeight(),src.getType());
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
	    tx.translate(-fin.getWidth(null), 0);
	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	    fin= op.filter(fin, null);
	    		return fin;
	}
	
	private BufferedImage flip180(BufferedImage src) {
		BufferedImage fin = new BufferedImage(src.getWidth(),src.getHeight(), src.getType());
		AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
	    tx.translate(-fin.getWidth(null), -fin.getHeight(null));
	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	    return op.filter(fin, null);
	    
	}

//	private BufferedImage flipVert(BufferedImage src) {
//		BufferedImage fin = new BufferedImage(src.getWidth(),src.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
//		AffineTransform tx = AffineTransform.getScaleInstance(1,-1);
//	    tx.translate(-fin.getWidth(), -fin.getHeight(null));
//	    AffineTransformOp op = new AffineTransformOp(tx,
//		        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//	    return op.filter(fin, null);
//	}

	private void save(String directory, BufferedImage image, String name) {

		File file = new File(directory + "/"+name + ".png");
		try {
			ImageIO.write(image, "png", file);  // ignore returned boolean
		} catch(IOException e) {
			System.out.println("Write error for " + file.getPath() +
		             ": " + e.getMessage());
		}
	}
	
}
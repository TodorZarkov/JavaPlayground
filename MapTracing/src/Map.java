import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

/**
 * This class has a functionality to convert a simple image file to
 *mapFile that used in Graph method to setup a graph.
 * @author dqdo
 * @version 1.0
 */
public class Map {
	
	private Map() {};
	
	
	public static File createHMap(File imageFile, int r, int g, int b) {
		File mapFile = new File(imageFile.getName()+".hmap");
		
		PrintStream ps = null;
		try {
			BufferedImage image = ImageIO.read(imageFile);
			int width = image.getWidth();
			int height = image.getHeight();
			
			ps = new PrintStream(mapFile);
			
			byte[] pixels = (byte[]) image.getRaster().getDataElements(0, 0, width, height, null);
			boolean hasAlpha = image.getAlphaRaster() != null;
		
			// First row express the type, the width and the height.
			ps.println("HM" + " " + image.getWidth() + " " + image.getHeight());
			
			int[] heirs = new int[8]; float[] weights = new float[8];
			for(int pixel = 0;pixel < width*height;pixel++) {
	
				filter(heirs, weights, pixels, pixel, width, height, r, g, b, hasAlpha);
				
				StringBuilder sb = new StringBuilder(30);
				
				
				// The zero column of every row indicates the vertex
				sb.append(pixel);
				
				boolean hasHeirs = false;
				for(int i = 0;i < heirs.length;i++) {
					if(heirs[i] != -1) {
						hasHeirs = true;
						
						// The first(odd) column of every row indicates the i-th heir.
						// The second(even) column of every row indicates the i-th weight.
						sb.append(" "); sb.append(heirs[i]); 
						sb.append(" "); sb.append(weights[i]);
						
					}
				}
				if(hasHeirs) {
					sb.append(" n\n");
					ps.print(sb);
				}
				
				
			}
			
			
			
		}catch(FileNotFoundException fnf) {
			System.err.println(
					"Problem with writing map " + mapFile.getAbsolutePath());
		}catch(IOException e) {
			System.err.println(
					"Problem with reading picture " + imageFile.getAbsolutePath());
		}finally {
			if(ps != null) {
				ps.close();
			}
		}
		
		return mapFile;
	}
	
	
	public static File createMap(File imageFile, int r, int g, int b) {
		File mapFile = new File(imageFile.getName()+".map");
		
		FileOutputStream fos = null;
		try {
			BufferedImage image = ImageIO.read(imageFile);
			int width = image.getWidth();
			int height = image.getHeight();
			
			// The stream we are going to write onto.
			fos = new FileOutputStream(mapFile);
			
			// Picture extraction initialization.
			byte[] pixels = (byte[]) image.getRaster().getDataElements(0, 0, width, height, null);
			boolean hasAlpha = image.getAlphaRaster() != null;
			int[] heirs = new int[8]; float[] weights = new float[8];
			
			// Creating buffer to convert from integer and float to byte
			byte[] data = new byte[width*4];
			ByteBuffer bb = ByteBuffer.allocateDirect(width*4).wrap(data);		
	
			// mapFile initialization; the first 12 bytes must be |MAP|width| |height|.
			bb.put((byte)0x4d);bb.put((byte)0x41); bb.put((byte)0x50);
			bb.putInt(width); bb.put((byte)0x20); bb.putInt(height); bb.flip();
			while(bb.hasRemaining()) { fos.write(bb.get()); }
			bb.clear();
			
			// Concerning every pixel in the image, extracting data from it,
			//converting it to byte data and put it in the mapFile.
			for(int pixel = 0;pixel < width*height;pixel++) {
				
				// Filtering every pixel for neighbors, forbidden colors and
				//corresponding weights.
				filter(heirs, weights, pixels, pixel, width, height, r, g, b, hasAlpha);
				
				boolean isBegin = true;
				for(int i = 0;i < heirs.length;i++) {
					if(heirs[i] != -1) {
						
						// has next 1
						if(!bb.hasRemaining()) {
							bb.clear();
							fos.write(data);
						}
						
						// increase buffer with 4 (eventually)
						// That +1 is intentionally set; we mustn't
						//have the zero heir(encoding/decoding reasons).
						if(isBegin){ bb.putInt(pixel + 1); isBegin = false;}
						
						// has next 2
						if(!bb.hasRemaining()) {
							bb.clear();
							fos.write(data);
						}
						
						// increase buffer with 3
						// That +1 is intentionally set; we mustn't
						//have the zero heir(encoding/decoding reasons).
						bb.putInt(heirs[i] +1);
												
						// has next 4
						if(!bb.hasRemaining()) {
							bb.clear();
							fos.write(data);
						}
						
						// increase buffer with 4
						bb.putFloat(weights[i]);
						
						//has next 5
						if(!bb.hasRemaining()) {
							bb.clear();
							fos.write(data);
						}
					}
				}
				// increase buffer with 4 (eventually)
				if(!isBegin) bb.putInt(0);
			}
			
			// Writing the rest of the buffer onto the fos stream.
			bb.flip();
			while(bb.hasRemaining()) {
				fos.write(bb.get());
			}
			
		}catch(FileNotFoundException fnf) {
			System.err.println(
					"Problem with writing map " + mapFile.getAbsolutePath());
		}catch(IOException e) {
			System.err.println(
					"Problem with reading picture " + imageFile.getAbsolutePath());
		}finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// There's nothing we can do;
				}
			}
		}
		
		return mapFile;
		
	}

	
	private static void filter(
			int[] heirsFilter, float[] weightsFilter,
			byte[] pixels, int position, int width,int height, 
			int redFilter,int greenFilter, int blueFilter, boolean hasAlpha){
		if(weightsFilter.length != 8) {
			throw new IllegalArgumentException(
					"The array weightsFilter length must be exactly 8!");
		}
		
		int[] na = new int[8];// The absolute neighbors of the current position
		int[] nr = heirsFilter;// The relative neighbors of the current position
		
		int pw = hasAlpha?4:3;// Pixel width
		int h = height;// The height remains unchanged;
		int w = width * pw;// The absolute width according to the pixels array
		int p = position * pw;// The absolute position in the pixels array
		int a = pw/4;// Auxiliary offset index related to Alpha byte
		
		// colorFilter condition for the current pixel
		if((pixels[p+a] == redFilter) && (pixels[p+a+1] == greenFilter)
				&& (pixels[p+a+2] == blueFilter)) {
			for(int i = 0;i < nr.length;i++) {
				nr[i] = -1;
			}
			return ;
		}
		
		// Absolute position of the p neighbors in the array pixels;
		// and the corresponding relative position of the position var.
		na[0] = p-pw;		nr[0] = position-1;// Left
		na[1] = p+pw;		nr[1] = position+1;// Right
		na[2] = p-w;		nr[2] = position-width;// Up
		na[3] = p+w;		nr[3] = position+width;// Down
		na[4] = p-w-pw;		nr[4] = position-width-1;// Up-Left
		na[5] = p-w+pw;		nr[5] = position-width+1;// Up-Right
		na[6] = p+w-pw;		nr[6] = position+width-1;// Down-Left
		na[7] = p+w+pw;		nr[7] = position+width+1;// Down-Right
		
		
		// Left border condition filtering
		if(p%w == 0) {
			nr[4] = nr[0] = nr[6] = -1;
			
			// Left corners condition filtering
			if(p == 0) {
				nr[2] = nr[5] = -1;
			}else if(p == w*(h-1)) {
				nr[3] = nr[7] = -1;
			}
		}
		
		// Right border condition filtering
		if((p+pw)%w == 0) {
			nr[5] = nr[1] = nr[7] = -1;
			
			// Right corners condition filtering
			if((p+pw) == w) {
				nr[2] = nr[4] = -1;
			}else if(p == w*h) {
				nr[3] = nr[6] = -1;
			}
		}
		
		// Up border condition filtering
		if(p/w == 0) {
			nr[4] = nr[2] = nr[5] = -1;
		}
		
		// Down border condition filtering
		if(p/(w*(h-1)) != 0) {
			nr[6] = nr[3] = nr[7] = -1;
		}
		
		
		// Neighbors color filtering
		for(int i = 0;i < na.length;i++) {
			// Out of bounds checking
			if(nr[i] == -1) {
				continue;
			}
			if(		(pixels[na[i]+a] == redFilter) &&
					(pixels[na[i]+a+1] == greenFilter) &&
					(pixels[na[i]+a+2] == blueFilter))
			{
				nr[i] = -1;
			}
			
			// Calculating the weights
			if(nr[i] == -1) {
				weightsFilter[i] = 0;
				continue;
			}
			
			if(i < 4) {// Left, Right, Up, Down sides
				weightsFilter[i] =Math.abs(
						(pixels[na[i]+a] + pixels[na[i]+a+1] + pixels[na[i]+a+2]) -
						(pixels[p+a] + pixels[p+a+1] + pixels[p+a+2]) + 1);
			}else {// Diagonal sides
				weightsFilter[i] = (float)Math.sqrt(2) * Math.abs(
						(pixels[na[i]+a] + pixels[na[i]+a+1] + pixels[na[i]+a+2]) -
						(pixels[p+a] + pixels[p+a+1] + pixels[p+a+2]) + 1);
			}
			
			
		}
		
		return ;
	}
	
	

}


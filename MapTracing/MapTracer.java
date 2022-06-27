import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *Wrap some Graph methods in order to implement GUI.
 * @author dqdo
 * @version 1.0.
 *It's a hastily written class to implement some basic GUI functionality 
 *to Graph class.
 */
public class MapTracer extends JPanel implements MouseListener{

	Graph gr;
	
	int pathColor;
	
	boolean hadMLC;
	
	Point startPoint;
	Point endPoint;
	Point[] path;
	
	static File canvasFile;
	BufferedImage pathImg;
	
	public MapTracer(BufferedImage mapImg, BufferedImage canvasImg,int fr, int fg, int fb) {
		pathImg = canvasImg;
		
		//Setting the graph with BufferedImage mapImage.
		gr = new Graph(mapImg,fr,fg,fb);

		pathColor = 0xff0000;
		hadMLC = false;
		
		repaint();
		
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage((Image)pathImg, 0, 0, null);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent mc) {
		if(!hadMLC && (mc.getButton() == MouseEvent.BUTTON1)) {
			startPoint = mc.getPoint();
			hadMLC = true;
		}else if(hadMLC && (mc.getButton() == MouseEvent.BUTTON1)) {
			endPoint = mc.getPoint();
			hadMLC = false;
			
			// Actual calculating the path using pure Dijkstra algorithm
			//with the java build-in priority queue.
			path = gr.shortestPathDijkstra(startPoint,endPoint);
			//TODO: PRINT THE PATH LENGTH.

			gr.printPath(path, pathImg, pathColor);
			
			repaint();
		}else{
			hadMLC = false;
			pathImg = null;
			try {
				pathImg = ImageIO.read(canvasFile);
			} catch (IOException e) {
				// Do nothing. File already exist;
			}
			repaint();
		}
		
	}
	
	
	public static void main(String[] args) {
		args = new String[1];
		args[0] =new String( "map7.bmp");
		if(args.length == 0) {
			System.err.println(
					"There must be at least one argument(the mapImage file neame)");
			return;
		}
		File mapFile = new File(args[0]);
		if(args.length > 1) {
			canvasFile = new File(args[1]);
		}else {
			canvasFile = new File(args[0]);
		}
		
		BufferedImage mapImg = null;
		try {
			mapImg = ImageIO.read(mapFile);
		} catch (IOException e) {
			System.err.println(
					"Problem reading file " + mapFile.getAbsolutePath());
		}
		
		BufferedImage canvasImg = null;
		try {
			canvasImg = ImageIO.read(canvasFile);
		} catch (IOException e) {
			System.err.println(
					"Problem reading file " + canvasFile.getAbsolutePath());
		}
		
		JFrame frame = new JFrame("Path Tracer");
		MapTracer mt = new MapTracer(mapImg,canvasImg,0,0,0);
		frame.add(mt);
		frame.addMouseListener(mt);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(mapImg.getWidth(), mapImg.getHeight());
		frame.setResizable(false);
		frame.setVisible(true);
		
		
	}

	
	//UNUSED
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}


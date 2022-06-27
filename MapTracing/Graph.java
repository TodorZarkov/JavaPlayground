import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Scanner;


import javax.imageio.ImageIO;

/**
 * Implements graph as adjacency list.
 *Implementation notes: time complexity cost for performing ....
 * @author dqdo
 * @version 1.0
 */
public class Graph {
	
	/**
	 * Implements a single vertex with list of heirs(neighbours), list of
	 *weights, the value of vertex, the index of vertex which is useful when 
	 *search vertexIndex of given vertex, prev and isVisited fields useful 
	 *in search operations.
	 *
	 * @author dqdo
	 * @version 1.0
	 */
	private class Vertex {
		// FIELDS
		int index;
		ArrayList<Integer> heirs;
		ArrayList<Float> weights ;
		int prev;
		double value;
		boolean isVisited;
		
		/**
		 * Constructs vertex at the given index of the list with vertices that
		 *the Graph contains. The given list of heirs(neighbours) indicates all 
		 *the neighbours of this vertex, and the list of edges contains the 
		 *corresponding weights to the given heirs(neighbours).
		 * Implementation notes: the time complexity cost to set a vertex is
		 *O(2 * #h), (#h - number of heirs(neighbours)).
		 * This constructor replaces the old vertex if there has been any.
		 * @param vertIndex - the integer representation of the name of the 
		 *vertex. Also the index in the list of vertices the Graph contains.
		 * @param heirs - list of the heirs(neighbours).
		 * @param edges - corresponding list of weights to the given heirs.
		 * @throws Unchecked NullPointerException if the index exceed the 
		 *size of the list of vertices the Graph contains.
		 */
		Vertex(int vertIndex, ArrayList<Integer> heirs, ArrayList<Float> edges) {
			
			this.heirs = new ArrayList<Integer>(heirs.size());
			this.weights = new ArrayList<Float>(edges.size());
			this.index = vertIndex;
			this.prev = 0;
			this.isVisited = false;
			this.value = Double.POSITIVE_INFINITY;
			
			vertices.set(vertIndex, this);
			
			this.heirs.addAll(heirs);
			
			this.weights.addAll(edges);
			
			verticesNumber++;
		}
		
		/**
		 * Constructs with empty neighbours and weights lists and add it to the
		 *Graph list of vertices on the given index. This new vertex replaces 
		 *the old vertex in the Graph list of vertices, if any.
		 * @param vertIndex - the integer representation of the name of the 
		 *vertex. Also the index in the list of vertices the Graph contains.
		 */
		Vertex(int vertIndex) {
			this.heirs = new ArrayList<Integer>(8);
			this.weights = new ArrayList<Float>(8);
			
			this.index = vertIndex;
			this.prev = 0;
			this.isVisited = false;
			this.value = Double.POSITIVE_INFINITY;
			vertices.set(vertIndex, this);
			verticesNumber++;
		}
		
		
		
	}
	
	
	
	// Implements comparator class to compare Vertex objects.
	private class VertexValueComparator implements Comparator<Vertex> {
		
		public int compare(Vertex v1, Vertex v2) {
			if(v1.value > v2.value) {
				return 1;
			}
			if(v1.value < v2.value) {
				return -1;
			}
			return 0;
		}
		
		
	}
	
	
	// Contains all the vertices. The index of the vertex always coincide with
	//the indexes of this list.
	// Faster but more inflexible way is Vertex[] implementation.
	private ArrayList<Vertex> vertices;
	
	// Includes all vertices no matter null or not.
	private int size;
	
	// The width of the map.
	//This is useful when the Graph is represented as a picture with a pixel as
	//a Vertex.
	private int width;
	
	// The height of the map.
	//This is useful when the Graph is represented as a picture with a pixel as
	//a Vertex.
	private int height;
	
	// The number of vertices excluding the null vertices in the vertices list.
	private int verticesNumber;
	
	// These are internal getters and setters. There are no boundary or any other control,
	//so they are faster. The meaning of all this internals is precapsulation, i.e. they
	//must remain unchanged during any graph implementation changes.
	private int getHeight() {
		return this.height;
	}
	
	private int getWidth() {
		return this.width;
	}
	
	/**
	 * Gets the vertex at the given vertexIndex in the vertices list.
	 * @param vertIndex
	 * @return the vertex of the given index that vertices list contains.
	 *It may return null pointer if there's no vertex at that position it the
	 *vertices list.
	 * @exception Unchecked NullPointerException if the index is exceed vertices
	 *  list size.
	 */
	private Vertex getVertexOf(int vertIndex) {
		return vertices.get(vertIndex);
	}
	
	/**
	 * Gets the heir(neighbour) of the given vertex represented with its index.
	 *The desired heir is given(passed) by its index in the heir's list in the 
	 *vertex, so it is relative index. The absolute heir index is returned and
	 *it represents the index of the desired heir vertex.
	 * @param ofRelativHeirIndex - the relative(inner) heir index.
	 * @param ofVertIndex - the index of the vertex which heir we want to obtain.
	 * @return the vertexIndex of the heir with relative index ofRelativHeirIndex.
	 * @exception Unchecked NullPointerException if ofRelativHeirIndex exceed 
	 * the ofVertIndex heir's list size, or ofVertIndex exceed vertices list size.
	 */
	private int getHeir(int ofRelativHeirIndex, int ofVertIndex) {
		return vertices.get(ofVertIndex).heirs.get(ofRelativHeirIndex);
	}
	
	private int getIndexOf(Vertex vertex) {
		return vertex.index;
	}
	
	private int countHeirsOf(int vertIndex) {
		return vertices.get(vertIndex).heirs.size();
	}
	
	private double getWeight(int fromVertIndex, int toRelativHeirIndex) {
		return vertices.get(fromVertIndex).weights.get(toRelativHeirIndex);
	}
	
	/**
	 * Gets the value of vertex represented wigh its index(vertIndex)
	 * @param vertIndex - the index of the vertex which value we want
	 *to obtain.
	 * @return the value of the vertex represented by vertIndex.
	 */
	private double getValueOf(int vertIndex) {
		return vertices.get(vertIndex).value;
	}
	
	private Boolean getIsVertexVisited(int vertIndex) {
		return vertices.get(vertIndex).isVisited;
	}
	
	/**
	 * Sets the value onto the vertex with the given vertIndex.
	 * @param vertIndex
	 * @param value
	 * @exception Unchecked NullPointerException if the index is exceed vertices
	 *  list size.
	 */
	private void setValueOf(int vertIndex,double value) {
		vertices.get(vertIndex).value = value;
	}
	
	/**
	 * @param vertIndex
	 * @param isVisited
	 * @exception Unchecked NullPointerException if the index is exceed vertices
	 *  list size.
	 */
	private void setIsVertexVisited(int vertIndex, boolean isVisited) {
		vertices.get(vertIndex).isVisited = isVisited;
	}
	
	/**
	 * Sets vertex with vertexIndex or replace the old one if exist.
	 * @param vertIndex
	 * @param heirs list of the the vertex heirs(neighbours).
	 * @param weights the corresponding weights to the given heirs(neighbours)
	 * @return false if no heirs in the list or if vertIndex > getSize() 
	 * (not >=, only >); otherwise true.
	 */
	private boolean setVertex
	(int vertIndex, ArrayList<Integer> heirs, ArrayList<Float> weights){
		if(heirs.size() == 0) return false;
		if(vertIndex == vertices.size()){
			vertices.add(null);
		}else if(vertIndex > vertices.size()){return false;}
		
		new Vertex(vertIndex, heirs, weights);
		return true;
	}
	
	/**
	 * It's a special method that sets various vertices from a special Map file.
	 *It is implemented to improve setting the vertices in compare with setting
	 *vertices from picture, but it doesn't work much faster.
	 * @param bb it's a ByteBuffer that must contain repeatedly the following 
	 *group of Bytes:
	 *|int (the vertIndex)|int (the first heir1)|float (the heir1 weight)|...|int 0|...;
	 *so it's like that: 
	 *|root1|h11|w11|h12|w12|h13|w13|...|h1n|w1n|0|root1|h21|w21|h22|w22|...
	 * When the method returns, that means the buffer is empty so it can be
	 *filled and passed again (BUT WITHOUT CHANGING vIndex and hIndex ANYMORE).
	 * @param vIndex - vIndex[0] must be set to zero at first invocation and 
	 *not changed in subsequent invocation of the method. it's kind of flag.
	 * @param hIndex - hIndex[0] must be set to zero at first invocation and 
	 *not changed in subsequent invocation of the method. it's kind of flag.
	 */
	private void setVertices(ByteBuffer bb, int[] vIndex,int[] hIndex) {
		Vertex tmpV = null;	
	
		while(bb.hasRemaining()) {
		
			// Constructs if the buffer hasn't been stopped at "has next 1/3" 
			//at the previous invocation of this method.
			if(vIndex[0] == 0) {

				//decrease buff. by 4 (eventually)
				//"-1" is kind of decoding
				tmpV = new Vertex(bb.getInt()-1);
				vIndex[0] = tmpV.index;
				
			}
			
			//has next 1
			if(!bb.hasRemaining()) {
				return;
			}
			
			// Add weight if the buffer has been stopped at "has next 2"
			//at the previous invocation of this method.
			if(hIndex[0] != 0) {
				vertices.get(vIndex[0]).weights.add(bb.getFloat());
				hIndex[0] = 0;
			}
		
			int heir;
			while (bb.hasRemaining() &&   // Increase the buffer with 4.
					((heir = bb.getInt()) != 0) ) {	

				heir--;// kind of decoding
				
				vertices.get(vIndex[0]).heirs.add(heir);
				hIndex[0] = heir;
				
				// has next 2
				if(!bb.hasRemaining()) {
					return;
				}
				
				// Increase the buffer with 4.
				vertices.get(vIndex[0]).weights.add(bb.getFloat());
				hIndex[0] = 0;
				
				// has next 3
				if(!bb.hasRemaining()) {
					return;
				}	
			}
			
			// This indicates there has been completely set vertex.
			vIndex[0] = 0;
			
		}
	}
	
	private int getPrevOf(int vertIndex) {
		return vertices.get(vertIndex).prev;
	}
	
	/**
	 * @param vertIndex
	 * @param asVertIndex
	 * @exception Unchecked NullPointerException if vertIndex or asVertIndex
	 *exceed getSize();
	 */
	private void setPrevOf(int vertIndex, int asVertIndex) {
		vertices.get(vertIndex).prev = asVertIndex;
	}
	
	/**
	 * Gets the size of the graph, i.e. the number of vertices in it no matter 
	 *forbidden(null) or not.
	 * @return the size.
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the size of the graph, i.e. the number of vertices in it without 
	 *the forbidden(null) ones.
	 * @return the size.
	 */
	public int getVerticesNumber() {
		return this.verticesNumber;
	}
	
	/**
	 * Constructs the graph from the given file which can be one of the following
	 *types:
	 *	Microsoft BitMap (BM(.bmp)) picture (a faster way to set the graph);
	 *	Human written file which specification can be obtained SOMEWHERE TODO: WHERE?
	 *(the slowest way to set the graph);
	 *	BinayMap file which can be produced from picture(look native class Map 
	 *TODO: MORE INFO)
	 *(theoretically the fastest way to set the graph).
	 *
	 * The fx parameters is the forbidden color, the color that would be ignored during 
	 *setting the graph(parsing the picture). That means pixel with that color would 
	 *be set to null and wouldn't be concern in any way; to that pixel(vertex) it won't
	 *be add any neighbours, or in other words, this vertex(pixel) would not have any 
	 *children nor any successors. 
	 * The weights of every neighbour pixel to given pixel would be set as following:
	 *weight(rootX,rootY) = abs(rootXr + rootXg + rootXb - rootYr + rootYg + rootYb)+1
	 *if rootX andn rootY are non diagonal neighbours, and 
	 *weight(rootX,rootY) = weight(rootX,rootY)*sqrt(2) if they are diagonal neighbours.
	 *rootXr means the red byte of the given pixel(rootX) color. It must be bring to 
	 *mind that this is byte so for example ff would mean -1 and so on.
	 * @param file BM, HM or MAP file.
	 * @param fr forbidden color - red saturation (0,1,...,127,-128,...,-1)
	 * @param fg forbidden color - green saturation (0,1,...,127,-128,...,-1)
	 * @param fb forbidden color - blue saturation (0,1,...,127,-128,...,-1)
	 *For example fr=0,fg=0,fb=0 represent black color to be forbidden.
	 */
	public Graph(File file, Integer fr, Integer fg, Integer fb) {
		
		this.verticesNumber = 0;
		
		FileInputStream fis = null; ByteBuffer bb = null; Scanner sc = null;
		try {
			fis = new FileInputStream(file);
			byte[] data = new byte[12];
			bb = ByteBuffer.allocate(12).wrap(data);
			fis.read(data); bb.get(data);
			
			bb.flip();
			int sign1 = bb.get();
			int sign2 = bb.get();
			int sign3 = bb.get();
			
			
			// Constructs using IMAGE (eventually)
			if(sign1  == 0x42) {// B
				if((sign2 == 0x4d) && (fr != null) // M , null p e check
						&& (fg != null) && (fb != null) 
						&& (fr < 128)&& (fg < 128)// exceed check
						&& (fb < 128)&& (fr >= -128)// under limit check
						&& (fg >= -128)&& (fb >= -128)) {
							
					BufferedImage image = ImageIO.read(file);
					width = image.getWidth();
					height = image.getHeight();
					size = width*height;
					vertices = new ArrayList<Vertex>(size);
					for(int i = 0;i < size;i++) {
						vertices.add(null);
					}

					// The actual vertices setting.
					setFromImageFile(file,fr,fg,fb);
					
				}else {
					throw new IllegalArgumentException();
				}
				
			// Constructs using HUMAN WRITTEN mapFile (eventually)	
			}else if(sign1  == 0x48) {// H
				if((sign2 == 0x4d) && sign3 == 0x20) {// M SPACE
					sc = new Scanner(file);sc.next();
					width = sc.nextInt();
					height = sc.nextInt();
					size = width*height;
					vertices = new ArrayList<Vertex>(size);
					for(int i = 0;i < size;i++) {
						vertices.add(null);
					}
					
					// The actual vertices setting.
					setFromHFile(file);
				}else {
					throw new IllegalArgumentException();
				}
				
			// Constructs using BINARY mapFile (eventually)	
			}else if(sign1  == 0x4d) {// M
				if((sign2 == 0x41) && sign3 == 0x50) {// AP
					
					width = bb.getInt();
					bb.get();// Passing the space.
					height = bb.getInt();
					size = width*height;
					vertices = new ArrayList<Vertex>(size);
					for(int i = 0;i < size;i++) {
						vertices.add(null);
					}
					
					// The actual vertices setting.
					setFromBinaryMap(file);
				}else {
					throw new IllegalArgumentException();
				}
				
				
			}else {
				throw new IllegalArgumentException();
			}
			

			
			
			
		}catch (IllegalArgumentException iae) {
			System.err.println(
					"The file " + file.getAbsolutePath()  +
					" is not correct map file!\n" +
					"Otherwise, there are invalid fr, fg or fb arguments while " +
					"file argument is image!" );
			iae.printStackTrace();
			
		}catch (FileNotFoundException e) {
			System.err.println(
					"The file" + file.getAbsolutePath() + " is not " );
			e.printStackTrace();
			
		}catch(IOException e) {
			System.err.println(
					"Error while reading image " + file.getAbsolutePath());
			e.printStackTrace();
			
		}finally {
		
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// THERE'S NOTHING WE CAN DO!!!
				}
			}
		}
		
	}
	
	/**
	 * Constructs the graph in the same way as constructor with image file
	 *for argument although here we have BufferedImage instead.
	 * @param image it is the mapImage that would be parsed.
	 * @param fr forbidden color - red saturation (0,1,...,127,-128,...,-1)
	 * @param fg forbidden color - green saturation (0,1,...,127,-128,...,-1)
	 * @param fb forbidden color - blue saturation (0,1,...,127,-128,...,-1)
	 */
	public Graph(BufferedImage image, Integer fr, Integer fg, Integer fb) {
		
		this.verticesNumber = 0;


		width = image.getWidth();
		height = image.getHeight();
		size = width*height;
		vertices = new ArrayList<Vertex>(size);
		for(int i = 0;i < size;i++) {
			vertices.add(null);
		}

		// The actual vertices setting.
		setFromImage(image,fr,fg,fb);
		
				
		
	}
	
	
	
	// INDEPENDENT OF GRAPH IMPLEMENTATION METHODS
	
	/**
	 * The method implements Dijkstra's algorithm with a priority queue - 
	 *that implemented in Java, for now. TODO// REPLASE THE PRIORITY QUEUE WITH
	 *Fibonacci/Brodal PRIORITY QUEUE.
	 * The method works with 8*V*log(V)(for eight edges per vertex) --> O(E*V*log(V))
	 *complexity worst case.
	 * @param fromPoint represents the start x,y coordinates on the map.
	 * @param toPoint represents the destination x,y coordinates on the map.
	 * @return an array containing the desired path. Zero element is the start point.
	 * @throws IllegalArgumentException when a point exceed the borders of the map.
	 */
	public Point[] shortestPathDijkstra(Point fromPoint, Point toPoint) {
		
		int from = fromPoint.x + getWidth()*fromPoint.y;
		int to = toPoint.x + getWidth()*toPoint.y;
		
		if((from >= getSize()) || (to >= getSize())) {
			throw new IllegalArgumentException(
					"Coordinates must be in the intervals: X(" +
					"0, " + getWidth() + ") " +
					"; Y(0," + getHeight() + ")." );
		}
		
		if((getVertexOf(from) == null) || (getVertexOf(to) == null)) return null;
		
		// This priority queue must be changed with Fibonacci/Brodal priority queue
		Comparator<Vertex> comparator = new VertexValueComparator();
		PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(getSize(),comparator);
		
		Point[] result = null;
		
		// Initialization
		for(int i = 0;i < getSize();i++) {
			if(getVertexOf(i) != null) {
				setValueOf(i, Double.POSITIVE_INFINITY);
				setIsVertexVisited(i,false);
				pq.offer(getVertexOf(i));
			}
		}
		pq.remove(getVertexOf(from));
		setValueOf(from, 0);
		pq.offer(getVertexOf(from));
		
		// The actual Dijkstra algorithm
		while(pq.size() != 0) {

			int currV = getIndexOf(pq.poll());
			setIsVertexVisited(currV,true);

			// If the destination vertex is obtained
			if(currV == to) {
				int i = to, k = 0;
				while(i != from) {
					k++;
					i = getPrevOf(i);
				}
				i = to; result = new Point[k+1];
				for(int j = k;j > 0;j--) {
					result[j] = new Point(i%getWidth(), i/getWidth());
					i = getPrevOf(i);
				}
				result[0] = new Point(from%getWidth(),from/getWidth());
				
				return result;
			}
			
			// The loop consider all the heirs of the current vertex currV.
			for(int i = 0;i < countHeirsOf(currV);i++) {

				int heir = getHeir(i, currV);				
				if(getIsVertexVisited(heir)) continue;
				
				double currPath =  getValueOf(currV) + getWeight(currV,i);
				if(getValueOf(heir) > currPath) {

					//This updates the pq; it is of O(log(n)) complexity;
					//it may be done with O(1) complexity with Brodal queue.
					pq.remove(getVertexOf(heir));
					
					setValueOf(heir,currPath);
					setPrevOf(heir,currV);
					
					pq.offer(getVertexOf(heir));
				}	
			}
		}
		return result;
	}
	
	/**
	 * Prints the given array of points onto the given imageFile
	 *in the given color (rgb representation);
	 * @param path - an Point array representing the path.
	 * @param imageFile onto we want to draw the path.
	 * @param color with which we want to draw the path.
	 */
	public void printPath(Point[] path, File imageFile, int color) {
		if(path == null) return;
		
		try {
			BufferedImage image = ImageIO.read(imageFile);
			for(int i = 0;i < path.length;i++) {
				image.setRGB(path[i].x, path[i].y, color);
			}
			
			File outputFile = new File("path_" + imageFile.getName());
			ImageIO.write(image, "bmp", outputFile);
			
			
		} catch (IOException e) {
			System.err.println("An I//O problem occures with file " +
					imageFile.getAbsolutePath());
		}
		
	}
	
	/**
	 * Same as printPath(...) but with BufferedImage as an agrument instead 
	 *File argument.
	 * @param path - an Point array representing the path.
	 * @param image onto we want to draw the path.
	 * @param color with which we want to draw the path.
	 */
	public void printPath(Point[] path, BufferedImage image, int color) {
		if(path == null) return;
		
		for(int i = 0;i < path.length;i++) {
			image.setRGB(path[i].x, path[i].y, color);
		}
		
	}
	
	/**
	 * Represents this Graph as a picture in which each pixel is a vertex. 
	 *Each pixel can have eight neighbors, so each pixel is colored differently 
	 *according to how many neighbors it has.
	 * @return creates an bitmap picture in current directory, it is named 
	 *"print_graph.bmp".
	 */
	public void print() {
		File outputFile = null;
		BufferedImage image = null;
		try {
			
			image = new BufferedImage(width, height,BufferedImage.TYPE_3BYTE_BGR );
	
			for(int i = 0;i < this.getSize();i++) {

				if(getVertexOf(i) == null){
					image.setRGB(i%width, i/width, 0x000000);//black
					
				}else if(this.countHeirsOf(i) == 8) {
					image.setRGB(i%width, i/width, 0xffffff);//white
					
				}else if(this.countHeirsOf(i) == 7){
					image.setRGB(i%width, i/width, 0xffd800);//yellow
					
				}else if(this.countHeirsOf(i) == 6){
					image.setRGB(i%width, i/width, 0xff0000);//red
					
				}else if(this.countHeirsOf(i) == 5){
					image.setRGB(i%width, i/width, 0x0026FF);//blue
					
				}else if(this.countHeirsOf(i) == 4){
					image.setRGB(i%width, i/width, 0xF50E61);//pink
					
				}else if(this.countHeirsOf(i) == 3){
					image.setRGB(i%width, i/width, 0xD71CC8);//purple
					
				}else if(this.countHeirsOf(i) == 2){
					image.setRGB(i%width, i/width, 0x25590F);//dark green
					
				}else if(this.countHeirsOf(i) == 1){
					image.setRGB(i%width, i/width, 0x5F2C0B);//brown
					
				}
				
			}
			
			outputFile = new File("print_graph.bmp");
			ImageIO.write(image, "bmp", outputFile);
			
			
		} catch (IOException e) {
			System.err.println("An I//O problem occures with file " +
					outputFile.getAbsolutePath());
		}
	}
	
	/**
	 * Filters the heirs(neighbors) of the pixel(vertex) position passed as an argument. Concerns if 
	 *the position is border, corner, or forbidden, and decides whether has it neighbor or not
	 *according to that information. Returns an array that contains all the neighbors of 
	 *the concerning position as an integer numbers, or -1 if the neighbor is forbidden. It
	 *can return all neighbors as -1, that means the position itself is forbidden. Also returns
	 *as an argument array weightsFilter the weights to the corresponding neighbors; for example
	 *the heirsFilter(...)[2] corresponds to weightsFilter[2].
	 *TODO: HOW CREATES THE WEIGHTS DISAMBIGUATION
	 *	
	 * @param pixels - an array containing information of all the pixels, see 
	 *java.awt.image.Raster.getDataElements(int x,...).
	 * @param position - the position of the pixel in the passed pixels array.
	 * @param width - the width of the picture(map).
	 * @param height - the height of the picture(map).
	 * @param colorFilter - the color in the picture that is considered as forbidden possiton.
	 * @param hasAlpha - if the pixel is represented with alpha raster.
	 * @return the desired neighbors of the passed position. Also updates weightsFilter array
	 *every time the method is called.
	 * @throws IllegalArgumentException if the array weightsFilter length is different than 8.
	 */
	private int[] heirsFilter(
			float[] weightsFilter,
			byte[] pixels, int position, int width,int height, 
			int redFilter,int greenFilter, int blueFilter, boolean hasAlpha){
		if(weightsFilter.length != 8) {
			throw new IllegalArgumentException(
					"The array weightsFilter length must be exactly 8!");
		}
		
		int[] na = new int[8];// The absolute neighbors of the current position
		int[] nr = new int[8];// The relative neighbors of the current position
		
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
			return nr;
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
//System.out.println(pixels[na[i]+a]+ " " +pixels[na[i]+a+1] +" " +pixels[na[i]+a+2] );
			if(		(pixels[na[i]+a] ==  redFilter) &&
					(pixels[na[i]+a+1] == greenFilter) &&
					(pixels[na[i]+a+2] == blueFilter))
			{
				nr[i] = -1;
			}
			
			// Calculating the weights
			if(nr[i] == -1) {
				weightsFilter[i] = Float.POSITIVE_INFINITY;
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
		
		return nr;
	}
	
	
	private void setFromHFile(File hMapFile) {
		
		Scanner sc = null; int row = 0; 
		try {
			sc = new Scanner(hMapFile);
			sc.nextLine();// Skipping the first initial line.(HM width height)
			
			
			ArrayList<Integer> heirsList = new ArrayList<Integer>(8);
			ArrayList<Float> weightsList = new ArrayList<Float>(8);
			int vertIndex = 0;
			
			while(sc.hasNextLine()) {
				
				vertIndex = sc.nextInt();
				while(!sc.hasNext("n")) {
				
					heirsList.add(sc.nextInt());
					
					weightsList.add(sc.nextFloat());
				
				}sc.nextLine();
				
				this.setVertex(vertIndex, heirsList, weightsList);
				heirsList.clear(); weightsList.clear();
			}
		
			
			
		}catch (InputMismatchException ime) {
			System.err.println(
					"In the file " + hMapFile.getAbsolutePath() + ", at the row:col " +
					(row) + ":unknown  - there is no proper value."+
					" Must be integer or float!");
			ime.printStackTrace();
			
		}catch (FileNotFoundException e) {
			System.err.println(
					"The file" + hMapFile.getAbsolutePath() + "is missing!" );
			e.printStackTrace();
			
		}finally {
			if(sc != null) {
				sc.close();
			}
		}
	}
	
	private void setFromImageFile(File imageFile, int r, int g, int b) {
		 
		try {
			BufferedImage image = ImageIO.read(imageFile);
			
			byte[] pixels = 
			(byte[]) image.getRaster().getDataElements(0, 0, getWidth(), getHeight(), null);
			boolean hasAlpha = image.getAlphaRaster() != null;
			
			ArrayList<Integer> heirsList = new ArrayList<Integer>(8);
			ArrayList<Float> weightsList = new ArrayList<Float>(8);
			
			int[] heirs = new int[8]; float[] weights = new float[8];
			for(int pixel = 0;pixel < getSize();pixel++) {
	
				heirs = this.heirsFilter(weights, pixels, pixel, getWidth(), getHeight(),r,g,b, hasAlpha);
				for(int i = 0;i < heirs.length;i++) {
					if(heirs[i] != -1) {
						heirsList.add(heirs[i]);
						weightsList.add(weights[i]);
	
					}
				}
				if(heirsList.size() == 0) continue;
				
				setVertex(pixel, heirsList, weightsList);
	
				heirsList.clear();  weightsList.clear();
			}
			
			
		}catch(IOException e) {
			System.out.println(
					"Error reading image" + imageFile.getAbsolutePath());
		}
	}

	private void setFromBinaryMap(File binaryMapFile) {
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(binaryMapFile);
			fis.skip(12);
			
			
			byte[] data = new byte[1000000]; //data.lenght % 4 = 0 must be true.
			ByteBuffer bb = ByteBuffer.allocateDirect(1000000).wrap(data);
			int limit; // the limit of the buffer
			
			int[] vIndex = {0}, hIndex = {0};
			while((limit = fis.read(data)) != -1) {

				bb.limit(limit);
				setVertices(bb, vIndex, hIndex);

				bb.clear();

			}

			
		}catch(FileNotFoundException fnf) {
			System.err.println(
					"The binaryMapFile is " + binaryMapFile.getAbsolutePath() + " mising! ");
		}catch(IOException e) {
			System.err.println(
					"Error while reading file " + binaryMapFile.getAbsolutePath());
		}
		
	}
	
	private void setFromImage(BufferedImage image, int r, int g, int b) {
		 
		byte[] pixels = 
		(byte[]) image.getRaster().getDataElements(0, 0, getWidth(), getHeight(), null);
		boolean hasAlpha = image.getAlphaRaster() != null;
		
		ArrayList<Integer> heirsList = new ArrayList<Integer>(8);
		ArrayList<Float> weightsList = new ArrayList<Float>(8);
		
		int[] heirs = new int[8]; float[] weights = new float[8];
		for(int pixel = 0;pixel < getSize();pixel++) {

			heirs = this.heirsFilter(weights, pixels, pixel, getWidth(), getHeight(),r,g,b, hasAlpha);
			for(int i = 0;i < heirs.length;i++) {
				if(heirs[i] != -1) {
					heirsList.add(heirs[i]);
					weightsList.add(weights[i]);

				}
			}
			if(heirsList.size() == 0) continue;
			
			setVertex(pixel, heirsList, weightsList);

			heirsList.clear();  weightsList.clear();
		}
			
			
		
	}

}






package file;

import java.awt.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * 
 * @author Todor Zarkov
 *
 */
public class Files {
  
  //This class cannot be instantiated.
  private Files() {};

  /**
   * You can define empty line by the regex argument in the following way:
   *  if regex is between two \n symbols, the file splits in that 
   *  point, discarding the regex and putting \n in the end of the 
   *  first string.
   * @param file - the file we want to split(text file, means it contains 
   * \n delimiter for the new line)
   * @param regex - that is regular expression pointing out the specific 
   * empty line.
   * @return the array list containing the split file.
   * @throws FileNotFoundException if the file specified doesn't exist 
   * or is unreachable.
   */
  public static ArrayList<StringBuilder> splitFileAtEmptyLine(
      File file, String regex) 
          throws FileNotFoundException {
    Scanner fScanner = null;
    ArrayList<StringBuilder> result =
        new ArrayList<StringBuilder>();
    boolean hadEmptyRow = true;
    int index = -1;
    
    try {
      fScanner = new Scanner(file);
      while (fScanner.hasNextLine()) {
        String line = null;
        while (fScanner.hasNextLine() && 
            !(line = fScanner.nextLine()).matches(regex)){
          if (hadEmptyRow) {
            result.add(new StringBuilder());
            hadEmptyRow = false;
            index++;
          }
          result.get(index).append(line);
          result.get(index).append("\n");
        }
        hadEmptyRow = true;
      }
      fScanner.close();
      return result;
      
    } finally {
      if (fScanner != null) {
        fScanner.close();
      }
    }
  }
  
  
  /**
   * 
   * @param f - the file containing the 2D set. The content of the 
   * file must obey the following rules:<br>
   * The first row must contains only one integer number - the number 
   * of all the points. Every other row must contains two integer 
   * numbers - the coordinates of the point; the first one is the x 
   * coordinate, the second - y coordinate of the point.
   * @return all the points of the specified 2D set in the file.
   * @throws FileNotFoundException if the file is missing or 
   * unreadable.<br>
   * IllegalArgumentexception if the file doesn't match the 
   * specification.
   */
  public static HashSet<Point> get2DSet(File f) 
      throws FileNotFoundException, IllegalArgumentException {
    Scanner sFile = new Scanner(f);
    Scanner sLine = null;
    int rowCounter = 1;
    try {
      String firstLine = sFile.nextLine();//nse
      sLine = new Scanner(firstLine);
      int length = sLine.nextInt();//ime,nse
      if (sLine.hasNext()) {
        throw new InputMismatchException();//ime
      }
      sLine.close();
      
      HashSet<Point> result = new HashSet<Point>();
      for (int i = 0;i < length;i++) {
        rowCounter++;
        String line = sFile.nextLine();//nse
        sLine = new Scanner(line);
        int x = sLine.nextInt();//ime,nse
        int y = sLine.nextInt();//ime,nse
        result.add(new Point(x,y));
        sLine.close();
      }
      sFile.close();
      return result;
      
      
    } catch(NoSuchElementException e) {
      throw new IllegalArgumentException(
          "Wrong data at row: " + rowCounter
          + ".\n First row  must contains only one integer number."
          + "Every other row must contains |int|SPACE|int|, "
          + "nothing else and nothing less.\n "
          + "There must be as much rows in the file as the first"
          + "row specifies, plus one(the first row).\n ");
    } finally {
      if (sLine != null) {
        sLine.close();
      }
      if (sFile != null)   {
        sFile.close();
      }
    }
  }

  
  /**
   * Gets only the text from the specified HTML inFile and writes it
   * into the specified outFile.
   * @param inFile the HTML input file.
   * @param outFile the text output file.
   * @throws FileNotFoundException if the specified files do not exist
   * or are unaccessible.
   * @throws IOException if error during the reading or writing.
   */
  public static void getTextFromHTML(
      File inFile, File outFile, String charset) 
      throws FileNotFoundException, IOException{
    InputStreamReader reader = 
        new InputStreamReader(new FileInputStream(inFile), charset);
    PrintWriter writer = new PrintWriter(outFile, charset);
    
    StringBuilder buffer = new StringBuilder();
    
    try {
      boolean inTag = true;
      while (reader.ready()) {
        int nextChar = reader.read();
        char ch = (char) nextChar;
        if (ch == '<') {
          if (!inTag) {
            printBuffer(buffer, writer);
          }
          buffer.setLength(0);
          inTag = true;
        } else if (ch == '>') {
          inTag = false;
        } else {
          if (!inTag) {
            buffer.append(ch);
          }
        }
      }
    
      
    } catch (IOException ioe) {
      throw new IOException(
              "An reading error occure in file "
              + inFile.getAbsolutePath());
    } finally {
      reader.close();
      writer.close();
    }
  }

  
  private static void printBuffer(
      StringBuilder buffer, PrintWriter writer) {
    String str = buffer.toString();
    String trimmed = str.trim();
    String result = trimmed.replaceAll("\n\\s+", "\n");
    if (result.length() != 0) {
      writer.println(result);
    }
  }
}

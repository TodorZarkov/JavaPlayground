
package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Stack;

import dataStructures.ArrayQueue;



/**
 * Represents the system directory tree structure.
 * @author Todor Zarkov
 *
 */
public class FileSystemTree {

  public static void main(String[] args) throws IOException {
    printAsDFS(new File("DFStreeOut"));
    // printAsBFS(new File ("BFStreeOut"));
  }



  /**
   * Prints onto the specified file all the system directories.
   * It is in Deep representation.
   * @param file
   * @throws IOException if error till file writing.
   * @throws FileNotFoundexception if the specified file is missing
   * or unreachable.
   */
  public static void printAsDFS(File file) 
      throws IOException {
    PrintStream outStream = null;
    File[] roots = File.listRoots();
    Path root = roots[0].toPath();
    ArrayQueue queue = new ArrayQueue();
    DirectoryStream<Path> dirStream = null;

    DirectoryStream.Filter<Path> filter = 
        new DirectoryStream.Filter<Path>() {
      @Override
      public boolean accept(Path file) throws IOException {

        return Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)
            && Files.isReadable(file);
      }
    };

    try {
      outStream = new PrintStream(file);
      queue.offer(root);

      while (queue.size() != 0) {
        outStream.println(queue.peek());
        dirStream = Files.newDirectoryStream((Path) queue.poll(),
            filter);

        for (Path dir : dirStream) {
          queue.offer(dir);
        }
        dirStream.close();
      }

    } catch (FileNotFoundException fnf) {
      throw fnf;
    } catch (IOException e) {
      throw e;
    } finally {
      if (dirStream == null) {
        try {
          dirStream.close();
        } catch (IOException e) {
          // Intentionally do nothing; there's nothing we can do.
        }
      }
      if (outStream == null) {
        outStream.close();
      }
    }
  }

  
  /**
   * Prints onto the specified file all the system directories.
   * It is in Breadth representation.
   * @param file
   * @throws IOException if error till file writing.
   * @throws FileNotFoundexception if the specified file is missing
   * or unreachable.
   */
  public static void printAsBFS(File file) throws IOException {
    PrintStream outStream = null;
    File[] roots = File.listRoots();
    Path root = roots[0].toPath();
    //DynamicStack stack = new DynamicStack();
    Stack<Path> stack =new Stack<Path>();
    DirectoryStream<Path> dirStream = null;

    DirectoryStream.Filter<Path> filter = 
        new DirectoryStream.Filter<Path>() {
      @Override
      public boolean accept(Path file) throws IOException {

        return Files.exists(
            file, LinkOption.NOFOLLOW_LINKS)
            && Files.isDirectory(file, 
                LinkOption.NOFOLLOW_LINKS)
            && Files.isReadable(file);
      }
    };

    try {
      outStream = new PrintStream(file);
      stack.push(root);
      outStream.println(root);

      while (stack.size() != 0) {
        outStream.println(stack.peek());
        dirStream = Files
            .newDirectoryStream((Path) stack.pop(), filter);

        for (Path dir : dirStream) {
          stack.push(dir);
        }
        dirStream.close();
      }

    } catch (FileNotFoundException fnf) {
      throw fnf;
    } catch (IOException e) {
      throw e;
    } finally {
      if (dirStream == null) {
        try {
          dirStream.close();
        } catch (IOException e) {
          // Intentionally do nothing; there's nothing we can do.
        }
      }
      if (outStream == null) {
        outStream.close();
      }
    }
  }
}

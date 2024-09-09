/*
 * Class that will track the user's inputs, and write them to a .txt file,
 * allowing the user to undo their last n actions. Actions must be valid, 
 * and must meet a criteria, that criteria being it has interacted with
 * the canvas. The stack will grow downwards.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class UndoStack {
    private String path;
    private int stackSize = 20; //max amount of actions that can be stored in stack
    private File undoStackTxt;
    private File tempStackTxt;
    private ArrayList<String> storeContents;

    public UndoStack(String path){
        this.path = path;
        this.storeContents = new ArrayList<String>();
        try {
            undoStackTxt = new File(path + "undoStack.txt");
            undoStackTxt.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /*
     * With each user action there will be a code associated with the user's actions,
     * I am calling this the opcode, and it will be used to determine what actions are
     * required to be taken for the actions to be undone. 
    */
    
    /**
     * Opcode - 0
     * Operand - Undo last drawn pixel 
    */
    public void pushDrawn(int x, int y){
        try (BufferedWriter pusher = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            pusher.write("0 " + x + " " + y + " ");
            pusher.newLine();
            pusher.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Opcode - 1
     * Operand - Undo last eraser pixel
    */
    public void pushEraser(int x, int y){
        try (BufferedWriter pusher = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            pusher.write("1 " + x + " " + y + " ");
            pusher.newLine();
            pusher.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Opcode - 3
     * Operand - Undo lasst eraser pixel
    */

    public void pushBucket(int x, int y){
        try (BufferedWriter pusher = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            pusher.write("3 " + x + " " + y + " ");
            pusher.newLine();
            pusher.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method pops the last pushed line of the undoStack.txt, removes that line and rewrites
     * Very convoluted way of doing this, and I feel like I can implement this much better.
     * Returns NUL if stack is empty
    */
    public String popUndoStack(){
        String returnValue = "NUL";
        try (BufferedReader popper = new BufferedReader(new FileReader(path + "undoStack.txt"))){
            String line;
            while ((line = popper.readLine()) != null){
                storeContents.add(line);
                returnValue = line;
            }
            popper.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        try (BufferedWriter clear = new BufferedWriter(new FileWriter(path + "undoStack.txt", false))){
            clear.write("");
            clear.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        try (BufferedWriter rewrite = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            int index = 0;
            while (index < storeContents.size() - 1){
                rewrite.write(storeContents.get(index));
                rewrite.newLine();
                index++;
            }
            rewrite.close();
            storeContents.clear();
        } catch (IOException e){
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * Deltes the undoStack.txt file, after use.
     */
    public void deleteUndoStack(){
        if (undoStackTxt.delete()){
            System.out.println("File Deleted");
        } else {
            System.out.println("File does not Exist");
        }
    }
}

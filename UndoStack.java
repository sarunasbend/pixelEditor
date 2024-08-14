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
import java.util.ArrayList;

public class UndoStack {
    private String path;
    private int stackSize = 20; //max amount of actions that can be stored in stack
    private File undoStackTxt;
    private ArrayList<String> storeContents;

    public UndoStack(String path){
        this.path = path;
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
     * Opcode - ADD
     * Operand - Undo last drawn pixel 
     */
    public void pushDrawn(int x, int y){
        try (BufferedWriter pusher = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            pusher.write("[ADD][" + x + " : " + y + "]");
            pusher.newLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Opcode - ERS
     * Operand - Undo lasst eraser pixel
     */

    public void pushEraser(int x, int y){
        try (BufferedWriter pusher = new BufferedWriter(new FileWriter(path + "undoStack.txt", true))){
            pusher.write("[ERS][" + x + " : " + y + "]");
            pusher.newLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void popUndoStack(){
        try (BufferedReader popper = new BufferedReader(new FileReader(path + "undoStack.txt"))){
            System.out.println(popper.readLine());
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }

    public void deleteUndoStack(){
        if (undoStackTxt.delete()){
            System.out.println("File Deleted");
        } else {
            System.out.println("File does not Exist");
        }
        try {
            Scanner temp = new Scanner(undoStackTxt);
        } catch (FileNotFoundException e){

        }

    }
}

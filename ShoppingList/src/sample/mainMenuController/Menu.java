package sample.mainMenuController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.exception.InvalidData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {
   private Stage mWindow;
   private TextField textField;
   private ArrayList<Button> shoppingLists;
   private GridPane grid;

   private int modifyItem = -1 ;
   private Boolean isClicked = true;
   private BorderPane parentNodeSecondScene;
   private Button submitButton, saveButton, addButton, deleteButton, modifyButton, input_result;
   public Menu(Stage window){
       this.mWindow = window;
       this.shoppingLists = new ArrayList<>();
   }
   public void startApp(){
       //firstSceneofTheProgram
       HBox parentNode = textFieldAndSubmitBtn();
       this.mWindow.setTitle("Shopping List");
       this.mWindow.setScene(new Scene(parentNode, 600, 300));
       //waitting for user to click submit input
       submitButton.setOnAction(actionEvent -> {
           try{
               //check if the input is
               // only letters and process to secondscene.
               checkforInput();
               secondScene();
           }catch (Exception e){
               System.out.println(e.getMessage());
           }

       });
       //run the application.
       this.mWindow.show();
   }
   private HBox textFieldAndSubmitBtn(){
       textField = new TextField();
       textField.setPromptText("addItem");

       submitButton = new Button("submit");
       //set eventlistener to submit button.
       HBox hbox = new HBox(textField, submitButton);
       hbox.setAlignment(Pos.CENTER);
       return hbox;
   }
   private void checkforInput() throws InvalidData {
       String input = textField.getText().trim().replaceAll("\\s", "");
       if(!input.matches("[a-zA-Z]+")){
           try{
               int convertInputToInteger = Integer.parseInt(input);
           }catch (NumberFormatException e){
               popUP("only letters");
               throw new InvalidData("Only letter");
           }
       }
   }
   private void secondScene(){
       parentNodeSecondScene  = new BorderPane();
       grid = new GridPane();
       createAllButtonsOfSecondScene();
       HBox box = new HBox(addButton, deleteButton, modifyButton, saveButton);
       box.setAlignment(Pos.CENTER);
       //add the items inside the grid
       //after the user click submit
       add();
       //add the childeNode to the layout.
       parentNodeSecondScene.setBottom(box);
       parentNodeSecondScene.setTop(grid);
       parentNodeSecondScene.setStyle("-fx-border-color: violet;" +
                                      "-fx-border-width: 5px;" +
                                        "-fx-border-style: solid;"+
                                        "-fx-background-color: violet;");

       this.mWindow.setScene(new Scene(parentNodeSecondScene, 600, 300));
   }
   private void createAllButtonsOfSecondScene(){
       input_result = new Button(textField.getText());
       saveButton = new Button("save");
       addButton = new Button("add");
       deleteButton = new Button("delete");
       modifyButton = new Button("modify");
       //click the add button to switch back to
       //first scene
       addButton.setOnAction(actionEvent -> {
           startApp();
       });
       //click the save button to switch back to
       saveButton.setOnAction(actionEvent -> {
           FileChooser fileChooser = new FileChooser();
           FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)","*.txt");
           fileChooser.getExtensionFilters().add(extFilter);

           File file = fileChooser.showSaveDialog(this.mWindow);
           if(file != null){
                SaveFile(file);
           }
       });
   }
   private void SaveFile(File file){
       try{
           FileWriter fileWriter = new FileWriter(file);
           BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
           //add items to shopping list
           for(int i=0;i<shoppingLists.size();i++){
               bufferedWriter.write(shoppingLists.get(i).getText() + "\n");
           }
           bufferedWriter.close();
       }catch (IOException e){
           System.out.println(e.getMessage());
       }
   }
   private void add(){
       Button listItems;
       getCssStyleColorForButton(input_result);
       if(modifyItem != -1){
           shoppingLists.set(modifyItem, input_result);
           modifyItem = -1;
       }else{
           shoppingLists.add(input_result);
       }
       //loop to display all items in the shoppinglists.
       for(int row=0;row<shoppingLists.size();row++){
           listItems = shoppingLists.get(row);
           //set the style for the input
           grid.setAlignment(Pos.CENTER);
           grid.setVgap(5);
           grid.add(setEventListernForItemInLists(listItems, row), 0 , row);
       }

   }
   private Button getCssStyleColorForButton(Button listItem){
       listItem.setStyle("-fx-text-fill: white; -fx-background-color: #20639B; -fx-pref-width: 200px");
       return listItem;
   }

   private Button setEventListernForItemInLists(Button listItem, int positionItem){
       //After the input item in the shopping Lists clicked
       //deleteButton, and modifyButton are watting to be clicked.
       listItem.setOnAction(actionEvent -> {
           hoverButton(listItem);
           //delete when the user specify the position to the user want to delete
           deleteButton.setOnAction(actionEvent1 -> {
               delete(listItem);
           });
           modifyButton.setOnAction(actionEvent1 -> {
               modify(listItem, positionItem);
           });
       });
       return listItem;
   }
   private void delete(Button listItem){
       shoppingLists.remove(listItem);
       grid.getChildren().remove(listItem);
   }
   private void modify(Button listItem, int positionItem){
       if(shoppingLists.contains(listItem)){
           modifyItem = positionItem;
           //when modify trigger, switch back to
           //first scene.
           startApp();
       }
   }
    private void hoverButton(Button listItem){
        listItem.setStyle("-fx-background-color: red; -fx-pref-width: 200px");
    }
    private void popUP(String invalidData){
        Stage stage = new Stage();
        Label label = new Label(invalidData);

        Button button1 = new Button("Close the pop up");
        button1.setOnAction(e-> stage.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);
        stage.setScene(scene1);
        stage.showAndWait();

    }
       //create buffer memory
       //for the next list item.

 }



package MainPack;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TreeView<String> treeView;
    @FXML
    private Button addBtn, updateBtn, deleteBtn, flyTo, scanFram;
    @FXML
    private Canvas farmCanvas;

    private GraphicsContext gc;
    private String itemName;

    private Map<String, double[]> itemLocations; // Store item locations
    private double droneX = 50, droneY = 50;
    
    @FXML 
    private ImageView dot;

    @FXML
    private Label label;
    
    //private Map<String, double[]> itemLocations = new HashMap<>();
    private Map<String, double[]> itemDimensions = new HashMap<>();
    private Map<String, Double> itemPrices = new HashMap<>();
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initialize TreeView
        TreeItem<String> root = new TreeItem<>("Farm");
        treeView.setRoot(root);
        treeView.setShowRoot(true);

        // Initialize Canvas
        gc = farmCanvas.getGraphicsContext2D();
        itemLocations = new HashMap<>();
        drawFarm();

        // Add Command Center as an initial item
        TreeItem<String> commandCenter = new TreeItem<>("Command Center");
        root.getChildren().add(commandCenter);
        addItemToCanvas("Command Center", 0,0, 50, 50, Color.GRAY);

        // Initialize drone as a circle
        
        //farmCanvas.getGraphicsContext2D().fillOval(dot.getCenterX() - 5, dot.getCenterY() - 5, 10, 10);
    }

//    @FXML
//    public void addItems() {
//        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
//
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Add Item");
//        dialog.setHeaderText("Add Item In : " + (selectedItem != null ? selectedItem.getValue() : "Farm"));
//        dialog.setContentText("Enter item name:");
//
//        Optional<String> result = dialog.showAndWait();
//        result.ifPresent(name -> {
//            // Prompt for position
//            TextInputDialog positionDialog = new TextInputDialog("100,100");
//            positionDialog.setTitle("Add Item Position");
//            positionDialog.setHeaderText("Specify Position for: " + name);
//            positionDialog.setContentText("Enter coordinates (x, y):");
//
//            Optional<String> positionResult = positionDialog.showAndWait();
//            positionResult.ifPresent(position -> {
//                try {
//                    // Parse coordinates
//                    String[] coordinates = position.split(",");
//                    double x = Double.parseDouble(coordinates[0].trim());
//                    double y = Double.parseDouble(coordinates[1].trim());
//
//                    // Add item to TreeView and Canvas
//                    if (selectedItem != null) {
//                        selectedItem.getChildren().add(new TreeItem<>(name));
//                        selectedItem.setExpanded(true); // Expand the parent node
//                    } else {
//                        treeView.getRoot().getChildren().add(new TreeItem<>(name));
//                    }
//
//                    addItemToCanvas(name, x, y, 50, 50, Color.GREEN);
//                } catch (NumberFormatException e) {
//                    // Handle invalid input
//                    System.err.println("Invalid coordinates entered. Please try again.");
//                }
//            });
//        });
//    }
    
    @FXML
    public void addItems() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Item");
        dialog.setHeaderText("Add Item In: " + (selectedItem != null ? selectedItem.getValue() : "Root"));

        // Create input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Enter item name");

        TextField positionField = new TextField("100,100");
        positionField.setPromptText("Enter position (x, y)");

        TextField dimensionsField = new TextField("50,50");
        dimensionsField.setPromptText("Enter dimensions (width, height)");

        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");

        // Arrange inputs in a grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Item Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Position:"), 0, 1);
        grid.add(positionField, 1, 1);
        grid.add(new Label("Dimensions:"), 0, 2);
        grid.add(dimensionsField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Retrieve user inputs
                String name = nameField.getText().trim();
                String[] coordinates = positionField.getText().split(",");
                double x = Double.parseDouble(coordinates[0].trim());
                double y = Double.parseDouble(coordinates[1].trim());

                String[] dimensions = dimensionsField.getText().split(",");
                double width = Double.parseDouble(dimensions[0].trim());
                double height = Double.parseDouble(dimensions[1].trim());

                double price = Double.parseDouble(priceField.getText().trim());

                // Add item to TreeView
                TreeItem<String> newItem = new TreeItem<>(name);
                if (selectedItem != null) {
                    selectedItem.getChildren().add(newItem);
                    selectedItem.setExpanded(true); // Ensure parent is expanded
                } else {
                    treeView.getRoot().getChildren().add(newItem); // Add to root
                }

                // Add item to Canvas
                addItemToCanvas(name, x, y, width, height, Color.GREEN);

                System.out.println("Item added: Name=" + name + ", Position=(" + x + ", " + y + "), "
                        + "Dimensions=(" + width + ", " + height + "), Price=" + price);
            } catch (Exception e) {
                System.err.println("Invalid input: " + e.getMessage());
            }
        }
    }




    private void addItemToCanvas(String name, double x, double y, double width, double height, Color color) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
        gc.strokeText(name, x + 5, y - 5);
        itemLocations.put(name, new double[]{x + width / 2, y + height / 2});
    }

//    @FXML
//    public void flyToItem() {
//        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
//        if (selectedItem != null && itemLocations.containsKey(selectedItem.getValue())) {
//            double[] targetLocation = itemLocations.get(selectedItem.getValue());
//            animateDrone(droneX, droneY, targetLocation[0], targetLocation[1]);
//        }
//    }
    
    @FXML
    private void flyToItem() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            label.setText("No item selected.");
            return;
        }

        String itemName = selectedItem.getValue();
        if (!itemLocations.containsKey(itemName)) {
            label.setText("No location found for item: " + itemName);
            return;
        }

        double[] targetLocation = itemLocations.get(itemName);
        double targetX = targetLocation[0];
        double targetY = targetLocation[1];

        // Update label to indicate starting animation
        label.setText("Flying to: " + itemName);

        // Move to target
        PathTransition moveToTarget = createPathTransition(dot, dot.getX(), dot.getY(), targetX, targetY, 2);

        // Pause at target
        PauseTransition pauseAtTarget = new PauseTransition(Duration.seconds(5));
        pauseAtTarget.setOnFinished(e -> label.setText("Pausing at: " + itemName));

        // Return to start
        double startX = dot.getX();
        double startY = dot.getY();
        PathTransition returnToStart = createPathTransition(dot, targetX, targetY, startX, startY, 2);
        returnToStart.setOnFinished(e -> label.setText("Drone returned to start."));

        // Chain animations
        moveToTarget.setOnFinished(e -> {
            label.setText("Reached: " + itemName);
            pauseAtTarget.play();
        });
        pauseAtTarget.setOnFinished(e -> {
            label.setText("Returning to start...");
            returnToStart.play();
        });

        // Start animation
        moveToTarget.play();
    }

    private PathTransition createPathTransition(ImageView node, double startX, double startY, double endX, double endY, double durationSeconds) {
        Line path = new Line(startX, startY, endX, endY);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(durationSeconds));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setInterpolator(Interpolator.EASE_BOTH);
        return pathTransition;
    }


    @FXML
    public void scanFarm() {
        double centerX = farmCanvas.getWidth() / 2;
        double centerY = farmCanvas.getHeight() / 2;

        // Get the current position of the ImageView
        double startX = dot.getX();
        double startY = dot.getY();

        // Create a path that will go around the canvas perimeter (4 corners)
        Path path = new Path();

        // Define the four corners of the canvas
        double topLeftX = 0, topLeftY = 0;
        double topRightX = farmCanvas.getWidth(), topRightY = 0;
        double bottomRightX = farmCanvas.getWidth(), bottomRightY = farmCanvas.getHeight();
        double bottomLeftX = 0, bottomLeftY = farmCanvas.getHeight();

        // Start from the current position of the dot (ImageView)
        path.getElements().add(new MoveTo(startX, startY));

        // Draw path from corner to corner around the canvas
        path.getElements().add(new LineTo(topLeftX, topLeftY));
        path.getElements().add(new LineTo(topRightX, topRightY));
        path.getElements().add(new LineTo(bottomRightX, bottomRightY));
        path.getElements().add(new LineTo(bottomLeftX, bottomLeftY));
        path.getElements().add(new LineTo(startX, startY)); // Return to start

        // Create a PathTransition for the drone animation
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(dot);
        pathTransition.setPath(path);
        pathTransition.setInterpolator(Interpolator.EASE_BOTH);
        pathTransition.setCycleCount(1);  // Play the animation once
        pathTransition.setAutoReverse(false); // No auto-reverse
        pathTransition.setDuration(Duration.seconds(20)); // Duration for one full round

        // Update status at key points
        Timeline statusUpdater = new Timeline(
        	new KeyFrame(Duration.seconds(0),e-> updateStatus("Scanning Farm.")),
            new KeyFrame(Duration.seconds(5), e -> updateStatus("Scanning Farm....")),
            new KeyFrame(Duration.seconds(10), e -> updateStatus("Scanning Farm........")),
            new KeyFrame(Duration.seconds(15), e -> updateStatus("Scanning Farm.............")),
            new KeyFrame(Duration.seconds(20), e -> updateStatus("Scanning complete"))
        );
        
        // Start the status update timeline along with the animation
        statusUpdater.setCycleCount(1);
        statusUpdater.play();

        // Play the animation
        pathTransition.play();

        // When the animation is finished, reset status
        pathTransition.setOnFinished(e -> updateStatus("Idle"));
    }

    // Method to update the status (this could update a label or log messages)
    private void updateStatus(String status) {
        // Assuming you have a Label named `statusLabel`
        label.setText(status);
    }


    
//    @FXML
//    public void updateItem() {
//        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
//            dialog.setTitle("Update Item");
//            dialog.setHeaderText("Update the selected TreeView item");
//            dialog.setContentText("Enter new value:");
//
//            Optional<String> result = dialog.showAndWait();
//            result.ifPresent(newValue -> selectedItem.setValue(newValue));
//        }
//    }
    
    @FXML
    public void updateItem() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String oldName = selectedItem.getValue();

            // Create a dialog for updating item details
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Item");
            dialog.setHeaderText("Update details for: " + oldName);

            // Create input fields
            TextField nameField = new TextField(oldName);
            nameField.setPromptText("Enter item name");

            TextField positionField = new TextField(itemLocations.containsKey(oldName)
                    ? String.format("%.0f,%.0f", itemLocations.get(oldName)[0], itemLocations.get(oldName)[1])
                    : "100,100");
            positionField.setPromptText("Enter position (x, y)");

            TextField dimensionsField = new TextField(itemDimensions.containsKey(oldName)
                    ? String.format("%.0f,%.0f", itemDimensions.get(oldName)[0], itemDimensions.get(oldName)[1])
                    : "50,50");
            dimensionsField.setPromptText("Enter dimensions (width, height)");

            TextField priceField = new TextField(itemPrices.containsKey(oldName)
                    ? String.format("%.2f", itemPrices.get(oldName))
                    : "0.00");
            priceField.setPromptText("Enter price");

            // Arrange inputs in a grid layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(new Label("Item Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Position:"), 0, 1);
            grid.add(positionField, 1, 1);
            grid.add(new Label("Dimensions:"), 0, 2);
            grid.add(dimensionsField, 1, 2);
            grid.add(new Label("Price:"), 0, 3);
            grid.add(priceField, 1, 3);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Retrieve updated values
                    String newName = nameField.getText().trim();

                    String[] coordinates = positionField.getText().split(",");
                    double x = Double.parseDouble(coordinates[0].trim());
                    double y = Double.parseDouble(coordinates[1].trim());

                    String[] dimensions = dimensionsField.getText().split(",");
                    double width = Double.parseDouble(dimensions[0].trim());
                    double height = Double.parseDouble(dimensions[1].trim());

                    double price = Double.parseDouble(priceField.getText().trim());

                    // Update the TreeView item
                    selectedItem.setValue(newName);

                    // Update maps only for the selected item
                    if (itemLocations.containsKey(oldName)) {
                        itemLocations.remove(oldName); // Remove old entry
                    }
                    itemLocations.put(newName, new double[]{x, y});

                    if (itemDimensions.containsKey(oldName)) {
                        itemDimensions.remove(oldName); // Remove old entry
                    }
                    itemDimensions.put(newName, new double[]{width, height});

                    if (itemPrices.containsKey(oldName)) {
                        itemPrices.remove(oldName); // Remove old entry
                    }
                    itemPrices.put(newName, price);

                    // Redraw the canvas to reflect the changes for this specific item
                    redrawCanvas();

                    System.out.println("Updated item: Name=" + newName + ", Position=(" + x + ", " + y + "), "
                            + "Dimensions=(" + width + ", " + height + "), Price=" + price);
                } catch (Exception e) {
                    System.err.println("Invalid input: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No item selected for updating.");
        }
    }

    @FXML
    public void deleteItem() {
        // Get the selected item from the TreeView
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            String itemName = selectedItem.getValue(); // Get the name of the selected item

            // Remove the item from the parent in the TreeView
            TreeItem<String> parent = selectedItem.getParent();
            if (parent != null) {
                parent.getChildren().remove(selectedItem);
            }

            // Remove the item from the canvas and map
            if (itemLocations.containsKey(itemName)) {
                itemLocations.remove(itemName); // Remove from map
                redrawCanvas();                // Redraw canvas without the deleted item
                System.out.println("Item '" + itemName + "' deleted from TreeView and canvas.");
            } else {
                System.out.println("Item '" + itemName + "' not found on the canvas.");
            }
        } else {
            System.out.println("No item selected for deletion.");
        }
    }

//    private void redrawCanvas() {
//        // Clear the canvas
//        gc.clearRect(0, 0, farmCanvas.getWidth(), farmCanvas.getHeight());
//
//        // Redraw all items from the itemLocations map
//        for (Map.Entry<String, double[]> entry : itemLocations.entrySet()) {
//            String name = entry.getKey();
//            double[] position = entry.getValue();
//            double x = position[0];
//            double y = position[1];
//            double width = 100; // Adjust based on your items' sizes
//            double height = 100; // Adjust based on your items' sizes
//
//            // Draw the item
//            gc.setFill(Color.GREEN); // Example color
//            gc.fillRect(x - width / 2, y - height / 2, width, height);
//            gc.setStroke(Color.BLACK);
//            gc.strokeRect(x - width / 2, y - height / 2, width, height);
//            gc.strokeText(name, x - width / 2 + 5, y - height / 2 - 5);
//        }
//    }

    private void redrawCanvas() {
        GraphicsContext gc = farmCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, farmCanvas.getWidth(), farmCanvas.getHeight());

        for (Map.Entry<String, double[]> entry : itemLocations.entrySet()) {
            String itemName = entry.getKey();
            double[] location = entry.getValue();
            double[] dimensions = itemDimensions.get(itemName);
            double price = itemPrices.get(itemName);

            // Draw the item
            gc.setFill(Color.BLUE);
            gc.fillRect(location[0], location[1], dimensions[0], dimensions[1]);

            gc.setFill(Color.WHITE);
            gc.fillText(itemName + " ($" + price + ")", location[0] + 5, location[1] + 15);
        }
    }

    
    private  void animateDrone(double startX, double startY, double endX, double endY) {
        // Animate the drone (ImageView) by transitioning only its position
        PathTransition transition = new PathTransition();
        transition.setRate(2); // Speed of the animation
        transition.setNode(dot); // Move the drone only, not the entire canvas
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.setDuration(Duration.seconds(2));

        // Set the path from the current position to the target position
        transition.setPath(new Line(startX, startY, endX, endY));

        // Set the action after the animation finishes
        transition.setOnFinished(e -> {
            droneX = endX;
            droneY = endY;
        });

        // Start the animation
        transition.play();
    }

    private void drawFarm() {
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, 0, farmCanvas.getWidth(), farmCanvas.getHeight());
    }
    
}

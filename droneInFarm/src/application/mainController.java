package application;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import tellolib.camera.TelloCamera;
import tellolib.communication.TelloConnection;
import tellolib.control.TelloControl;
import tellolib.drone.TelloDrone;

public class mainController implements Initializable{
	
	@FXML
	private TextArea message;
	
	@FXML
	private Button collect;
	
	@FXML
	private Button scanFarm;
	
	@FXML
	private Button item;
	
	@FXML
	private TreeView treeView;
	
    @FXML
    private Button forward,Con,DisCon;

    @FXML
    private Button backward;

    @FXML
    private Button left;

    @FXML
    private Button right;

    @FXML
    private Button takeOff;

    @FXML
    private Button land;
	
	
	private static final Logger logger = Logger.getGlobal();
	private static final ConsoleHandler handler = new ConsoleHandler();
	
	private TelloControl	telloControl;
	private TelloDrone		drone;
	private TelloCamera		camera;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		TreeItem<String> rootItem = new TreeItem<>("Farm");
	    rootItem.setExpanded(true);

	    TreeItem<String> commandItem = new TreeItem<>("Command Center");
	    
	    // Add "Barn" node with children
	    TreeItem<String> barnItem = new TreeItem<>("Barn");
	    barnItem.getChildren().add(new TreeItem<>("Live Stock Area"));
	    barnItem.getChildren().add(new TreeItem<>("Milk Storage"));

	    // Add "Crops" node with children
	    TreeItem<String> cropsItem = new TreeItem<>("Crops");
	    cropsItem.getChildren().add(new TreeItem<>("Wheat"));
	    cropsItem.getChildren().add(new TreeItem<>("Corn"));

	    // Add items to root
	    rootItem.getChildren().add(barnItem);
	    rootItem.getChildren().add(cropsItem);

	    // Assign the root item to the TreeView
	    treeView.setRoot(rootItem);

	
		
	}

	public void fletch(ActionEvent event) {
		
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		
		logger.setLevel(Level.FINE);
		handler.setLevel(Level.FINE);
		 message.appendText("start \n");
			message.appendText("battery level="+";\n speed="+ ";\ntime=");

		
		 logger.info("start");
		 
		
		 
//		 Demo1 demo = new Demo1();
//		 
//		 demo.execute();
		 
		 TelloControl telloControl = TelloControl.getInstance();
		 message.appendText("connecting \n");
		    
		    // TelloControl class has a separate logger so it can log at
		    // a different level than the code that calls it. TelloControl
		    // logging is off by default. The rest of the Tello SDK classes use
		    // this separate logger as well.
		    // Set log level to FINE to see information from the SDK classes 
		    // about what they are doing. Helps to understand how they work
		    // and helpful for debugging.
		    
		    telloControl.setLogLevel(Level.FINE);

		    // Wrap all code in a try/catch block so that we trap any error
		    // that occurs and handle it by safely landing the drone.
		    
		    try 
		    {
		    	// Call method to establish network connection with the drone.
		    	
			    telloControl.connect();
			    
			    // Send command to put drone in 'command mode', where it will
			    // accept the rest of the commands it knows instead of flying
			    // itself.
			    
			    telloControl.enterCommandMode();
					    
			    // In this demo we will issue drone commands to retrieve all of
			    // the status information available from the drone. Here we create
			    // variables to hold the information.
			    
			    int battery = 0, speed = 0, time = 0, height = 0, temp = 0;
			    
			    double baro = 0, tof = 0;
			    
			    String sn = "", sdk = "";
			    
			    int[] attitude;
			    double [] acceleration;
			    
			    // Issue commands to get the drone status information. When to call 
			    // these methods on TelloControl, the data is retrieved and stored
			    // in the global Drone class as well as returned for your use.
			    
				battery = telloControl.getBattery();
				
				speed = telloControl.getSpeed();
				
				time = telloControl.getTime();
				
				baro = telloControl.getBarometer();
				
				height = telloControl.getHeight();
				
				tof = telloControl.getTof();
				
				temp = telloControl.getTemp();
				
				sn = telloControl.getSN();
				
				sdk = telloControl.getSDK();
				
				attitude = telloControl.getAttitude();
				
				acceleration = telloControl.getAcceleration();
				
				message.appendText("battery level=" + battery + ";\n speed=" + speed + ";\ntime=" + time);
				
			    logger.info("battery level=" + battery + ";speed=" + speed + ";time=" + time);
			    logger.info("baro=" + baro + ";height=" + height + ";tof=" + tof + ";temp=" + temp);
			    logger.info("sdk=" + sdk + ";sn=" + sn);
			    logger.info("pitch=" + attitude[0] + ";roll=" + attitude[1] + ";yaw=" + attitude[2]);
			    logger.info("accel x=" + acceleration[0] + ";y=" + acceleration[1] + ";z=" + acceleration[2]);
				
		    }	
		    catch (Exception e) {
		    	// If an error occurs, log it to the console window.
		    	e.printStackTrace();
		    } finally {
		    	// Error or not, make sure the drone lands before program ends. Note we
		    	// only want to issue land command if we are connected.
		    	if (telloControl.getConnection() == TelloConnection.CONNECTED)
		    	{
		    		try
		    		{telloControl.land();}
		    		catch(Exception e) { e.printStackTrace();}
		    	}
		    }
		    
		    // Close network connection to drone.
	    	telloControl.disconnect();
		    
		    logger.info("end");
		        
	}
	
	@FXML
    public void takeOff(ActionEvent event) {
        try {
        	logger.addHandler(handler);
    		logger.setUseParentHandlers(false);
    		
    		logger.setLevel(Level.FINE);
    		handler.setLevel(Level.FINE);
    		
    		 logger.info("start");
    		 
//    		 FlySquare demo = new FlySquare();
//    		 
//    		 demo.execute();
    		 
    		 telloControl = TelloControl.getInstance();
    		    
    		    drone = TelloDrone.getInstance();
    		    
    		    camera = TelloCamera.getInstance();

    		    telloControl.setLogLevel(Level.FINE);

    		    telloControl.connect();
    			    
    			telloControl.enterCommandMode();
    			    
    			    telloControl.startStatusMonitor();
    			    
    			    telloControl.streamOn();
    			    
    			    camera.setStatusBar(this::updateWindow);
    			    
    			    camera.startVideoCapture(true);
    			    message.appendText(" start recording video ");

    			    

            message.appendText("Taking off...\n");
            telloControl.takeOff();
            message.appendText("Drone is in the air.\n");
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error taking off.\n");
        }
    }

    @FXML
    public void land(ActionEvent event) {
        try {
        
            message.appendText("Landing...\n");
            telloControl.land();
            message.appendText("Drone has landed.\n");
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error landing.\n");
        }
    }

    @FXML
    public void moveForward(ActionEvent event) {
        try {
        	
            message.appendText("Moving forward 50cm...\n");
            telloControl.forward(50);
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error moving forward.\n");
        }
    }

    @FXML
    public void moveBackward(ActionEvent event) {
        try {
        	
            message.appendText("Moving backward 50cm...\n");
            telloControl.backward(50);
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error moving backward.\n");
        }
    }

    @FXML
    public void connectingDrone(ActionEvent event) {
    	try {
    		logger.addHandler(handler);
    		logger.setUseParentHandlers(false);
    		
    		logger.setLevel(Level.FINE);
    		handler.setLevel(Level.FINE);
    		
    		 logger.info("start");
    		 
//    		 FlySquare demo = new FlySquare();
//    		 
//    		 demo.execute();
    		 
    		 telloControl = TelloControl.getInstance();
    		    
    		    drone = TelloDrone.getInstance();
    		    
    		    camera = TelloCamera.getInstance();

    		    telloControl.setLogLevel(Level.FINE);

    		    telloControl.connect();
    			    
    			    telloControl.enterCommandMode();
    			    
    			    telloControl.startStatusMonitor();
    			    
    			    telloControl.streamOn();
    			    

    		
    	}catch(Exception e) {
    		
    	}
    }
    
    @FXML
    public void disConnectingDrone(ActionEvent event) {
    	telloControl.disconnect();
    }
    
    @FXML
    public void moveLeft(ActionEvent event) {
        try {
            message.appendText("Moving left 50cm...\n");
            telloControl.left(50);
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error moving left.\n");
        }
    }

    @FXML
    public void moveRight(ActionEvent event) {
        try {
            message.appendText("Moving right 50cm...\n");
            telloControl.right(50);
        } catch (Exception e) {
            e.printStackTrace();
            message.appendText("Error moving right.\n");
        }
    }
	
//	public void scanFarm(ActionEvent event) {
//		
//		logger.addHandler(handler);
//		logger.setUseParentHandlers(false);
//		
//		logger.setLevel(Level.FINE);
//		handler.setLevel(Level.FINE);
//		
//		 logger.info("start");
//		 
////		 FlySquare demo = new FlySquare();
////		 
////		 demo.execute();
//		 
//		 telloControl = TelloControl.getInstance();
//		    
//		    drone = TelloDrone.getInstance();
//		    
//		    camera = TelloCamera.getInstance();
//
//		    telloControl.setLogLevel(Level.FINE);
//
//		    try 
//		    {
//			    telloControl.connect();
//			    
//			    telloControl.enterCommandMode();
//			    
//			    telloControl.startStatusMonitor();
//			    
//			    telloControl.streamOn();
//			    
//			    // Set the video stream processor to automatically update the video
//			    // stream status area by repeatedly calling the method specified.
//			    
//			    camera.setStatusBar(this::updateWindow);
//			    
//			    camera.startVideoCapture(true);
//			    
//			    telloControl.takeOff();
//			    
//			    // Now we will execute a series of movement commands to fly in a square
//			    // pattern. Distances in centimeters.
//			    
//			    for (int i = 0; i < 4; i++)
//			    {
//			    	
//			    	telloControl.forward(100);
//			    	
//			    	telloControl.rotateRight(90);
//			    	
//			    }
//		    }	
//		    catch (Exception e) {
//		    	e.printStackTrace();
//		    } finally 
//		    {
//		    	if (telloControl.getConnection() == TelloConnection.CONNECTED && drone.isFlying())
//		    	{
//		    		try
//		    		{telloControl.land();}
//		    		catch(Exception e) { e.printStackTrace();}
//		    	}
//		    }
//		    
//	    	telloControl.disconnect();
//		    
//		    logger.info("end");
//		
//	}
    
    public void scanFarm(ActionEvent event) {
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);

        logger.setLevel(Level.FINE);
        handler.setLevel(Level.FINE);

        logger.info("start");

        telloControl = TelloControl.getInstance();
        drone = TelloDrone.getInstance();
        camera = TelloCamera.getInstance();

        telloControl.setLogLevel(Level.FINE);

        try {
            // Prompt the user for the forward distance
            TextInputDialog dialog = new TextInputDialog("100"); // Default value is 100
            dialog.setTitle("Enter Distance");
            dialog.setHeaderText("Enter the distance to move forward:");
            dialog.setContentText("Distance (in cm):");

            Optional<String> result = dialog.showAndWait();
            int distance = 100; // Default value

            if (result.isPresent()) {
                try {
                    distance = Integer.parseInt(result.get());
                } catch (NumberFormatException e) {
                    message.appendText("Invalid input. Using default distance of 100cm.\n");
                }
            }

            telloControl.connect();
            telloControl.enterCommandMode();
            telloControl.startStatusMonitor();
            telloControl.streamOn();

            camera.setStatusBar(this::updateWindow);
            camera.startVideoCapture(true);

            telloControl.takeOff();

            // Fly in a square pattern using the dynamic distance
            for (int i = 0; i < 4; i++) {
                message.appendText("Moving forward " + distance + " cm.\n");
                telloControl.forward(distance);

                message.appendText("Rotating 90 degrees to the right.\n");
                telloControl.rotateRight(90);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (telloControl.getConnection() == TelloConnection.CONNECTED && drone.isFlying()) {
                try {
                    telloControl.land();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        telloControl.disconnect();

        logger.info("end");
    }

	public void flyToItem(ActionEvent event) {
		
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);
		
		logger.setLevel(Level.FINE);
		handler.setLevel(Level.FINE);
		
		 logger.info("start");
		 
//		 FlyGrid demo = new FlyGrid();
//		 
//		 demo.execute();
		 
		  telloControl = TelloControl.getInstance();
		    
		    drone = TelloDrone.getInstance();
		    
		    camera = TelloCamera.getInstance();

		    telloControl.setLogLevel(Level.FINE);

		    try 
		    {
			    telloControl.connect();
			    message.appendText("connected");
  
			    telloControl.enterCommandMode();
	    
			    telloControl.startStatusMonitor();
			    message.appendText("get the camera status");
			    
			    telloControl.streamOn();

			    camera.setStatusBar(this::updateWindow);
			    
			    camera.startVideoCapture(true);
			    message.appendText(" start recording video ");

			    telloControl.takeOff();
			    message.appendText("drone intiate to take off");

			    
			    // Now we will execute a series of movement commands to fly in a grid
			    // pattern. Distances in centimeters.
			    
			    for (int i = 0; i < 2; i++)
			    {
			    	telloControl.forward(300);
			    	
			    	telloControl.rotateLeft(180);

			    }
		    }	
		    catch (Exception e) {
		    	e.printStackTrace();
		    } finally 
		    {
		    	if (telloControl.getConnection() == TelloConnection.CONNECTED && drone.isFlying())
		    	{
		    		try
		    		{telloControl.land();}
		    		catch(Exception e) { e.printStackTrace();}
		    	}
		    }
		    
	    	telloControl.disconnect();
		    
		    logger.info("end");
		    
		    message.appendText("end");
		
	}
	
	
	private String updateWindow()
	{
    	 return String.format("Batt: %d  Alt: %d  Hdg: %d", drone.getBattery(), drone.getHeight(), 
    			drone.getHeading());
	}

}

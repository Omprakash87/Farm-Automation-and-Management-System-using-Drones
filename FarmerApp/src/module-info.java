module FarmerApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens MainPack to javafx.graphics, javafx.fxml;
}

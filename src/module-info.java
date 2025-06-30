module Algoritmo_Project_2_v2 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jsr310;
	
        opens business to javafx.graphics, javafx.fxml;
        opens domain to com.fasterxml.jackson.databind;
        opens data to com.fasterxml.jackson.databind;
}

package model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Grid extends Group {
	public Grid(double width, double height, double line_thickness, double spacing){
		super();
		PhongMaterial m = new PhongMaterial(Color.BLACK);
		m.setSpecularPower(0);
		for(int i=0;i<=height/spacing;i++){
			Box xbar = new Box(width, line_thickness, line_thickness);
			xbar.setMaterial(m);
			xbar.translateXProperty().set(width/2);
			xbar.translateYProperty().set(i*spacing);
			this.getChildren().add(xbar);
		}
		for(int i=0;i<1+width/spacing;i++){
			Box ybar = new Box(line_thickness, height, line_thickness);
			ybar.setMaterial(m);
			ybar.translateYProperty().set(height/2);
			ybar.translateXProperty().set(i*spacing);
			this.getChildren().add(ybar);
		}
		this.setMouseTransparent(true);		
	}
}

package model;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class MaterialLibrary {
	static MaterialLibrary ref = new MaterialLibrary();
	public PhongMaterial model = new PhongMaterial();
	public PhongMaterial surface = new PhongMaterial();
	public PhongMaterial grid = new PhongMaterial();
	public PhongMaterial none = new PhongMaterial();
	public PhongMaterial support = new PhongMaterial();
	public MaterialLibrary(){
		model.setDiffuseColor(Color.BLUE);
		model.setSpecularColor(Color.LIGHTBLUE);
		model.setSpecularPower(10);
		
		surface.setDiffuseColor(Color.GREEN);
		surface.setSpecularColor(Color.LIGHTGREEN);
		surface.setSpecularPower(10);
		
		grid.setDiffuseColor(Color.BLACK);
		grid.setSpecularPower(0);
		
		support.setDiffuseColor(Color.DARKORANGE);
		support.setSpecularColor(Color.ORANGE);
		support.setSpecularPower(10);
		
		none.setDiffuseColor(Color.GREY);
		none.setSpecularColor(Color.LIGHTGREY);
		none.setSpecularPower(10);
	}
}

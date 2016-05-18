package model;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.MeshView;

public class MeshDataView extends MeshView {
	public MeshData data;
	private Tooltip t;
	public MeshDataView(MeshData d){
		super(d);
		this.data = d;
		this.t = new Tooltip("Mesh type: " + this.data.type);
		Tooltip.install(this, this.t);
	}
	public void updateView(){
		this.t.setText("Mesh type: " + this.data.type);
		switch(this.data.type){
		case part:
			this.setMaterial(MaterialLibrary.ref.model);
			break;
		case surface:
			this.setMaterial(MaterialLibrary.ref.surface);
			break;
		case support:
			this.setMaterial(MaterialLibrary.ref.support);
		case none:
			this.setMaterial(MaterialLibrary.ref.none);
		}
	}
}

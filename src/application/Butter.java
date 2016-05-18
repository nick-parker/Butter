package application;

import java.io.IOException;

import xform.Xform;
import main.Slicer;
import math.geom3d.Vector3D;
import mesh3d.Model3D;
import mesh3d.Surface3D;
import model.Grid;
import model.MaterialLibrary;
import model.MeshData;
import model.MeshDataView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;

public class Butter {
	@FXML
	private ToggleButton mvMode;
	@FXML
	private AnchorPane rightPane;
	@FXML
	private AnchorPane leftPane;
	@FXML
	private TextField import_addr;
	@FXML
	private TextField export_addr;
	@FXML
	private TextField config_addr;
	
	private Rectangle ground;
	private SubScene viewport;

	private Group models = new Group();
	private MeshDataView selection = null;
	private Slicer slicer = null;

	public Butter(){
		//Do nothing for now
	}

	@FXML
	public void initialize(){
		world.getChildren().add(models);
		world.getChildren().add(new Grid(200, 200, 0.15, 25));
		buildCamera();

		viewport = new SubScene(world, 1000, 900, true, SceneAntialiasing.BALANCED);
		viewport.setFill(Color.AZURE);
		viewport.setCamera(camera);
		viewport.setManaged(false);
		
		handleMouse(viewport);
		handleGui();
		handleMesh();
		rightPane.getChildren().add(viewport);

		//rectangle for pick results to enable dragging on the XY plane.
		ground = new Rectangle(1000,1000);
		ground.setX(-500);
		ground.setY(-500);
		ground.setFill(Color.TRANSPARENT);
		world.getChildren().add(ground);
	}

	//3d stuff
	final Xform world = new Xform();
	MaterialLibrary ml = new MaterialLibrary();
	
	final PerspectiveCamera camera = new PerspectiveCamera(true);
	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();



	private static final double ROTATION_SPEED = 2.0;
	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	String[] search_locations = new String[]{"",
											 "C:/Users/Nick/Documents/Eclipse/Dslicer/Bread/Prints/",
											 "C:/Users/Nick/Documents/Eclipse/Dslicer/Bread/Configs/"};

	public void importMesh(String filename){
		MeshData tm = null;
		for(String prefix:search_locations){
			try {
				tm = new MeshData(prefix.concat(filename));
			} catch (IOException exc) {
				// Do nothing.
			}
			try {
				tm = new MeshData(prefix.concat(filename).concat(".stl"));
			} catch (IOException exc) {
				// Do nothing.
				continue;
			}
		}
		if(tm == null){
			System.out.println("Couldn't find file " + filename);
		}
		else{
			System.out.println("Imported " + filename + " successfully.");
		}
		MeshDataView imp = new MeshDataView(tm);
		imp.setDrawMode(DrawMode.FILL);
		imp.setDepthTest(DepthTest.ENABLE);
		imp.setMaterial(ml.none);
		models.getChildren().add(imp);
	}
	private void buildCamera() {
		world.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		camera.setNearClip(0.1);
		camera.setFarClip(10000);
		camera.setTranslateZ(-400);
		camera.setTranslateY(-100);
		cameraXform.rx.setAngle(240);
		cameraXform.rz.setAngle(-45);

	}

	private void handleMouse(SubScene scene) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		scene.setOnMouseDragged(me->{
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX); 
			mouseDeltaY = (mousePosY - mouseOldY);

			if (me.isPrimaryButtonDown()) {
				cameraXform.rz.setAngle(cameraXform.rz.getAngle() -
						mouseDeltaX*ROTATION_SPEED);
				cameraXform.rx.setAngle(limit(cameraXform.rx.getAngle() -
						mouseDeltaY*ROTATION_SPEED));
			}
			else if (me.isSecondaryButtonDown()) {
				camera.setTranslateX(camera.getTranslateX()-mouseDeltaX);
				camera.setTranslateY(camera.getTranslateY()-mouseDeltaY);
			}
		});
		// Handle zooming
		scene.setOnScroll((ScrollEvent e)-> {
			double z = camera.getTranslateZ();
			double newZ = z + e.getDeltaY()*0.5;
			camera.setTranslateZ(newZ);
		});
	}
	private void handleMesh(){
		// Set up context menu.
		ContextMenu cm = new ContextMenu();
		MenuItem delete = new MenuItem("delete");
		delete.onActionProperty().set(e->{
			models.getChildren().remove(selection);
		});
		cm.getItems().add(delete);
		Menu meshTypes = new Menu("Set Mesh Type");
		MenuItem none = new MenuItem("none");
		none.onActionProperty().set(e->{
			selection.data.type = MeshData.meshType.none;
			selection.updateView();
		});
		meshTypes.getItems().add(none);
		MenuItem part = new MenuItem("part");
		part.onActionProperty().set(e->{
			selection.data.type = MeshData.meshType.part;
			selection.updateView();
		});
		meshTypes.getItems().add(part);
		MenuItem surface = new MenuItem("surface");
		surface.onActionProperty().set(e->{
			selection.data.type = MeshData.meshType.surface;
			selection.updateView();
		});
		meshTypes.getItems().add(surface);
//		MenuItem support = new MenuItem("support");
//		support.onActionProperty().set(e->{
//			selection.data.type = MeshData.meshType.support;
//			selection.updateTooltip();
//		});
//		meshTypes.getItems().add(support);
		cm.getItems().add(meshTypes);
		
		
		// Set up drag based movement.
		models.setOnMousePressed(e->{
			PickResult pr = e.getPickResult();
			selection = (MeshDataView) pr.getIntersectedNode();
			if(e.isSecondaryButtonDown()){
				cm.show(pr.getIntersectedNode(), e.getScreenX(), e.getScreenY());
			}
			else{
				models.setMouseTransparent(true);
			}
			e.consume();
		});
		world.setOnMousePressed(e->{
			selection = null;
		});
		world.setOnMouseDragged(me->{
			if(me.isPrimaryButtonDown()){
				PickResult pr = me.getPickResult();
				Point3D p = pr.getIntersectedPoint();
				if(selection != null){
					selection.setTranslateX(p.getX());
					selection.setTranslateY(p.getY());
				}
			}
			if(selection != null){
				me.consume();
			}
		});
		world.setOnMouseReleased(e->{
			models.setMouseTransparent(false);
		});
	}
	private double limit(double a){
		return Math.max(180, Math.min(a,270));
	}
	private void handleGui() {
		import_addr.setOnKeyPressed( e->{
			if(e.getCode()==KeyCode.ENTER){
				importMesh(import_addr.getCharacters().toString());
				import_addr.clear();
				e.consume();
			}
		});
		config_addr.setOnKeyPressed( e->{
			if(e.getCode()==KeyCode.ENTER){
				String addr = config_addr.getCharacters().toString();
				importConfig(addr);
				config_addr.clear();
				e.consume();
			}
		});
		export_addr.setOnKeyPressed(e->{
			if(e.getCode()==KeyCode.ENTER){
				this.slice(export_addr.getCharacters().toString());
				export_addr.clear();
				e.consume();
			}
		});
		
	}
	private void importConfig(String filename){
		Slicer slcr = null;
		for(String prefix:search_locations){
			try {
				 slcr = new Slicer(prefix.concat(filename));
			} catch (IOException exc) {
				// Do nothing.
			}
		}
		if(slcr == null){
			System.out.println("Couldn't load config " + filename);
		}
		else{
			System.out.println("Successfully loaded config " + filename);
		}
		this.slicer = slcr;
	}
	private void slice(String file_out){
		MeshDataView partView = null;
		MeshDataView surfaceView = null;
		for(Node p:models.getChildren()){
			MeshDataView v = (MeshDataView)p;
			switch(v.data.type){
			case part:
				partView = v;
				continue;
			case surface:
				surfaceView = v;
				continue;
			case none:
				continue;
			case support:
				continue;
			}
		}
		Model3D part = new Model3D(partView.data.tris);
		Vector3D partVector = new Vector3D(partView.getTranslateX(),
										   partView.getTranslateY(),
										   partView.getTranslateZ());
		part.move(partVector);
		this.slicer.setPart(part);
		Surface3D surface = new Surface3D(surfaceView.data.tris);
		Vector3D surfaceVector = new Vector3D(surfaceView.getTranslateX(),
				surfaceView.getTranslateY(),
				surfaceView.getTranslateZ());
		surface.move(surfaceVector);
		this.slicer.setSurface(surface);
		this.slicer.slice(file_out);
	}

}

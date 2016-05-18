package model;

import io.Stli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import math.geom3d.Point3D;
import mesh3d.Tri3D;
import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;

public class MeshData extends TriangleMesh {
	public Tri3D[] tris;
	private String filename;
	public meshType type;
	public MeshData(String filename) throws IOException{
		this.filename = filename;
		this.type = meshType.none;
		Path f = Paths.get(filename);
		if(Files.exists(f)){
			this.tris = Stli.importBinMesh(filename);
		}
		else{
			throw new IOException();
		}
		this.getTexCoords().addAll(0,0);
		ObservableFloatArray points = this.getPoints();
		ObservableFaceArray faces = this.getFaces();
		int k=0;
		for(Tri3D t : this.tris){
			faces.addAll(new int[]{k,0,k+1,0,k+2,0});
			for(int i=0;i<3;i++){
				Point3D p = t.getPoint3D(i);
				points.addAll(new float[]{(float) p.getX(), (float) p.getY(), (float) p.getZ()});
				k+=1;
			}
		}
	}
	public enum meshType{
		none,
		part,
		surface,
		support
		// Add modifier meshes here in the future.
	}
}

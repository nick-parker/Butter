package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.Stli;
import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import math.geom3d.Point3D;
import mesh3d.Tri3D;

public class MeshConverter {
	public static TriangleMesh makeMesh(Tri3D[] m){
		TriangleMesh output = new TriangleMesh();
		output.getTexCoords().addAll(0,0);
		ObservableFloatArray points = output.getPoints();
		ObservableFaceArray faces = output.getFaces();
		int k=0;
		for(Tri3D t : m){
			faces.addAll(new int[]{k,0,k+1,0,k+2,0});
			for(int i=0;i<3;i++){
				Point3D p = t.getPoint3D(i);
				points.addAll(new float[]{(float) p.getX(), (float) p.getY(), (float) p.getZ()});
				k+=1;
			}
		}
		return output;
	}
	public static TriangleMesh importMesh(String filename, boolean ascii) throws IOException{
		Path f = Paths.get(filename);
		if(Files.exists(f)){
			Tri3D[] m = Stli.importBinMesh(filename);
			return makeMesh(m);
		}
		throw new IOException();
	}
}

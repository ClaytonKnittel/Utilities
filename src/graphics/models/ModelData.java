package graphics.models;

import graphics.Color;

public class ModelData {
	
	private float[][] vertices, normals, textures;
	private int[][] triangles;
	
	public ModelData(float[][] vertices, float[][] normals, float[][] textures, int[][] triangles) {
		this.vertices = vertices;
		this.normals = normals;
		this.textures = textures;
		this.triangles = triangles;
	}
	
	public ModelData(float[][] vertices, float[][] normals, int[][] triangles) {
		this(vertices, normals, null, triangles);
		textures = new float[normals.length][2];
		for (int i = 0; i < textures.length; i++) {
			textures[i][0] = 0;
			textures[i][1] = 0;
		}
	}
	
	public static void scale(float[] data, float scale) {
		for (int i = 0; i < data.length; i++)
			data[i] *= scale;
	}
	
	public float[] getData() {
		float[] f = new float[3 * 8 * triangles.length];
		
		int fLoc = 0;
		for (int[] faces : triangles) {			// FIXME
			for (int j = 0; j < 9; j += 3) {
				f[fLoc] = vertices[faces[j] - 1][0];
				f[fLoc + 1] = vertices[faces[j] - 1][1];
				f[fLoc + 2] = vertices[faces[j] - 1][2];
				
				f[fLoc + 3] = normals[faces[j + 2] - 1][0];
				f[fLoc + 4] = normals[faces[j + 2] - 1][1];
				f[fLoc + 5] = normals[faces[j + 2] - 1][2];
				
				f[fLoc + 6] = textures[faces[j + 1] - 1][0];
				f[fLoc + 7] = textures[faces[j + 1] - 1][1];
				
				fLoc += 8;
			}
		}
		return f;
	}
	
	@Deprecated
	public float[] getData(Color c, float scale) {
		float[] f = new float[3 * 9 * triangles.length];
		float[] color = {c.red() / 255f, c.green() / 255f, c.blue() / 255f};
		
		int fLoc = 0;
		for (int[] faces : triangles) {
			for (int j = 0; j < 6; j += 2) {
				for (int i = 0; i < 3; i++) {
					f[fLoc + i] = vertices[faces[j] - 1][i] * scale; 
					f[fLoc + i + 3] = normals[faces[j + 1] - 1][i];
					f[fLoc + i + 6] = color[i];
				}
				fLoc += 9;
			}
		}
		return f;
	}
	
	public String toString() {
		String ret = "v:\n";
		for (float[] i : vertices) {
			for (float j : i)
				ret += j + "\t";
			ret += "\n";
		}
		ret += "\nvt:\n";
		for (float[] i : textures) {
			for (float j : i)
				ret += j + "\t";
			ret += "\n";
		}
		ret += "\nvn:\n";
		for (float[] i : normals) {
			for (float j : i)
				ret += j + "\t";
			ret += "\n";
		}
		ret += "\nf:\n";
		for (int[] i : triangles) {
			for (int j : i)
				ret += j + "\t";
			ret += "\n";
		}
		return ret;
	}
	
}

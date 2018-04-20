package graphics.models;

import files.FileManager;

public class OBJLoader {
	
	private static final String vertex = "v";
	private static final String norm = "vn";
	private static final String texture = "vt";
	private static final String face = "f";
	
	public static ModelData loadVertexNormOBJ(String path) {
		
		String[] data = FileManager.toString(path);
		
		float[][] vertices;
		float[][] norms;
		int[][] faces;
		
		int v = 0, vn = 0, f = 0;
		
		String[][] datas = new String[data.length][];
		
		int datasLoc = 0;
		for (int i = 0; i < data.length; i++) {
			datas[datasLoc] = data[i].split(" ");
			switch (datas[datasLoc++][0]) {
			case vertex:
				v++;
				break;
			case norm:
				vn++;
				break;
			case face:
				f++;
				break;
			default:
				datasLoc--;
			}
		}
		
		vertices = new float[v][];
		norms = new float[vn][];
		faces = new int[f][];
		
		int vl = 0, vnl = 0, fl = 0;
		
		for (int i = 0; i < datas.length; i++) {
			if (datas[i] == null)
				continue;
			switch (datas[i][0]) {
			case vertex:
				vertices[vl++] = parseVertex(datas[i]);
				break;
			case norm:
				norms[vnl++] = parseNorm(datas[i]);
				break;
			case face:
				faces[fl++] = parseFace(datas[i]);
				break;
			}
		}
		
		return new ModelData(vertices, norms, faces);
	}
	
	public static ModelData loadVertexNormTextureOBJ(String path) {
		
		String[] data = FileManager.toString(path);
		
		float[][] vertices;
		float[][] norms;
		float[][] textures;
		int[][] faces;
		
		int v = 0, vn = 0, vt = 0, f = 0;
		
		String[][] datas = new String[data.length][];
		
		int datasLoc = 0;
		for (int i = 0; i < data.length; i++) {
			datas[datasLoc] = data[i].split(" ");
			switch (datas[datasLoc++][0]) {
			case vertex:
				v++;
				break;
			case norm:
				vn++;
				break;
			case texture:
				vt++;
				break;
			case face:
				f++;
				break;
			default:
				datasLoc--;
			}
		}
		
		vertices = new float[v][];
		norms = new float[vn][];
		textures = new float[vt][];
		faces = new int[f][];
		
		int vl = 0, vnl = 0, vtl = 0, fl = 0;
		
		for (int i = 0; i < datas.length; i++) {
			if (datas[i] == null)
				continue;
			switch (datas[i][0]) {
			case vertex:
				vertices[vl++] = parseVertex(datas[i]);
				break;
			case norm:
				norms[vnl++] = parseNorm(datas[i]);
				break;
			case texture:
				textures[vtl++] = parseTexture(datas[i]);
			case face:
				faces[fl++] = parseFace(datas[i]);
				break;
			}
		}
		
		return new ModelData(vertices, norms, textures, faces);
	}
	
	private static float[] parseVertex(String[] data) {
		return new float[] {Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3])};
	}
	
	private static float[] parseNorm(String[] data) {
		return parseVertex(data);
	}
	
	private static float[] parseTexture(String[] data) {
		return new float[] {Float.parseFloat(data[1]), Float.parseFloat(data[2])};
	}
	
	private static int[] parseFace(String[] data) {
		String[][] datas = new String[][] {data[1].split("/"), data[2].split("/"), data[3].split("/")};
		return new int[] {parse(datas[0][0]), parse(datas[0][1]), parse(datas[0][2]),
				parse(datas[1][0]), parse(datas[1][1]), parse(datas[1][2]),
				parse(datas[2][0]), parse(datas[2][1]), parse(datas[2][2])};
	}
	
	private static int parse(String data) {
		if (data.equals(""))
			return 1;
		return Integer.parseInt(data);
	}
	
}

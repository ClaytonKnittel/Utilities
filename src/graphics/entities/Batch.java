package graphics.entities;

import java.nio.BufferUnderflowException;
import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Batch {
	
	private final int size;
	private FloatBuffer vertices;
	private VertexArrayObject vao;
	private final VertexBufferObject vbo;
	
	private boolean drawing;
	private int numVertices;
	private int numVertexAttribBytes;
	
	/**
	 * 
	 * @param size the number of bytes allocated to this Batch
	 * @param numBytesInVertexAttributes the number of bytes in the vertex attributes (usually 3 for location, 3 for color, 2 for texture, etc.)
	 */
	public Batch(int size, int numBytesInVertexAttributes) {
		numVertexAttribBytes = numBytesInVertexAttributes;
		
		vao = new VertexArrayObject();
		vao.bind();
		
    	vbo = new VertexBufferObject();
    	vbo.bind(GL_ARRAY_BUFFER);
    	
    	/*
    	 * we want to fill the batch dynamically we will do glBufferData with no data at all,
    	 * but we tell OpenGL the size of data, so that the storage for our VBO gets allocated.
    	 */
        vertices = MemoryUtil.memAllocFloat(size);
		this.size = vertices.capacity() * Float.BYTES;
		glBufferData(GL_ARRAY_BUFFER, this.size, GL_DYNAMIC_DRAW);
		
		numVertices = 0;
		drawing = false;
	}
	
    /**
     * Begin rendering.
     */
    public void begin() {
        if (drawing) {
            throw new IllegalStateException("Renderer is already drawing!");
        }
        drawing = true;
        numVertices = 0;
    }
    
    /**
     * End rendering.
     */
    public void end() {
        if (!drawing) {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
        drawing = false;
        flush();
    }
    
    /**
     * Dispose renderer and clean up its used data.
     */
    public void dispose() {
        MemoryUtil.memFree(vertices);
        vao.delete();
        vbo.delete();
    }
    
    public void draw(FloatBuffer data) {
    	while (data.hasRemaining()) {
			try {
	    		for (int i = 0; i < numVertexAttribBytes; i++) {
	    			vertices.put(data.get());
	    		}
			} catch (BufferUnderflowException e) {
				System.err.println("Error in drawing float buffer data: not a multiple of " + numVertexAttribBytes + " buffer data was input");
				e.printStackTrace();
			}
			numVertices ++;
    	}
    }
    
    public void draw(float[] data) {
    	draw(FloatBuffer.wrap(data));
    }
	
	
    /**
     * Flushes the data to the GPU to let it get rendered.
     */
    private void flush() {
        if (numVertices > 0) {
            vertices.flip();
            
            vao.bind();
            
            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
    }
}

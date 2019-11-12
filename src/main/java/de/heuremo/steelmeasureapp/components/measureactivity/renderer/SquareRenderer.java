/*
 * Copyright (C) GreenMile UG, - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * @author Jan-Hendrik Telke
 * on the 12 06 2019
 */
package de.heuremo.steelmeasureapp.components.measureactivity.renderer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.google.ar.core.examples.java.helloar.rendering.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * Renderer for square flag objects.
 */
public class SquareRenderer {

    // number of coordinates pervertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float coords[] = {
            -0.05f, 0.05f, 0.0f,   // top left
            -0.05f, -0.05f, 0.0f,   // bottom left
            0.05f, -0.05f, 0.0f,   // bottom right
            0.05f, 0.05f, 0.0f}; // top right
    final String TAG = "Rectangle";
    private final int mProgram;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final int vertexCount = coords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertex
    // Use to access and set the view transformation
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    // Temporary matrices allocated here to reduce number of allocations for each frame.
    private float[] mModelMatrix = new float[16];
    private float[] mModelViewMatrix = new float[16];
    private float[] mModelViewProjectionMatrix = new float[16];

    public SquareRenderer() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);   // 2 bytes per short
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        // create OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void setVerts(float v0, float v1, float v2,
                         float v3, float v4, float v5,
                         float v6, float v7, float v8,
                         float v9, float v10, float v11) {
        coords[0] = v0;
        coords[1] = v1;
        coords[2] = v2;

        coords[3] = v3;
        coords[4] = v4;
        coords[5] = v5;

        coords[6] = v6;
        coords[7] = v7;
        coords[8] = v8;

        coords[9] = v9;
        coords[10] = v10;
        coords[11] = v11;

        vertexBuffer.put(coords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    public void setColor(float red, float green, float blue, float alpha) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
        color[3] = alpha;
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void draw(float[] cameraView, float[] cameraPerspective) { // pass in the calculated transformation matrix

        ShaderUtil.checkGLError(TAG, "Before draw");

        // Build the ModelView and ModelViewProjection matrices
        // for calculating object position and light.
        Matrix.multiplyMM(mModelViewMatrix, 0, cameraView, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mModelViewProjectionMatrix, 0, cameraPerspective, 0, mModelViewMatrix, 0);

        // add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mModelViewProjectionMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

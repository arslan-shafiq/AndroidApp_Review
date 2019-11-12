/*
 * Copyright (C) GreenMile UG, - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * @author Jan-Hendrik Telke
 * on the 12 06 2019
 */
package de.heuremo.steelmeasureapp.components.measureactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.PointCloud;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.examples.java.helloar.CameraPermissionHelper;
import com.google.ar.core.examples.java.helloar.DisplayRotationHelper;
import com.google.ar.core.examples.java.helloar.rendering.BackgroundRenderer;
import com.google.ar.core.examples.java.helloar.rendering.ObjectRenderer;
import com.google.ar.core.examples.java.helloar.rendering.PlaneRenderer;
import com.google.ar.core.examples.java.helloar.rendering.PointCloudRenderer;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.NotTrackingException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;
import com.google.ar.sceneform.math.Vector3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.heuremo.BuildConfig;
import de.heuremo.R;
import de.heuremo.steelmeasureapp.components.ActivityMain;
import de.heuremo.steelmeasureapp.components.makeorder.IntentConstants;
import de.heuremo.steelmeasureapp.components.measureactivity.image.ImageConverter;
import de.heuremo.steelmeasureapp.components.measureactivity.renderer.RectanglePolygonRenderer;

/**
 * Main activity for measuring.
 */
public class ArMeasureActivity extends Activity {
    private static final String TAG = ArMeasureActivity.class.getSimpleName();
    private static final String ASSET_NAME_CUBE_OBJ = "cube.obj";
    private static final String ASSET_NAME_CUBE = "cube_green.png";
    private static final String ASSET_NAME_CUBE_SELECTED = "cube_cyan.png";

    private static final int MAX_CUBE_COUNT = 16;
    private static final float CM_ABOVE = 10f;
    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private final PlaneRenderer planeRenderer = new PlaneRenderer();
    private final PointCloudRenderer pointCloud = new PointCloudRenderer();
    private final ObjectRenderer cube = new ObjectRenderer();
    private final ObjectRenderer cubeSelected = new ObjectRenderer();
    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private final float[] anchorMatrix = new float[MAX_CUBE_COUNT];
    private final ArrayList<Anchor> anchors = new ArrayList<>();
    // Rendering. The Renderers are created here, and initialized when the GL surface is created.
    private GLSurfaceView surfaceView = null;
    private boolean installRequested;
    private Session session = null;
    private GestureDetector gestureDetector;
    private Snackbar messageSnackbar = null;
    private DisplayRotationHelper displayRotationHelper;
    private RectanglePolygonRenderer rectRenderer = null;
    private FloatingActionButton fab;
    private Anchor heightAnchor = null;
    private Anchor heightAnchorBase = null;
    // Tap handling and UI.
    private ArrayBlockingQueue<MotionEvent> queuedSingleTaps = new ArrayBlockingQueue<>(MAX_CUBE_COUNT);
    private ArrayBlockingQueue<MotionEvent> queuedLongPress = new ArrayBlockingQueue<>(MAX_CUBE_COUNT);
    private ArrayList<Float> showingTapPointX = new ArrayList<>();
    private ArrayList<Float> showingTapPointY = new ArrayList<>();

    private ArrayBlockingQueue<Float> queuedScrollDx = new ArrayBlockingQueue<>(MAX_CUBE_COUNT);
    private ArrayBlockingQueue<Float> queuedScrollDy = new ArrayBlockingQueue<>(MAX_CUBE_COUNT);
    //    OverlayView overlayViewForTest;
    private TextView tv_result;
    private GLSurfaceRenderer glSerfaceRenderer = null;
    private GestureDetector.SimpleOnGestureListener gestureDetectorListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // Queue tap if there is space. Tap is lost if queue is full.
            queuedSingleTaps.offer(e);
//            log(TAG, "onSingleTapUp, e=" + e.getRawX() + ", " + e.getRawY());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            queuedLongPress.offer(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
//            log(TAG, "onScroll, dx=" + distanceX + " dy=" + distanceY);
            queuedScrollDx.offer(distanceX);
            queuedScrollDy.offer(distanceY);
            return true;
        }
    };
    private boolean isVerticalMode = false;
    private PopupWindow popupWindow;
    private FloatingActionButton fab2;
    private float[] measuredValues = new float[5];


    private void log(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, log);
        }
    }

    private void log(Exception e) {
        try {

            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) {
                ex.printStackTrace();
            }
        }
    }

    private void logStatus(String msg) {
        try {

        } catch (Exception e) {
            log(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);

        fab2 = findViewById(R.id.fab2);

        hidePhotoButton(this);

        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Clear", Snackbar.LENGTH_LONG)
                    .show();
            reset();
        });

        fab2.setOnClickListener(view -> {
            //Image camImage = this.session.update().acquireCameraImage();

            surfaceView = findViewById(R.id.surfaceview);
            // Create a bitmap the size of the scene view.
            final Bitmap bp = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(),
                    Bitmap.Config.ARGB_8888);

            byte[] bitmap = ImageConverter.convertBitmapToByteArray(bp);

            Intent previousIntent = getIntent();

            String[] strings = previousIntent.getStringArrayExtra(IntentConstants.MEASURED_ITEMS);
            final ArrayList<String> bundledMeasureValues = previousIntent.hasExtra(IntentConstants.MEASURED_ITEMS) ? new ArrayList<>(Arrays.asList(strings)) : new ArrayList<>();

            final float width = this.measuredValues[1];
            final float length = this.measuredValues[2];
            final float height = this.measuredValues[4];
            final String imageRef = IntentConstants.IMAGE_PREFIX + bundledMeasureValues.size();

            Gson gson = new GsonBuilder().create();
            bundledMeasureValues.add(gson.toJson(BundledMeasureValues.builder()
                    .setHeight(height)
                    .setWidth(width)
                    .setLength(length)
                    .setImageRef(imageRef)
                    .build()));

            Intent intent = new Intent(this, ActivityMain.class);

            Bundle mBundle = new Bundle();

            mBundle.putStringArrayList(IntentConstants.MEASURED_ITEMS, bundledMeasureValues);
            //mBundle.putByteArray("image0", bitmap);


            for (String bundledValuesString : bundledMeasureValues) {

                BundledMeasureValues bundledMeasureValues1 = gson.fromJson(bundledValuesString, BundledMeasureValues.class);

                if (imageRef.equals(bundledMeasureValues1.getImageRef())) {
                    // add data for new image ref
                    mBundle.putByteArray(bundledMeasureValues1.getImageRef(), bitmap);
                } else {
                    mBundle.putByteArray(bundledMeasureValues1.getImageRef(), previousIntent.getByteArrayExtra(bundledMeasureValues1.getImageRef()));
                }
            }


            intent.putExtras(mBundle);

            // Sent intent


            startActivity(intent);


        });

        tv_result = findViewById(R.id.tv_result);

        displayRotationHelper = new DisplayRotationHelper(/*context=*/ this);

        if (CameraPermissionHelper.hasCameraPermission(this)) {
            setupRenderer();
        }

        installRequested = false;
    }

    private void reset() {
        this.anchors.clear();
        this.heightAnchor = null;
        this.heightAnchorBase = null;
        hidePhotoButton(this);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupRenderer() {
        if (surfaceView != null) {
            return;
        }
        surfaceView = findViewById(R.id.surfaceview);

        // Set up tap listener.
        gestureDetector = new GestureDetector(this, gestureDetectorListener);
        surfaceView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        glSerfaceRenderer = new GLSurfaceRenderer(this);
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(glSerfaceRenderer);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logStatus("onResume()");
        if (session == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this);
                    return;
                }

                session = new Session(/* context= */ this);
            } catch (UnavailableArcoreNotInstalledException
                    | UnavailableUserDeclinedInstallationException e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (Exception e) {
                message = "This device does not support AR";
                exception = e;
            }

            if (message != null) {
                showSnackbarMessage(message, true);
                Log.e(TAG, "Exception creating session", exception);
                return;
            }

            // Create default config and check if supported.
            Config config = new Config(session);
            if (!session.isSupported(config)) {
                showSnackbarMessage("This device does not support AR", true);
            }
            session.configure(config);

            setupRenderer();
        }

        showLoadingMessage();
        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
        surfaceView.onResume();
        displayRotationHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        logStatus("onPause()");
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            displayRotationHelper.onPause();
            surfaceView.onPause();
            session.pause();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        logStatus("onRequestPermissionsResult()");
        Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG)
                .show();
        if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
            // Permission denied with checking "Do not ask again".
            CameraPermissionHelper.launchPermissionSettings(this);
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logStatus("onWindowFocusChanged()");
        if (hasFocus) {
            // Standard Android full-screen functionality.
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void showLoadingMessage() {
        runOnUiThread(() -> {
            messageSnackbar = Snackbar.make(
                    ArMeasureActivity.this.findViewById(android.R.id.content),
                    getString(R.string.searching_for_surfaces), Snackbar.LENGTH_INDEFINITE);
            messageSnackbar.getView().setBackgroundColor(0xbf323232);
            messageSnackbar.show();
        });
    }

    private void hideLoadingMessage() {
        runOnUiThread(() -> {
            if (messageSnackbar != null) {
                messageSnackbar.dismiss();
            }
            messageSnackbar = null;
        });
    }

    private void hidePhotoButton(Context context) {
        Activity act = (Activity) context;
        act.runOnUiThread(() -> fab2.hide());
    }

    private void showPhotoButton(Context context) {
        Activity act = (Activity) context;
        act.runOnUiThread(() -> fab2.show());
    }

    private void showSnackbarMessage(String message, boolean finishOnDismiss) {
        messageSnackbar =
                Snackbar.make(
                        ArMeasureActivity.this.findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_INDEFINITE);
        messageSnackbar.getView().setBackgroundColor(0xbf323232);
        if (finishOnDismiss) {
            messageSnackbar.setAction(
                    "Dismiss",
                    v -> messageSnackbar.dismiss());
            messageSnackbar.addCallback(
                    new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    });
        }
        messageSnackbar.show();
    }

    private void toast(int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
    }

    private PopupWindow getPopupWindow() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(this);

        ArrayList<String> sortList = new ArrayList<>();
        sortList.add(getString(R.string.action_1));
        sortList.add(getString(R.string.action_2));
        sortList.add(getString(R.string.action_3));
        sortList.add(getString(R.string.action_4));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                sortList);
        // the drop down list is a list view
        ListView listViewSort = new ListView(this);
        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 3:// move vertical axis
                        toast(R.string.action_4_toast);
                        break;
                    case 0:// delete
                        toast(R.string.action_1_toast);
                        break;
                    case 1:// set as first
                        toast(R.string.action_2_toast);
                        break;
                    case 2:// move horizontal axis
                    default:
                        toast(R.string.action_3_toast);
                        break;
                }
                return true;
            }
        });
        // set on item selected
        listViewSort.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 3:// move vertical axis
                    isVerticalMode = true;
                    popupWindow.dismiss();
                    break;
                case 0:// delete
                    glSerfaceRenderer.deleteNowSelection();
                    popupWindow.dismiss();
                    break;
                case 1:// set as first
                    glSerfaceRenderer.setNowSelectionAsFirst();
                    popupWindow.dismiss();
                    break;
                case 2:// move horizontal axis
                default:
                    isVerticalMode = false;
                    popupWindow.dismiss();
                    break;
            }

        });
        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth((int) (getResources().getDisplayMetrics().widthPixels * 0.4f));
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // set the listview as popup content
        popupWindow.setContentView(listViewSort);
        return popupWindow;
    }

    private class GLSurfaceRenderer implements GLSurfaceView.Renderer {
        private static final String TAG = "GLSurfaceRenderer";
        private final int DEFAULT_VALUE = -1;
        // according to cube.obj, cube diameter = 0.02f
        private final float cubeHitAreaRadius = 0.08f;
        private final float[] centerVertexOfCube = {0f, 0f, 0f, 1};
        private final float[] vertexResult = new float[4];
        private final float[] mPoseTranslation = new float[3];
        private final float[] mPoseRotation = new float[4];
        private Context context;
        private int nowTouchingPointIndex = DEFAULT_VALUE;
        private int viewWidth = 0;
        private int viewHeight = 0;
        private float[] tempTranslation = new float[3];
        private float[] tempRotation = new float[4];
        private float[] projmtx = new float[16];
        private float[] viewmtx = new float[16];

        public GLSurfaceRenderer(Context context) {
            this.context = context;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            logStatus("onSurfaceCreated()");
            GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread(context);
            if (session != null) {
                session.setCameraTextureName(backgroundRenderer.getTextureId());
            }

            // Prepare the other rendering objects.
            try {
                rectRenderer = new RectanglePolygonRenderer();
                cube.createOnGlThread(context, ASSET_NAME_CUBE_OBJ, ASSET_NAME_CUBE);
                cube.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
                cubeSelected.createOnGlThread(context, ASSET_NAME_CUBE_OBJ, ASSET_NAME_CUBE_SELECTED);
                cubeSelected.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
            } catch (IOException e) {
                log(TAG, "Failed to read obj file");
            }
            try {
                planeRenderer.createOnGlThread(context, "trigrid.png");
            } catch (IOException e) {
                log(TAG, "Failed to read plane texture");
            }
            pointCloud.createOnGlThread(context);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            if (width <= 0 || height <= 0) {
                logStatus("onSurfaceChanged(), <= 0");
                return;
            }
            logStatus("onSurfaceChanged()");

            displayRotationHelper.onSurfaceChanged(width, height);
            GLES20.glViewport(0, 0, width, height);
            viewWidth = width;
            viewHeight = height;
            setNowTouchingPointIndex(DEFAULT_VALUE);
        }

        public void deleteNowSelection() {
            logStatus("deleteNowSelection()");
            int index = nowTouchingPointIndex;
            if (index > -1) {
                if (index < anchors.size()) {
                    anchors.remove(index).detach();
                }
                if (index < showingTapPointX.size()) {
                    showingTapPointX.remove(index);
                }
                if (index < showingTapPointY.size()) {
                    showingTapPointY.remove(index);
                }
            }
            setNowTouchingPointIndex(DEFAULT_VALUE);
        }

        public void setNowSelectionAsFirst() {
            logStatus("setNowSelectionAsFirst()");
            int index = nowTouchingPointIndex;
            if (index > -1 && index < anchors.size()) {
                if (index < anchors.size()) {
                    for (int i = 0; i < index; i++) {
                        anchors.add(anchors.remove(0));
                    }
                }
                if (index < showingTapPointX.size()) {
                    for (int i = 0; i < index; i++) {
                        showingTapPointX.add(showingTapPointX.remove(0));
                    }
                }
                if (index < showingTapPointY.size()) {
                    for (int i = 0; i < index; i++) {
                        showingTapPointY.add(showingTapPointY.remove(0));
                    }
                }
            }
            setNowTouchingPointIndex(DEFAULT_VALUE);
        }

        public int getNowTouchingPointIndex() {
            return nowTouchingPointIndex;
        }

        public void setNowTouchingPointIndex(int index) {
            nowTouchingPointIndex = index;
            showCubeStatus();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//            log(TAG, "onDrawFrame(), mTouches.size=" + mTouches.size());
            // Clear screen to notify driver it should not load any pixels from previous frame.
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            if (viewWidth == 0 || viewWidth == 0) {
                return;
            }
            if (session == null) {
                return;
            }
            // Notify ARCore session that the view size changed so that the perspective matrix and
            // the video background can be properly adjusted.
            displayRotationHelper.updateSessionIfNeeded(session);

            try {
                session.setCameraTextureName(backgroundRenderer.getTextureId());

                // Obtain the current frame from ARSession. When the configuration is set to
                // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
                // camera framerate.
                Frame frame = session.update();
                Camera camera = frame.getCamera();
                // Draw background.
                backgroundRenderer.draw(frame);

                // If not tracking, don't draw 3d objects.
                if (camera.getTrackingState() == TrackingState.PAUSED) {
                    return;
                }

                // Get projection matrix.
                camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

                // Get camera matrix and draw.
                camera.getViewMatrix(viewmtx, 0);

                // Compute lighting from average intensity of the image.
                final float lightIntensity = frame.getLightEstimate().getPixelIntensity();

                // Visualize tracked points.
                PointCloud pointCloud = frame.acquirePointCloud();
                ArMeasureActivity.this.pointCloud.update(pointCloud);
                ArMeasureActivity.this.pointCloud.draw(viewmtx, projmtx);

                // Application is responsible for releasing the point cloud resources after
                // using it.
                pointCloud.release();

                // Check if we detected at least one plane. If so, hide the loading message.
                if (messageSnackbar != null) {
                    for (Plane plane : session.getAllTrackables(Plane.class)) {
                        if (plane.getType() == com.google.ar.core.Plane.Type.HORIZONTAL_UPWARD_FACING &&
                                plane.getTrackingState() == TrackingState.TRACKING) {
                            hideLoadingMessage();
                            break;
                        }
                    }
                }

                // Visualize planes.
                planeRenderer.drawPlanes(
                        session.getAllTrackables(Plane.class), camera.getDisplayOrientedPose(), projmtx);

                // draw cube & line from last frame
                if (anchors.size() < 1) {
                    // no point
                    showResult("");
                } else {
                    // draw selected cube
                    if (nowTouchingPointIndex != DEFAULT_VALUE) {
                        drawObj(getPose(anchors.get(nowTouchingPointIndex)), cubeSelected, viewmtx, projmtx, lightIntensity);
                        checkIfHit(cubeSelected, nowTouchingPointIndex);
                    }
                    StringBuilder sb = new StringBuilder();
                    double total = 0;
                    Pose point1;


                    List<Anchor> firstThreeAnchors = new ArrayList<>();


                    int i = 0;
                    for (Anchor anchor : anchors) {
                        Pose point_anchor = getPose(anchor);
                        drawObj(point_anchor, cube, viewmtx, projmtx, lightIntensity);


                        if (anchor != heightAnchorBase && anchor != heightAnchor) {
                            firstThreeAnchors.add(anchor);
                            i++;
                        }
                    }


                    if (anchors.size() > 0) {
                        // draw first cube
                        Pose point0 = getPose(firstThreeAnchors.get(0));

                        // draw base level lines
                        //drawObj(point0, cube, viewmtx, projmtx, lightIntensity);

                        checkIfHit(cube, 0);
                        // draw the rest cube
                        for (int y = 1; y < firstThreeAnchors.size(); y++) {
                            point1 = getPose(firstThreeAnchors.get(y));
                            log("onDrawFrame()", "before drawObj()");

                            checkIfHit(cube, i);
                            log("onDrawFrame()", "before drawLine()");
                            drawLine(point0, point1, viewmtx, projmtx);
                            float distanceCm = ((int) (getDistance(point0, point1) * 1000)) / 10.0f;
                            total += distanceCm;
                            sb.append(" + ").append(distanceCm);

                            point0 = point1;

                            measuredValues[y] = distanceCm;
                        }
                    }

                    /// draw height line
                    if (heightAnchorBase != null && heightAnchor != null) {
                        drawLine(heightAnchor.getPose(), heightAnchorBase.getPose(), viewmtx, projmtx);
                    }

                    // show result
                    String result = sb.toString().replaceFirst("[+]", "") + " = " + (((int) (total * 10f)) / 10f) + " cm";

                    if (heightAnchorBase != null && heightAnchor != null) {
                        Pose point3 = heightAnchorBase.getPose();
                        Pose point4 = heightAnchor.getPose();
                        float distanceCm = ((int) (getDistance(point3, point4) * 1000)) / 10.0f;
                        result = result + " " + getString(R.string.st_height) + " " + (((int) (distanceCm * 10f)) / 10f) + " cm";
                        measuredValues[4] = distanceCm;
                    }

                    showResult(result);
                }

                // check if there is any touch event
                MotionEvent tap = queuedSingleTaps.poll();
                if (tap != null && camera.getTrackingState() == TrackingState.TRACKING) {
                    for (HitResult hit : frame.hitTest(tap)) {

                        // Check if any plane was hit, and if it was hit inside the plane polygon.j
                        Trackable trackable = hit.getTrackable();
                        // Creates an anchor if a plane or an oriented point was hit.
                        if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())
                                || (trackable instanceof Point
                                && ((Point) trackable).getOrientationMode()
                                == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL)) {
                            // Cap the number of objects created. This avoids overloading both the
                            // rendering system and ARCore.

                            /*
                            if (anchors.size() >= 3) {
                                anchors.get(0).detach();
                                anchors.remove(0);

                                showingTapPointX.remove(0);
                                showingTapPointY.remove(0);
                            }

                            // Adding an Anchor tells ARCore that it should track this position in
                            // space. This anchor will be used in PlaneAttachment to place the 3d model
                            // in the correct position relative both to the world and to the plane.
                            anchors.add(hit.createAnchor());

                            */

                            if (anchors.size() <= 3) {
                                anchors.add(hit.createAnchor());
                            }

                            if (anchors.size() == 3) {
                                showPhotoButton(context);
                                drawCenterObject();
                            }

                            showingTapPointX.add(tap.getX());
                            showingTapPointY.add(tap.getY());
                            nowTouchingPointIndex = anchors.size() - 1;

                            showCubeStatus();
                            break;
                        }

                    }
                } else {
                    handleMoveEvent(nowTouchingPointIndex);
                }

                if (anchors.size() > 3) {
                    handleMoveEvent(nowTouchingPointIndex);
                }

            } catch (Throwable t) {
                // Avoid crashing the application due to unhandled exceptions.
                Log.e(TAG, "Exception on the OpenGL thread", t);
            }
        }


        private void drawCenterObject() {

            Anchor anchor1 = anchors.get(0);
            Anchor anchor2 = anchors.get(1);
            Anchor anchor3 = anchors.get(2);


            float[] translation1 = anchor1.getPose().getTranslation();
            float[] translation2 = anchor2.getPose().getTranslation();
            float[] translation3 = anchor3.getPose().getTranslation();

            float[] translation = centerOfTranslations(translation1, translation2, translation3);


            float[] rotation = anchor3.getPose().getRotationQuaternion();

            // calculate center between

            /*    x
                  |       n
                  |      /
                  x     /
                       /
            n---------n

            And set new anchors there -> one of them can be moved, resulting in height value
            */

            heightAnchorBase = session.createAnchor(new Pose(translation, rotation));
            anchors.add(anchors.size(), heightAnchorBase);


            float[] higherTranslation = translation.clone();


            higherTranslation[1] += CM_ABOVE / 100f;
            heightAnchor = session.createAnchor(new Pose(higherTranslation, rotation));
            anchors.add(anchors.size(), heightAnchor);


        }

        private float[] centerOfTranslations(float[] translation1, float[] translation2, float[] translation3) {

            Vector3 point1 = vectorFromFloatArray(translation1);
            Vector3 point2 = vectorFromFloatArray(translation2);
            Vector3 point3 = vectorFromFloatArray(translation3);

            Vector3 result = Vector3.add(Vector3.add(point1, point2), point3).scaled(1f / 3f);

            return new float[]{result.x, result.y, result.z};
        }

        Vector3 vectorFromFloatArray(float[] array) {
            return new Vector3(array[0], array[1], array[2]);
        }

        private void handleMoveEvent(int nowSelectedIndex) {


            try {
                if (showingTapPointX.size() < 1 || queuedScrollDx.size() < 2) {
                    // no action, don't move
                    return;
                }
                /*if (nowTouchingPointIndex == DEFAULT_VALUE) {
                    // no selected cube, don't move
                    return;
                }*/
                /*
                if (nowSelectedIndex >= showingTapPointX.size()) {
                    // wrong index, don't move.
                    return;
                }*/
                float scrollDx = 0;
                float scrollDy = 0;
                int scrollQueueSize = queuedScrollDx.size();
                for (int i = 0; i < scrollQueueSize; i++) {
                    scrollDx += queuedScrollDx.poll();
                    scrollDy += queuedScrollDy.poll();
                }

                if (heightAnchor != null) {
                    Anchor anchor = anchors.remove(anchors.indexOf(heightAnchor));
                    anchor.detach();

                    setPoseDataToTempArray(getPose(anchor));
//                        log(TAG, "point[" + nowSelectedIndex + "] move vertical "+ (scrollDy / viewHeight) + ", tY=" + tempTranslation[1]
//                                + ", new tY=" + (tempTranslation[1] += (scrollDy / viewHeight)));
                    tempTranslation[1] += (scrollDy / viewHeight);
                    heightAnchor = session.createAnchor(new Pose(tempTranslation, tempRotation));
                    anchors.add(nowSelectedIndex, heightAnchor);
                }

                /* else {
                    float toX = showingTapPointX.get(nowSelectedIndex) - scrollDx;
                    showingTapPointX.remove(nowSelectedIndex);
                    showingTapPointX.add(nowSelectedIndex, toX);

                    float toY = showingTapPointY.get(nowSelectedIndex) - scrollDy;
                    showingTapPointY.remove(nowSelectedIndex);
                    showingTapPointY.add(nowSelectedIndex, toY);

                    if (anchors.size() > nowSelectedIndex) {
                        Anchor anchor = anchors.remove(nowSelectedIndex);
                        anchor.detach();
                        // remove duplicated anchor
                        setPoseDataToTempArray(getPose(anchor));
                        tempTranslation[0] -= (scrollDx / viewWidth);
                        tempTranslation[2] -= (scrollDy / viewHeight);
                        anchors.add(nowSelectedIndex,
                                session.createAnchor(new Pose(tempTranslation, tempRotation)));
                    }
                } */
            } catch (NotTrackingException e) {
                e.printStackTrace();
            }
        }

        private Pose getPose(Anchor anchor) {
            Pose pose = anchor.getPose();
            pose.getTranslation(mPoseTranslation, 0);
            pose.getRotationQuaternion(mPoseRotation, 0);
            return new Pose(mPoseTranslation, mPoseRotation);
        }

        private void setPoseDataToTempArray(Pose pose) {
            pose.getTranslation(tempTranslation, 0);
            pose.getRotationQuaternion(tempRotation, 0);
        }

        private void drawLine(Pose pose0, Pose pose1, float[] viewmtx, float[] projmtx) {
            float lineWidth = 0.006f;
            float lineWidthH = lineWidth / viewHeight * viewWidth;
            rectRenderer.setVerts(
                    pose0.tx() - lineWidth, pose0.ty() + lineWidthH, pose0.tz() - lineWidth,
                    pose0.tx() + lineWidth, pose0.ty() + lineWidthH, pose0.tz() + lineWidth,
                    pose1.tx() + lineWidth, pose1.ty() + lineWidthH, pose1.tz() + lineWidth,
                    pose1.tx() - lineWidth, pose1.ty() + lineWidthH, pose1.tz() - lineWidth
                    ,
                    pose0.tx() - lineWidth, pose0.ty() - lineWidthH, pose0.tz() - lineWidth,
                    pose0.tx() + lineWidth, pose0.ty() - lineWidthH, pose0.tz() + lineWidth,
                    pose1.tx() + lineWidth, pose1.ty() - lineWidthH, pose1.tz() + lineWidth,
                    pose1.tx() - lineWidth, pose1.ty() - lineWidthH, pose1.tz() - lineWidth
            );
            rectRenderer.setColor(1f, 0.039f, 0.125f, 1f);

            rectRenderer.draw(viewmtx, projmtx);
        }

        private void drawObj(Pose pose, ObjectRenderer renderer, float[] cameraView, float[] cameraPerspective, float lightIntensity) {
            pose.toMatrix(anchorMatrix, 0);
            renderer.updateModelMatrix(anchorMatrix, 2.5f);
            renderer.draw(cameraView, cameraPerspective, lightIntensity);
        }

        private void checkIfHit(ObjectRenderer renderer, int cubeIndex) {
            if (isMVPMatrixHitMotionEvent(renderer.getModelViewProjectionMatrix(), queuedLongPress.peek())) {
                // long press hit a cube, show context menu for the cube
                nowTouchingPointIndex = cubeIndex;
                queuedLongPress.poll();
                showCubeStatus();

            } else if (isMVPMatrixHitMotionEvent(renderer.getModelViewProjectionMatrix(), queuedSingleTaps.peek())) {
                nowTouchingPointIndex = cubeIndex;
                queuedSingleTaps.poll();
                showCubeStatus();
            }
        }

        private boolean isMVPMatrixHitMotionEvent(float[] ModelViewProjectionMatrix, MotionEvent event) {
            if (event == null) {
                return false;
            }
            Matrix.multiplyMV(vertexResult, 0, ModelViewProjectionMatrix, 0, centerVertexOfCube, 0);
            /**
             * vertexResult = [x, y, z, w]
             *
             * coordinates in View
             * ┌─────────────────────────────────────────┐╮
             * │[0, 0]                     [viewWidth, 0]│
             * │       [viewWidth/2, viewHeight/2]       │view height
             * │[0, viewHeight]   [viewWidth, viewHeight]│
             * └─────────────────────────────────────────┘╯
             * ╰                view width               ╯
             *
             * coordinates in GLSurfaceView frame
             * ┌─────────────────────────────────────────┐╮
             * │[-1.0,  1.0]                  [1.0,  1.0]│
             * │                 [0, 0]                  │view height
             * │[-1.0, -1.0]                  [1.0, -1.0]│
             * └─────────────────────────────────────────┘╯
             * ╰                view width               ╯
             */
            // circle hit test
            float radius = (viewWidth / 2) * (cubeHitAreaRadius / vertexResult[3]);
            float dx = event.getX() - (viewWidth / 2) * (1 + vertexResult[0] / vertexResult[3]);
            float dy = event.getY() - (viewHeight / 2) * (1 - vertexResult[1] / vertexResult[3]);
            double distance = Math.sqrt(dx * dx + dy * dy);
//            // for debug
//            overlayViewForTest.setPoint("cubeCenter", screenX, screenY);
//            overlayViewForTest.postInvalidate();
            return distance < radius;
        }

        private double getDistance(Pose pose0, Pose pose1) {
            float dx = pose0.tx() - pose1.tx();
            float dy = pose0.ty() - pose1.ty();
            float dz = pose0.tz() - pose1.tz();
            return Math.sqrt(dx * dx + dz * dz + dy * dy);
        }

        private void showResult(final String result) {
            runOnUiThread(() -> tv_result.setText(result));
        }


        private void showCubeStatus() {
            runOnUiThread(() -> {

            });
        }

    }
}

package it.playfellas.superapp.ui.slave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;
import it.playfellas.superapp.ui.BitmapUtils;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class PhotoFragment extends Fragment {
    public static final String TAG = PhotoFragment.class.getSimpleName();
    private static final String MESSAGE = "Il gioco sta per iniziare";

    private Bitmap photo;
    private PhotoFragmentListener mListener;
    private SurfaceHolder surfaceHolder;
    private PhotoSurface photoSurface;

    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;

    @Bind(R.id.takePhotoButton)
    Button continueButton;


    /**
     * Callback interface implemented in {@link SlaveActivity}
     */
    public interface PhotoFragmentListener {
        void setPhotoBitmap(Bitmap photo);

        void recallWaitingFragment(String message);
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    public PhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.slave_photo_fragment, container, false);
        ButterKnife.bind(this, root);

        surfaceHolder = surfaceView.getHolder();

        photoSurface = new PhotoSurface(surfaceHolder);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(photoSurface);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PhotoFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement " + PhotoFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.takePhotoButton)
    public void onClickTakePhotoButton() {
        continueButton.setEnabled(false) ;
        photoSurface.getCamera().takePicture(null, null, new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                if (mListener != null) {
                    if (data == null || data.length == 0) {
                        //if photo is not available set a default photo
                        photo = BitmapFactory.decodeResource(getActivity().getResources(),
                                R.mipmap.ic_launcher);
                    } else {
                        //get data and create a Bitmap
                        if (PhotoUtils.getFixedOrientationDegree() != 0) {
                            //it's a device with a rotated camera
                            photo = BitmapUtils.rotateBitmap(
                                    BitmapUtils.fromByteArraytoBitmap(data), PhotoUtils.getFixedOrientationDegree());
                        } else {
                            photo = BitmapUtils.fromByteArraytoBitmap(data);
                        }
                    }
                    mListener.setPhotoBitmap(photo);
                    mListener.recallWaitingFragment(MESSAGE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

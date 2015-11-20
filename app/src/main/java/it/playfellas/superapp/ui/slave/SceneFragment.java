package it.playfellas.superapp.ui.slave;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import it.playfellas.superapp.Scene;

public class SceneFragment extends AndroidFragmentApplication {

  private FragmentListener fragmentListener;

  private Scene scene;

  public static SceneFragment newInstance() {
    SceneFragment fragment = new SceneFragment();
    return fragment;
  }

  public SceneFragment() {
    // Required empty public constructor
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      fragmentListener = getParent(this, SceneFragment.FragmentListener.class);
    } catch (ClassCastException e) {
      throw new ClassCastException(fragmentListener.toString() + " must implement FragmentListener");
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    scene = new Scene(new Scene.SceneListener() {
      @Override public void onSceneReady(Scene scene) {
        // Call the parent listener as soon as the scene is ready
        fragmentListener.onSceneReady(scene);
      }
    },size.x, size.y);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    return initializeForView(scene, config);
  }

  /* Utils */

  @Nullable
  public static <T> T getParent(@NonNull Fragment fragment, @NonNull Class<T> parentClass) {
    Fragment parentFragment = fragment.getParentFragment();
    if (parentClass.isInstance(parentFragment)) {
      //Checked by runtime methods
      //noinspection unchecked
      return (T) parentFragment;
    } else if (parentClass.isInstance(fragment.getActivity())) {
      //Checked by runtime methods
      //noinspection unchecked
      return (T) fragment.getActivity();
    }
    return null;
  }

  public Scene getScene() {
    return scene;
  }

  /* Interfaces */

  /**
   * Interface to comunicate to the parent (Activity or Fragment).
   */
  public interface FragmentListener {
    void onSceneReady(Scene scene);
  }
}

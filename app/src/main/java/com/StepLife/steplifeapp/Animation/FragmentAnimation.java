package com.StepLife.steplifeapp.Animation;

import androidx.fragment.app.Fragment;

import com.google.android.material.transition.MaterialFadeThrough;

public interface FragmentAnimation {
    public static void SetAnimation(Fragment fragment) {
        fragment.setExitTransition(new MaterialFadeThrough());
        fragment.setEnterTransition(new MaterialFadeThrough());
    }

}

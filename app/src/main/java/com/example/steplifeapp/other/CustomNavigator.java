package com.example.steplifeapp.other;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

public class CustomNavigator extends FragmentNavigator {
    private Context context;
    private FragmentManager manager;
    int ContainerID;

    public CustomNavigator(@NonNull Context context, @NonNull FragmentManager fragmentManager, int containerId) {
        super(context, fragmentManager, containerId);
    }


    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {

        String tag = String.valueOf(destination.getId());
         manager.beginTransaction();
        return super.navigate(destination, args, navOptions, navigatorExtras);
    }
}

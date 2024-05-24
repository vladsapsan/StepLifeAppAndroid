package com.StepLife.steplifeapp.ui.FirebaseData;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebasedatamanager {
    public static void getDataFromFirebase(String dataPath, ValueEventListener listener) {
        FirebaseDatabase.getInstance().getReference(dataPath).addListenerForSingleValueEvent(listener);
    }
}

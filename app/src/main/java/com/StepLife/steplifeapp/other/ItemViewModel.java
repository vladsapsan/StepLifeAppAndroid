package com.StepLife.steplifeapp.other;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//Класс для передачи данных между фрагментами и активити
public class ItemViewModel extends ViewModel {
    private final MutableLiveData<ClipData.Item> selectedItem = new MutableLiveData<ClipData.Item>();
    public void selectItem(ClipData.Item item) {
        selectedItem.setValue(item);
    }
    public LiveData<ClipData.Item> getSelectedItem() {
        return selectedItem;
    }
}

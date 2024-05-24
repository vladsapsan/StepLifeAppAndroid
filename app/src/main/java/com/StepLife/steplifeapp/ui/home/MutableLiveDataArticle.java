package com.StepLife.steplifeapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.StepLife.steplifeapp.Model.LightArticle;

import java.util.ArrayList;

public class MutableLiveDataArticle extends MutableLiveData<LightArticle> {
    ArrayList<MutableLiveData<LightArticle>> MainList;
}

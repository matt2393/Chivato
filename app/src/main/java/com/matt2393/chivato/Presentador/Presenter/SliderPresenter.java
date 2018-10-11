package com.matt2393.ind.Presentador.Presenter;

import com.matt2393.ind.Modelo.Imagen;
import com.matt2393.ind.Presentador.Interactor.SliderInteractor;
import com.matt2393.ind.Vista.View.SliderView;

import java.util.ArrayList;

public class SliderPresenter implements SliderInteractor.OnLoadSlider{

    private SliderView sliderView;
    private SliderInteractor sliderInteractor;

    public SliderPresenter(SliderView sliderView, SliderInteractor sliderInteractor) {
        this.sliderView = sliderView;
        this.sliderInteractor = sliderInteractor;
    }

    public void load(){
        sliderInteractor.load(this);
    }

    public void addListener(){
        sliderInteractor.starListeners();
    }
    public void removeListener(){
        sliderInteractor.stopListeners();
    }

    @Override
    public void loadSlider(ArrayList<Imagen> imgs) {
        sliderView.loadSliders(imgs);
    }
}

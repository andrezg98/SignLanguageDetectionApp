package com.andreaziqing.signlanguagedetectionapp.HelperClasses.CustomView;


import com.andreaziqing.signlanguagedetectionapp.TFLiteInterpreter.Detector;
import java.util.List;

public interface ResultsView {
    public void setResults(final List<Detector.Recognition> results);
}

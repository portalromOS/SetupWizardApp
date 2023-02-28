package com.portalrom.setupwizard.Utils;

import android.graphics.Color;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.portalrom.setupwizard.R;

public  class SetupWizardUtils {

    public static void hideTaskBar(WindowInsetsController insetsController){

        if (insetsController != null) {
            insetsController.hide(WindowInsets.Type.statusBars());
        }
    }

    /*
    public void closeAplication(View view) {
        finishAffinity();
        System.exit(0);

    }*/

    public  static  void dinamicTextViews(View view){
        ConstraintLayout localeLayout;
        localeLayout = view.findViewById(R.id.localeScrollTextParent);
        ConstraintSet set = new ConstraintSet();
        set.clone(localeLayout);

        int nButtons = 10;
        TextView parent = new TextView(view.getContext());

        for(int i = 0;i<nButtons;i++){

            TextView tView = new TextView(view.getContext());
            tView.setText("Line " + i);
            tView.setId(View.generateViewId());           // <-- Important
            tView.setTextColor(Color.WHITE);
            tView.setTextSize(20);
            tView.setClickable(true);
            localeLayout.addView(tView);

            set.constrainWidth(tView.getId(),ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(tView.getId(),ConstraintSet.WRAP_CONTENT);

            if(i==0){
                set.connect(tView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                set.connect(tView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            }
            else {
                set.connect(tView.getId(), ConstraintSet.TOP, parent.getId(), ConstraintSet.BOTTOM, 0);
                set.connect(tView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            }

            set.applyTo(localeLayout);
            parent = tView;
        }
    }

}

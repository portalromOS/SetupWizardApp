package com.portalrom.setupwizard.Utils;

import android.view.WindowInsets;
import android.view.WindowInsetsController;

public  class SetupWizardUtils {

    public static void hideTaskBar(WindowInsetsController insetsController){

        if (insetsController != null) {
            insetsController.hide(WindowInsets.Type.statusBars());
        }
    }

}

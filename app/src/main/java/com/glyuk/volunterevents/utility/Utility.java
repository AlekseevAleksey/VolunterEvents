package com.glyuk.volunterevents.utility;

import android.widget.EditText;

public class Utility {
    public static boolean isBlankField(EditText etEventData) {
        return etEventData.getText().toString().trim().equals("");
    }
}

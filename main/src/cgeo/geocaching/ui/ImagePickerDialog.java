package cgeo.geocaching.ui;

import cgeo.geocaching.R;
import cgeo.geocaching.cgImage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

public abstract class ImagePickerDialog extends AlertDialog {
    public ImagePickerDialog(final Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = (FrameLayout) findViewById(android.R.id.custom);
        frame.addView(getLayoutInflater().inflate(R.layout.imagepickerdialog, frame));

        setButton(BUTTON_POSITIVE, "OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onFinish(new cgImage("url", "title", "description"));
                dismiss();
            }
        });

        setButton(BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    public abstract void onFinish(final cgImage image);
}

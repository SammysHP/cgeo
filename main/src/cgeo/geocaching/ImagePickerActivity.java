package cgeo.geocaching;

import cgeo.geocaching.activity.AbstractActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ImagePickerActivity extends AbstractActivity {
    public static final String EXTRA_IMAGELIST = "imagelist";

    private List<cgImage> imageList = new ArrayList<cgImage>();

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();
        setContentView(R.layout.imagepicker);
        setTitle(res.getString(R.string.imagepicker));

        final Bundle extras = getIntent().getExtras();
        if (null != extras && null != extras.get(EXTRA_IMAGELIST)) {
            imageList = (List<cgImage>) extras.get(EXTRA_IMAGELIST);
        }
    }

    public void finishWithData(@SuppressWarnings("unused") View view) {
        final Intent result = new Intent();
        result.putExtra(EXTRA_IMAGELIST, imageList);
        setResult(RESULT_OK, null);
        finish();
    }
}

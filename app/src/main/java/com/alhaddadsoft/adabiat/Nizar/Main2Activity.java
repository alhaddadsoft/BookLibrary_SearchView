package com.alhaddadsoft.adabiat.Nizar;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alhaddadsoft.adabiat.R;
import com.alhaddadsoft.searchadapter.SearchAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main2Activity extends Activity {
    public List<Nizar> nizar = new ArrayList<>();


    @InjectView(R.id.gridView)
    GridView gridView;
    @InjectView(R.id.editText)
    EditText editText;

    InputStream in;
    BufferedReader reader;
    String line;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main2);
        ButterKnife.inject(this);


        fillList(nizar);

        final SearchAdapter adapter = new MyAdapterNizar(nizar, this).registerFilter(Nizar.class, "enTitle")
                .setIgnoreCase(true);
        gridView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    public void fillList(List<Nizar> nizar ) {
        nizar.add(new Nizar("book1", "أثر الفراشة", R.drawable.book1));

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

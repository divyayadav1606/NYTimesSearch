package com.dyadav.nytimessearch.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dyadav.nytimessearch.R;
import com.dyadav.nytimessearch.databinding.FilterBinding;
import com.dyadav.nytimessearch.modal.Filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilterDialog extends DialogFragment {

    private EditText edittext;
    private Calendar beginDate = Calendar.getInstance();
    private Spinner sortOrder;

    private FilterBinding binding;

    public FilterDialog() {}

    public interface FilterTaskListener {
        void onFinishDialog(Filter filter);
    }

    private FilterTaskListener mListener;

    public void setFinishDialogListener(FilterTaskListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                            R.layout.filter,
                                            container, false);

        edittext = binding.startDate;
        Button btnSave = binding.save;
        sortOrder = binding.sortOrder;

        //Read bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            Filter filter = bundle.getParcelable("filter");

            //Set begin date
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(filter.getBegin_date());
                beginDate.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            edittext.setText(new SimpleDateFormat("MM/dd/yy").format(date));

            if (filter.getSort_order().equals("Oldest")) {
                sortOrder.setSelection(0);
            } else {
                sortOrder.setSelection(1);
            }

            if (filter.getNews_desk()!= null) {
                // Parse new_desk string and set checkboxes
                if (filter.getNews_desk().contains("Arts"))
                    binding.arts.setChecked(true);
                if (filter.getNews_desk().contains("Style"))
                    binding.style.setChecked(true);
                if (filter.getNews_desk().contains("Sports"))
                    binding.sports.setChecked(true);
                if (filter.getNews_desk().contains("Travel"))
                    binding.travel.setChecked(true);
                if (filter.getNews_desk().contains("Science"))
                    binding.science.setChecked(true);
                if (filter.getNews_desk().contains("Politics"))
                    binding.politics.setChecked(true);
            }
        } else {
            updateLabel();
        }

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            beginDate.set(Calendar.YEAR, year);
            beginDate.set(Calendar.MONTH, monthOfYear);
            beginDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edittext.setOnClickListener(v -> {
            //Hide Keyboard
            InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            new DatePickerDialog(getContext(), date, beginDate
                    .get(Calendar.YEAR), beginDate.get(Calendar.MONTH),
                    beginDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(view -> {
            //Send data back to main activity
            Filter filter = new Filter();

            if(edittext.getText().toString().length() > 0){
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(edittext.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                filter.setBegin_date(new SimpleDateFormat("yyyyMMdd").format(date1));
            }

            filter.setSort_order(sortOrder.getSelectedItem().toString());

            List<String> news_desk = new ArrayList<>();

            if(binding.arts.isChecked()){
                news_desk.add("Arts");
            }
            if(binding.style.isChecked()){
                news_desk.add("Style");
            }
            if(binding.sports.isChecked()){
                news_desk.add("Sports");
            }
            if(binding.travel.isChecked()){
                news_desk.add("Travel");
            }
            if(binding.science.isChecked()){
                news_desk.add("Science");
            }
            if(binding.politics.isChecked()){
                news_desk.add("Poltics");
            }

            //create news_desk string
            String categories = null;
            if(!news_desk.isEmpty()){
                categories = "news_desk:(";
                for(int i = 0;  i < news_desk.size()-1; i++){
                    categories = categories.concat('"'+news_desk.get(i)+'"'+" ");
                }
                categories = categories.concat('"'+news_desk.get(news_desk.size()-1)+'"'+")");
            }
            filter.setNews_desk(categories);
            //return searchModel;

            mListener.onFinishDialog(filter);
            dismiss();
        });
        return binding.getRoot();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext.setText(sdf.format(beginDate.getTime()));
    }
}

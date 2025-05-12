package com.example.androidnavigationdrawer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class TemperatureConverterFragment extends Fragment {

    private EditText inputTemperature;
    private RadioGroup inputUnitGroup;
    private RadioGroup outputUnitGroup;
    private Button convertButton;
    private TextView resultTextView;
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature_converter, container, false);

        // Initialize views
        inputTemperature = view.findViewById(R.id.editTextTemperature);
        inputUnitGroup = view.findViewById(R.id.radioGroupInputUnit);
        outputUnitGroup = view.findViewById(R.id.radioGroupOutputUnit);
        convertButton = view.findViewById(R.id.buttonConvert);
        resultTextView = view.findViewById(R.id.textViewResult);

        // Initialize decimal format for rounding
        df = new DecimalFormat("#.##");

        // Set up convert button click listener
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTemperature();
            }
        });

        return view;
    }

    private void convertTemperature() {
        // Get input temperature
        String inputStr = inputTemperature.getText().toString().trim();
        if (inputStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a temperature", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double temperature = Double.parseDouble(inputStr);

            // Get selected input unit
            int inputUnitId = inputUnitGroup.getCheckedRadioButtonId();
            // Get selected output unit
            int outputUnitId = outputUnitGroup.getCheckedRadioButtonId();

            // Convert to Kelvin first (as intermediate)
            double kelvin;
            if (inputUnitId == R.id.radioInputCelsius) {
                kelvin = temperature + 273.15;
            } else if (inputUnitId == R.id.radioInputFahrenheit) {
                kelvin = (temperature + 459.67) * 5/9;
            } else { // Kelvin
                kelvin = temperature;
            }

            // Convert from Kelvin to output unit
            double result;
            String unit;
            if (outputUnitId == R.id.radioOutputCelsius) {
                result = kelvin - 273.15;
                unit = "°C";
            } else if (outputUnitId == R.id.radioOutputFahrenheit) {
                result = kelvin * 9/5 - 459.67;
                unit = "°F";
            } else { // Kelvin
                result = kelvin;
                unit = "K";
            }

            // Display result
            resultTextView.setText(df.format(result) + " " + unit);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }
}

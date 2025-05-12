package com.example.androidnavigationdrawer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private TextView displayResult;
    private TextView displayOperation;
    private String currentNumber = "";
    private String currentOperation = "";
    private double firstOperand = 0;
    private double secondOperand = 0;
    private boolean isOperationClicked = false;
    private DecimalFormat decimalFormat;

    // Buttons
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    private Button btnEquals, btnClear, btnDecimal, btnPercent, btnPlusMinus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Initialize decimal format
        decimalFormat = new DecimalFormat("#.##########");

        // Initialize TextViews
        displayResult = view.findViewById(R.id.textViewResult);
        displayOperation = view.findViewById(R.id.textViewOperation);

        // Initialize number buttons
        btn0 = view.findViewById(R.id.button0);
        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);
        btn5 = view.findViewById(R.id.button5);
        btn6 = view.findViewById(R.id.button6);
        btn7 = view.findViewById(R.id.button7);
        btn8 = view.findViewById(R.id.button8);
        btn9 = view.findViewById(R.id.button9);

        // Initialize operation buttons
        btnAdd = view.findViewById(R.id.buttonAdd);
        btnSubtract = view.findViewById(R.id.buttonSubtract);
        btnMultiply = view.findViewById(R.id.buttonMultiply);
        btnDivide = view.findViewById(R.id.buttonDivide);
        btnEquals = view.findViewById(R.id.buttonEquals);
        btnClear = view.findViewById(R.id.buttonClear);
        btnDecimal = view.findViewById(R.id.buttonDecimal);
        btnPercent = view.findViewById(R.id.buttonPercent);
        btnPlusMinus = view.findViewById(R.id.buttonPlusMinus);

        // Set click listeners for number buttons
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        // Set click listeners for operation buttons
        btnAdd.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);
        btnMultiply.setOnClickListener(this);
        btnDivide.setOnClickListener(this);
        btnEquals.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnDecimal.setOnClickListener(this);
        btnPercent.setOnClickListener(this);
        btnPlusMinus.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Number buttons
        if (id == R.id.button0) {
            appendNumber("0");
        } else if (id == R.id.button1) {
            appendNumber("1");
        } else if (id == R.id.button2) {
            appendNumber("2");
        } else if (id == R.id.button3) {
            appendNumber("3");
        } else if (id == R.id.button4) {
            appendNumber("4");
        } else if (id == R.id.button5) {
            appendNumber("5");
        } else if (id == R.id.button6) {
            appendNumber("6");
        } else if (id == R.id.button7) {
            appendNumber("7");
        } else if (id == R.id.button8) {
            appendNumber("8");
        } else if (id == R.id.button9) {
            appendNumber("9");
        }
        // Decimal point
        else if (id == R.id.buttonDecimal) {
            if (!currentNumber.contains(".")) {
                if (currentNumber.isEmpty()) {
                    currentNumber = "0.";
                } else {
                    currentNumber += ".";
                }
                displayResult.setText(currentNumber);
            }
        }
        // Operation buttons
        else if (id == R.id.buttonAdd) {
            handleOperation("+");
        } else if (id == R.id.buttonSubtract) {
            handleOperation("-");
        } else if (id == R.id.buttonMultiply) {
            handleOperation("×");
        } else if (id == R.id.buttonDivide) {
            handleOperation("÷");
        } else if (id == R.id.buttonPercent) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                number = number / 100;
                currentNumber = decimalFormat.format(number);
                displayResult.setText(currentNumber);
            }
        } else if (id == R.id.buttonPlusMinus) {
            if (!currentNumber.isEmpty()) {
                if (currentNumber.charAt(0) == '-') {
                    currentNumber = currentNumber.substring(1);
                } else {
                    currentNumber = "-" + currentNumber;
                }
                displayResult.setText(currentNumber);
            }
        } else if (id == R.id.buttonEquals) {
            calculateResult();
        } else if (id == R.id.buttonClear) {
            clearCalculator();
        }
    }

    private void appendNumber(String number) {
        if (isOperationClicked) {
            currentNumber = "";
            isOperationClicked = false;
        }
        currentNumber += number;
        displayResult.setText(currentNumber);
    }

    private void handleOperation(String operation) {
        if (!currentNumber.isEmpty()) {
            if (!currentOperation.isEmpty()) {
                calculateResult();
            }
            firstOperand = Double.parseDouble(currentNumber);
            currentOperation = operation;
            displayOperation.setText(decimalFormat.format(firstOperand) + " " + operation);
            isOperationClicked = true;
        } else if (!currentOperation.isEmpty()) {
            // Change operation if no second operand entered yet
            currentOperation = operation;
            displayOperation.setText(decimalFormat.format(firstOperand) + " " + operation);
        }
    }

    private void calculateResult() {
        if (!currentNumber.isEmpty() && !currentOperation.isEmpty()) {
            secondOperand = Double.parseDouble(currentNumber);
            double result = 0;

            switch (currentOperation) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "×":
                    result = firstOperand * secondOperand;
                    break;
                case "÷":
                    if (secondOperand != 0) {
                        result = firstOperand / secondOperand;
                    } else {
                        displayResult.setText("Error");
                        displayOperation.setText("");
                        currentNumber = "";
                        currentOperation = "";
                        return;
                    }
                    break;
            }

            displayOperation.setText(decimalFormat.format(firstOperand) + " " + currentOperation + " " +
                    decimalFormat.format(secondOperand) + " =");
            currentNumber = decimalFormat.format(result);
            displayResult.setText(currentNumber);
            currentOperation = "";
        }
    }

    private void clearCalculator() {
        currentNumber = "";
        currentOperation = "";
        firstOperand = 0;
        secondOperand = 0;
        displayResult.setText("0");
        displayOperation.setText("");
    }
}
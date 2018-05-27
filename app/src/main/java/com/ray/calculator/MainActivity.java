package com.ray.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.*;
import java.math.*;

public class MainActivity extends AppCompatActivity  {

    protected TextView solText;
    protected SeekBar precisionBar;
    protected TextView precisionText;
    protected String infix = "";
    protected String finalSolution;

    /**
     * Returns an integer that represents the operators precedence for shunting yard algorithm
     * @param str An operator string
     * @return an int
     */
    protected static int precedence(String str)
    {
        if(str.equals("(") || str.equals(")"))
            return 3;
        else if (str.equals("/") || str.equals("x"))
            return 2;
        else if (str.equals("-") || str.equals("+"))
            return 1;
        else
            return 0;
    }


    // Listener for operator/operand buttons
    protected void mainClick(View view)
    {
        switch(view.getId())
        {
            case com.ray.calculator.R.id.zeroButton:
                infix += "0";
                break;
            case com.ray.calculator.R.id.oneButton:
                infix += "1";
                break;

            case com.ray.calculator.R.id.twoButton:
                infix += "2";
                break;

            case com.ray.calculator.R.id.threeButton:
                infix += "3";
                break;

            case com.ray.calculator.R.id.fourButton:
                infix += "4";
                break;
            case com.ray.calculator.R.id.fiveButton:
                infix += "5";
                break;
            case com.ray.calculator.R.id.sixButton:
                infix += "6";
                break;
            case com.ray.calculator.R.id.sevenButton:
                infix += "7";
                break;
            case com.ray.calculator.R.id.eightButton:
                infix += "8";
                break;
            case com.ray.calculator.R.id.nineButton:
                infix += "9";
                break;
            case com.ray.calculator.R.id.decimalButton:
                infix += ".";
                break;
            case com.ray.calculator.R.id.paraButton1:
                infix += "(";
                break;
            case com.ray.calculator.R.id.paraButton2:
                infix += ")";
                break;
            case com.ray.calculator.R.id.multiplyButton:
                infix += "x";
                break;
            case com.ray.calculator.R.id.divideButton:
                infix += "%";
                break;
            case com.ray.calculator.R.id.subtractionButton:
                infix += "-";
                break;
            case com.ray.calculator.R.id.additionButton:
                infix += "+";
                break;
        }
        solText.setText(infix);
    }

    // Delete key listener
    protected void delClick(View view) {
        if (!infix.isEmpty()) {
            infix = infix.substring(0, infix.length()-1);
            solText.setText(infix);

        }
    }

    protected void ansClick(View view) {
        if(finalSolution != null) {
            infix += finalSolution;
            solText.setText(infix);
        }
    }

    protected void clearClick(View view) {
        if(solText.getText() != null) {
            solText.setText("");
            infix = "";
        }
    }


    //Equal listener + operation computation
    protected void equalClick(View view) {
        if (!infix.isEmpty()) {

            int infixLength = infix.length();
            infix = infix.replace("%", "/");


            //The following implements support for unary operator '-'
            infix = infix.replace("+-", "-");
            infix = infix.replace("--", "+");
            infix = infix.replace("x-", "x(0-");
            infix = infix.replace("/-", "/(0-");

            for(int i =0; i < infixLength; i++) {
                StringBuilder sb = new StringBuilder(infix);
                if(infix.charAt(i) == '(' && infix.charAt(i+1) == '0' && infix.charAt(i+2) == '-' && Character.isDigit(infix.charAt(i+3))) {
                    sb.insert(i+4, ')');
                }
                infix = sb.toString();
                infixLength = infix.length();
            }


            Stack operations = new Stack();
            com.ray.calculator.Queue postfix = new com.ray.calculator.Queue();
            String token;
            int i = 0;
            Stack eval = new Stack(); // stack to store
            double temp; // initializing temp variable to store top of stack in
            double retVal; // result of operation varying on case
            StringTokenizer st = new StringTokenizer(infix, "+-x/()", true); // separate into tokens, operators act as division line

            /*
            If precedence of the new token is greater, push it onto the stack
            If precedence of the new token is less, first pop the operator stack and add it to the output queue, then push the new token
            In case of parentheses, left para is pushed to provide a marker. When the next operator is read, the stack is treated like it's empty, ie; ignore para on left.
            When the right one is read, stop and pop stack until you find the left para. Para's are not printed into the postfix.
                         */
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if ((Character.isDigit(token.charAt(i)))) { // if its a number, add it to output queue
                    postfix.Enqueue(token);
                } else { // moving on to operators
                    if (operations.top == null) {
                        operations.push(token);

                    } else if (precedence(token) > precedence(operations.top.key_val)) {
                        operations.push(token);
                    } else {
                        while (operations.top != null && precedence(token) <= precedence(operations.top.key_val) && !operations.top.key_val.equals("(") && !token.equals(")")) {
                            postfix.Enqueue(operations.pop());
                        }

                        operations.push(token);

                        if(operations.top.key_val.equals(")")) {
                            operations.pop();
                            while (operations.top != null && !operations.top.key_val.equals("(")) {
                                postfix.Enqueue(operations.pop());
                            }
                            if(operations.top.key_val.equals("(")) {
                                operations.pop();
                            }
                        }
                    }
                }
            }


            while (operations.top != null) {
                if(operations.top.key_val.equals("(") || operations.top.key_val.equals(")")) {
                    operations.pop();
                }
                else {
                    postfix.Enqueue(operations.pop());
                }
            }


            // PostFix Evaluation

            while (postfix.front != null) { // repeating until all characters checked
                String postfixToken = postfix.Dequeue(); // for switch statement

                /*
                 * Switch statement to vary operation based on the operator in question, same
                 * procedure. Store top of stack in variable temp, pop stack, complete the
                 * operation (top of stack (operator depending on case) temp) Pop stack, push
                 * retVal (value of the above operation) into stack.
                 */

                switch (postfixToken) { // if its an operator (with cases for each +, -, x, /)

                    case "+":
                        temp = Double.parseDouble(eval.top.key_val); // switching string to a double to evaluate with significance
                        eval.pop();
                        retVal = (Double.parseDouble(eval.top.key_val) + temp); // addition case
                        eval.pop();
                        eval.push(Double.toString(retVal)); // return to string, so it can be pushed into the stack
                        break;

                    case "-":
                        temp = Double.parseDouble(eval.top.key_val);
                        eval.pop();
                        retVal = (Double.parseDouble(eval.top.key_val) - temp); // subtraction case
                        eval.pop();
                        eval.push(Double.toString(retVal));
                        break;

                    case "x":
                        temp = Double.parseDouble(eval.top.key_val);
                        eval.pop();
                        retVal = (Double.parseDouble(eval.top.key_val) * temp); // multiplication case
                        eval.pop();
                        eval.push(Double.toString(retVal));
                        break;

                    case "/":
                        temp = Double.parseDouble(eval.top.key_val);
                        eval.pop();
                        retVal = (Double.parseDouble(eval.top.key_val) / temp); // division case
                        eval.pop();
                        eval.push(Double.toString(retVal));
                        break;

                    default: // else if its an operand, push to stack
                        eval.push(postfixToken);
                        break;
                }
            }

            // Precision setting for slider
            String solution = eval.pop(); // this is the final solution of the postfix eval
            Double.parseDouble(solution); // converting it to a double to set precision
            for (int precision = 0; precision < 11; precision++) {
                if (precision == precisionBar.getProgress()) {
                    BigDecimal db_solution = new BigDecimal(solution);
                    db_solution = db_solution.setScale(precision, BigDecimal.ROUND_HALF_UP);
                    finalSolution = String.valueOf(db_solution);
                    solText.setText(finalSolution);
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ray.calculator.R.layout.activity_main);

        solText = findViewById(com.ray.calculator.R.id.solText);
        precisionBar = findViewById(com.ray.calculator.R.id.precisionBar);
        precisionText = findViewById(com.ray.calculator.R.id.precisionText);


        precisionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                precisionText.setText(Integer.toString(precisionBar.getProgress()));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.ray.calculator.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.ray.calculator.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

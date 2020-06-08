package com.bignerdranch.android.delizioso.view.activities;

import android.print.PrintAttributes;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.delizioso.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity
        implements View.OnClickListener{
    private double mark;

    //widgets
    private List<AppCompatImageView> stars;
    private AppCompatEditText emailInput;
    private AppCompatEditText nameInput;
    private AppCompatEditText commentInput;

    private final int[] starsId = new int[]{
            R.id.one_star,R.id.two_stars, R.id.three_stars, R.id.four_stars, R.id.five_stars
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stars = new ArrayList<>();

        for (int id : starsId) {
            View view = findViewById(id);
            view.setOnClickListener(this);
            stars.add((AppCompatImageView) view);
        }

        emailInput = (AppCompatEditText) findViewById(R.id.email);
        nameInput = (AppCompatEditText) findViewById(R.id.name);
        commentInput = (AppCompatEditText) findViewById(R.id.opinion);

    }

    @Override
    public void onClick(View view) {
        int position = 0;
        boolean valid = true;

        switch (view.getId()){
            case R.id.five_stars:
                position = 5;
                break;
            case R.id.four_stars:
                position = 4;
                break;
            case R.id.three_stars:
                position = 3;
                break;
            case R.id.two_stars:
                position = 2;
                break;
            case R.id.one_star:
                position = 1;
                break;
            case R.id.send_opinion:
               valid = validationFields();
        }

        if(valid) {
            for (int i = 0; i < position; i++) {
                stars.get(i).setImageResource(android.R.drawable.btn_star_big_on);
            }

            for (int i = position; i < starsId.length; i++) {
                stars.get(i).setImageResource(android.R.drawable.btn_star_big_off);
            }

            mark = position;
        }
    }

    private boolean validationFields(){
        String emailErr = "";
        String commentErr = "";
        String markErr = "";
        if(emailInput.getText() != null) {
            if (!emailInput.getText().toString().isEmpty()) {
                Pattern pattern = Pattern.compile("[a-z]+@[a-z]+[.][a-z]{2,4}");
                Matcher matcher = pattern.matcher(emailInput.getText().toString());
                if (!matcher.find()) {
                    emailErr = "Input valid email!\n";
                }
            } else {
                emailErr = "Fill email field!\n";
            }
        }
        if(commentInput.getText() != null) {
            if (commentInput.getText().toString().isEmpty()) {
                commentErr = "Fill comment filed!\n";
            }
        }

        if(mark == 0) {
            markErr = "Put the mark to our application!\n";
        }

        Toast toast = Toast.makeText(this, "",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,32);

        if(emailErr.isEmpty() && commentErr.isEmpty() && markErr.isEmpty()){
            toast.setText("Thank you for your opinion!");
            toast.show();
            finish();
            return true;
        } else{
            toast.setText(emailErr + commentErr + markErr);
            toast.show();
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

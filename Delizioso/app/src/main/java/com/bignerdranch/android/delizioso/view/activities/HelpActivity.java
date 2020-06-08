package com.bignerdranch.android.delizioso.view.activities;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.adapters.HelpAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Question;
import model.food.Food;
import model.food.pizza.Pizza;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView helpRecycler = (RecyclerView) findViewById(R.id.help_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        helpRecycler.setLayoutManager(layoutManager);

        HelpAdapter adapter = new HelpAdapter(getListOfQuestions());
        helpRecycler.setAdapter(adapter);


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

    private List<Question> getListOfQuestions(){
        Resources resources = getResources();
        List<Question> questionsList = new ArrayList<>();

        String[] questions = resources.getStringArray(R.array.questions);
        String[] answers = resources.getStringArray(R.array.answers);

        for (int i = 0; i < questions.length; i++) {
            Question question = new Question(questions[i], answers[i]);
            questionsList.add(question);
        }

        return questionsList;
    }
}

package com.bignerdranch.android.delizioso.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.delizioso.R;

import java.util.List;

import model.Question;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    private List<Question> questions;

    public HelpAdapter(List<Question> questions) {
        this.questions = questions;
        setHasStableIds(true);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.help_list_item, viewGroup, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        View item = viewHolder.item;
        Question question = questions.get(i);

        TextView questionTV = item.findViewById(R.id.question);
        TextView answerTV = item.findViewById(R.id.answer);

        questionTV.setText(question.getQuestion());
        answerTV.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View item;

        public ViewHolder(View item) {
            super(item);
            this.item = item;
        }
    }
}

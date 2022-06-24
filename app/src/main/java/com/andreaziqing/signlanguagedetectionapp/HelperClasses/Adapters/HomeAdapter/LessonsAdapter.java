package com.andreaziqing.signlanguagedetectionapp.HelperClasses.Adapters.HomeAdapter;
import com.andreaziqing.signlanguagedetectionapp.PracticeGames.FirstGame;
import com.andreaziqing.signlanguagedetectionapp.PracticeGames.FullSecondGame;
import com.andreaziqing.signlanguagedetectionapp.PracticeGames.ThirdGame;
import com.andreaziqing.signlanguagedetectionapp.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder> {

    ArrayList<LessonsHelperClass> lessons;
    private Context context;

    public LessonsAdapter(ArrayList<LessonsHelperClass> lessons) {
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lessons_card_design, parent, false);
        LessonsViewHolder lessonsViewHolder = new LessonsViewHolder(view);
        context = parent.getContext();

        return lessonsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsViewHolder holder, int position) {

        LessonsHelperClass lessonsHelperClass = lessons.get(position);
        holder.image.setImageResource(lessonsHelperClass.getImage());
        holder.title.setText(lessonsHelperClass.getTitle());
        holder.desc.setText(lessonsHelperClass.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullSecondGame.class);
                //intent.putExtra("position", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public static class LessonsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, desc;

        public LessonsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Hooks
            image = itemView.findViewById(R.id.lesson_image);
            title = itemView.findViewById(R.id.lesson_title);
            desc = itemView.findViewById(R.id.lesson_desc);
        }
    }
}

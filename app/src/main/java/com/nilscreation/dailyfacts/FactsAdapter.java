package com.nilscreation.dailyfacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.MovieHolder> {

    private Context context;
    private List<FactsModel> factList;
    FragmentActivity activity;

    public FactsAdapter(Context context, List<FactsModel> movies, FragmentActivity activity) {
        this.context = context;
        factList = movies;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);

        FactsModel fact = factList.get(position);

        Glide.with(holder.imageView.getContext()).load(fact.getPoster()).placeholder(R.drawable.app_logo).into(holder.imageView);
        holder.title.setText(fact.getTitle());
        holder.category.setText(fact.getCategory());

        holder.itemView.startAnimation(animation);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("poster", fact.getPoster());
                bundle.putString("category", fact.getCategory());
                bundle.putString("title", fact.getTitle());
                bundle.putString("text", fact.getText());
                intent.putExtras(bundle);

                holder.imageView.getContext().startActivity(intent);
            }
        });

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = holder.category.getText().toString();
//                Toast.makeText(holder.category.getContext(), "" + categoryName, Toast.LENGTH_SHORT).show();
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                MainFragment fragment = new MainFragment();
                fragment.category(categoryName, activity);
                ft.replace(R.id.mainContainer, fragment);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, category;
        CardView mainLayout;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            title = itemView.findViewById(R.id.main_title);
            category = itemView.findViewById(R.id.category_name);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}

package com.nilscreation.dailyfacts;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.MovieHolder> {

    private Context context;
    private List<FactsModel> factList;
    FragmentActivity activity;
    private InterstitialAd mInterstitialAd;
    private int mCounter = 0;

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

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);

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

                holder.mainLayout.getContext().startActivity(intent);

                mCounter++;
//                Toast.makeText(holder.mainLayout.getContext(), "click" + mCounter, Toast.LENGTH_SHORT).show();

                if ((mCounter % 2) == 0) {

                    //SHOW INTERSTITIAL AD
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show((Activity) holder.mainLayout.getContext());

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();

                                mInterstitialAd = null;
                                mInterstitialAd();
                            }
                        });

                    } else {
//                        Toast.makeText(context, "ad not ready", Toast.LENGTH_SHORT).show();
                    }

                } else if (mCounter == 1) {
                    mInterstitialAd();
                } else {

                }
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

    private void mInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
//                        Toast.makeText(activity, "loaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
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

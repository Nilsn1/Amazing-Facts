package com.nilscreation.dailyfacts;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CategoryFragment extends Fragment {

    CardView card_mystery, card_animals, card_health, card_technology,
            card_psychology, card_geography, card_random, card_history;
    String categoryName;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        card_mystery = view.findViewById(R.id.card_mystery);
        card_animals = view.findViewById(R.id.card_animals);
        card_health = view.findViewById(R.id.card_health);
        card_technology = view.findViewById(R.id.card_technology);
        card_psychology = view.findViewById(R.id.card_psychology);
        card_geography = view.findViewById(R.id.card_geography);
        card_random = view.findViewById(R.id.card_random);
        card_history = view.findViewById(R.id.card_history);

        card_animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Animals";
                changeFragment();
            }
        });
        card_geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Geography";
                changeFragment();
            }
        });
        card_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Health";
                changeFragment();
            }
        });
        card_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "History";
                changeFragment();
            }
        });
        card_mystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Mystery";
                changeFragment();
            }
        });
        card_psychology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Psychology";
                changeFragment();
            }
        });
        card_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Random";
                changeFragment();
            }
        });
        card_technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryName = "Technology";
                changeFragment();
            }
        });

        return view;
    }

    private void changeFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        CategorySearchFragment fragment = new CategorySearchFragment();
        fragment.category(categoryName, getActivity());
        ft.replace(R.id.mainContainer, fragment);
        ft.commit();
    }
}
package com.nilscreation.dailyfacts;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.rvadapter.AdmobNativeAdAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<FactsModel> factslist;
    Context context;
    String getCategory = "all";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();

        factslist = new ArrayList<>();
        fetchMovies();
        //INTERSTITIAL ADS INITILIZATION
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        return view;
    }

    private void fetchMovies() {

        String url = "https://nilsn1.github.io/nilscreation/DailyFacts.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String poster = jsonObject.getString("poster");
                        String category = jsonObject.getString("category");
                        String title = jsonObject.getString("title");
                        String text = jsonObject.getString("text");

                        if (getCategory.equals("all")) {
                            FactsModel facts = new FactsModel(poster, category, title, text);
                            factslist.add(facts);
                        } else if (getCategory.equals("Animals")) {
                            if (category.equals("Animals")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Geography")) {
                            if (category.equals("Geography")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Health")) {
                            if (category.equals("Health")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("History")) {
                            if (category.equals("History")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Mystery")) {
                            if (category.equals("Mystery")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Psychology")) {
                            if (category.equals("Psychology")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Random")) {
                            if (category.equals("Random")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else if (getCategory.equals("Technology")) {
                            if (category.equals("Technology")) {
                                FactsModel facts = new FactsModel(poster, category, title, text);
                                factslist.add(facts);
                            }
                        } else {
//                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FactsAdapter adapter = new FactsAdapter(context, factslist, getActivity());
//                    recyclerView.setAdapter(adapter);
                    AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-9137303962163689/7340301951", adapter,
                            "medium").adItemInterval(3).build();
                    recyclerView.setAdapter(admobNativeAdAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void category(String category, Context context) {
        this.getCategory = category;
    }
}
package gmp.thiago.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import gmp.thiago.popularmovies.R;
import gmp.thiago.popularmovies.adapter.ReviewAdapter;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView reviewsRv;
    private LinearLayoutManager layoutManager;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsRv = (RecyclerView)findViewById(R.id.reviews_recyclerview);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reviewsRv.setLayoutManager(layoutManager);

        reviewAdapter = new ReviewAdapter();
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.reviews))) {
            ArrayList reviews = intent.getParcelableArrayListExtra(getString(R.string.reviews));
            reviewAdapter.setReviews(reviews);
        }
        reviewsRv.setAdapter(reviewAdapter);
    }
}

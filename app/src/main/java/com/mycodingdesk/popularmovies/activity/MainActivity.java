package com.mycodingdesk.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;

import com.mycodingdesk.popularmovies.R;
import com.mycodingdesk.popularmovies.adapter.MovieAdapter;
import com.mycodingdesk.popularmovies.adapter.MovieClickListener;
import com.mycodingdesk.popularmovies.databinding.ActivityMainBinding;
import com.mycodingdesk.popularmovies.model.Movie;
import com.mycodingdesk.popularmovies.model.MovieResult;
import com.mycodingdesk.popularmovies.network.MovieAPI;
import com.mycodingdesk.popularmovies.network.NetworkUtil;
import com.mycodingdesk.popularmovies.viewmodel.MainViewModel;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Movie>>,
    MovieClickListener {

    private MovieAdapter mMovieAdapter;
    private ActivityMainBinding mMainActivityBinding;
    private boolean mIsTablet;
    private static final int GRID_LAYOUT_SPAN = 2;
    private static final int MOVIE_LOADER_ID = 5553;
    private MainViewModel mViewModel;

    private final AdapterView.OnItemSelectedListener spinnerChangeQueryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position!=2) {
                if(mViewModel != null) mViewModel.getMovies().removeObservers(MainActivity.this);
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MainActivity.this);
            }
            else
                setupMainViewModel();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainActivityBinding.movieListRecycler.setLayoutManager(new StaggeredGridLayoutManager(GRID_LAYOUT_SPAN,
                StaggeredGridLayoutManager.VERTICAL));
        mMainActivityBinding.movieListRecycler.setHasFixedSize(true);
        mMainActivityBinding.changeQuerySpin.setOnItemSelectedListener(spinnerChangeQueryListener);

        mMovieAdapter = new MovieAdapter(this);
        mMainActivityBinding.movieListRecycler.setAdapter(mMovieAdapter);

        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        mMovieAdapter.setMovieList(data, MovieAPI.buildImageURL(mIsTablet));
        mMainActivityBinding.loadingProgress.setVisibility(View.GONE);
        if (null == data) {
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_DATA, movie);
        startActivity(intent);
    }

    private static class MovieTaskLoader extends AsyncTaskLoader<List<Movie>> {

        private final WeakReference<MainActivity> mainActivity;

        MovieTaskLoader(MainActivity contextActivity) {
            super(contextActivity);
            mainActivity = new WeakReference<>(contextActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            mainActivity.get().mMainActivityBinding.loadingProgress.setVisibility(View.VISIBLE);
            forceLoad();
        }

        @Nullable
        @Override
        public List<Movie> loadInBackground() {
            if(NetworkUtil.isConnectionAvailable( mainActivity.get())) {
                int searchType =  mainActivity.get().mMainActivityBinding.changeQuerySpin.getSelectedItemPosition();
                URL url = MovieAPI.buildURL(searchType);
                try {
                    MovieResult movieResult = MovieAPI.getMovieList(url);
                    return movieResult.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }else
                return null;
        }
    }

    private void setupMainViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovieList(movies, MovieAPI.buildImageURL(mIsTablet));
            }
        });
    }

    private void showErrorMessage() {
        mMainActivityBinding.errorMessageTv.setVisibility(View.VISIBLE);
        mMainActivityBinding.movieListRecycler.setVisibility(View.GONE);
    }

    private void showMovieDataView() {
        mMainActivityBinding.errorMessageTv.setVisibility(View.GONE);
        mMainActivityBinding.movieListRecycler.setVisibility(View.VISIBLE);
    }
}

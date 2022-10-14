package com.milonsheikh.rxjavaandretrofit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.milonsheikh.rxjavaandretrofit.R;
import com.milonsheikh.rxjavaandretrofit.adapter.PostAdapter;
import com.milonsheikh.rxjavaandretrofit.model.Post;
import com.milonsheikh.rxjavaandretrofit.network.ApiClient;
import com.milonsheikh.rxjavaandretrofit.network.ApiService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchResultActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        userId = getIntent().getStringExtra("USER");

        progressDialog = new ProgressDialog(SearchResultActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        apiService = ApiClient.getApiService();

        recyclerView = findViewById(R.id.post_recycler_view_find);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDataWithCompositeDisposable(userId);
    }

    private void fetchDataWithCompositeDisposable(String userId) {
        System.out.println("======= IN Find fetchDataWithCompositeDisposable =======");
        compositeDisposable.add(apiService.getPostsByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableSingleObserver()));
    }

    private DisposableSingleObserver<List<Post>> getDisposableSingleObserver() {
        return new DisposableSingleObserver<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                System.out.println("======= onSuccess =======");
                progressDialog.dismiss();
                displayData(posts);
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                System.out.println("onError ===>>> " + e.getMessage());
            }
        };
    }

    private void displayData(List<Post> posts) {
        PostAdapter adapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.dispose();

    }
}
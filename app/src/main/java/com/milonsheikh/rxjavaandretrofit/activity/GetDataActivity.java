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
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetDataActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ApiService apiService;

    private Observable<List<Post>> myObservable;
    private Single<List<Post>> mySingle;
    private Disposable myDisposable;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        action = getIntent().getStringExtra("KEY");

        progressDialog = new ProgressDialog(GetDataActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        apiService = ApiClient.getApiService();

        recyclerView = findViewById(R.id.post_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (action.equals("observable")){
            fetchDataWithObservable();
        }
        if (action.equals("single")){
            fetchDataWithSingle();
        }
        if (action.equals("Composite")){
            fetchDataWithCompositeDisposable();
        }
    }

    private void fetchDataWithObservable() {
        System.out.println("======= fetchDataWithObservable =======");
        myObservable = apiService.getAllPostsWithObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        myObservable.subscribe(getObserver());
    }

    private Observer<List<Post>> getObserver() {
        return new Observer<List<Post>>() {

            @Override
            public void onSubscribe(Disposable d) {
                myDisposable = d;
                System.out.println("======= onSubscribe =======");

            }

            @Override
            public void onNext(List<Post> posts) {
                System.out.println("======= onNext =======");
                progressDialog.dismiss();
                displayData(posts);
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                System.out.println("onError ===>>> " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("======= onComplete =======");
            }
        };
    }

    private void fetchDataWithSingle() {
        System.out.println("======= fetchDataWithSingle =======");
        mySingle = apiService.getAllPostsWithSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mySingle.subscribe(getSingleObserver());
    }

    private SingleObserver<List<Post>> getSingleObserver() {
        return new SingleObserver<List<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {
                myDisposable = d;
                System.out.println("======= onSubscribe =======");
            }

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

    private void fetchDataWithCompositeDisposable() {
        System.out.println("======= fetchDataWithCompositeDisposable =======");
        compositeDisposable.add(apiService.getAllPostsWithSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDisposableSingleObserver()));

        /*In Short without Observer and error handling*/
//            compositeDisposable.add(apiService.getAllPostsWithSingle()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(posts -> displayData(posts)));

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

        if (action.equals("observable") || action.equals("single")){
            myDisposable.dispose();
        }
        if (action.equals("Composite")){
            compositeDisposable.dispose();
        }

    }
}
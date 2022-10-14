package com.milonsheikh.rxjavaandretrofit.network;

import com.milonsheikh.rxjavaandretrofit.model.Post;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("posts")
    Observable<List<Post>> getAllPostsWithObservable();

    @GET("posts")
    Single<List<Post>> getAllPostsWithSingle();

    @GET("users/{userId}/posts")
    Single<List<Post>> getPostsByUserId(@Path("userId") String id);

}

package com.zedeff.marvellous;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zedeff.marvellous.api.ApiClient;
import com.zedeff.marvellous.api.models.Comic;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ComicListActivity extends AppCompatActivity {
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_list);
        setUpList();
    }

    private void setUpList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        disposables.add(new ApiClient(this).createMarvelApi().getComics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cdw -> {
                    ComicsAdapter adapter = new ComicsAdapter(this, cdw.data.results);
                    recyclerView.setAdapter(adapter);
                }));
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }

    private static class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ViewHolder> {
        private Context context;
        private List<Comic> comics;

        ComicsAdapter(Context context, List<Comic> comics) {
            this.context = context;
            this.comics = comics;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comic, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(context, comics.get(position));
        }

        @Override
        public int getItemCount() {
            return comics.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView thumbnail;
            final TextView title;

            ViewHolder(View rootView) {
                super(rootView);
                thumbnail = rootView.findViewById(R.id.thumbnail);
                title = rootView.findViewById(R.id.title);
            }

            void bind(Context context, Comic comic) {
                Glide.with(context).load(comic.thumbnail.path.replace("http://", "https://") + "." + comic.thumbnail.extension).into(thumbnail);
                title.setText(comic.title);
            }
        }
    }
}

package com.wiut12860.recipemanagement.ui.WebAPI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wiut12860.recipemanagement.R;
import com.wiut12860.recipemanagement.databinding.FragmentApiBinding;

public class WebAPIFragment extends Fragment {

    private FragmentApiBinding binding;
    private ListView lvMovies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentApiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();

        lvMovies = root.findViewById(R.id.movies);
        MoviesTask task = new MoviesTask(lvMovies);
        task.execute("1");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


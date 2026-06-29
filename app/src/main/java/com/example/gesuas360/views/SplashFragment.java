package com.example.gesuas360.views;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;

public class SplashFragment extends Fragment {

    private static final long DURACAO_MS = 2500;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                Navigation.findNavController(view).navigate(R.id.action_splash_to_login);
            }
        }, DURACAO_MS);
    }
}

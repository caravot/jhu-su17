package com.javadude.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnAboutFragmentListener))
            throw new IllegalStateException("Activities using AboutFragment must implement AboutFragment.OnAboutFragmentListener");
        onAboutFragmentListener = (OnAboutFragmentListener) context;
    }

    @Override
    public void onDetach() {
        onAboutFragmentListener = null;
        super.onDetach();
    }

    private OnAboutFragmentListener onAboutFragmentListener;

    public interface OnAboutFragmentListener {
    }
}

package com.example.messagingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.messagingapp.Fragments.CallsFragment;
import com.example.messagingapp.Fragments.ChatFragment;
import com.example.messagingapp.Fragments.StatusFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            default:return new ChatFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}

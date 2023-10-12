
package com.example.blogsaga.utils.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.blogsaga.fragments.PublishedFragments;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) {
//            return new DraftsFragment();
        }
        return new PublishedFragments();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
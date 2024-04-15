package com.example.todo_list;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        switch (position) {
            case 0:
                return new AllTasksFragment();
            case 1:
                return new ToDoFragment();
            case 2:
                return new CompletedTasksFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}

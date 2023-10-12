package com.example.blogsaga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.blogsaga.fragments.CreatePage;
import com.example.blogsaga.fragments.LoginFragment;
import com.example.blogsaga.fragments.SignupFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new SignupFragment());

    }

    public void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).
                disallowAddToBackStack().commit();
    }
}
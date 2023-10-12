package com.example.blogsaga.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogsaga.MainActivity;
import com.example.blogsaga.R;
import com.example.blogsaga.utils.models.UserToken;
import com.example.blogsaga.utils.ErrorCodes;
import com.example.blogsaga.utils.FirebaseUtilities.GeneralFirebaseUtiliities;
import com.example.blogsaga.utils.GeneralUtilities;
import com.example.blogsaga.utils.callbacks.GeneralCallbacks;
import com.example.blogsaga.utils.models.userDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class SignupFragment extends Fragment {
    private ShapeableImageView dpView;
    private AppCompatButton clickImageButton;
    private EditText emailEt, passwordEt, confPasswordEt;
    private Button signUpButton;
    private TextView haveAccount;
    private GeneralCallbacks callbacks;
    private MainActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);
        init(root);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                String[] data = {email, password,
                        confPasswordEt.getText().toString()};
                int errorCode = GeneralUtilities.verificationUtils(data);
                if (errorCode != -1) {
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
                    return;
                }
                Drawable drawable = dpView.getDrawable();
                Bitmap dpMap = ((BitmapDrawable) drawable).getBitmap();
                UserToken user = new UserToken(email, password, dpMap);
                GeneralFirebaseUtiliities.registerUser(user, callbacks);
            }
        });
        ActivityResultLauncher<String> imageSelectionLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            dpView.setImageURI(result);
                        }
                    }
                }
        );
        clickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectionLauncher.launch("image/*");
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new LoginFragment());
            }
        });
        return root;
    }


    private void init(final View root) {
        dpView = root.findViewById(R.id.profile_pic);
        clickImageButton = root.findViewById(R.id.change_profile_button);
        emailEt = root.findViewById(R.id.txt_email);
        passwordEt = root.findViewById(R.id.txt_password);
        confPasswordEt = root.findViewById(R.id.txt_confirm_password);
        signUpButton = root.findViewById(R.id.btn_sign_up);
        haveAccount=root.findViewById(R.id.already_account);
        callbacks = new GeneralCallbacks() {
            @Override
            public void onSignUp(boolean flag, int errorCode) {

                if (flag) {
                    activity.addFragment(new LoginFragment());
                    Toast.makeText(activity, "Verify your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdate(userDetails details) {

            }

            @Override
            public void onLogin(boolean flag, int errorCode) {

            }
        };
    }


}
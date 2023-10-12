package com.example.blogsaga.fragments;

import static com.example.blogsaga.utils.FirebaseUtilities.GeneralFirebaseUtiliities.loginUser;
import static com.example.blogsaga.utils.GeneralUtilities.verificationUtils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.blogsaga.utils.callbacks.GeneralCallbacks;
import com.example.blogsaga.utils.models.userDetails;


public class LoginFragment extends Fragment {
    private EditText emailEt, passwordEt;
    private Button loginButton;
    private TextView noAccount;
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
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        init(root);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                String[] data = {email, password};
                int errorCode = verificationUtils(data);
                if (errorCode != -1) {
                    Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
                    return;
                }
                UserToken token=new UserToken(email,password,null);
                loginUser(token,callbacks);
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addFragment(new SignupFragment());
            }
        });
        return root;
    }

    private void init(View root) {
        emailEt = root.findViewById(R.id.txt_email);
        passwordEt = root.findViewById(R.id.txt_password);
        loginButton = root.findViewById(R.id.btn_login);
        noAccount=root.findViewById(R.id.No_account);
        callbacks = new GeneralCallbacks() {
            @Override
            public void onSignUp(boolean flag, int errorCode) {
            }

            @Override
            public void onUpdate(userDetails details) {

            }

            @Override
            public void onLogin(boolean flag, int errorCode) {
                if (flag) {
                    activity.addFragment(new HomePage());
                    return;
                }
                Toast.makeText(getContext(), ErrorCodes.getMap().get(errorCode), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
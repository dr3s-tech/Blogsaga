package com.example.blogsaga.utils.callbacks;

import com.example.blogsaga.utils.models.userDetails;

public interface GeneralCallbacks {
    void onSignUp(final boolean flag, final int errorCode);

    void onLogin(final boolean flag, final int errorCode);
    void onUpdate(final userDetails details);
}

package com.ak.cardstore.activity.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ak.cardstore.activity.login.data.LoginRepository;
import com.ak.cardstore.activity.login.data.Result;
import com.ak.cardstore.exception.PasswordValidationException;
import com.ak.cardstore.validation.PasswordValidator;

import lombok.AccessLevel;
import lombok.Getter;

public class LoginViewModel extends ViewModel {

    @Getter(value = AccessLevel.PACKAGE)
    private final MutableLiveData<LoginFormState> loginFormStateLiveData;
    @Getter(value = AccessLevel.PACKAGE)
    private final MutableLiveData<LoginResult> loginResultLiveData;
    private final LoginRepository loginRepository;
    private final PasswordValidator passwordValidator;

    LoginViewModel(final LoginRepository loginRepository) {
        this.loginFormStateLiveData = new MutableLiveData<>();
        this.loginResultLiveData = new MutableLiveData<>();
        this.loginRepository = loginRepository;

        this.passwordValidator = new PasswordValidator();
    }

    void login(final String password) {
        final Result result = this.loginRepository.login(password);

        if (result instanceof Result.Success) {
            this.loginResultLiveData.setValue(new LoginResult(true));
        } else {
            final String loginError = Result.Error.class.cast(result)
                    .getError()
                    .getMessage();
            this.loginResultLiveData.setValue(new LoginResult(loginError));
        }
    }

    void loginDataChanged(final String password) {
        try {
            this.passwordValidator.validatePassword(password);
            this.loginFormStateLiveData.setValue(new LoginFormState(true));
        } catch (final PasswordValidationException e) {
            this.loginFormStateLiveData.setValue(new LoginFormState(e.getMessage()));
        }
    }
}

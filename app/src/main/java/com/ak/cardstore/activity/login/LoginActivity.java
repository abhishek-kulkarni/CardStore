package com.ak.cardstore.activity.login;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ak.cardstore.R;

import lombok.AllArgsConstructor;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        this.loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText passwordEditText = this.findViewById(R.id.passwordEditText);
        final Button loginButton = this.findViewById(R.id.loginButton);
        final ProgressBar loadingProgressBar = this.findViewById(R.id.loadingProgressBar);

        this.loginViewModel.getLoginFormStateLiveData()
                .observe(this, new LoginFormStateObserver(passwordEditText, loginButton));
        this.loginViewModel.getLoginResultLiveData()
                .observe(this, new LoginResultObserver(loadingProgressBar));

        passwordEditText.addTextChangedListener(new PasswordEditTextWatcher(passwordEditText));
        passwordEditText.setOnEditorActionListener(new PasswordEditTextActionListener(passwordEditText));

        loginButton.setOnClickListener(new LoginButtonClickListerner(passwordEditText, loadingProgressBar));
    }

    /**
     * A class to take an action on the login button click
     */
    @AllArgsConstructor
    private final class LoginButtonClickListerner implements View.OnClickListener {

        private final EditText passwordEditText;
        private final ProgressBar loadingProgressBar;

        @Override
        public void onClick(final View view) {
            this.loadingProgressBar.setVisibility(View.VISIBLE);
            LoginActivity.this.loginViewModel.login(this.passwordEditText.getText()
                    .toString());
        }
    }

    /**
     * A class to take an action on the action performed on password edit text
     */
    @AllArgsConstructor
    private final class PasswordEditTextActionListener implements EditText.OnEditorActionListener {

        private final EditText passwordEditText;

        @Override
        public boolean onEditorAction(final TextView textView, final int actionId, final KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                LoginActivity.this.loginViewModel.login(this.passwordEditText.getText().toString());
            }

            return false;
        }
    }

    /**
     * A class to take action on the text change for the password edit text.
     */
    @AllArgsConstructor
    public final class PasswordEditTextWatcher implements TextWatcher {

        private final EditText passwordEditText;

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            // no need to take any action
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            // no need to take any action
        }

        @Override
        public void afterTextChanged(final Editable s) {
            LoginActivity.this.loginViewModel.loginDataChanged(this.passwordEditText.getText().toString());
        }
    }

    /**
     * An observer class to react on the {@link LoginFormState} changes.
     */
    @AllArgsConstructor
    private final class LoginFormStateObserver implements Observer<LoginFormState> {

        private final EditText passwordEditText;
        private final Button loginButton;

        @Override
        public void onChanged(final LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }

            this.loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getPasswordError().isPresent()) {
                this.passwordEditText.setError(loginFormState.getPasswordError().get());
            }
        }
    }

    /**
     * An observer class to react on {@link LoginResult} changes
     */
    @AllArgsConstructor
    private final class LoginResultObserver implements Observer<LoginResult> {

        private final ProgressBar loadingProgressBar;

        @Override
        public void onChanged(final LoginResult loginResult) {
            if (loginResult == null) {
                return;
            }

            this.loadingProgressBar.setVisibility(View.GONE);

            if (loginResult.getError().isPresent()) {
                LoginActivity.this.showLoginFailed(loginResult.getError().get());
            }

            if (loginResult.isSuccess()) {
                LoginActivity.this.updateUiWithUser();
            }

            LoginActivity.this.setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            LoginActivity.this.finish();
        }
    }

    private void updateUiWithUser() {
        final String welcome = this.getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(this.getApplicationContext(), welcome, Toast.LENGTH_LONG)
                .show();
    }

    private void showLoginFailed(final String errorMessage) {
        Toast.makeText(this.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT)
                .show();
    }
}

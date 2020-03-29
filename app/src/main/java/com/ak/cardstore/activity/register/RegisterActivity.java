package com.ak.cardstore.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ak.cardstore.R;

import lombok.AllArgsConstructor;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);

        final EditText registerPasswordEditText = this.findViewById(R.id.registerPasswordEditText);
        final EditText confirmPasswordEditText = this.findViewById(R.id.confirmPasswordEditText);
        final Button registerButton = this.findViewById(R.id.registerButton);
        final ProgressBar registerLoadingProgressBar = this.findViewById(R.id.registerLoadingProgressBar);

        registerButton.setOnClickListener(new RegisterButtonClickListener(registerPasswordEditText, confirmPasswordEditText,
                registerLoadingProgressBar));
    }

    /**
     * A class to take an action on the login button click
     */
    @AllArgsConstructor
    private final class RegisterButtonClickListener implements View.OnClickListener {

        final EditText registerPasswordEditText;
        final EditText confirmPasswordEditText;
        final ProgressBar registerLoadingProgressBar;

        @Override
        public void onClick(final View view) {
            this.registerLoadingProgressBar.setVisibility(View.VISIBLE);

            // Check passwords match
            // Check password validity
            // Create user
            // Save user
        }
    }
}

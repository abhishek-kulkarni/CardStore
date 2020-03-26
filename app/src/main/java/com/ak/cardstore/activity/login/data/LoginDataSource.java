package com.ak.cardstore.activity.login.data;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result login(final String password) {

        try {
            // TODO: handle loggedInUser authentication
            return new Result.Success();
        } catch (final Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}

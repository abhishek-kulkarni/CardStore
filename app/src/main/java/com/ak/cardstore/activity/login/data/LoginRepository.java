package com.ak.cardstore.activity.login.data;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final LoginDataSource dataSource;

    // private constructor : singleton access
    private LoginRepository(final LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(final LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }

        return instance;
    }

    public void logout() {
        this.dataSource.logout();
    }

    public Result login(final String password) {
        final Result result = this.dataSource.login(password);
        return result;
    }
}

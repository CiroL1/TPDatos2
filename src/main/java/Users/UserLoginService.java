package Users;

public class UserLoginService {
    private final UserManager userManager;

    public UserLoginService(UserManager userManager) {
        this.userManager = userManager;
    }

    public boolean login(int dni, String password) {
        // Delegates password check to UserManager
        return userManager.checkPassword(dni, password);
    }
}

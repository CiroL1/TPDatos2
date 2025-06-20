package Users;

public class SessionManager {
    private static int currentDni = -1;
    private long loginTime = 0;

    private final UserManager userManager;

    public SessionManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public boolean login(int dni, String password) {
        boolean success = userManager.checkPassword(dni, password);
        if (success) {
            currentDni = dni;
            loginTime = System.currentTimeMillis();
            System.out.println("User " + dni + " logged in.");
            return true;
        } else {
            System.out.println("Login failed for user " + dni);
            return false;
        }
    }

    public void logout() {
        if (currentDni == -1) {
            System.out.println("No user is currently logged in.");
            return;
        }

        long logoutTime = System.currentTimeMillis();
        int sessionMinutes = (int) Math.ceil((logoutTime - loginTime) / (1000.0 * 60.0));

        User user = userManager.getOne(currentDni);
        if (user != null) {
            user.setSessionTime(user.getSessionTime()+sessionMinutes);

            if (user.getSessionTime() > 240) {
                user.setUserType("TOP");
            } else if (user.getSessionTime() >= 120) {
                user.setUserType("MEDIUM");
            } else {
                user.setUserType("LOW");
            }

            userManager.update(user);

            System.out.println("User " + currentDni + " logged out. Session time added: " + sessionMinutes + " minutes.");
            System.out.println("Updated user type: " + user.getUserType());
        } else {
            System.out.println("User not found to update session time.");
        }

        currentDni = -1;
        loginTime = 0;
    }

    public static int getDniUsuarioActivo() {
        return currentDni;
    }
}

package Users;

public class UserSessionManager {
    private int currentDni = -1;
    private long loginTime = 0;

    private final UserManager manager;
    private final UserLoginService loginService; //si es final me parece que se tendria que escribir en mayusculas

    public UserSessionManager(UserManager manager) {
        this.manager = manager;
        this.loginService = new UserLoginService(manager);
    }

    public boolean login(int dni, String password) {
        boolean success = loginService.login(dni, password);
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
            System.out.println("No user is currently logged in."); //se intenta hacer logout cuando nadie hizo login en primer lugar
            return;
        }

        long logoutTime = System.currentTimeMillis();
        int sessionMinutes = (int) Math.ceil((logoutTime - loginTime) / (1000.0 * 60.0));

        // Get current user
        User user = manager.getUserByDni(currentDni);
        if (user != null) {  // si encuentra al usuario
            // se agregan los minutos que estuvo conectado
            user.sessionTime += sessionMinutes;

            // Se actualiza el tipo de usuario dependiendo del tiempo que estuvo conectado (en total)
            if (user.sessionTime > 240) {
                user.userType = "TOP";
            } else if (user.sessionTime >= 120) {
                user.userType = "MEDIUM";
            } else {
                user.userType = "LOW";
            }

            // Se actualiza la informacion del usuario en Cassandra
            manager.updateUser(user);

            System.out.println("User " + currentDni + " logged out. Session time added: " + sessionMinutes + " minutes.");
            System.out.println("Updated user type: " + user.userType);
        } else {
            System.out.println("User not found to update session time.");
        }

        // Reset session
        currentDni = -1;
        loginTime = 0;
    }

}

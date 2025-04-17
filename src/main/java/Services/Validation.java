package Services;

import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean ValidateText(String text) {
        if (text == null || text.trim().isEmpty()) {

            return false;
        }
        return true;
    }
    public  static  void AlertSucces(String Text ){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(Text);
        alert.showAndWait();
    }
    public  static  void AlertError(String Text ){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(Text);
        alert.showAndWait();
    }
    public static boolean isValidEmail(String email) {
        // Expression régulière pour valider l'email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

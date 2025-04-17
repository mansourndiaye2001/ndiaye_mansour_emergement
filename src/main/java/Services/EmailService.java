package Services;

import Entity.Cours;
import Entity.Utulisateur;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    public static void envoyerEmail(Utulisateur professeur, Cours cours) {
        String from = "ndiayemansour20001@gmail.com";
        String password = "gnkd fbgw gddp frab";

        // Propriétés de connexion SMTP (ici pour Gmail)
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Créer une session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Créer un message e-mail
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(professeur.getEmail()));
            message.setSubject("Affectation d'un nouveau cours");
            message.setText("Bonjour " + professeur.getNom() + ",\n\n" +
                    "Vous avez été affecté au cours suivant : " + cours.getNom() + ".\n\n" +
                    cours.getJour()+".\n\n"+
                    "Merci et à bientôt.");

            // Envoyer l'e-mail
            Transport.send(message);
            System.out.println("E-mail envoyé à " + professeur.getEmail() + " !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'e-mail.");
        }
    }
}

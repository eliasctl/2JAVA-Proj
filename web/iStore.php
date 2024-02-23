<?php

require 'vendor/autoload.php';
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;



$conn = mysqli_connect("Server:PORT", "root", "root", "java");

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

if (isset($_POST['email'])) {
    $email = mysqli_real_escape_string($conn, $_POST['email']);
    $email = filter_var($email, FILTER_VALIDATE_EMAIL);

    $sql = "SELECT * FROM users WHERE email = '$email'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        $token = bin2hex(random_bytes(50));
        $sql = "INSERT INTO reset_password (email, token) VALUES ('$email', '$token')";

        if (mysqli_query($conn, $sql)) {
            $url = "http://localhost:8888/iStore/iStore.php?token=$token";
            
            // Envoi d'e-mail avec PHPMailer
            $mail = new PHPMailer(true);

            try {
                //Server settings pour OVH (changer les paramètres pour d'autres hébergeurs)
                $mail->isSMTP();
                $mail->Host       = 'ssl0.ovh.net';
                $mail->SMTPAuth   = true;
                $mail->Username   = 'Mail';
                $mail->Password   = 'MDP';
                $mail->SMTPSecure = 'ssl';
                $mail->Port       = 465;

                //Recipients
                $mail->setFrom('Mail', 'iStore');
                $mail->addAddress($email);

                //Content
                $mail->isHTML(true);
                $mail->Subject = 'Changement de mot de passe iStore';
                $mail->Body    = "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe.</p>";
                $mail->Body   .= "<a href='$url'>$url</a>";

                $mail->send();
                echo "<p>Consultez votre adresse e-mail pour réinitialiser votre mot de passe.</p>";
            } catch (Exception $e) {
                echo "Erreur lors de l'envoi de l'e-mail : {$mail->ErrorInfo}";
            }
        } else {
            echo "<p>Erreur: " . mysqli_error($conn) . "</p>";
        }
    } else {
        echo "<p>Aucun compte trouvé avec cette adresse e-mail.</p>";
    }
} elseif (isset($_GET['token'])) {
    $token = mysqli_real_escape_string($conn, $_GET['token']);
    $sql = "SELECT * FROM reset_password WHERE token = '$token'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) == 0) {
        echo "<p>Le lien de réinitialisation du mot de passe est invalide.</p>";
    } else {
        echo "<h1>Réinitialiser votre mot de passe</h1>";
        echo "<form action='iStore.php' method='post'>";
        echo "<input type='hidden' name='token' value='$token'>";
        echo "<input type='password' name='password' placeholder='Nouveau mot de passe' required>";
        echo "<input type='password' name='confirm_password' placeholder='Confirmer le mot de passe' required>";
        echo "<input type='submit' value='Réinitialiser le mot de passe'>";
        echo "</form>";
    }
} elseif (isset($_POST['token'])) {
    $token = mysqli_real_escape_string($conn, $_POST['token']);
    $password = $_POST['password'];
    $confirm_password = $_POST['confirm_password'];

    if ($password != $confirm_password) {
        echo "<p>Les mots de passe ne correspondent pas.</p>";
    } else {
        $password_hash = crypt($password, '$2a$10$VM/GfVScMMgdLVtHwABv6u');
        $sql = "UPDATE users SET password = '$password_hash' WHERE email IN (SELECT email FROM reset_password WHERE token = '$token')";

        if (mysqli_query($conn, $sql)) {
            $sql = "DELETE FROM reset_password WHERE token = '$token'";
            
            if (mysqli_query($conn, $sql)) {
                echo "<p>Votre mot de passe a été réinitialisé avec succès.</p>";
            } else {
                echo "<p>Erreur lors de la suppression du jeton de réinitialisation.</p>";
            }
        } else {
            echo "<p>Erreur lors de la réinitialisation du mot de passe: " . mysqli_error($conn) . "</p>";
        }
    }
}
?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>iStore Mot de passe oublié</title>
</head>
<body>
    <h1>Vous avez oublié votre mot de passe ?</h1>
    <p>Entrez votre adresse e-mail pour réinitialiser votre mot de passe.</p>
    <form action="iStore.php" method="post">
        <input type="email" name="email" placeholder="Entrez votre adresse e-mail" required>
        <input type="submit" value="Envoyer">
    </form>
</body>
</html>
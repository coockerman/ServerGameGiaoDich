package ServerNew.Utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    // Hàm gửi email chứa mật khẩu
    public static void sendPasswordEmail(String toEmail, String password) throws Exception {
        // Thông tin cấu hình email
        String host = "smtp.gmail.com";
        final String username = "nghai1827@gmail.com"; // Thay bằng email của bạn
        final String passwordApp = "qcml knxk iazt squp";   // Mật khẩu ứng dụng (không phải mật khẩu email)

        // Cài đặt thuộc tính kết nối SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Tạo phiên kết nối
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, passwordApp);
            }
        });

        try {
            // Tạo tin nhắn email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mật khẩu tài khoản của bạn");
            message.setText("Đế chế giao dịch xin chào,\n" +
                    "Mật khẩu tài khoản của bạn là: " + password + "\n" +
                    "Vui lòng không tiết lộ mật khẩu này của bạn cho người khác.");

            // Gửi email
            Transport.send(message);
            System.out.println("Email chứa mật khẩu đã được gửi thành công!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}


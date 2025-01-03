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
            // Tạo tin nhắn email với HTML
            String emailContent = "<html>" +
                    "<body>" +
                    "<h2 style='color: #4CAF50;'>Đế chế giao dịch xin chào,</h2>" +
                    "<p>Mật khẩu tài khoản của bạn là:</p>" +
                    "<p style='font-size: 20px; font-weight: bold; color: #FF6347;'>" + password + "</p>" +
                    "<p>Vui lòng không tiết lộ mật khẩu này cho người khác.</p>" +
                    "<p style='color: gray;'>Trân trọng,<br>Đội ngũ Đế chế giao dịch</p>" +
                    "<footer style='font-size: 10px; color: gray; text-align: center;'>" +
                    "Nếu bạn không yêu cầu thay đổi mật khẩu, vui lòng bỏ qua email này." +
                    "</footer>" +
                    "</body>" +
                    "</html>";

// Tạo MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mật khẩu tài khoản của bạn");

// Thiết lập kiểu nội dung là HTML
            message.setContent(emailContent, "text/html; charset=utf-8");

// Gửi email
            Transport.send(message);
            System.out.println("Email chứa mật khẩu đã được gửi thành công!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}


package com.equipo2.Integrador.util;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.service.impl.ClienteService;
import com.equipo2.Integrador.service.impl.VerificationTokenService;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = Logger.getLogger(RegistrationListener.class);


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event)
    {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
    {
        System.out.println(event.getAppUrl());
        ClienteDTO clienteDTO = event.getClienteDTO();
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(clienteDTO, token);

        String recipientAddress = clienteDTO.getEmail();
        String subject = "Confirmación de tu registro en Digital Booking";
        // Para usar en local
        // String confirmationUrl = "http://localhost:3000" + "/login?confirmacionToken=" + token;
        // Para usar en deploy
        String confirmationUrl = "https://www.digitalbookingdh.tk" + "/login?confirmacionToken=" + token;

        String msg =    "<html lang=\"en\">\n" +
                        "  <head>\n" +
                        "    <meta charset=\"UTF-8\" />\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                        "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />\n" +
                        "    <link\n" +
                        "      href=\"https://fonts.googleapis.com/css2?family=Roboto&display=swap\"\n" +
                        "      rel=\"stylesheet\"\n" +
                        "    />\n" +
                        "    <title>Registro Exitoso</title>\n" +
                        "\n" +
                        "    <style>\n" +
                        "      body {\n" +
                        "        background-color: #f9f9f9;\n" +
                        "        font-family: roboto, helvetica neue, helvetica, arial, sans-serif;\n" +
                        "        margin: auto;\n" +
                        "        text-align: center;\n" +
                        "        width: 60%;\n" +
                        "      }\n" +
                        "\n" +
                        "      table {\n" +
                        "        margin: auto;\n" +
                        "        padding: 40px 0;\n" +
                        "        text-align: center;\n" +
                        "        width: 60%;\n" +
                        "        margin: auto;\n" +
                        "        padding-top: 24px;\n" +
                        "      }\n" +
                        "      tbody {\n" +
                        "        background-color: #fff;\n" +
                        "      }\n" +
                        "\n" +
                        "      h1 {\n" +
                        "        color: #1dbeb4;\n" +
                        "        font-size: 1.8rem;\n" +
                        "        text-align: center;\n" +
                        "      }\n" +
                        "      img:first-child {\n" +
                        "        padding-top: 24px;\n" +
                        "      }\n" +
                        "      .img img {\n" +
                        "        width: 648px;\n" +
                        "        height: 424px;\n" +
                        "      }\n" +
                        "\n" +
                        "      .text-box {\n" +
                        "        background-color: #545776;\n" +
                        "        color: #fff;\n" +
                        "        margin: 1rem;\n" +
                        "        width: 90%;\n" +
                        "      }\n" +
                        "      p {\n" +
                        "        font-size: 14px;\n" +
                        "      }\n" +
                        "          \n" +
                        "      .buttonContainer {\n" +
                        "        padding: 30px 0;\n" +
                        "      }\n" +
                        "      .buttonContainer a {\n" +
                        "        background-color: #1dbeb4;\n" +
                        "        border: transparent;\n" +
                        "        box-sizing: border-box;\n" +
                        "        font-size: 1rem;\n" +
                        "        font-weight: bold;\n" +
                        "        margin-bottom: 1rem;\n" +
                        "        text-decoration: none;\n" +
                        "        padding: 1rem;\n" +
                        "        color: white;\n" +
                        "        cursor: pointer;\n" +
                        "        text-decoration: none;\n" +
                        "      }\n" +
                        "      tfoot {\n" +
                        "        margin: 34px 0;\n" +
                        "      }\n" +
                        "      tfoot p {\n" +
                        "        font-size: 12px;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <table>\n" +
                        "      <tbody>\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <img\n" +
                        "              src=\"https://images-ext-2.discordapp.net/external/HGSGLaIzqEKqt4YWI-D68TJ_emK4PYXJri5gYGxo3Zw/https/imagenes-emails.s3.amazonaws.com/logo.png\"\n" +
                        "              alt=\"Logo Digital Booking\"\n" +
                        "            />\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <h1>\n" +
                        "              Hola, " + clienteDTO.getNombre() + " " + clienteDTO.getApellido() + " \uD83D\uDE03</h2>\n" +
                        "            </h1>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <p> ¡Te damos la bienvenida a Digital Booking!</p>\n" +
                        "           \n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td class=\"img\" colspan=\"2\">\n" +
                        "            <img\n" +
                        "              src=\"https://images-ext-1.discordapp.net/external/bhvK4TcyGEWaYWcstqcjwuq0trjYtYV7mOtk49x3J_M/https/imagenes-emails.s3.amazonaws.com/registro.jpg?width=985&height=676\"\n" +
                        "              alt=\"Mujer feliz frente a una computadora\"\n" +
                        "            />\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"2\">\n" +
                        "            <p>\n" +
                        "              Estás a solo un paso de acceder a las mejores ofertas en todo tipo\n" +
                        "              de alojamientos.\n" +
                        "            </p>\n" +
                        "            <p><b>Por favor, haz click en el siguiente botón para confirmar tu registro:</b></p>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "  \n" +
                        "          <td colspan=\"2\" class=\"buttonContainer\">\n" +
                        "            <a href=\"" + confirmationUrl + "\">Confirmar registro aquí</a>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tbody>\n" +
                        "\n" +
                        "      <tfoot>\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"2\">\n" +
                        "              <p>\n" +
                        "                  <i>Copyright &copy; 2021 Digital Booking All rights reserved.</i>\n" +
                        "              </p>\n" +
                        "            </p>\n" +
                        "            <p>\n" +
                        "              <strong>Nuestra dirección de correo electrónico es:</strong>\n" +
                        "            </p>\n" +
                        "            <p><u>digitalbookingdh@gmail.com</u> </p>\n" +
                        "            \n" +
                        "            <p>\n" +
                        "                Este correo fue enviado porque alguien ha usado esta dirección de email para registrarse en Digital Booking. \n" +
                        "                Si consideras que esto fue un error, por favor, ignora este mensaje.</p>\n" +
                        "            <p>\n" +
                        "          </td>\n" +
                        "        </tr>\n" +
                        "      </tfoot>\n" +
                        "    </table>\n" +
                        "  </body>\n" +
                        "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom("Digital Booking <registration@digitalbooking.com>");
            message.setContent(msg, "text/html; charset=utf-8");
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(recipientAddress);
            helper.setSubject(subject);
            mailSender.send(message);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }
}
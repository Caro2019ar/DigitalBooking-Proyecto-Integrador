package com.equipo2.Integrador.util;


import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.service.impl.UsuarioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Component
public class ReservationListener implements ApplicationListener<OnReservationCompleteEvent> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = Logger.getLogger(RegistrationListener.class);


    @Override
    public void onApplicationEvent(OnReservationCompleteEvent event)
    {
        this.confirmReservation(event);
    }

    private void confirmReservation(OnReservationCompleteEvent event)
    {
        ReservaDTO reservaDTO = event.getReservaDTO();

        String recipientAddress = reservaDTO.getCliente().getEmail();
        String subject = "Tu reserva en Digital Booking";

        // Para usar en local
        //String websiteUrl = "http://localhost:3000";
        // Para usar en deploy
        String websiteUrl = "https://www.digitalbookingdh.tk/";

        String msg =
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin />\n" +
                    "    <link\n" +
                    "      href=\"https://fonts.googleapis.com/css2?family=Roboto&display=swap\"\n" +
                    "      rel=\"stylesheet\"\n" +
                    "    />\n" +
                    "    <title>Reserva Exitosa</title>\n" +
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
                    "        margin:auto;\n" +
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
                    "      img:first-child{\n" +
                    "          padding-top: 24px;\n" +
                    "      }\n" +
                    "      .img img {\n" +
                    "        width: -webkit-fill-available;\n" +
                    "      }\n" +
                    "      .text-box {\n" +
                    "        background-color: #545776;\n" +
                    "        color: #fff;\n" +
                    "        margin: 1rem;\n" +
                    "        width: 90%;\n" +
                    "      }\n" +
                    "      .detailsTitle {\n" +
                    "        padding: 10px 15px;\n" +
                    "        background-color: #1dbeb4;\n" +
                    "        color: #fff;\n" +
                    "        text-align: center;\n" +
                    "      }\n" +
                    "      .rowStyle {\n" +
                    "        background-color: #e7fdfc;\n" +
                    "        font-size: 14px;\n" +
                    "        padding: 10px 15px;\n" +
                    "      }\n" +
                    "      .leftInfoBookingRow {\n" +
                    "        font-weight: bold;\n" +
                    "        text-align: end;\n" +
                    "        width: 30%;\n" +
                    "      }\n" +
                    "      .rightInfoBookingRow {\n" +
                    "        text-align: left;\n" +
                    "      }\n" +
                    "      .buttonContainer{\n" +
                    "          padding: 30px 0;\n" +
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
                    "        color: white;\n"+
                    "        text-decoration: none;\n" +
                    "      }\n" +
                    "      tfoot {\n" +
                    "        margin: 34px 0;\n" +
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
                    "              src=\"https://mcusercontent.com/61759187021cf0720bd927584/images/2d43694b-9760-c2a1-1a43-733ddcefd212.png\"\n" +
                    "              alt=\"Logo Digital Booking\"\n" +
                    "            />\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td colspan=\"2\">\n" +
                    "            <h1>¡Tu reserva ha sido exitosa!</h1>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td colspan=\"2\">\n" +
                    "            <p>\n" +
                    "              ¡Ya estás a un paso de vivir una gran aventura en el destino de\n" +
                    "              tus sueños!\n" +
                    "            </p>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"img\" colspan=\"2\">\n" +
                    "            <img\n" +
                    "              src=\"https://image.freepik.com/foto-gratis/concepto-viaje-plano-completo-puntos-referencia_23-2149153258.jpg?1\"\n" +
                    "              alt=\"Viajando por el mundo\"\n" +
                    "            />\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "        <th>\n" +
                    "          <tr class=\"text-box\">\n" +
                    "            <td colspan=\"2\">\n" +
                    "              <p>Estos son los detalles de tu reserva:</p>\n" +
                    "            </td>\n" +
                    "          </tr>\n" +
                    "        </th>\n" +
                    "\n" +
                    "        <th colspan=\"2\" class=\"detailsTitle\">SOBRE EL ITINERARIO</th>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Alojamiento:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getProducto().getNombre() + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Ciudad:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getProducto().getCiudad().getNombre() + ", "
                                                                            + reservaDTO.getProducto().getCiudad().getPais() + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Dirección:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getProducto().getDireccion() + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Ckeck in:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getFechaInicial().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Ckeck out:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getFechaFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Hora de llegada:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getHoraInicio().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US)) + "</td>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "        <th colspan=\"2\" class=\"detailsTitle\">SOBRE TU INFORMACIÓN PERSONAL</th>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Nombre completo:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getCliente().getNombre() + " " + reservaDTO.getCliente().getApellido() + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td class=\"rowStyle leftInfoBookingRow\">Correo electrónico:</td>\n" +
                    "          <td class=\"rowStyle rightInfoBookingRow\">" + reservaDTO.getCliente().getEmail() + "</td>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "          <td colspan=\"2\" class=\"buttonContainer\">\n" +
                    "            <a href=\"" + websiteUrl + "\">Ir a la página web</a>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "      </tbody>\n" +
                    "      <tfoot>\n" +
                    "        <tr>\n" +
                    "          <td colspan=\"2\">\n" +
                    "            <p>\n" +
                    "              <i>Copyright &copy; 2021 Digital Booking All rights reserved.</i>\n" +
                    "            </p>\n" +
                    "            <p>\n" +
                    "              <strong>Nuestra dirección de correo electrónico es:</strong>\n" +
                    "            </p>\n" +
                    "            <p>digitalbookingdh@gmail.com</p>\n" +
                    "            <br />\n" +
                    "            <p><strong> Imprime sólo en caso de ser necesario.</strong></p>\n" +
                    "            <p>\n" +
                    "              <span style=\"color: red; font-weight: 600\">Aviso Legal:</span> La\n" +
                    "              información contenida en este mensaje es confidencial y para uso\n" +
                    "              exclusivo de la persona u organización a la cual está dirigida. Si\n" +
                    "              no es el receptor autorizado, cualquier retención, difusión,\n" +
                    "              distribución o copia de este mensaje es prohibida y sancionada por\n" +
                    "              la ley. Si por error recibe este mensaje, por favor reenvíelo al\n" +
                    "              remitente y borre el mensaje recibido inmediatamente. Los archivos\n" +
                    "              anexos han sido escaneados y se cree que están libres de virus.\n" +
                    "              Sin embargo, es responsabilidad del receptor asegurarse de ello.\n" +
                    "              <i> Digital Booking</i> no se hace responsable por pérdidas o\n" +
                    "              daños causados por su uso.\n" +
                    "            </p>\n" +
                    "          </td>\n" +
                    "        </tr>\n" +
                    "      </tfoot>\n" +
                    "    </table>\n" +
                    "  </body>\n" +
                    "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom("Digital Booking <reservation@digitalbooking.com>");
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
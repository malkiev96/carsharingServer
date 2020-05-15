package com.carsharing.controller.rest;

import com.carsharing.model.Client;
import com.carsharing.model.Document;
import com.carsharing.model.android.ClientReg;
import com.carsharing.model.android.LogPass;
import com.carsharing.model.android.Token;
import com.carsharing.service.ClientService;
import com.carsharing.service.DocumentService;
import com.carsharing.valid.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Date;

import static org.apache.tomcat.util.codec.binary.Base64.decodeBase64;

@RestController
@AllArgsConstructor
public class SignApiController {

    private ClientService clientService;
    private DocumentService documentService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("api/client/{id}")
    public Client getClient(@PathVariable("id") int id) {
        return clientService.getById(id);
    }

    @PostMapping("api/client/token")
    public @ResponseBody
    Client token(@RequestBody Token token, HttpServletResponse response) {

        Client client = clientService.getById(token.getId());

        if (client != null && client.getToken().equals(token.getToken())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return client;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @PostMapping("/api/client/registration")
    public @ResponseBody
    Client regClient(@RequestBody ClientReg clientReg, HttpServletResponse response) {

        if (clientReg != null) {
            if (Pattern.checkPhone(clientReg.getTelephone()) &&
                    Pattern.checkEmail(clientReg.getMail()) &&
                    Pattern.checkPassword(clientReg.getPassword())) {
                if (clientService.getByTelephone(clientReg.getTelephone()) == null &&
                        clientService.getByEmail(clientReg.getMail()) == null) {

                    Client client = new Client();
                    client.setTelephone(clientReg.getTelephone());
                    client.setEmail(clientReg.getMail());
                    client.setPassword(bCryptPasswordEncoder.encode(clientReg.getPassword()));
                    client.setActivated(false);
                    client.setEnabled(true);
                    client.setToken(clientService.generateToken());
                    client.setRegistrationDate(new Date());
                    client.setMiddleName(clientReg.getMiddlename());
                    client.setFirstName(clientReg.getFirstname());
                    client.setSecondName(clientReg.getSecondname());

                    clientService.save(client);
                    client = clientService.getByTelephone(clientReg.getTelephone());

                    try {
                        BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(decodeBase64(clientReg.getImageByte1())));
                        BufferedImage image2 = ImageIO.read(new ByteArrayInputStream(decodeBase64(clientReg.getImageByte2())));
                        BufferedImage image3 = ImageIO.read(new ByteArrayInputStream(decodeBase64(clientReg.getImageByte3())));
                        BufferedImage image4 = ImageIO.read(new ByteArrayInputStream(decodeBase64(clientReg.getImageByte4())));
                        BufferedImage image5 = ImageIO.read(new ByteArrayInputStream(decodeBase64(clientReg.getImageByte5())));

                        Document document = new Document();
                        document.setImageSrc1(clientService.saveImage(image1, client.getId(), "Photo1.png"));
                        document.setImageSrc2(clientService.saveImage(image2, client.getId(), "Photo2.png"));
                        document.setImageSrc3(clientService.saveImage(image3, client.getId(), "Photo3.png"));
                        document.setImageSrc4(clientService.saveImage(image4, client.getId(), "Photo4.png"));
                        document.setImageSrc5(clientService.saveImage(image5, client.getId(), "Photo5.png"));
                        document.setClient(client);

                        documentService.save(document);

                        response.setStatus(HttpServletResponse.SC_OK);
                        return client;

                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @PostMapping("/api/client/valid")
    public @ResponseBody
    String validClient(@RequestBody ClientReg clientReg, HttpServletResponse response) {

        if (clientReg != null) {
            if (Pattern.checkPhone(clientReg.getTelephone()) &&
                    Pattern.checkEmail(clientReg.getMail()) &&
                    Pattern.checkPassword(clientReg.getPassword())) {
                if (clientService.getByTelephone(clientReg.getTelephone()) == null &&
                        clientService.getByEmail(clientReg.getMail()) == null) {

                    response.setStatus(HttpServletResponse.SC_OK);
                    return "OK";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);//400
        return "ERROR";
    }


    @PostMapping("api/client/login")
    public @ResponseBody
    Client login(@RequestBody LogPass logPass) {
        return clientService.login(logPass);
    }
}

package com.carsharing.service.impl;

import com.carsharing.model.Client;
import com.carsharing.model.android.LogPass;
import com.carsharing.model.android.Token;
import com.carsharing.repository.ClientRepository;
import com.carsharing.service.ClientService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllByActivated(boolean activated) {
        return clientRepository.getAllByActivated(activated);
    }

    public List<Client> getAllByEnabled(boolean enabled) {
        return clientRepository.getAllByEnabled(enabled);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getAllByActivatedAndEnabled(boolean activated, boolean enabled) {
        return clientRepository.getAllByActivatedAndEnabled(activated, enabled);
    }

    public Client getByMail(String mail) {
        return clientRepository.getClientByMail(mail);
    }

    public Client getByTelephone(String telephone) {
        return clientRepository.getClientByTelephone(telephone);
    }

    public Client getById(int id) {
        return clientRepository.getOne(id);
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    public boolean tokenAuthentication(Token token) {
        Client client = getById(token.getId());
        if (client != null) return client.getToken().equals(token.getToken());
        return false;
    }

    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        long longToken = Math.abs(secureRandom.nextLong());
        String random = Long.toString(longToken, 16);
        return random;
    }

    public Client login(LogPass logPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client client = clientRepository.getClientByTelephone(logPass.getLogin());
        if (client != null && passwordEncoder.matches(logPass.getPassword(), client.getPassword())) {
            return client;
        } else return null;
    }

    public String saveImage(BufferedImage image, int clientId, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String path = "C:/carsharing/client/" + clientId;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path + "/" + fileName);
        try {
            ImageIO.write(image, "png", file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

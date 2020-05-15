package com.carsharing.service.impl;

import com.carsharing.model.Client;
import com.carsharing.model.android.LogPass;
import com.carsharing.model.android.Token;
import com.carsharing.repository.ClientRepository;
import com.carsharing.service.ClientService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getActivatedClients(boolean activated) {
        return clientRepository.getAllByActivated(activated);
    }

    public List<Client> getEnabledClients(boolean enabled) {
        return clientRepository.getAllByEnabled(enabled);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getActivatedAndEnabledClients(boolean activated, boolean enabled) {
        return clientRepository.getAllByActivatedAndEnabled(activated, enabled);
    }

    public Client getByEmail(String email) {
        return clientRepository.getClientByEmail(email);
    }

    public Client getByTelephone(String telephone) {
        return clientRepository.getClientByTelephone(telephone);
    }

    public Client getById(int id) {
        return clientRepository.getOne(id);
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        long longToken = Math.abs(secureRandom.nextLong());
        return Long.toString(longToken, 16);
    }

    public Client login(LogPass logPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Client client = clientRepository.getClientByTelephone(logPass.getLogin());
        if (client != null && passwordEncoder.matches(logPass.getPassword(), client.getPassword())) {
            return client;
        } else return null;
    }

    public String saveImage(BufferedImage image, int clientId, String fileName) {
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

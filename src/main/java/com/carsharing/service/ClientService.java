package com.carsharing.service;

import com.carsharing.model.Client;
import com.carsharing.model.android.LogPass;
import com.carsharing.model.android.Token;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ClientService {

    List<Client> getActivatedClients(boolean activated);
    List<Client> getEnabledClients(boolean enabled);
    List<Client> getAllClients();
    List<Client> getActivatedAndEnabledClients(boolean activated, boolean enabled);
    Client getByEmail(String mail);
    Client getByTelephone(String telephone);
    Client getById(int id);
    void save(Client client);
    Client login(LogPass logPass);
    String generateToken();
    String saveImage(BufferedImage image, int clientId, String name);
}

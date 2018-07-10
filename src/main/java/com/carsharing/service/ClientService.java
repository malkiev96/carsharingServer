package com.carsharing.service;

import com.carsharing.model.Client;
import com.carsharing.model.android.LogPass;
import com.carsharing.model.android.Token;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ClientService {
    List<Client> getAllByActivated(boolean activated);

    List<Client> getAllByEnabled(boolean enabled);

    List<Client> getAllClients();

    List<Client> getAllByActivatedAndEnabled(boolean activated, boolean enabled);

    Client getByMail(String mail);

    boolean tokenAuthentication(Token token);

    Client getByTelephone(String telephone);

    Client getById(int id);

    void saveClient(Client client);

    Client login(LogPass logPass);

    String generateToken();

    String saveImage(BufferedImage image, int clientId, String name);
}

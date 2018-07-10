package com.carsharing.repository;

import com.carsharing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> getAllByActivated(boolean activated);

    List<Client> getAllByEnabled(boolean enabled);

    List<Client> getAllByActivatedAndEnabled(boolean activated, boolean enabled);

    Client getClientByMail(String mail);

    Client getClientByTelephone(String telephone);
}

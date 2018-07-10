package com.carsharing.controller.admin;

import com.carsharing.model.Client;
import com.carsharing.model.Document;
import com.carsharing.model.Order;
import com.carsharing.service.CarService;
import com.carsharing.service.ClientService;
import com.carsharing.service.OrderService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ClientController {

    private final ClientService clientService;
    private final CarService carService;
    private final OrderService orderService;

    public ClientController(ClientService clientService, CarService carService, OrderService orderService) {
        this.clientService = clientService;
        this.carService = carService;
        this.orderService = orderService;
    }


    @ModelAttribute
    public void carCount(Model model,Principal principal){
        model.addAttribute("username",principal.getName());
        model.addAttribute("carOffline",carService.getAllByOnline(false).size());
        model.addAttribute("clientNew",clientService.getAllByActivatedAndEnabled(false,true).size());
        model.addAttribute("orderNotPaid",orderService.getAllNotPaid().size());
    }

    @RequestMapping(value = "/admin/order/{id}/end",method = RequestMethod.GET)
    public String endOrder(@PathVariable("id") int id){
        Order order = orderService.getOrderById(id);
        orderService.finishOrder(order.getClient().getId());

        return "redirect:/admin/order/"+id;
    }

    @RequestMapping(value = "/admin/client", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "v",required = false) String v) {
        model.addAttribute("allCount",clientService.getAllClients().size());
        model.addAttribute("activatedCount",clientService.getAllByActivated(true).size());
        model.addAttribute("notActivatedCount",clientService.getAllByActivatedAndEnabled(false,true).size());
        model.addAttribute("bannedCount",clientService.getAllByEnabled(false).size());
        if (v == null){
            model.addAttribute("clients", clientService.getAllClients());
        }else {
            switch (v) {
                case "all":
                    model.addAttribute("clients", clientService.getAllClients());
                    break;
                case "activated":
                    model.addAttribute("clients", clientService.getAllByActivated(true));
                    break;
                case "notActivated":
                    model.addAttribute("clients", clientService.getAllByActivatedAndEnabled(false,true));
                    break;
                case "banned":
                    model.addAttribute("clients", clientService.getAllByEnabled(false));
            }
        }
        return "admin/client/index";
    }

    @RequestMapping(value = "/admin/order",method = RequestMethod.GET)
    public String orders(Model model, @RequestParam(value = "v",required = false)String v){

        model.addAttribute("allCount",orderService.getAllOrders().size());
        model.addAttribute("paidCount",orderService.getAllPaid().size());
        model.addAttribute("notPaidCount",orderService.getAllNotPaid().size());
        model.addAttribute("endedCount",orderService.getAllByEnded(true).size());
        model.addAttribute("notEndedCount",orderService.getAllByEnded(false).size());

        if (v == null){
            model.addAttribute("orders",orderService.getAllOrders());
        }else {
            switch (v){
                case "all":
                    model.addAttribute("orders",orderService.getAllOrders());
                    break;
                case "paid":
                    model.addAttribute("orders",orderService.getAllPaid());
                    break;
                case "notPaid":
                    model.addAttribute("orders",orderService.getAllNotPaid());
                    break;
                case "ended":
                    model.addAttribute("orders",orderService.getAllByEnded(true));
                    break;
                case "notEnded":
                    model.addAttribute("orders",orderService.getAllByEnded(false));
                    break;
            }
        }

        return "admin/order/index";
    }

    @RequestMapping(value = "/admin/order/{id}",method = RequestMethod.GET)
    public String orderInfo(Model model, @PathVariable("id") int id){
        Order order = orderService.getOrderById(id);
        if (order!=null){
            model.addAttribute("order",order);
            model.addAttribute("data",order.getOrderData());
            return "admin/order/info";
        }else return "redirect:/admin/order";

    }

    @RequestMapping(value = "/admin/client/{id}/valid", method = RequestMethod.GET)
    public String clientValid(Model model, @PathVariable("id") int id){
        Client client = clientService.getById(id);
        if (client != null){
            model.addAttribute(client);
            return "admin/client/valid";
        }else return "redirect:/admin/client";
    }

    @RequestMapping(value = "/admin/client/{id}/rent", method = RequestMethod.GET)
    public String clientRent(Model model, @PathVariable("id") int id){
        Client client = clientService.getById(id);
        if (client != null){
            model.addAttribute(client);
            return "admin/client/rent";
        }else return "redirect:/admin/client";
    }


    @RequestMapping(value = "/admin/client/{id}", method = RequestMethod.GET)
    public String clientId(Model model, @PathVariable("id") int id) {
        Client client = clientService.getById(id);
        if (client != null) {
            model.addAttribute(client);
            return "admin/client/id";
        } else return "redirect:/admin/client";
    }

    @RequestMapping(value = "admin/client/edit",method = RequestMethod.POST)
    public String clientEditPost(@ModelAttribute("client") Client client,
                                 @RequestParam("birth") String birth){

        Client cl = clientService.getById(client.getId());
        cl.setMail(client.getMail());
        cl.setFirstname(client.getFirstname());
        cl.setSecondname(client.getSecondname());
        cl.setMiddlename(client.getMiddlename());
        cl.setGender(client.getGender());
        if (birth!=null) {
            try {
                Date birthday = new SimpleDateFormat("dd-MM-yyyy").parse(birth);
                cl.setBirthday(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        clientService.saveClient(cl);

        return "redirect:/admin/client/"+client.getId();
    }

    @RequestMapping(value = "/admin/client/{id}/image/{count}.png",method = RequestMethod.GET)
    public void getImageClient(@PathVariable("id") int id, @PathVariable("count") int count, HttpServletResponse response){
        try {
            Document document = clientService.getById(id).getDocument();
            File file = null;
            if (count==1){
                file = new File(document.getImageSrc1());
            }else if (count==2){
                file = new File(document.getImageSrc2());
            }else if (count==3){
                file = new File(document.getImageSrc3());
            }else if (count==4){
                file = new File(document.getImageSrc4());
            }else if (count==5){
                file = new File(document.getImageSrc5());
            }
            InputStream in = new FileInputStream(file);
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            IOUtils.copy(in, response.getOutputStream());

        } catch (IOException | EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/admin/client/{id}/activate",method = RequestMethod.POST)
    public String activateClient(@PathVariable("id") int id){
        Client client = clientService.getById(id);
        if (client.getId()==id){
            client.setActivated(true);
            clientService.saveClient(client);
        }
        return "redirect:/admin/client/"+id;
    }

    @RequestMapping(value = "/admin/client/{id}/ban",method = RequestMethod.POST)
    public String banClient(@PathVariable("id") int id){
        Client client = clientService.getById(id);
        if (client.getId()==id){
            client.setEnabled(false);
            clientService.saveClient(client);
        }
        return "redirect:/admin/client/"+id;
    }

    @RequestMapping(value = "/admin/client/{id}/unban",method = RequestMethod.POST)
    public String unbanClient(@PathVariable("id") int id){
        Client client = clientService.getById(id);
        if (client.getId()==id){
            client.setEnabled(true);
            clientService.saveClient(client);
        }
        return "redirect:/admin/client/"+id;
    }


}




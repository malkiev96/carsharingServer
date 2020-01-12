package com.carsharing.controller.admin;

import com.carsharing.model.Tariff;
import com.carsharing.service.CarService;
import com.carsharing.service.ClientService;
import com.carsharing.service.OrderService;
import com.carsharing.service.TariffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class TariffController {

    private final TariffService tariffService;
    private final CarService carService;
    private final ClientService clientService;
    private final OrderService orderService;

    public TariffController(TariffService tariffService, CarService carService, ClientService clientService, OrderService orderService) {
        this.tariffService = tariffService;
        this.carService = carService;
        this.clientService = clientService;
        this.orderService = orderService;
    }


    @ModelAttribute
    public void carCount(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("carOffline", carService.getAllByOnline(false).size());
        model.addAttribute("clientNew", clientService.getAllByActivatedAndEnabled(false, true).size());
        model.addAttribute("orderNotPaid", orderService.getAllNotPaid().size());
    }


    @RequestMapping(value = "/admin/tariff", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("tariffs", tariffService.getAllTariffs());
        return "admin/tariff/index";
    }

    @RequestMapping(value = "/admin/tariff/{id}/edit", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") int id) {
        Tariff tariff = tariffService.getById(id);
        if (tariff != null) {
            model.addAttribute("tariff", tariff);

            return "admin/tariff/edit";
        } else return "redirect:/admin/tariff";
    }


    @RequestMapping(value = "/admin/tariff/new", method = RequestMethod.GET)
    public String addTariff(Model model) {
        Tariff tariff = new Tariff();
        tariff.setEnabled(true);
        tariff.setFreeBookingMin(20);
        model.addAttribute("tariff", tariff);

        return "admin/tariff/new";
    }

    @PostMapping("/admin/tariff/new")
    public String addTariffPost(@Valid @ModelAttribute Tariff tariff,
                                @RequestParam(value = "timeStart", required = false) String timeStart,
                                @RequestParam(value = "timeEnd", required = false) String timeEnd) {

        if (!timeStart.equals("") && !timeEnd.equals("")) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                tariff.setFreeWaitingStart(new Time(dateFormat.parse(timeStart).getTime()));
                tariff.setFreeWaitingEnd(new Time(dateFormat.parse(timeEnd).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            tariffService.save(tariff);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/tariff";
    }
}

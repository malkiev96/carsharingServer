package com.carsharing.controller.admin;

import com.carsharing.model.Car;
import com.carsharing.model.Tariff;
import com.carsharing.model.Tracker;
import com.carsharing.model.TrackerData;
import com.carsharing.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CarController {

    private CarService carService;
    private TrackerService trackerService;
    private TrackerDataService trackerDataService;
    private OrderService orderService;
    private ClientService clientService;
    private TariffService tariffService;

    public CarController(OrderService orderService, CarService carService, TrackerService trackerService, TrackerDataService trackerDataService, ClientService clientService, TariffService tariffService) {
        this.orderService = orderService;
        this.carService = carService;
        this.trackerService = trackerService;
        this.trackerDataService = trackerDataService;
        this.clientService = clientService;
        this.tariffService = tariffService;
    }

    @ModelAttribute
    public void carCount(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("carOffline", carService.getAllByOnline(false).size());
        model.addAttribute("clientNew", clientService.getAllByActivatedAndEnabled(false, true).size());
        model.addAttribute("orderNotPaid", orderService.getAllNotPaid().size());
    }

    @RequestMapping(value = "/admin/car/{id}/delete", method = RequestMethod.POST)
    public String deleteCar(@PathVariable("id") int id) {
        Car car = carService.getCarById(id);
        if (car != null) {
            carService.deleteCar(car);
        }
        return "redirect:/admin/car";
    }

    @RequestMapping(value = "/admin/car", method = RequestMethod.GET)
    public String carIndex(Model model, @RequestParam(value = "view", required = false) String view) {
        model.addAttribute("allCount", carService.getAllCars().size());
        model.addAttribute("enableCount", carService.getAllByEnabled(true).size());
        model.addAttribute("disableCount", carService.getAllByEnabled(false).size());
        model.addAttribute("openedCount", carService.getAllByOpened(true).size());
        model.addAttribute("rentedCount", carService.getAllByRented(true).size());
        model.addAttribute("offlineCount", carService.getAllByOnline(false).size());
        if (view == null) {
            model.addAttribute("cars", carService.getAllCars());
            return "admin/cars/index";
        } else {
            switch (view) {
                case "all":
                    model.addAttribute("cars", carService.getAllCars());
                    break;
                case "enable":
                    model.addAttribute("cars", carService.getAllByEnabled(true));
                    break;
                case "disable":
                    model.addAttribute("cars", carService.getAllByEnabled(false));
                    break;
                case "opened":
                    model.addAttribute("cars", carService.getAllByOpened(true));
                    break;
                case "rented":
                    model.addAttribute("cars", carService.getAllByRented(true));
                    break;
                case "offline":
                    model.addAttribute("cars", carService.getAllByOnline(false));
                    break;
            }
        }

        return "admin/cars/index";
    }

    @RequestMapping(value = "/admin/car/{id}", method = RequestMethod.GET)
    public String carId(Model model, @PathVariable("id") int id) {
        Car car = carService.getCarById(id);
        if (car.getNumber() != null) {
            model.addAttribute("car", car);
            model.addAttribute("tariff", car.getTariff());
            model.addAttribute("tariffs", tariffService.getAllByEnabled(true));
            model.addAttribute("trackers", trackerService.getEmptyTrackers());
            return "admin/cars/car";

        } else return "redirect:/admin/car";
    }

    @RequestMapping(value = "/admin/car/{id}/control", method = RequestMethod.GET)
    public String carIdControl(Model model, @PathVariable("id") int id) {

        Car car = carService.getCarById(id);
        Tracker tracker = car.getTracker();
        TrackerData trackerData = trackerDataService.getLastDataByTracker(tracker);
        if (car.getNumber() != null) {
            model.addAttribute("car", car);
            model.addAttribute("tracker", tracker);
            model.addAttribute("tariff", car.getTariff());
            model.addAttribute("data", trackerData);
            model.addAttribute("trackers", trackerService.getEmptyTrackers());

            return "admin/cars/control";
        } else return "redirect:/admin/car";
    }

    @RequestMapping(value = "/admin/car/{id}/rent", method = RequestMethod.GET)
    public String carIdRent(Model model, @PathVariable("id") int id) {
        Car car = carService.getCarById(id);
        if (car.getNumber() != null) {
            model.addAttribute("car", car);
            model.addAttribute("orders", orderService.getAllByCar(car));

            return "admin/cars/rent";
        } else return "redirect:/admin/car";
    }

    @RequestMapping(value = "/admin/car/edit", method = RequestMethod.POST)
    public String editCar(@ModelAttribute(value = "car") Car car,
                          @RequestParam("tracker") int trackerId,
                          @RequestParam("tariff") int tariffId) {
        car.setTracker(trackerService.getById(trackerId));
        car.setTariff(tariffService.getById(tariffId));
        carService.saveCar(car);
        return "redirect:/admin/car/" + car.getId();
    }

    @RequestMapping(value = "/admin/car/new", method = RequestMethod.GET)
    public String newCar(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("trackers", trackerService.getEmptyTrackers());
        model.addAttribute("tariffs", tariffService.getAllByEnabled(true));

        return "admin/cars/new";
    }

    @RequestMapping(value = "/admin/car/new", method = RequestMethod.POST)
    public String newCarPost(@ModelAttribute(value = "car") Car car,
                             @RequestParam("tracker") int trackerId,
                             @RequestParam("tariff") int tariffId) {

        Tracker tracker = trackerService.getById(trackerId);
        Tariff tariff = tariffService.getById(tariffId);
        if (tracker.getCar() == null) {
            car.setTariff(tariff);
            car.setTracker(tracker);
            car.setRented(false);
            try {
                carService.saveCar(car);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/admin/car";
    }
}

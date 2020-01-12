package com.carsharing.controller.admin;

import com.carsharing.model.Tracker;
import com.carsharing.service.CarService;
import com.carsharing.service.ClientService;
import com.carsharing.service.OrderService;
import com.carsharing.service.TrackerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class TrackerController {

    private final CarService carService;
    private final ClientService clientService;
    private final OrderService orderService;
    private TrackerService trackerService;

    public TrackerController(TrackerService trackerService, CarService carService, ClientService clientService, OrderService orderService) {
        this.trackerService = trackerService;
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

    @RequestMapping(value = "/admin/tracker", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "v", required = false) String v) {
        trackerService.testAllOnline();

        model.addAttribute("allCount", trackerService.getAll().size());
        model.addAttribute("enableCount", trackerService.getAllByEnabled(true).size());
        model.addAttribute("disableCount", trackerService.getAllByEnabled(false).size());
        model.addAttribute("onlineCount", trackerService.getAllByOnline(true).size());
        model.addAttribute("offlineCount", trackerService.getAllByOnline(false).size());
        model.addAttribute("emptyCount", trackerService.getEmptyTrackers().size());

        if (v == null) {
            model.addAttribute("trackers", trackerService.getAll());

        } else {
            switch (v) {
                case "all":
                    model.addAttribute("trackers", trackerService.getAll());
                    break;
                case "enable":
                    model.addAttribute("trackers", trackerService.getAllByEnabled(true));
                    break;
                case "disable":
                    model.addAttribute("trackers", trackerService.getAllByEnabled(false));
                    break;
                case "online":
                    model.addAttribute("trackers", trackerService.getAllByOnline(true));
                    break;
                case "offline":
                    model.addAttribute("trackers", trackerService.getAllByOnline(false));
                    break;
                case "empty":
                    model.addAttribute("trackers", trackerService.getEmptyTrackers());
                    break;
            }
        }

        return "admin/tracker/index";
    }

    @RequestMapping(value = "/admin/tracker/{id}/delete", method = RequestMethod.POST)
    public String deleteTracker(@PathVariable("id") int id) {
        Tracker tracker = trackerService.getById(id);
        if (tracker != null) {
            trackerService.delete(tracker);
        }
        return "redirect:/admin/tracker";
    }

    @RequestMapping(value = "/admin/tracker/{id}", method = RequestMethod.GET)
    public String trackerId(Model model, @PathVariable("id") int id) {
        Tracker tracker = trackerService.getById(id);
        if (tracker != null) {
            model.addAttribute(tracker);
            return "admin/tracker/trackerId";
        } else return "redirect:/admin/tracker";
    }

    @RequestMapping(value = "/admin/tracker/edit", method = RequestMethod.POST)
    public String editTracker(@Valid @ModelAttribute(value = "tracker") Tracker tracker, BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            tracker.setOnline(false);
            trackerService.save(tracker);
        }
        return "redirect:/admin/tracker/" + tracker.getId();
    }

    @RequestMapping(value = "/admin/tracker/new", method = RequestMethod.GET)
    public String newTracker(Model model) {
        model.addAttribute("tracker", new Tracker());
        return "admin/tracker/new";
    }
    /*@RequestMapping(value = "/admin/tracker/new",method = RequestMethod.POST)
    public String newTrackerPost(@ModelAttribute)*/
}

package com.carsharing.controller.rest;


import com.carsharing.model.Action;
import com.carsharing.model.Car;
import com.carsharing.model.Order;
import com.carsharing.model.Payment;
import com.carsharing.model.android.AndroidCar;
import com.carsharing.model.android.AndroidOrder;
import com.carsharing.model.android.OrderPay;
import com.carsharing.model.android.PayInfo;
import com.carsharing.service.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class OrderApiController {

    private final CarService carService;
    private final ClientService clientService;
    private final OrderService orderService;
    private final OrderDataService orderDataService;
    private final TariffService tariffService;
    private final PaymentService paymentService;

    public OrderApiController(CarService carService, ClientService clientService, OrderService orderService, OrderDataService orderDataService, TariffService tariffService, PaymentService paymentService) {
        this.carService = carService;
        this.clientService = clientService;

        this.orderService = orderService;
        this.orderDataService = orderDataService;
        this.tariffService = tariffService;
        this.paymentService = paymentService;
    }


    @GetMapping("api/car/{id}")
    public AndroidCar getCarById(@PathVariable("id") int id) {
        return carService.getAndroidCarById(id);
        }

    @PostMapping("api/order/test")
    public @ResponseBody AndroidCar testBooking(@RequestParam("client_id") int client_id,
                              @RequestParam("car_number") String car_number,
                              HttpServletResponse response){
        if (orderService.testBooking(client_id,car_number)){
            response.setStatus(200);
            Car car = carService.getCarByNumber(car_number);

            return carService.getAndroidCarById(car.getId());
        }else {
            response.setStatus(400);
            return null;
        }
    }

    @PostMapping("api/order/actual")
    public @ResponseBody AndroidOrder getActual(@RequestParam("client_id") int clientId, HttpServletResponse response){
        AndroidOrder order = orderService.getActual(clientId);
        System.out.println("Инфо"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/booking")
    public @ResponseBody AndroidOrder booking(@RequestParam("client_id") int clientId,
                                              @RequestParam("car_id") int carId,
                                              HttpServletResponse response){

        AndroidOrder order = orderService.startBooking(clientId, carId);
        System.out.println("Начало бронирования"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }


    @PostMapping("api/order/rent")
    public @ResponseBody AndroidOrder orderRent(@RequestParam("client_id") int clientId,
                                           HttpServletResponse response){

        AndroidOrder order = orderService.startAction(clientId, Action.RENT);
        System.out.println("Аренда"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/wait")
    public @ResponseBody AndroidOrder orderWait(@RequestParam("client_id") int clientId,
                                           HttpServletResponse response){

        AndroidOrder order = orderService.startAction(clientId, Action.WAITING);
        System.out.println("Ожидание"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/finish")
    public @ResponseBody AndroidOrder rent(@RequestParam("client_id") int clientId,
                                           HttpServletResponse response){

        AndroidOrder order = orderService.finishOrder(clientId);
        System.out.println("Финиш"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/stopBooking")
    public @ResponseBody AndroidOrder stopBooking(@RequestParam("client_id") int clientId,
                                                  HttpServletResponse response){
        AndroidOrder order = orderService.stopBooking(clientId);
        System.out.println("Стоп"+order);
        if (order!=null){
            response.setStatus(200);
            return order;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/pay")
    public @ResponseBody OrderPay getPay(@RequestParam("client_id") int clientId,
                                         HttpServletResponse response){
        OrderPay orderPay = orderService.clientGetPay(clientId);

        System.out.println("PAY"+orderPay);
        if (orderPay!=null){
            response.setStatus(200);
            return orderPay;
        }
        response.setStatus(400);
        return null;
    }

    @PostMapping("api/order/makePay")
    public @ResponseBody
    String makePay(@RequestBody PayInfo payInfo,HttpServletResponse response){
        if (payInfo!=null){

            System.out.println("PAYINFO"+payInfo);
            Order order = orderService.getOrderById(payInfo.getOrderId());

            if (order!=null) {

                Payment payment = new Payment();
                payment.setDate(new Date());
                payment.setOrder(order);
                payment.setToken(payInfo.getToken());
                payment.setPrice(payInfo.getPrice());

                paymentService.save(payment);

                response.setStatus(HttpServletResponse.SC_OK);
                return "";
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "";
    }




}

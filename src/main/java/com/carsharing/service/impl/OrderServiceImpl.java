package com.carsharing.service.impl;

import com.carsharing.model.*;
import com.carsharing.model.android.AndroidOrder;
import com.carsharing.model.android.OrderPay;
import com.carsharing.repository.OrderRepository;
import com.carsharing.service.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class OrderServiceImpl implements OrderService {

    private final ClientService clientService;
    private final PaymentService paymentService;
    private OrderRepository orderRepository;
    private OrderDataService orderDataService;
    private CarService carService;


    public OrderServiceImpl(OrderDataService orderDataService, OrderRepository orderRepository, CarService carService, ClientService clientService, TariffService tariffService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderDataService = orderDataService;
        this.carService = carService;
        this.clientService = clientService;
        this.paymentService = paymentService;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(int id) {
        return setDataToOrder(orderRepository.getOne(id));
    }

    public List<Order> getAllOrders() {
        return setDataToList(orderRepository.findAll());
    }

    public List<Order> getAllByEnded(boolean ended) {
        return setDataToList(orderRepository.findAllByEnded(ended));
    }

    public List<Order> getAllByCar(Car car) {
        return setDataToList(orderRepository.findAllByCar(car));
    }

    public List<Order> getAllByClient(Client client) {
        return setDataToList(orderRepository.findAllByClient(client));
    }

    public boolean testBooking(int clientId, String carNumber) {
        Car car = carService.getCarByNumber(carNumber);
        Client client = clientService.getById(clientId);

        if (client != null && car != null) {
            Order order = orderRepository.findByPaymentIsNullAndClientAndEndedIsTrue(client);
            if (order != null) return false;
            if (!car.getRented() && car.getEnabled() && car.getTracker().getOnline()) {
                return client.getActivated() && client.getEnabled();
            }
        }

        return false;
    }

    public AndroidOrder startBooking(int clientId, int carId) {

        if (testBooking(clientId, carId)) {

            Order order = new Order();

            Car car = carService.getCarById(carId);
            Client client = clientService.getById(clientId);

            order.setCar(car);
            order.setClient(client);
            order.setStart(new Date());
            order.setPrice(0f);
            order.setBlocked(false);
            order.setEnded(false);
            saveOrder(order);

            car.setRented(true);
            carService.saveCar(car);

            order = getCurrentOrderByClientID(clientId);

            OrderData orderData = new OrderData();
            orderData.setAction(Action.BOOKING);
            orderData.setStart(order.getStart());
            orderData.setEnded(false);
            orderData.setOrder(order);

            orderDataService.save(orderData);

            AndroidOrder androidOrder = new AndroidOrder();
            androidOrder.setPrice(0);
            androidOrder.setTime("0");
            androidOrder.setCurrentAction(Action.BOOKING);
            androidOrder.setClient(client);

            return androidOrder;

        }
        return null;
    }

    @Override
    public AndroidOrder stopBooking(int clientId) {
        Order order = getCurrentOrderByClientID(clientId);

        if (!order.getEnded() && !order.getBlocked()) {

            OrderData dataBooking = orderDataService.getByOrderAndEnded(order, false);
            dataBooking.setEnded(true);
            dataBooking.setEnd(new Date());
            dataBooking.setPrice(getPrice(dataBooking, order.getCar().getTariff()));

            Car car = order.getCar();
            car.setRented(false);
            carService.saveCar(car);

            orderDataService.save(dataBooking);

            order.setEnded(true);
            order.setEnd(dataBooking.getEnd());
            order.setBlocked(false);

            saveOrder(order);

            if (getPrice(order) == 0) {
                Payment payment = new Payment();
                payment.setPrice(0f);
                payment.setToken("NULL PRICE");
                payment.setOrder(order);
                payment.setDate(order.getEnd());
                paymentService.save(payment);

            }

            AndroidOrder androidOrder = new AndroidOrder();
            androidOrder.setClient(order.getClient());
            androidOrder.setCurrentAction(Action.FINISH);
            androidOrder.setTime(getTimeByStartEnd(dataBooking.getStart(), dataBooking.getEnd()));
            androidOrder.setPrice(getPrice(order));

            return androidOrder;
        }

        return null;
    }

    @Override
    public AndroidOrder finishOrder(int clientId) {
        Order order = getCurrentOrderByClientID(clientId);
        if (!order.getEnded() && !order.getBlocked()) {

            OrderData data = orderDataService.getByOrderAndEnded(order, false);
            data.setPrice(getPrice(data, order.getCar().getTariff()));
            data.setEnd(new Date());
            data.setEnded(true);
            orderDataService.save(data);

            Car car = order.getCar();
            car.setRented(false);
            carService.saveCar(car);

            AndroidOrder androidOrder = new AndroidOrder();
            androidOrder.setClient(order.getClient());
            androidOrder.setCurrentAction(Action.FINISH);
            androidOrder.setPrice(getPrice(order));
            androidOrder.setTime(getTime(order));


            order.setEnded(true);
            order.setEnd(data.getEnd());
            order.setBlocked(false);

            saveOrder(order);

            return androidOrder;
        }

        return null;
    }

    @Override
    public AndroidOrder startAction(int clientId, int act) {

        Order order = getCurrentOrderByClientID(clientId);

        if (!order.getEnded() && !order.getBlocked()) {

            //Завершаем текущий статус
            OrderData data = orderDataService.getByOrderAndEnded(order, false);
            if (data.getAction() != act) {
                data.setEnded(true);
                data.setEnd(new Date());
                data.setPrice(getPrice(data, order.getCar().getTariff()));
                orderDataService.save(data);

                //Начинаем аренду
                OrderData dataRent = new OrderData();
                dataRent.setStart(data.getEnd());
                dataRent.setOrder(order);
                dataRent.setEnded(false);
                if (Action.RENT == act) {
                    carService.openCar(order.getCar());
                    dataRent.setAction(Action.RENT);
                } else if (Action.WAITING == act) {
                    carService.closeCar(order.getCar());
                    dataRent.setAction(Action.WAITING);
                }
                dataRent.setPrice(0f);
                orderDataService.save(dataRent);

                AndroidOrder androidOrder = new AndroidOrder();
                androidOrder.setClient(order.getClient());
                androidOrder.setCurrentAction(dataRent.getAction());
                androidOrder.setTime(getTime(order));
                androidOrder.setPrice(getPrice(order));

                return androidOrder;
            }
        }
        return null;
    }

    public AndroidOrder getActual(int clientId) {
        Order order = getCurrentOrderByClientID(clientId);

        if (order != null) {
            OrderData currentData = orderDataService.getByOrderAndEnded(order, false);

            AndroidOrder androidOrder = new AndroidOrder();
            androidOrder.setClient(order.getClient());
            androidOrder.setCurrentAction(currentData.getAction());
            androidOrder.setTime(getTime(order));
            androidOrder.setPrice(getPrice(order));
            androidOrder.setAndroidCar(carService.getAndroidCarById(order.getCar().getId()));

            return androidOrder;

        } else return null;
    }

    @Override
    public Order getCurrentOrderByClientID(int clientId) {
        Client client = clientService.getById(clientId);
        Order order = orderRepository.findByClientAndEnded(client, false);
        if (order.getOrderData() != null) {
            return setDataToOrder(order);
        } else return order;
    }

    @Override
    public boolean testBooking(int clientId, int carId) {
        Car car = carService.getCarById(carId);
        Client client = clientService.getById(clientId);
        if (client != null && car != null) {
            if (!car.getRented() && car.getEnabled() && car.getTracker().getOnline()) {
                return client.getActivated() && client.getEnabled();
            }
        }
        return false;
    }

    private List<Order> setDataToList(List<Order> orders) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            order = setDataToOrder(order);
            list.add(order);
        }
        return list;
    }

    private Order setDataToOrder(Order order) {
        order.setBookingTime(getTime(order, Action.BOOKING));
        order.setRentTime(getTime(order, Action.RENT));
        order.setWaitingTime(getTime(order, Action.WAITING));
        order.setPrice(getPrice(order));
        return order;
    }

    private String getTime(Order order) {
        if (order.getEnded()) {
            return getTimeByStartEnd(order.getStart(), order.getEnd());
        } else {
            return getTimeByStartEnd(order.getStart(), new Date());
        }
    }

    private String getTime(Order order, int act) {
        try {
            String strDate;
            List<OrderData> orderDataList = orderDataService.getAllByOrder(order);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+00"));

            Long time = 0L;
            for (OrderData data : orderDataList) {
                if (data.getAction() == act) {
                    time += sdf.parse(getTimeByStartEnd(data.getStart(), data.getEnd())).getTime();
                }
            }
            strDate = sdf.format(new Date(time));
            return strDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTimeByStartEnd(Date start, Date end) {
        String strDate;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+00"));
        if (end != null) {
            strDate = sdf.format(new Date(end.getTime() - start.getTime()));
        } else {
            strDate = sdf.format(new Date(System.currentTimeMillis() - start.getTime()));
        }
        return strDate;
    }


    private float getPrice(Order order) {
        List<OrderData> orderDataList = order.getOrderData();
        Tariff tariff = order.getCar().getTariff();
        Float price = 0f;
        for (OrderData data : orderDataList) {
            price += getPrice(data, tariff);
        }
        return price;
    }

    private float getPrice(OrderData data, Tariff tariff) {
        try {
            if (data.getEnded()) {
                return data.getPrice();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+00"));
                Date currentDate = new Date();
                Date date = sdf.parse(getTimeByStartEnd(data.getStart(), currentDate));
                if (data.getAction() == Action.BOOKING) {

                    int lim = tariff.getFreeBookingMin() * 60 * 1000;
                    if ((date.getTime() - lim) > 0) {
                        //Беспл ожидание прошло
                        long delta = (date.getTime() - lim) / 60000;
                        float pr = tariff.getPayBooking() * delta;
                        data.setPrice(pr);
                        orderDataService.save(data);
                        return pr;
                    } else {
                        data.setPrice(0f);
                        orderDataService.save(data);
                        return 0;
                    }

                } else if (data.getAction() == Action.RENT) {

                    Float pr = calcPrice(tariff.getPayRent(), date);
                    data.setPrice(pr);
                    orderDataService.save(data);
                    return pr;

                } else if (data.getAction() == Action.WAITING) {

                    Float pr = calcPrice(tariff.getPayWaiting(), date);
                    data.setPrice(pr);
                    orderDataService.save(data);
                    return pr;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0f;
    }

    @Override
    public List<Order> getAllPaid() {
        return orderRepository.findAllByPaymentIsNotNull();
    }

    @Override
    public List<Order> getAllNotPaid() {
        return orderRepository.findAllByPaymentIsNullAndEnded(true);
    }

    @Override
    public OrderPay clientGetPay(int clientId) {
        OrderPay orderPay = new OrderPay();
        Order order = orderRepository.findByPaymentIsNullAndClientAndEndedIsTrue(clientService.getById(clientId));
        if (order != null) {
            Car car = order.getCar();
            Client client = order.getClient();
            orderPay.setCarId(car.getId());
            orderPay.setCarName(car.toString());
            orderPay.setCarNumber(car.getNumber());
            orderPay.setClientId(client.getId());
            orderPay.setOrderId(order.getId());
            order = setDataToOrder(order);
            orderPay.setPrice(order.getPrice());
            orderPay.setTime(getTime(order));
            return orderPay;
        }
        return null;
    }

    private Float calcPrice(Float price, Date date) {
        float f = ((float) date.getTime()) / (float) 60000;
        f *= price;
        f = Math.round(f);
        return f;
    }


}

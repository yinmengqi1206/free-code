package com.ymq.stripe.controller;

import com.google.gson.Gson;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.ymq.util.URLUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yinmengqi
 * @version 1.0
 * @date 2022/11/18 18:59
 */
@Controller
@RequestMapping("/stripe")
public class StripeController {

    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String pay(HttpServletRequest request) throws StripeException {
        String cancelUrl = URLUtils.getBaseURl(request) + "/stripe/cancel";
        String successUrl = URLUtils.getBaseURl(request) + "/stripe/success";
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl(cancelUrl)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount(2000L)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("T-shirt")
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();
        Session session = Session.create(params);
        return "redirect:" + session.getUrl();
    }

    @RequestMapping(method = RequestMethod.GET, value = "index")
    public String stripe() {
        return "stripe";
    }

    @RequestMapping(method = RequestMethod.GET, value = "cancel")
    public String cancelPay() {
        return "cancel";
    }

    @RequestMapping(method = RequestMethod.GET, value = "success")
    public String successPay() {
        return "success";
    }
}

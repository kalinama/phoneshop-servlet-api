package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceShift;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ProductDemoDataServletContextListener implements ServletContextListener {

    private ProductDao productDao = ArrayListProductDao.getInstance(); //initialization there need to test this class
                                                                    // => can't mocking static method without PowerMockito
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        boolean insertDemoData = Boolean.valueOf(servletContextEvent.getServletContext().getInitParameter("insertDemoData"));

        if(insertDemoData) {
          // productDao = ArrayListProductDao.getInstance();
            for (Product product: getSampleProducts()) {
                product.setPriceHistory(getSamplePriceHistory(product));
                productDao.save(product);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private List<PriceShift> getSamplePriceHistory(Product product)
    {
        List<PriceShift> samplePriceHistory = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        samplePriceHistory.add(new PriceShift(new BigDecimal(100), usd, LocalDate.of(2010, 4,12) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(130), usd, LocalDate.of(2010, 8,23) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(133), usd, LocalDate.of(2010, 11,2) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(155), usd, LocalDate.of(2010, 4,11) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(123), usd, LocalDate.of(2011, 8,12) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(150), usd, LocalDate.of(2015, 3,28) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(155), usd, LocalDate.of(2016, 10,8) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(158), usd, LocalDate.of(2018, 5,27) ));
        samplePriceHistory.add(new PriceShift(new BigDecimal(166), usd, LocalDate.of(2019, 2,4) ));
        samplePriceHistory.add(new PriceShift(product.getPrice(), product.getCurrency(), LocalDate.now() ));
        return samplePriceHistory;
    }

    private List<Product> getSampleProducts(){
        List<Product> sampleProducts = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        sampleProducts.add(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        sampleProducts.add(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        sampleProducts.add(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        sampleProducts.add(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        sampleProducts.add(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        sampleProducts.add(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        sampleProducts.add(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        sampleProducts.add(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        sampleProducts.add(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        sampleProducts.add(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        sampleProducts.add(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        sampleProducts.add(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        sampleProducts.add(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
        return sampleProducts;
    }
}

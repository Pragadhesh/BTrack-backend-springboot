package com.example.btrack.service;

import com.example.btrack.dto.Product;
import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    UserService userService;

    @Autowired
    ProductsRepository productsRepository;

    public ResponseEntity<Object> getProductsByCategory(String module, String category) {
        System.out.println("Entered getProductsByCategory");
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream stream = getClass().getResourceAsStream("/data/" + module + "/" + category + ".json");
            if (stream == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Object json = mapper.readValue(stream, Object.class);
            return ResponseEntity.ok(json);
        } catch (IOException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Object> addProduct(String idToken,Product item)
    {
        System.out.println("Entered addProduct");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null)
            {
                ObjectMapper mapper = new ObjectMapper();
                InputStream stream = getClass().getResourceAsStream("/data/" + item.getModule() + "/" + item.getCategory() + ".json");
                List<Product> productList = mapper.readValue(stream, mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

                // Use streams to compare the input parameters with the items in the list
                List<Product> matchingProducts = productList.stream()
                        .filter(product -> product.getName().equals(item.getName()) && product.getDescription().equals(item.getDescription()) &&
                                product.getImage_url().equals(item.getImage_url()) )
                        .collect(Collectors.toList());

                if (matchingProducts.isEmpty())
                {
                    return new ResponseEntity<>("No product matched", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                else
                {
                    Products products = new Products(
                            actualUser,item.getName(),item.getDescription(),item.getImage_url(),
                            item.getModule(),item.getCategory(),item.getHealth(),matchingProducts.get(0).getDamage(),
                            item.getUsage(),item.getDays());
                    Products pd = productsRepository.save(products);
                    return new ResponseEntity<>(pd,HttpStatus.OK);
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateProduct(String idToken,Product item)
    {
        System.out.println("Entered updateProduct");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Products> pr = productsRepository.findById(item.getId());
                Products products = pr.orElse(null);
                if (products == null || products.getUser().getUsername() != actualUser.getUsername()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                else
                {
                    products.setUsage(item.getUsage());
                    products.setHealth(item.getHealth());
                    products.setDays(item.getDays());
                    Products pd = productsRepository.save(products);
                    return new ResponseEntity<>(pd,HttpStatus.OK);
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<Object> deleteProduct(String idToken,Product item)
    {
        System.out.println("Entered deleteProduct");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                Optional<Products> pr = productsRepository.findById(item.getId());
                Products products = pr.orElse(null);
                if (products == null || products.getUser().getUsername() != actualUser.getUsername()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
                else
                {
                    productsRepository.deleteById(products.getId());
                    return new ResponseEntity<>("Product deleted successfully",HttpStatus.OK);
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<Object> getProduct(String idToken,String module)
    {
        System.out.println("Entered getProduct");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
               List<Products> pr = productsRepository.findByUserAndModule(actualUser,module);
                List<Map<String, Object>> result = pr.stream()
                        .collect(Collectors.groupingBy(x -> x.getCategory()))
                        .entrySet().stream()
                        .map(entry -> {
                            Map<String, Object> groupedData = new HashMap<>();
                            groupedData.put("category", entry.getKey());
                            groupedData.put("products", entry.getValue());
                            return groupedData;
                        })
                        .collect(Collectors.toList());
               return new ResponseEntity<>(result,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<Object> getAlerts(String idToken)
    {
        System.out.println("Entered getAlerts");
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Products> pr = productsRepository.findByUserAndHealthLessThanOrderByHealthAsc(actualUser,21);
                return new ResponseEntity<>(pr,HttpStatus.OK);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<Products> getAlertsforUser(Userdetails user)
    {
        System.out.println("Entered getAlertsforUser");
        try {
                List<Products> pr = productsRepository.findByUserAndHealthLessThanOrderByHealthAsc(user,21);
                return pr;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public List<Products> getProductsToUseToday(List<Products> products) {
        System.out.println("Entered getProductsToUseToday");
        // Find products that need to be used today
        List<Products> productsToUseToday = new ArrayList<Products>();
        for (int i = 0; i < products.size(); i++) {
            Products product = products.get(i);
            String frequency = product.getUsage();
            if (frequency.equals("daily")) {
                // Product needs to be used daily
                productsToUseToday.add(product);
            } else if (frequency.equals("weekly")) {
                // Product needs to be used within a specific number of days in the current week
                int numDays = product.getDays();
                List<LocalDate> usageDays = getWeeklyUsageDays(numDays);
                if (usageDays.contains(LocalDate.now())) {
                    // Product needs to be used today
                    productsToUseToday.add(product);
                }
            } else if (frequency.equals("monthly")) {
                // Product needs to be used within a specific number of days in the current month
                int numDays = product.getDays();
                List<LocalDate> usageDays = getMonthlyUsageDays(numDays);
                if (usageDays.contains(LocalDate.now())) {
                    // Product needs to be used today
                    productsToUseToday.add(product);
                }
            }
        }
        return productsToUseToday;
    }

    // Given a number of days in a week and the current date, return the days of the week on which the product should be used
    private List<LocalDate> getWeeklyUsageDays(int numDays) {
        List<LocalDate> usageDays = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        for (int i = 0; i < 7; i++) {
            if (i % (7 / numDays) == 0 && i < numDays * (7 / numDays)) {
                usageDays.add(startOfWeek.plusDays(i));
            }
        }
        return usageDays;
    }


    // Given a number of days in a month and the current date, return the days of the month on which the product should be used
    private List<LocalDate> getMonthlyUsageDays(int numDays) {
        List<LocalDate> usageDays = new ArrayList<LocalDate>();
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        int daysPerMonth = firstDayOfMonth.getMonth().maxLength();
        int daysBetweenUsages = daysPerMonth / numDays;
        int daysToAdd = 0;
        for (int i = 0; i < numDays; i++) {
            LocalDate usageDay = firstDayOfMonth.plusDays(daysToAdd);
            usageDays.add(usageDay);
            daysToAdd += daysBetweenUsages;
        }
        return usageDays;
    }


    public static List<Products> filterProducts(List<Products> productList) {
        // Step 1: Create a Map to store the products based on their categories.
        Map<String, List<Products>> categoryMap = new HashMap<>();
        for (Products product : productList) {
            List<Products> products = categoryMap.getOrDefault(product.getCategory(), new ArrayList<>());
            products.add(product);
            categoryMap.put(product.getCategory(), products);
        }

        // Step 2: Iterate through the categories and sort the products based on their usage type.
        List<Products> filteredList = new ArrayList<>();
        for (Map.Entry<String, List<Products>> entry : categoryMap.entrySet()) {
            List<Products> products = entry.getValue();
            if (products.size() == 1) {
                filteredList.add(products.get(0));
            } else {
                products.sort(new UsageTypeComparator());
                filteredList.add(products.get(0));
            }
        }
        return filteredList;
    }

    private static class UsageTypeComparator implements Comparator<Products> {
        @Override
        public int compare(Products p1, Products p2) {
            if (p1.getUsage().equals(p2.getUsage())) {
                // If the usage type is same, select a random product.
                return new Random().nextInt(2) == 0 ? -1 : 1;
            } else if (p1.getUsage().equals("monthly")) {
                return -1;
            } else if (p2.getUsage().equals("monthly")) {
                return 1;
            } else if (p1.getUsage().equals("weekly")) {
                return -1;
            } else if (p2.getUsage().equals("weekly")) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public List<Products> sortProductsByModule(List<Products> products) {
        Collections.sort(products, new Comparator<Products>() {
            @Override
            public int compare(Products p1, Products p2) {
                if (p1.getModule().equals("skincare") && !p2.getModule().equals("skincare")) {
                    return -1;  // p1 should come before p2
                } else if (p2.getModule().equals("skincare") && !p1.getModule().equals("skincare")) {
                    return 1;   // p2 should come before p1
                } else if (p1.getModule().equals("makeup") && !p2.getModule().equals("makeup")) {
                    return -1;  // p1 should come before p2
                } else if (p2.getModule().equals("makeup") && !p1.getModule().equals("makeup")) {
                    return 1;   // p2 should come before p1
                } else {
                    return 0;   // no change in order
                }
            }
        });
        return products;
    }


    public ResponseEntity<Object> getDailyRecommendation(String idToken)
    {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null) {
                List<Products> products = productsRepository.findByUserAndHealthGreaterThan(actualUser, 10);
                List<Products> result = getProductsToUseToday(products);
                List<Products> finalresult = filterProducts(result);
                List<Products> sortedresult = sortProductsByModule(finalresult);

                return new ResponseEntity<>(sortedresult, HttpStatus.OK);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public List<Products> getDailyRoutine(Userdetails actualUser)
    {
        try {
            if (actualUser != null) {
                List<Products> products = productsRepository.findByUserAndHealthGreaterThan(actualUser, 10);
                List<Products> result = getProductsToUseToday(products);
                List<Products> finalresult = filterProducts(result);
                List<Products> sortedresult = sortProductsByModule(finalresult);

                return sortedresult;
            }
            else {
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }





}

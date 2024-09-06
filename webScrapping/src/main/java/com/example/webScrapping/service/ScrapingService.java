package com.example.webScrapping.service;

import com.example.webScrapping.entity.Product;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScrapingService {

    public Product scrapeProductData(String productCode) throws Exception {
        // Creeaza un client HTTP care gestionează cookies (pentru sesiune)
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        // Configurare pentru login POST request
        HttpPost loginPost = new HttpPost("https://online.autobrand.ro/csp/berta/portal/index.csp?SPR=RO");
        List<NameValuePair> loginData = new ArrayList<>();
        loginData.add(new BasicNameValuePair("username", "0009999!2"));  // Inlocuieste cu username-ul
        loginData.add(new BasicNameValuePair("password", "12345678"));  // Inlocuieste cu parola
        loginPost.setEntity(new UrlEncodedFormEntity(loginData));

        // Trimite cererea POST pentru autentificare
        try (CloseableHttpResponse loginResponse = httpClient.execute(loginPost)) {
            if (loginResponse.getCode() == 200) {
                System.out.println("Autentificare reușită.");
            } else {
                throw new RuntimeException("Eroare la autentificare: " + loginResponse.getCode());
            }
        }

        // Transforma lista de cookie-uri intr-o harta pentru a putea fi utilizata de Jsoup
        Map<String, String> cookiesMap = new HashMap<>();
        cookieStore.getCookies().forEach(cookie -> cookiesMap.put(cookie.getName(), cookie.getValue()));

        // Dupa autentificare, accesează pagina produsului protejata de login
        String url = "https://online.autobrand.ro/csp/berta/portal/Artikel.csp?suche=" + productCode.trim().replace(" ", "+");

        // Folosește Jsoup pentru a accesa pagina produsului protejata, adaugand cookie-urile de sesiune
        Connection.Response response = Jsoup.connect(url)
                .cookies(cookiesMap)
                .method(Connection.Method.GET)
                .execute();

        Document doc = response.parse();  // Parsează documentul primit

        // Extrage numele produsului
        Element productNameElement = doc.select(".properties-value").first();
        String productName = productNameElement != null ? productNameElement.text() : "Nume necunoscut";

        // Extrage pretul
        Element priceElement = doc.select(".numeric.preis15").first();
        String price = priceElement != null ? priceElement.text() : "Pret necunoscut";

        // Extrage disponibilitatea
        Element availabilityElement = doc.select(".properties-value a").first();
        String availability = availabilityElement != null ? availabilityElement.attr("title") : "Disponibilitate necunoscuta";

        // Extrage producatorul
        Element manufacturerElement = doc.select("tr:has(td.properties-desc:contains(Producator)) td.properties-value").first();
        String manufacturer = manufacturerElement != null ? manufacturerElement.text() : "Producator necunoscut";

        // Creeaza si returneaza obiectul Product cu datele extrase
        return new Product(productName, price, availability, manufacturer);
    }
}

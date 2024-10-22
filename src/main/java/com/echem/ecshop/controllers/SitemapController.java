package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.ProductDTO;
import com.echem.ecshop.service.product.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

@RestController
public class SitemapController {

    private final ProductService productService;

    public SitemapController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public ResponseEntity<String> getSitemap() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Створюємо кореневий елемент <urlset>
            Element urlSet = doc.createElement("urlset");
            urlSet.setAttribute("xmlns", "https://www.sitemaps.org/schemas/sitemap/0.9");
            doc.appendChild(urlSet);

            // Додаємо сторінки сайту
            addUrl(doc, urlSet, "https://e-chem.com.ua/", LocalDate.now().toString(), "monthly", "1.0");
            addUrl(doc, urlSet, "https://e-chem.com.ua/products", LocalDate.now().toString(), "weekly", "0.8");
            addUrl(doc, urlSet, "https://e-chem.com.ua/contact", LocalDate.now().toString(), "yearly", "0.5");

            // Отримуємо продукти з бази даних і додаємо до sitemap
            List<ProductDTO> products = productService.getAllAvailableProductDTOs();
            for (ProductDTO product : products) {
                addUrl(doc, urlSet, "https://e-chem.com.ua/products/" + product.getId(),
                        LocalDate.now().toString(), "weekly", "0.8");
            }

            // Трансформуємо документ у String
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return ResponseEntity.ok(writer.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private void addUrl(Document doc, Element urlset, String loc, String lastmod, String changefreq, String priority) {
        Element url = doc.createElement("url");

        Element locElement = doc.createElement("loc");
        locElement.setTextContent(loc);
        url.appendChild(locElement);

        Element lastmodElement = doc.createElement("lastmod");
        lastmodElement.setTextContent(lastmod);
        url.appendChild(lastmodElement);

        Element changefreqElement = doc.createElement("changefreq");
        changefreqElement.setTextContent(changefreq);
        url.appendChild(changefreqElement);

        Element priorityElement = doc.createElement("priority");
        priorityElement.setTextContent(priority);
        url.appendChild(priorityElement);

        urlset.appendChild(url);
    }
}

package com.imdbcrawler.services;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerService {

    private static final String IMDB_URL = "https://www.imdb.com";
    private static final String MOVIE_LIST_URL = "/chart/bottom?sort=rk,asc&mode=simple&page=1";
    private static final String REVIEW_PAGE_URL = "reviews?sort=helpfulnessScore&dir=desc&ratingFilter=5";

    private String[] review = new String[5];

    private Integer count = 0;

    public Document accessUrlWithHeader(String url, String headerName, String headerValue) throws IOException {
        return Jsoup.connect(url).header(headerName, headerValue).get();
    }

    public Document accessUrlWithoutHeader(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public void getData() throws IOException {

        Document movieListConnector;
        Document reviewPageConnector;

        movieListConnector = accessUrlWithHeader(IMDB_URL + MOVIE_LIST_URL, "Accept-Language", "en");

        for (final Element row : movieListConnector.select("table.chart.full-width tr")) {

            if (!row.select(".titleColumn a").text().equals("") && !row.select(".imdbRating").text().equals("")) {

                final Elements filme = row.select(".titleColumn a");
                final Double nota = Double.parseDouble(row.select(".imdbRating").text());
                final String diretor = filme.attr("title");
                final String tituloUrl = filme.attr("href").substring(0, 17);

                reviewPageConnector = accessUrlWithoutHeader(IMDB_URL + tituloUrl + REVIEW_PAGE_URL);

                final Elements reviews = reviewPageConnector.select("div.lister-item-content");

                for (Element rev : reviews) {
                    review[0] = rev.select("span.rating-other-user-rating").text();
                    review[1] = rev.select("a.title").text();
                    review[2] = rev.select("span.display-name-link").text();
                    review[3] = rev.select("span.review-date").text();
                    review[4] = rev.select("div.text.show-more__control").text();
                    if (count.equals(1))
                        break;
                }

                StringBuilder builder = new StringBuilder();
                builder.append("Filme: ").append(filme.text()).append("\nNota: ").append(nota).append("\nDiretor")
                        .append(diretor.replace(" (dir.), ", "\nElenco Principal: ")).append("\nReview Positivo: ")
                        .append("\nNota: ").append(review[0]).append("\nTítulo: ").append(review[1])
                        .append("\nUsuário: ").append(review[2]).append("\nData: ").append(review[3])
                        .append("\nAnálise: ").append(review[4]).append("\n------------------------------");

                System.out.println(builder.toString());
            }

            if (count.equals(10))
                break;

            count++;
        }
    }
}
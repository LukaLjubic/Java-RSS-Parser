/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.rss;

import hr.algebra.dal.models.Movie;
import hr.algebra.dal.models.Person;
import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author ljubo
 */
public class MovieParser {
    
    private static final String RSS_URL = "https://www.blitz-cinestar-bh.ba/rss.aspx?id=2682";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";
    private static final String DEL = ", ";

    private MovieParser() {
    }

    public static List<Movie> parse() throws IOException, XMLStreamException {
        List<Movie> movies = new ArrayList<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        try (InputStream is = con.getInputStream()) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            Movie movie = null;
            StartElement startElement = null;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();                     
                        tagType = TagType.from(qName);
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            movie = new Movie();
                            movies.add(movie);   
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (tagType.isPresent()) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();                        

                            switch (tagType.get()) {
                                case TITLE:   
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                    break;

                                case DESCRIPTION:  
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDescription(data);
                                    }
                                    break;
                                case DIRECTORS:  
                                    if (movie != null && !data.isEmpty()) {
                                        String[] directors = data.split(DEL);
                                        List<Person> directorsList = new ArrayList<>();
                                        for (String d : directors) {
                                            try {
                                                directorsList.add(new Person(d));
                                            } catch (Exception ex) {
                                            }
                                        }
                                        movie.setDirectors(directorsList);
                                    }
                                    break;
                                case ACTORS:  
                                    if (movie != null && !data.isEmpty()) {
                                        String[] allActors = data.split(DEL);
                                        List<Person> actorsList = new ArrayList<>();
                                        for (String actor : allActors) {
                                            try {
                                                actorsList.add(new Person(actor));
                                            } catch (Exception ex) {

                                            }
                                        }
                                        movie.setActors(actorsList);
                                    }
                                    break;

                                case DURATION:  
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDuration(data);
                                    }
                                    break;
                                case PHOTO:  
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setPhotoPath(data);
                                    }
                                    break;

                            }
                        }
                        break;
                   
                }

            }
        }
        return movies;

    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        DESCRIPTION("description"),
        DIRECTORS("redatelj"),
        ACTORS("glumci"),
        DURATION("trajanje"),
        PHOTO("plakat");
        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

}

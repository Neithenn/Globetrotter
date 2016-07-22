package com.globettroter.ezequiel.globetrotter;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ezequiel on 19/03/2016.
 */
public class ParserXml {

    // Namespace general. null si no existe
    private static final String ns = null;

    // Constantes del archivo Xml
    private static final String ETIQUETA_GEONAMES = "geonames";

    private static final String ETIQUETA_GEONAME = "geoname";

    private static final String ETIQUETA_TOPONYMNAME = "toponymName";
    private static final String ETIQUETA_NAME = "name";
    private static final String ETIQUETA_LAT = "lat";
    private static final String ETIQUETA_LNG = "lng";
    private static final String ETIQUETA_GEONAMEID = "geonameId";
    private static final String ETIQUETA_COUNTRYCODE = "countryCode";
    private static final String ETIQUETA_FCL = "fcl";
    private static final String ETIQUETA_FCODE = "fcode";
    /**
     * Parsea un flujo XML a una lista de objetos {@link Geoname}
     *
     * @param in flujo
     * @return Lista de hoteles
     * @throws XmlPullParserException
     * @throws IOException
     */
    public List<Geoname> parsear(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(in, null);
            parser.nextTag();
            return leerGeonames(parser);
        } finally {
            in.close();
        }
    }
    /**
     * Convierte una serie de etiquetas <hotel> en una lista
     *
     * @param parser
     * @return lista de hoteles
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<Geoname> leerGeonames(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Geoname> listaGeonames = new ArrayList<Geoname>();

        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_GEONAMES);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            // Buscar etiqueta <hotel>
            if (nombreEtiqueta.equals(ETIQUETA_GEONAME)) {
                listaGeonames.add(leerGeoname(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return listaGeonames;
    }
    /**
     * Convierte una etiqueta <hotel> en un objero Hotel
     *
     * @param parser parser XML
     * @return nuevo objeto Hotel
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Geoname leerGeoname(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_GEONAME);
        String toponymName = null;
        String name = null;
        String lat = null;
        String lng = null;
        String geonameId = null;
        String countryCode = null;
        String fcl = null;
        String fcode = null;
        HashMap<String, String> valoracion = new HashMap<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name_tag = parser.getName();

            switch (name_tag) {
                case ETIQUETA_TOPONYMNAME:
                    toponymName = leerToponymName(parser);
                    break;
                case ETIQUETA_NAME:
                    name = leerName(parser);
                    break;
                case ETIQUETA_LAT:
                    lat = leerLat(parser);
                    break;
                case ETIQUETA_LNG:
                    lng = leerLng(parser);
                    break;
                case ETIQUETA_GEONAMEID:
                    geonameId = leerGeonameId(parser);
                    break;
                case ETIQUETA_COUNTRYCODE:
                    countryCode = leerCountryCode(parser);
                    break;
                case ETIQUETA_FCL:
                    fcl  = leerFcl(parser);
                    break;
                case ETIQUETA_FCODE:
                    fcode = leerFcode(parser);
                    break;
                default:
                    saltarEtiqueta(parser);
                    break;
            }
        }
        return new Geoname(toponymName,
                name,
                lat,
                lng,
                geonameId,
                countryCode,
                fcl,
                fcode);

    }

    // Procesa la etiqueta <toponymName> de los hoteles
    private String leerToponymName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TOPONYMNAME);
        String toponymName = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TOPONYMNAME);
        return toponymName;
    }

    // Procesa las etiqueta <nombre> de los hoteles
    private String leerName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NAME);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NAME);
        return nombre;
    }

    // Procesa la etiqueta <precio> de los hoteles
    private String leerLat(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_LAT);
        String lat = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_LAT);
        return lat;
    }


    // Procesa la etiqueta <precio> de los hoteles
    private String leerLng(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_LNG);
        String lng = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_LNG);
        return lng;
    }


    // Procesa la etiqueta <precio> de los hoteles
    private String leerGeonameId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_GEONAMEID);
        String geonameid = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_GEONAMEID);
        return geonameid;
    }

    // Procesa la etiqueta <precio> de los hoteles
    private String leerCountryCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_COUNTRYCODE);
        String country = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_COUNTRYCODE);
        return country;
    }

    // Procesa la etiqueta <precio> de los hoteles
    private String leerFcl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FCL);
        String fcl = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FCL);
        return fcl;
    }


    // Procesa la etiqueta <precio> de los hoteles
    private String leerFcode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FCODE);
        String fcode = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FCODE);
        return fcode;
    }

    // Obtiene el texto de los atributos
    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }

    // Salta aquellos objeteos que no interesen en la jerarquía XML.
    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }








}

package cgeo.geocaching.files;

import cgeo.geocaching.location.Geopoint;
import cgeo.geocaching.models.Route;
import cgeo.geocaching.models.RouteItem;
import cgeo.geocaching.models.RouteSegment;

import android.sax.Element;
import android.sax.RootElement;
import android.util.Xml;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

abstract class AbstractTrackOrRouteParser {

    interface RouteParse {
        Route parse(@NonNull final InputStream stream) throws IOException, ParserException;
    }

    protected final String namespace;
    private final String version;
    protected final Route result = new Route(false);

    // temporary variables
    protected ArrayList<Geopoint> temp;
    protected Element points;
    protected Element point;

    protected AbstractTrackOrRouteParser(final String namespaceIn, final String versionIn) {
        namespace = namespaceIn;
        version = versionIn;
    }

    @NonNull
    public Route parse(@NonNull final InputStream stream, final RootElement root) throws IOException, ParserException {
        points.setStartElementListener(attrs -> temp = new ArrayList<>());
        points.getChild(namespace, "name").setEndTextElementListener(result::setName);
        points.setEndElementListener(() -> {
            if (temp.size() > 0) {
                result.add(new RouteSegment(new RouteItem(temp.get(temp.size() - 1)), temp));
                temp = null;
            }
        });

        point.setStartElementListener(attrs -> {
            if (attrs.getIndex("lat") > -1 && attrs.getIndex("lon") > -1) {
                final String latitude = attrs.getValue("lat");
                final String longitude = attrs.getValue("lon");
                if (StringUtils.isNotBlank(latitude) && StringUtils.isNotBlank(longitude)) {
                    temp.add(new Geopoint(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                }
            }
        });

        try {
            final ProgressInputStream progressStream = new ProgressInputStream(stream);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(progressStream, StandardCharsets.UTF_8));
            Xml.parse(new InvalidXMLCharacterFilterReader(reader), root.getContentHandler());
            return result;
        } catch (final SAXException e) {
            throw new ParserException("Cannot parse .gpx file as GPX " + version + ": could not parse XML", e);
        }
    }
}

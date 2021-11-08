/*
 * Copyright 2010-2021 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.help;

import au.gov.asd.tac.constellation.help.preferences.HelpPreferenceKeys;
import au.gov.asd.tac.constellation.help.utilities.Generator;
import au.gov.asd.tac.constellation.help.utilities.HelpMapper;
import com.github.rjeschke.txtmark.Processor;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.HelpCtx;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author algol
 * @author Delphinus8821
 * @author aldebaran30701
 */
@ServiceProvider(service = HelpCtx.Displayer.class, position = 9999)
public class ConstellationHelpDisplayer implements HelpCtx.Displayer {

    private static final Logger LOGGER = Logger.getLogger(ConstellationHelpDisplayer.class.getName());

    private static final String OFFICIAL_CONSTELLATION_WEBSITE = "https://www.constellation-app.com/help";

    public static void copy(final String filePath, final OutputStream out) throws IOException {
        final String sep = File.separator;
        final InputStream pageInput = getInputStream(filePath);
        final InputStream tocInput = getInputStream(Generator.getBaseDirectory() + sep + Generator.getTOCDirectory());

        if (pageInput == null || tocInput == null) {
            // files could not be found, don't progress.
            return;
        }

        // avoid parsing utility files or images into html
        if (filePath.contains(".css") || filePath.contains(".js") || filePath.contains(".png") || filePath.contains(".jpg")) {
            out.write(pageInput.readAllBytes());
            return;
        }

        out.write(generateHTMLOutput(sep, tocInput, pageInput).getBytes());
    }

    protected static InputStream getInputStream(final String filePath) throws FileNotFoundException {
        final Path path = Paths.get(filePath);
        return new FileInputStream(path.toString());
    }

    protected static String getFileURLString(final String fileSeparator, final String baseDirectory, final String relativePath) throws MalformedURLException {
        final File file = new File(baseDirectory + fileSeparator + relativePath);
        final URL url = file.toURI().toURL();
        return url.toString();
    }

    /**
     * Generate a String which represents the table of contents, the currently
     * displayed page, and the necessary html tags for formatting.
     *
     * @param tocInput
     * @param pageInput
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    protected static String generateHTMLOutput(final String separator, final InputStream tocInput, final InputStream pageInput) throws MalformedURLException, IOException {
        final StringBuilder html = new StringBuilder();

        // HTML elements
        final String startRowDiv = "<div class='row'>";
        final String endDiv = "</div>";
        final String startColDiv = "<div class='col-4 col-sm-3'>";
        final String startInnerColDiv = "<div class='col-8 col-sm-9'>";

        final String css = String.format("<link href=\"\\%s\" rel='stylesheet'></link>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/css/app.css"));
        final String noScript = String.format("<link href=\"\\%s\" rel='stylesheet'></link>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/css/noscript.css"));
        final String cssBootstrap = String.format("<link href=\"\\%s\" rel='stylesheet'></link>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/css/bootstrap.css"));
        final String jquery = String.format("<script src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/jquery.min.js"));
        final String dropotron = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/jquery.dropotron.min.js"));
        final String scrolly = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/jquery.scrolly.min.js"));
        final String scrollex = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/jquery.scrollex.min.js"));
        final String browser = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/browser.min.js"));
        final String breakpoints = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/breakpoints.min.js"));
        final String appJS = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/assets/js/app.js"));
        final String boostrapjs = String.format("<script type=\"text/javascript\" src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/js/bootstrap.js"));
        final String cookiejs = String.format("<script src=\"\\%s\" ></script>", getFileURLString(separator, Generator.baseDirectory, "constellation/bootstrap/js/js.cookie.min.js"));

        final String scriptTag = "<script>\n"
                + " // when a group is shown, save it as active\n"
                + " $(\".collapse\").on('shown.bs.collapse', function(e) {\n"
                + "     e.stopPropagation(); \n"
                + "     var active = $(this).attr('id');\n"
                + "     Cookies.set(active, \"true\");\n"
                + "     $(\"#\" + active).addClass('show');\n"
                + " });\n"
                + " // when a group is hidden, save it as inactive\n"
                + " $(\".collapse\").on('hidden.bs.collapse', function(e) {\n"
                + "     e.stopPropagation(); \n"
                + "     var active = $(this).attr('id');\n"
                + "     Cookies.set(active, \"false\");\n"
                + "     $(\"#\" + active).removeClass('show');\n"
                + "     $(\"#\" + active).collapse(\"hide\");\n"
                + " });\n"
                + " \n"
                + " $(document).ready(function() {\n"
                + "      var allCookies = Cookies.get();\n"
                + "      for (var cookie in allCookies) { \n"
                + "          if (cookie != null) {\n"
                + "              //remove default collapse settings\n"
                + "              $(\"#\" + cookie).removeClass('show');\n"
                + "              //show the group if the value is true \n"
                + "              var cookieValue = Cookies.get(cookie);\n"
                + "              if (cookieValue == (\"true\")) {\n"
                + "                 $(\"#\" + cookie).collapse(\"show\");\n"
                + "              } else {\n"
                + "                 $(\"#\" + cookie).collapse(\"hide\");\n"
                + "                 $(\"#\" + cookie + \" .collapse\").removeClass('show');\n"
                + "              }\n"
                + "          }\n"
                + "      }\n"
                + " });\n"
                + "\n"
                + "</script>";

        // Add items to StringBuilder
        html.append(css);
        html.append("\n");
        html.append(noScript);
        html.append("\n");
        html.append(cssBootstrap);
        html.append("\n");
        html.append(jquery);
        html.append("\n");
        html.append(cookiejs);
        html.append("\n");
        html.append(dropotron);
        html.append("\n");
        html.append(scrolly);
        html.append("\n");
        html.append(scrollex);
        html.append("\n");
        html.append(browser);
        html.append("\n");
        html.append(breakpoints);
        html.append("\n");
        html.append(appJS);
        html.append("\n");
        html.append(boostrapjs);
        html.append("\n");
        html.append(startRowDiv);
        html.append("\n");
        html.append(startColDiv);
        html.append("\n");
        html.append(Processor.process(tocInput));
        html.append("\n");
        html.append(endDiv);
        html.append("\n");
        html.append(startInnerColDiv);
        html.append("\n");
        html.append(Processor.process(pageInput));
        html.append("\n");
        html.append(endDiv);
        html.append("\n");
        html.append(endDiv);
        html.append("\n");
        html.append(endDiv);
        html.append("\n");
        html.append(scriptTag);

        return html.toString();
    }

    /**
     * Display the help page for the following HelpCtx
     *
     * @param helpCtx
     * @return
     */
    @Override
    public boolean display(final HelpCtx helpCtx) {
        final String sep = File.separator;

        final Preferences prefs = NbPreferences.forModule(HelpPreferenceKeys.class);
        final boolean isOnline = prefs.getBoolean(HelpPreferenceKeys.HELP_KEY, HelpPreferenceKeys.ONLINE_HELP);

        final String helpId = helpCtx.getHelpID();
        LOGGER.log(Level.INFO, "display help for: {0}", helpId);

        final String helpDefaultPath = sep + "constellation" + sep + "CoreFunctionality" + sep + "src" + sep + "au" + sep + "gov"
                + sep + "asd" + sep + "tac" + sep + "constellation" + sep + "functionality" + sep + "docs" + sep + "about-constellation.md";

        final String helpAddress = HelpMapper.getHelpAddress(helpId);
        // use the requested help file, or the About Constellation page if one is not given
        final String helpLink = StringUtils.isNotEmpty(helpAddress) ? helpAddress.substring(2) : helpDefaultPath;

        try {
            final String url;
            if (isOnline) {
                url = OFFICIAL_CONSTELLATION_WEBSITE + helpLink.replace(".md", ".html");
            } else {
                final File file = new File(Generator.getBaseDirectory() + sep + helpLink);
                final URL fileUrl = file.toURI().toURL();
                final int currentPort = HelpWebServer.start();
                url = String.format("http://localhost:%d/%s", currentPort, fileUrl);
            }

            final URI uri = new URI(url.replace("\\", "/"));

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                browse(uri);
                return true;
            }

            LOGGER.log(Level.WARNING, "Help Documentation was unable to launch because Desktop "
                    + "Browsing was not supported. Tried to navigate to: {0}", uri);

        } catch (final MalformedURLException | URISyntaxException ex) {
            LOGGER.log(Level.WARNING, ex, () -> "Help Documentation URL/URI was invalid - Tried to display help for: " + helpId);
        }
        return false;
    }

    /**
     * Browse to the supplied URI using the desktops browser.
     *
     * @param uri the URI to navigate to
     * @return true if successful, false otherwise
     */
    public static Future<?> browse(final URI uri) {
        LOGGER.log(Level.INFO, "Loading help uri {0}", uri);

        // Run in a different thread, not the JavaFX thread
        final ExecutorService pluginExecutor = Executors.newCachedThreadPool();
        return pluginExecutor.submit(new Thread(() -> {
            Thread.currentThread().setName("Browse Help");
            try {
                Desktop.getDesktop().browse(uri);
            } catch (final IOException ex) {
                LOGGER.log(Level.SEVERE, String.format("Failed to load the help URI %s", uri), ex);
            }
        }));
    }
}
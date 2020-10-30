package sample;

import javafx.fxml.FXML;

import java.io.IOException;

public class AboutController {
    private final String OS = System.getProperty("os.name").toLowerCase();

    @FXML
    private void openMyGithub() {
        openUrl("https://github.com/d9nchik");
    }

    @FXML
    private void openRepository() {
        openUrl("https://github.com/d9nchik/Alemungula");
    }

    @FXML
    private void openRules() {
        openUrl("https://www.iggamecenter.com/info/ru/alemungula.html");
    }

    private void openUrl(String URL) {
        Runtime rt = Runtime.getRuntime();
        try {
            if (OS.contains("win")) {
                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec("rundll32 URL.dll,FileProtocolHandler " + URL);
            } else if (OS.contains("mac")) {
                rt.exec("open " + URL);
            } else if (OS.contains("nix") || OS.contains("nux")) {
                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};
                // Build a command string which looks like "browser1 "URL" || browser2 "URL" ||..."
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(URL).append("\" ");
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

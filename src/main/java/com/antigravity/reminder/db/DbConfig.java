package com.antigravity.reminder.db;

import java.io.File;

public class DbConfig {
    public static String resolveUrl(String url) {
        if (url != null && (url.startsWith("jdbc:h2:./") || url.startsWith("jdbc:h2:db/"))) {
            String userHome = System.getProperty("user.home");

            // Extract the path part
            String prefix = url.startsWith("jdbc:h2:./") ? "jdbc:h2:./" : "jdbc:h2:";
            String pathAndParams = url.substring(prefix.length());

            String path;
            String params = "";
            int semiColonIndex = pathAndParams.indexOf(";");
            if (semiColonIndex != -1) {
                path = pathAndParams.substring(0, semiColonIndex);
                params = pathAndParams.substring(semiColonIndex);
            } else {
                path = pathAndParams;
            }

            // Normalize path for the current OS
            path = path.replace("/", File.separator).replace("\\", File.separator);

            // Determine base directory for data
            String baseDir;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                String appData = System.getenv("APPDATA");
                if (appData != null && !appData.isEmpty()) {
                    baseDir = appData + File.separator + "ReminderSystem";
                } else {
                    baseDir = userHome + File.separator + ".reminder_system";
                }
            } else {
                baseDir = userHome + File.separator + ".reminder_system";
            }

            File dbFile = new File(baseDir, path);
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            String absolutePath = dbFile.getAbsolutePath();
            // H2 URLs require forward slashes
            absolutePath = absolutePath.replace("\\", "/");

            String resolvedUrl = "jdbc:h2:" + absolutePath + params;
            System.out.println("Resolved DB URL: " + resolvedUrl);
            return resolvedUrl;
        }
        return url;
    }
}


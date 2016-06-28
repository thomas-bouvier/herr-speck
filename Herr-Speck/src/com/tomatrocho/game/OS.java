package com.tomatrocho.game;

import java.io.File;

public enum OS {

    WINDOWS,
    MACOS,
    LINUX,
    SOLARIS,
    UNKNOWN;


    /**
     *
     * @return
     */
    public static OS getOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return WINDOWS;
        }
        if (os.contains("mac")) {
            return MACOS;
        }
        if (os.contains("linux") || os.contains("unix")) {
            return LINUX;
        }
        if (os.contains("solaris") || os.contains("sunos")) {
            return SOLARIS;
        }
        return UNKNOWN;
    }

    /**
     *
     * @param str
     * @return
     */
    public static File getAppDirectory(String str) {
        String home = System.getProperty("user.home", ".");
        File file;
        switch (OS.getOs()) {
            case WINDOWS :
                String data = System.getenv("APPDATA");
                if (data == null) {
                    file = new File(home, '.' + str + '/');
                } else {
                    file = new File(data, '.' + str + '/');
                }
                break;
            case MACOS :
                file = new File(home, "Library/Application Support/" + str);
                break;
            case LINUX :
            case SOLARIS :
                file = new File(home, '.' + str + '/');
                break;
            default :
                file = new File(home, str + '/');
                break;
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + file);
        } else {
            return file;
        }
    }
}

package fr.edu.toolprod.bean

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 13/06/14
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
class AppBean {

    /**
     * Application name.
     */
    String name

    /**
     * Application description.
     */
    String description

    /**
     * url of Server which launch the application.
     */
    String serverUrl

    /**
     * port of Server which launch the application.
     */
    String serverPort


    @Override
    public java.lang.String toString() {
        return "AppBean{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverPort='" + serverPort + '\'' +
                '}';
    }
}

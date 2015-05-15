package fr.edu.toolprod.bean

/**
 * Store information parsed from ARENA xml files.
 */
class ArenaBean {

    /**
     * Name of application found in xml file
     */
    String appName

    /**
     * Path in Arena portal
     */
    String arenaPath = ""

    /**
     * Urls path FIM ( exemple : /redirectionhub/redirect.jsp?applicationname=... )
     */
    String fimPath = ""

    /**
     * federation.
     */
    String federationPath = ""

}

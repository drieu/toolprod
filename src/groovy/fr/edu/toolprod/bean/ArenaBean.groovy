package fr.edu.toolprod.bean

/**
 * Store information parsed from ARENA xml files.
 */
class ArenaBean {

    private static final String EMPTY = ""

    /**
     * Name of application found in xml file
     */
    String appName

    /**
     * Path in Arena portal
     */
    String arenaPath = EMPTY

    /**
     * Urls path FIM ( exemple : /redirectionhub/redirect.jsp?applicationname=... )
     */
    String fimPath = EMPTY

    /**
     * federation.
     */
    String federationPath = EMPTY

}

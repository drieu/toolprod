package fr.edu.toolprod.bean

import grails.validation.Validateable

/**
 * Bean use in PDF.
 */
@Validateable
class PrintAppBean {

    String name = ""

    String urls = ""

    String portals = ""
}

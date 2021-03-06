import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

grails.config.locations = [ "file:${userHome}/${appName}.properties"]

//if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
//}

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml'],
    excel: 'application/vnd.ms-excel',
    ods: 'application/vnd.oasis.opendocument.spreadsheet',
    pdf: 'application/pdf',
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000


grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/**/vendor/**':                 ['permitAll'],
]

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}
 
grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

def appName="toolprod"

// default for all environments
log4j = { root ->
    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: "%d [%t] %-5p %c %x - %m%n")
        rollingFile name:'stdout', file:"${appName}.log".toString(), maxFileSize:'100KB'
        rollingFile name:'stacktrace', file:"${appName}_stack.log".toString(), maxFileSize:'100KB'
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate'
    root.level= org.apache.log4j.Level.DEBUG
}

// special settings with production env
environments {
    development {
        reloadPath= "E:\\projet\\toolprod\\import\\"
        log4j = { root ->
            appenders {
                rollingFile name:'stdout', file:"${appName}.log".toString(), maxFileSize:'1000KB'
                rollingFile name:'stacktrace', file:"${appName}_stack.log".toString(), maxFileSize:'1000KB'
            }
            warn       'org.codehaus.groovy.grails.web.servlet',  //  controllers
                    'org.codehaus.groovy.grails.web.pages', //  GSP
                    'org.codehaus.groovy.grails.web.sitemesh', //  layouts
                    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                    'org.codehaus.groovy.grails.web.mapping', // URL mapping
                    'org.codehaus.groovy.grails.commons', // core / classloading
                    'org.codehaus.groovy.grails.plugins', // plugins
                    'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
                    'org.springframework',
                    'org.hibernate'
            info "grails.app"
            debug "fr.edu.toolprod"
            info "fr.edu.toolprod.parser"
            info "toolprod"
            root.level= org.apache.log4j.Level.INFO
        }
    }
    test {
        log4j = {root ->
            info "grails.app"
            debug "fr.edu.toolprod"
            root.level= org.apache.log4j.Level.INFO
        }
    }
    production {
        reloadPath= "/opt/toolprod/import/"
        grails.logging.jul.usebridge = false
        // Set level for all application artifacts
        log4j = {root ->
            appenders {
                rollingFile name:'stdout', file:"/opt/toolprod/logs/${appName}.log".toString(), maxFileSize:'1000KB'
                rollingFile name:'stacktrace', file:"/opt/toolprod/logs/${appName}_stack.log".toString(), maxFileSize:'1000KB'
            }
            error "grails.app"
            error "fr.edu.toolprod"
            root.level= org.apache.log4j.Level.INFO
        }
    }
}


//environments {
//    development {
//        grails.logging.jul.usebridge = true
//        // Set level for all application artifacts
//        log4j = {
//            info "grails.app"
//            debug "fr.edu.toolprod"
//            info "fr.edu.toolprod.parser"
//            info "toolprod"
//        }
//    }
//    test {
//        grails.logging.jul.usebridge = false
//        // Set level for all application artifacts
//        log4j = {
//            info "grails.app"
//            debug "fr.edu.toolprod"
//        }
//    }
//    production {
//        grails.logging.jul.usebridge = false
//        // Set level for all application artifacts
//        log4j = {
//            error "grails.app"
//            debug "fr.edu.toolprod"
//        }
//    }
//}
//
//// log4j configuration
//log4j = {
//
//    // Example of changing the log pattern for the default console appender:
//    //
//    appenders {
//        console name:'stdout', layout:pattern(conversionPattern: '[%-5p] %d %c{2} - %m%n')
//    }
//
//
//    info 'com.linkedin.grails.profiler'
//
//// Set for a specific controller in the default package debug "grails.app.controllers.YourController"
//// Set for a specific domain class debug "grails.app.domain.org.example.Book"
//// Set for all taglibs info "grails.app.taglib"
//
//    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
//           'org.codehaus.groovy.grails.web.pages',          // GSP
//           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
//           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
//           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
//           'org.codehaus.groovy.grails.commons',            // core / classloading
//           'org.codehaus.groovy.grails.plugins',            // plugins
//           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
//           'org.springframework',
//           'org.hibernate',
//           'net.sf.ehcache.hibernate'
//}

plugin.crash.config = [
        'crash.ssh.port': 2001,
        'crash.auth':'simple',
        'crash.auth.simple.username':'admin',
        'crash.auth.simple.password':'admin'
]


cacheManager {
    shared = true
}

grails {
    mail {
        host = "smtp.ac-limoges.fr"
        port = 25
        username = "drieu"
        password = ""
        props = ["mail.smtp.auth":"false"]

    }
}

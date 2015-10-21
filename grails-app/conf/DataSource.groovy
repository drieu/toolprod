hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
}

// environment specific settings
environments {
// save config
//    development {
//        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
//        }
//        dataSource_lookup {
//            pooled = true
//            driverClassName = "org.h2.Driver"
//            username = "sa"
//            password = ""
//        }
//    }
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost/toolprod?useUnicode=yes&characterEncoding=UTF-8"
            username = "root"
            password = ""
            pooled = true
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
        }
        hibernate {
            show_sql = false
        }

    }


    production {
        grails.config.locations = ["file:/opt/apache-tomcat-7.0.47/conf/ConfigToolprod.groovy"]
    }
}

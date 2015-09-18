package fr.edu.admin

import fr.edu.toolprod.bean.ArenaBean
import fr.edu.toolprod.bean.MultipartFileBean
import fr.edu.toolprod.parser.ArenaParser
import fr.edu.toolprod.parser.BigIpParser
import fr.edu.toolprod.parser.ConfigParser
import fr.edu.toolprod.parser.Data
import fr.edu.toolprod.parser.HttpdParser
import fr.edu.toolprod.parser.Parser
import org.springframework.web.multipart.MultipartHttpServletRequest
import toolprod.MachineGroup
import toolprod.Vip

/**
 * Admin Controller.
 */
class AdminController {

    private static final String EMPTY = ""

    def index() {
        log.info("index() Index action from AdminController !")
        redirect(action:'init')
    }

    def initFromHttpd() {
        log.info("initFromHttpd()")
        flash.clear()
        boolean bResult = false
    }

    /**
     * Deleta all data in table and import new data.
     * @return
     */
    def reloadData() {
        def path = "E:\\projet\\toolprod\\import\\"

        Data data = new Data()
        log.info("reloadData() Initializing configuration from config file in " + path + " directory ...")
        def initFile = new File(path + "config")
        InputStream inputStream = new FileInputStream(initFile)
        ConfigParser configParser = new ConfigParser(inputStream)
        boolean bResult = configParser.parse()
        if (bResult) {
            log.debug("reloadData() result:" + configParser.result)
            data.overwriteMachineGroup(configParser.machineByGroup)
            log.info("reloadData() Init config : OK")
        } else {
            log.error("reloadData() Init config : KO")
        }
        data.clean()
        log.info("reloadData() Initializing application from httpd.conf files in machine directory ...")
        new File(path).listFiles().findAll{
            if (it.isDirectory()) {
                log.info("reloadData() Machine name ( Directory name ) :" + it.name)
                String machineName = it.name
                log.debug("reloadData() files list:" + it.listFiles())
                for (File f : it.listFiles()) {
                    HttpdParser parser = new HttpdParser(machineName)
                    parser.parse(f)
                    bResult = parser.parse(f)
                    if (!parser.overwrite()) { //TODO : overwrite
                        bResult = false
                    }
                }
            }
        }

        log.info("reloadData() Initialisating VIP ...")

        File bigipconfFile = new File(path + File.separator + "bigip.conf")
        log.info("reloadData() file to parse :" + bigipconfFile.name)
        if (!bigipconfFile.name.isEmpty()) {
            BigIpParser bigIpParser = new BigIpParser(bigipconfFile)
            bResult = bigIpParser.parse()
        } else {
            log.warn("reloadData() File not found : bigip.conf. Can't parse VIP")
        }

        log.info("reloadData() initFromArena()")
        def filePattern = ~/ARENA_.*xml/
        new File(path).listFiles().findAll{
            if (filePattern.matcher(it.name).find()) {
                InputStream arenaStream = new FileInputStream(it)
                Parser parser = new ArenaParser(arenaStream)
                List<ArenaBean> arenaBeans = parser.parse()
                parser.save(arenaBeans)
                def message = parser.result
                if (message.isEmpty()) {
                    log.warn("reloadData() import from ARENA : 0 application initialisée.")
                } else {
                    log.info("reloadData() import from ARENA :" + message)
                }
            }
        }
        render "SUCCESS"
    }

    /**
     * Initialize datas Step 1.
     */
    def initData() {
        log.info("initData()")
        flash.clear()
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = EMPTY
            request.getFiles("files[]").each { file ->
                log.info("initData() file to parse :" + file.originalFilename)
                if (!file.originalFilename.isEmpty()) {
                    ConfigParser configParser = new ConfigParser(file.inputStream)
                    bResult = configParser.parse()
                    message = configParser.result
                    if (bResult) {
                        log.info("result:" + configParser.result)
                        Data data = new Data()
                        data.overwriteMachineGroup(configParser.machineByGroup)
                        log.info("Init config : OK")
                    } else {
                        log.error("Init config : KO")
                    }
                } else {
                    message = "File is empty !"
                }
            }

            if (bResult) {
                flash.message = "import successful."
            } else {
                flash.error = "FAILED : " + message
            }
        }
        render(view:"initData")
    }

    /**
     * Show web page to init VIP.
     * @return
     */
    def initPortals() {
        log.info("initPortals()")
        flash.clear()
        boolean bResult = false
        if (request instanceof MultipartHttpServletRequest) {
            def message = EMPTY
            request.getFiles("files[]").each { file ->
                log.info("initPortals() file to parse :" + file.originalFilename)
                if (!file.originalFilename.isEmpty()) {
                    BigIpParser configParser = new BigIpParser(file.inputStream)
                    bResult = configParser.parse()
                } else {
                    message = "File is empty !"
                }
            }
            flash.clear()
            if (bResult) {
                flash.message = "SUCCESS"
            } else {
                flash.error = "FAILED : " + message
            }

        }
        def portals = Vip.findAll()
        return [ portals: portals]
    }

    /**
     * Init data step 3 : Method which call httpd parser.
     * @return
     */
    def init() {
        log.info("init()")
        flash.clear()
        if (request instanceof MultipartHttpServletRequest) {
            String message = EMPTY
            boolean bResult = true
            def machineName = request.getParameterValues("machinename")
            if (machineName != null) {
                message = message + machineName
                message += " ==> "
            }
            log.info("Name of machine : " + machineName[0])
            request.getFiles("files[]").each { file ->
                log.debug("init() file to parse:" + file.originalFilename)
                if((machineName != null) && (file != null) && (!file.isEmpty())) {
                    MultipartFileBean f = new MultipartFileBean()
                    f.inputStream = file.inputStream
                    f.originalFilename = file.originalFilename
                    HttpdParser parser = new HttpdParser(f, machineName[0]);
                    bResult = parser.parse()
                    if (!parser.save()) {
                        bResult = false
                    }
                    message += parser.result
                } else {
                    bResult = false
                    log.debug("init() machineName:" + machineName)
                    message += 'Import failed because file is null or is empty'
                }
            }
            if (bResult) {
                flash.message = "SUCCESS : " + message
            } else {
                flash.error = "FAILED : " + message
            }
        }
    }

    /**
     * Init data from the ARENA xml files .
     */
    def initFromArena() {
        log.info("initFromArena()")
        List<String> errs = new ArrayList<>()
        flash.clear()
        if (request instanceof MultipartHttpServletRequest) {
            def message = EMPTY
            boolean bResult = true
            request.getFiles("files[]").each { file ->
                log.debug("init() file to parse:" + file.originalFilename)
                Parser parser = new ArenaParser(file.inputStream)
                List<ArenaBean> arenaBeans = parser.parse()
                errs = parser.save(arenaBeans)
                message = parser.result
                if (message.isEmpty()) {
                    message += " 0 application initialisée."
                }
            }
            if (bResult) {
                flash.message = "SUCCESS : " + message
            } else {
                flash.error = "FAILED : " + message
            }
        }
        [errs:errs]
    }

    /**
     * Redirect to info.gsp for having informations about plugins, versions ...
     */
    def info() {
    }

}

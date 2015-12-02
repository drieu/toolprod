package fr.edu.toolprod.js

import toolprod.App
import toolprod.Machine
import toolprod.MachineGroup
import toolprod.Server
import toolprod.Vip

/**
 * Generate JS strings
 */
class Generator {

    private List<String> chartConfig = new ArrayList<>()

    /**
     * Generate string with vip number
     * @return
     */
    String generateDataVipEnum() {
        String data = ""
        int cpt = 1
        for(String chartConf : chartConfig) {
            data += chartConf
            if (cpt <= (chartConfig.size() - 1))  {
                data += ","
            }
            cpt++
        }
        return data
    }

    /**
     * Generate a section of css graph.
     * @param nodeName
     * @param parentName
     * @param name
     * @param title
     * @return
     */
    String generateStrData(String nodeName, String parentName, String name, String title) {
        String data = ""

        String vipNumStr = "\n" + nodeName + " = {\n"
        data += vipNumStr
        data += "\t\t\t\tparent: " + parentName + ",\n"
        if (parentName.contains("root")) {
            data += "stackChildren: true,"
        }
        if (parentName.contains("root")) {
            data += "\t\t\t\tHTMLclass: 'blue',\n"
        }

        data += "\t\t\t\ttext: {\n"
        String strName = "\t\t\t\t\tname: \"" + name + "\",\n"
        data += strName

        String strTitle = "\t\t\t\t\tdesc: \"" + title + "\",\n"
        data += strTitle
        data += "\t\t\t\t},\n"
        String htmlId = "\n\t\t\t\tHTMLid: " + nodeName + "\n"
        data += htmlId
        data += "\t\t\t},\n\t"


        chartConfig.add(nodeName)

        return data
    }


    /**
     * Generate vip tree format with an existing list of vips.
     *  vip = {
     * text: {
     * name: "VIP",
     * title: "Description de la vip"
     },
     },
     * @return
     */
    String generateDataVip() {
        String data = ""
        List<Vip> vips = Vip.findAll()

        int cpt = 1
        for(Vip vip : vips) {
            String parent = "vip" + cpt
            String title = ""
            if (vip.technicalName.contains("_http")) {
                title = "HTTP"
            }
            if (vip.technicalName.contains("_ssl")) {
                title = "SSL"
            }
            data += generateStrData("vip" + cpt, "root", vip.name, title)

            List<String> machines = new ArrayList<>()
            Map<String, List<String>> maps = new TreeMap<>()
            for(Server server : vip.servers) {
                List<String> lst = new ArrayList<>()
                String appStr = ""
                for(String app : server.linkToApps) {
                    appStr += app
                    appStr += " "
                }

                if (!maps.containsKey(server.machineHostName)) {
                    lst.add(server.portNumber + ":" + appStr)

                } else {
                    lst = maps.get(server.machineHostName)
                    lst.add(server.portNumber + ":" + appStr)

                }
                maps.put(server.machineHostName, lst)
            }
            int i = 0
            for(String str : maps.keySet()) {
                String result = ""
                for(String strApp : maps.get(str) ) {
                    result += " "
                    result += strApp
                }
                data += generateStrData("serv" + i + "_" + cpt, parent, str, result)
                i++
            }
            cpt++
        }
        return data
    }


    /**
     * Generate vip tree format with an existing list of vips.
     *  vip = {
     * text: {
     * name: "VIP",
     * title: "Description de la vip"
     },
     },
     * @return
     */
    String generateDataMachines() {
        String data = ""

        List<Machine> machines = Machine.findAll()
        int cpt = 1
        def machineGroup = MachineGroup.findByGroupName("frontaux")
        for (String frontal : machineGroup.regex) {
                println("frontal:" + frontal)
                Machine machine = Machine.findByNameLike(frontal +"%")
                if (machine != null) {
                    String parent = "mach" + cpt
                    String title = ""
                    data += generateStrData("mach" + cpt, "root", machine.name, title)

                    Map<String, List<String>> maps = new TreeMap<>()
                    for(Server server : machine.servers) {
                        for(String myApp : server.linkToApps) {
                            App app = App.findByName(myApp)
                            for(Server s : app.servers) {
                                boolean bCheck = true
                                for(String str : machineGroup.regex) {
                                    if (s.machineHostName.startsWith(str)) {
                                        bCheck = false
                                    }
                                }
                                if(bCheck) {
                                    List<String> lst = new ArrayList<>()

                                    if (!maps.containsKey(s.machineHostName)) {
                                        if(!lst.contains(app.name)) {
                                            lst.add(app.name)
                                        }

                                    } else {
                                        lst = maps.get(s.machineHostName)
                                        if(!lst.contains(app.name)) {
                                            lst.add(app.name)
                                        }

                                    }
                                    maps.put(s.machineHostName, lst)
                                }
                            }
                        }
                    }
                    int i = 0
                    for(String str : maps.keySet()) {
                        String result = ""
                        for(String strApp : maps.get(str) ) {
                            result += " "
                            result += strApp
                        }
                        data += generateStrData("serv" + i + "_" + cpt, parent, str, result)
                        i++
                    }
                    cpt++
                }
        }
        return data
    }

    String generateDataMachinepEnum() {
        String data = ""
        int cpt = 1
        for(String chartConf : chartConfig) {
            data += chartConf
            if (cpt <= (chartConfig.size() - 1))  {
                data += ","
            }
            cpt++
        }
        return data
    }

}

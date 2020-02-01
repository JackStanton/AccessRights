package Classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class VBSUtility {

    private static String makeVbScript(String vbClassName, String[] propNames) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < propNames.length; i++) {
            if (i < propNames.length - 1) {
                sb.append(propNames[i]).append(",");
            } else {
                sb.append(propNames[i]);
            }
        }
        String colNameString = sb.toString();
        sb.setLength(0);
        sb.append("Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")").append("\n");
        sb.append("Set colItems = objWMIService.ExecQuery _ ").append("\n");
        sb.append("(\"Select ").append(colNameString).append(" from ").append(vbClassName).append("\") ").append("\n");
        sb.append("For Each objItem in colItems ").append("\n");
        for (String propName : propNames) {
            sb.append("    Wscript.Echo objItem.").append(propName).append("\n");
        }
        sb.append("Next ").append("\n");
        return sb.toString();
    }

    public static Map<String, String> printComputerSystemProductInfo(String[] propNames) {
        Map<String, String> map = null;
        String vbClassName = "Win32_Processor";
        String vbScript = VBSUtility.makeVbScript(vbClassName, propNames);
        try {
            File file = File.createTempFile("vbsfile", ".vbs");
            FileWriter fw = new FileWriter(file);
            fw.write(vbScript);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            map = new HashMap<String, String>();
            String line;
            int i = 0;
            while ((line = input.readLine()) != null) {
                if (i >= propNames.length) {
                    break;
                }
                String key = propNames[i];
                map.put(key, line);
                i++;
            }
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
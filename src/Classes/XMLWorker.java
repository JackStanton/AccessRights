package Classes;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public  class XMLWorker {
    public static ArrayList<FileObj> parseFiles() {
        ArrayList<FileObj> list = new ArrayList<FileObj>();
        try {
            File fXmlFile = new File("Files.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("file");
            for (int i = 0; i < nList.getLength(); i++) {
                Element nNode = (Element) nList.item(i);
                String fileName = nNode.getAttribute("name");
                FileObj file = new FileObj(fileName);
                list.add(file);
            }

        } catch (Exception ignored) {
        }
        return list;
    }

    public static ArrayList<UserObj> parseUsers() {
        ArrayList<UserObj> list = new ArrayList<UserObj>();
        try {
            File fXmlFile = new File("Users.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            for (int i = 0; i < nList.getLength(); i++) {
                Element nNode = (Element) nList.item(i);
                String userName = nNode.getAttribute("username");
                String userPass = nNode.getAttribute("pass");
                String userGrant = nNode.getAttribute("grant");

                UserObj user = new UserObj(userName,userPass,userGrant);
                list.add(user);
            }
        } catch (Exception ignored) {
        }
        return list;
    }

    public static void resetRights() throws TransformerException, FileNotFoundException, ParserConfigurationException {
        ArrayList<FileObj> files = parseFiles();
        ArrayList<UserObj> users = parseUsers();
        boolean value = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document doc = build.newDocument();
        Element rootElement = doc.createElement("files");
        doc.appendChild(rootElement);
        for (int i = 0; i < files.size(); i++) {
            Element file = doc.createElement("file");
            file.setAttribute("filename", files.get(i).getName());
            rootElement.appendChild(file);
            for (int j = 0; j < users.size(); j++) {
                Element user = doc.createElement("user");
                user.setAttribute("username",users.get(j).getName());
                value = rand(users.get(j).getGrant());
                user.setAttribute("full", Boolean.toString(value));
                if (!value){ value = rand(users.get(j).getGrant());}
                user.setAttribute("write", Boolean.toString(value));
                if (!value) {value = rand(users.get(j).getGrant());}
                user.setAttribute("read", Boolean.toString(value));
                if (!value) {value = rand(users.get(j).getGrant());}
                user.setAttribute("transfer", Boolean.toString(value));
                file.appendChild(user);
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("usersRights.xml")));
    }

    public static ArrayList<FileObj> parseRights() {
        ArrayList<UserObj> users;
        ArrayList<FileObj> files = new ArrayList<>();
        try {
            File fXmlFile = new File("usersRights.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nFileList = doc.getElementsByTagName("file");
            for (int i = 0; i < nFileList.getLength(); i++) {
                Element userList = (Element) nFileList.item(i);
                String filename = userList.getAttribute("filename");
                users = new ArrayList<>();
                for (int j = 0; j < userList.getElementsByTagName("user").getLength(); j++) {
                    Element user = (Element) userList.getElementsByTagName("user").item(j);
                    String username = user.getAttribute("username");

                    String write = user.getAttribute("write");
                    String read = user.getAttribute("read");
                    String transfer = user.getAttribute("transfer");
                    String full = user.getAttribute("full");
                    UserObj userObj = new UserObj(username,write,read,transfer,full);
                    users.add(userObj);
                }
                FileObj fileObj = new FileObj(filename,users);
                files.add(fileObj);
            }
        } catch (Exception ignored) {
        }
        return files;
    }

    public static void applyRights(ArrayList<FileObj> files) throws TransformerException, FileNotFoundException, ParserConfigurationException {
        String value = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document doc = build.newDocument();
        Element rootElement = doc.createElement("files");
        doc.appendChild(rootElement);
        for (int i = 0; i < files.size(); i++) {
            Element file = doc.createElement("file");
            file.setAttribute("filename", files.get(i).getName());
            ArrayList<UserObj> users = files.get(i).getUsers();
            rootElement.appendChild(file);
            for (int j = 0; j < users.size(); j++) {
                Element user = doc.createElement("user");
                user.setAttribute("username",users.get(j).getName());
                value = users.get(j).getFull();
                user.setAttribute("full", value);
                value = users.get(j).getWrite();
                user.setAttribute("write", value);
                value = users.get(j).getRead();
                user.setAttribute("read", value);
                value = users.get(j).getTransfer();
                user.setAttribute("transfer", value);
                file.appendChild(user);
            }
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("usersRights.xml")));
    }

    public static void addUser(ArrayList<UserObj> users) throws ParserConfigurationException, TransformerException, IOException, ClassNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document doc = build.newDocument();
        Element rootElement = doc.createElement("users");
        doc.appendChild(rootElement);
        for (int i = 0; i < users.size(); i++) {
            Element user = doc.createElement("user");
            user.setAttribute("username", new String(EncryptionUntil.encrypt(users.get(i).getName())));
            user.setAttribute("pass",new String(EncryptionUntil.encrypt(users.get(i).getPass())));
            user.setAttribute("grant",new String(EncryptionUntil.encrypt(users.get(i).getGrant())));
            rootElement.appendChild(user);
        }
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream("Users.xml")));
    }

    private static boolean rand(String str){
        double a = 0;
        boolean newValue = false;
        a = Math.random()*100;
        newValue = a < 37;
        newValue = (str.equals("admin")) || newValue;
        return newValue;
    };
}

import Classes.FileObj;
import Classes.UserObj;
import GUI.MainWindow;
import Classes.XMLWorker;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.net.UnknownServiceException;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        new MainWindow();
    }

}

